package com.dcspa.prism.service.effectif;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Applique {@link EffectifCoherenceRules} sur une entité d’effectif (champs {@code effectif*}).
 */
public final class EffectifEntityCoherence {

	private EffectifEntityCoherence() {
	}

	public static void applyAndValidate(Object entity) {
		if (entity == null) {
			return;
		}
		try {
			Map<String, Integer> values = new LinkedHashMap<>();
			Map<String, PropertyDescriptor> descriptors = new LinkedHashMap<>();
			for (PropertyDescriptor pd : Introspector.getBeanInfo(entity.getClass(), Object.class).getPropertyDescriptors()) {
				String name = pd.getName();
				if (name == null || !name.startsWith("effectif")) {
					continue;
				}
				Method read = pd.getReadMethod();
				Method write = pd.getWriteMethod();
				if (read == null || write == null) {
					continue;
				}
				Class<?> type = pd.getPropertyType();
				if (type != Integer.class && type != int.class) {
					continue;
				}
				Object raw = read.invoke(entity);
				values.put(name, raw instanceof Integer i ? i : raw instanceof Number n ? n.intValue() : null);
				descriptors.put(name, pd);
			}
			if (values.isEmpty()) {
				return;
			}
			Map<String, Integer> adjusted = EffectifCoherenceRules.applyTotalsAndValidate(values);
			for (Map.Entry<String, Integer> e : adjusted.entrySet()) {
				PropertyDescriptor pd = descriptors.get(e.getKey());
				if (pd == null || pd.getWriteMethod() == null) {
					continue;
				}
				pd.getWriteMethod().invoke(entity, e.getValue());
			}
		} catch (IllegalArgumentException ex) {
			throw ex;
		} catch (IntrospectionException | ReflectiveOperationException ex) {
			throw new IllegalStateException("Impossible de contrôler la cohérence des effectifs.", ex);
		}
	}
}
