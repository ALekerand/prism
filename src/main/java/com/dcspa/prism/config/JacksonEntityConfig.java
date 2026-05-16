package com.dcspa.prism.config;

import com.dcspa.prism.controller.support.ReferentielEnricher;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.deser.BeanDeserializerModifier;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.deser.ResolvableDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.BeanSerializerModifier;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Map;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * Sérialise toute classe annotée {@code @Entity} via {@link ReferentielEnricher#toMap(Object)}
 * (réponses au format B : FKs imbriquées sous {@code { id, code, libelle }} au lieu d'objets JPA bruts).
 * Désérialisation (POST/PUT) inchangée : seuls les serializers sont modifiés.
 */
@Configuration
public class JacksonEntityConfig {

	@Bean
	public SimpleModule entityFormatBModule() {
		SimpleModule module = new SimpleModule("PrismEntityFormatB");
		module.setSerializerModifier(new BeanSerializerModifier() {
			@Override
			public JsonSerializer<?> modifySerializer(
					SerializationConfig config, BeanDescription beanDesc, JsonSerializer<?> serializer) {
				if (beanDesc.getBeanClass().isAnnotationPresent(Entity.class)) {
					return new EntityFormatBSerializer();
				}
				return serializer;
			}
		});
		module.setDeserializerModifier(new BeanDeserializerModifier() {
			@SuppressWarnings("unchecked")
			@Override
			public JsonDeserializer<?> modifyDeserializer(
					DeserializationConfig config, BeanDescription beanDesc, JsonDeserializer<?> deserializer) {
				if (beanDesc.getBeanClass().isAnnotationPresent(Entity.class)) {
					return new EntityRefOrFullDeserializer(
							beanDesc.getBeanClass(), (JsonDeserializer<Object>) deserializer, false);
				}
				return deserializer;
			}
		});
		return module;
	}

	@Bean
	@Primary
	public ObjectMapper prismObjectMapper(SimpleModule entityFormatBModule) {
		ObjectMapper mapper = new ObjectMapper();
		mapper.registerModule(entityFormatBModule);
		mapper.registerModule(new JavaTimeModule());
		mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
		return mapper;
	}

	private static final class EntityFormatBSerializer extends JsonSerializer<Object> {
		@Override
		public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
			Map<String, Object> map = ReferentielEnricher.toMap(value);
			if (map == null) {
				gen.writeNull();
				return;
			}
			gen.writeStartObject();
			for (Map.Entry<String, Object> e : map.entrySet()) {
				gen.writeObjectField(e.getKey(), e.getValue());
			}
			gen.writeEndObject();
		}
	}

	/**
	 * Désérialiseur contextuel pour les classes annotées {@link Entity}.
	 * <p>
	 * Le comportement dépend du contexte d'usage (résolu via {@link ContextualDeserializer}):
	 * <ul>
	 *   <li><b>Top-level</b> (corps de requête, ex. {@code @RequestBody Foo body}) : délègue intégralement
	 *       au deserializer Jackson par défaut (toutes les propriétés sont peuplées).</li>
	 *   <li><b>Propriété imbriquée</b> (ex. {@code idCentre}) : interprète la valeur comme une référence FK :
	 *     <ul>
	 *       <li>scalaire (int/long/string) → entité instanciée avec uniquement {@code id} renseigné ;</li>
	 *       <li>objet {@code {"id": N}} (forme historique) → entité avec {@code id=N} ;</li>
	 *       <li>{@code null} → {@code null}.</li>
	 *     </ul>
	 *   </li>
	 * </ul>
	 */
	private static final class EntityRefOrFullDeserializer extends JsonDeserializer<Object>
			implements ContextualDeserializer, ResolvableDeserializer {
		private final Class<?> entityClass;
		private final JsonDeserializer<Object> defaultDeserializer;
		private final boolean asReference;

		EntityRefOrFullDeserializer(
				Class<?> entityClass, JsonDeserializer<Object> defaultDeserializer, boolean asReference) {
			this.entityClass = entityClass;
			this.defaultDeserializer = defaultDeserializer;
			this.asReference = asReference;
		}

		@Override
		public JsonDeserializer<?> createContextual(DeserializationContext ctxt, BeanProperty property)
				throws JsonMappingException {
			boolean nested = property != null;
			if (nested == this.asReference) {
				return this;
			}
			return new EntityRefOrFullDeserializer(entityClass, defaultDeserializer, nested);
		}

		@Override
		public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
			if (!asReference) {
				return defaultDeserializer.deserialize(p, ctxt);
			}
			JsonToken token = p.currentToken();
			if (token == JsonToken.VALUE_NUMBER_INT
					|| token == JsonToken.VALUE_NUMBER_FLOAT
					|| token == JsonToken.VALUE_STRING) {
				return instantiateWithId(parseId(p));
			}
			if (token == JsonToken.START_OBJECT) {
				JsonNode node = p.readValueAsTree();
				JsonNode idNode = node.get("id");
				if (idNode != null && !idNode.isNull()) {
					Object id = idNode.isIntegralNumber()
							? Integer.valueOf(idNode.intValue())
							: idNode.asText();
					return instantiateWithId(id);
				}
				return null;
			}
			if (token == JsonToken.VALUE_NULL) {
				return null;
			}
			return defaultDeserializer.deserialize(p, ctxt);
		}

		@Override
		public void resolve(DeserializationContext ctxt) throws JsonMappingException {
			if (defaultDeserializer instanceof ResolvableDeserializer resolvableDeserializer) {
				resolvableDeserializer.resolve(ctxt);
			}
		}

		private Object parseId(JsonParser p) throws IOException {
			if (p.currentToken() == JsonToken.VALUE_STRING) {
				String s = p.getValueAsString();
				try {
					return Integer.valueOf(s);
				} catch (NumberFormatException ignore) {
					return s;
				}
			}
			return p.getIntValue();
		}

		private Object instantiateWithId(Object idValue) {
			try {
				Object entity = entityClass.getDeclaredConstructor().newInstance();
				Field idField = findIdField(entityClass);
				if (idField == null) {
					return entity;
				}
				idField.setAccessible(true);
				idField.set(entity, coerceId(idValue, idField.getType()));
				return entity;
			} catch (ReflectiveOperationException e) {
				throw new IllegalStateException(
						"Cannot instantiate " + entityClass.getName() + " with id " + idValue, e);
			}
		}

		private static Field findIdField(Class<?> type) {
			Class<?> c = type;
			while (c != null && c != Object.class) {
				for (Field f : c.getDeclaredFields()) {
					if (f.isAnnotationPresent(Id.class)) {
						return f;
					}
				}
				c = c.getSuperclass();
			}
			return null;
		}

		private static Object coerceId(Object idValue, Class<?> targetType) {
			if (idValue == null) {
				return null;
			}
			if (targetType.isInstance(idValue)) {
				return idValue;
			}
			if (idValue instanceof Number n) {
				if (targetType == Integer.class || targetType == int.class) {
					return n.intValue();
				}
				if (targetType == Long.class || targetType == long.class) {
					return n.longValue();
				}
			}
			if (idValue instanceof String s) {
				if (targetType == Integer.class || targetType == int.class) {
					return Integer.valueOf(s);
				}
				if (targetType == Long.class || targetType == long.class) {
					return Long.valueOf(s);
				}
			}
			return idValue;
		}
	}
}
