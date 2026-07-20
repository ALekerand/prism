package com.dcspa.prism.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonMappingException.Reference;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import jakarta.validation.ConstraintViolationException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
/**
 * Réponses d'erreur uniformes et lisibles pour l'API MVC.
 * <p>
 * Principes :
 * <ul>
 *   <li>Le client reçoit toujours un payload JSON {@code { timestamp, status, error, message, errorId }}
 *       avec un message en français, sans jargon technique.</li>
 *   <li>Le détail technique complet (stacktrace, causes Jackson, SQL) est conservé dans les logs serveur,
 *       indexé par {@code errorId} pour faciliter le support.</li>
 * </ul>
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

	private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

	private static final String GENERIC_BAD_REQUEST = "Requête invalide. Vérifiez les champs envoyés.";
	private static final String GENERIC_SERVER_ERROR = "Une erreur inattendue est survenue. Veuillez réessayer ou contacter le support.";

	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<Map<String, Object>> handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {
		String errorId = newErrorId();
		LOGGER.warn("[{}] Body invalide (MVC): {}", errorId, ex.getMessage(), ex);
		return badRequest(friendlyDeserializationMessage(ex.getCause()), errorId);
	}

	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<Map<String, Object>> handleIllegalArgument(IllegalArgumentException ex) {
		String errorId = newErrorId();
		LOGGER.warn("[{}] IllegalArgument: {}", errorId, ex.getMessage());
		return badRequest(ex.getMessage() != null ? ex.getMessage() : "Paramètres invalides.", errorId);
	}

	@ExceptionHandler(org.springframework.web.HttpRequestMethodNotSupportedException.class)
	public ResponseEntity<Map<String, Object>> handleMethodNotSupported(
			org.springframework.web.HttpRequestMethodNotSupportedException ex) {
		String errorId = newErrorId();
		LOGGER.warn("[{}] Méthode HTTP non supportée: {}", errorId, ex.getMessage());
		HttpStatus status = HttpStatus.METHOD_NOT_ALLOWED;
		Map<String, Object> body = errorBody(status, "Méthode HTTP non autorisée pour cette ressource.", errorId);
		body.put("detail", ex.getMessage());
		return ResponseEntity.status(status).body(body);
	}

	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<Map<String, Object>> handleConstraintViolation(ConstraintViolationException ex) {
		String errorId = newErrorId();
		String details = ex.getConstraintViolations().stream()
				.map(v -> v.getPropertyPath() + ": " + v.getMessage())
				.collect(Collectors.joining("; "));
		LOGGER.warn("[{}] Validation: {}", errorId, details);
		return badRequest(details.isBlank() ? "Validation échouée." : details, errorId);
	}

	@ExceptionHandler(DataIntegrityViolationException.class)
	public ResponseEntity<Map<String, Object>> handleDataIntegrity(DataIntegrityViolationException ex) {
		String errorId = newErrorId();
		Throwable root = rootCause(ex);
		String technical = root != null && root.getMessage() != null ? root.getMessage() : ex.getMessage();
		LOGGER.warn("[{}] Intégrité des données: {}", errorId, technical, ex);
		return ResponseEntity.status(HttpStatus.CONFLICT)
				.body(errorBody(HttpStatus.CONFLICT, friendlyIntegrityMessage(technical), errorId));
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<Map<String, Object>> handleAny(Exception ex) {
		String errorId = newErrorId();
		LOGGER.error("[{}] Exception non gérée: {}", errorId, ex.getMessage(), ex);
		HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
		Map<String, Object> body = errorBody(status, GENERIC_SERVER_ERROR, errorId);
		// En local (include-message=always) : détail technique pour accélérer le diagnostic.
		body.put("detail", ex.getClass().getSimpleName() + ": " + String.valueOf(ex.getMessage()));
		return ResponseEntity.status(status).body(body);
	}

	private static String friendlyDeserializationMessage(Throwable cause) {
		String fromJackson3Text = friendlyFromThrowableMessages(cause);
		if (fromJackson3Text != null) {
			return fromJackson3Text;
		}
		Throwable c = cause;
		while (c != null) {
			if (c instanceof InvalidFormatException ife) {
				return formatInvalidFormatException(ife);
			}
			if (c instanceof UnrecognizedPropertyException upe) {
				return "Champ inconnu '" + upe.getPropertyName() + "'.";
			}
			if (c instanceof MismatchedInputException mie) {
				return friendlyMismatchedInput(mie);
			}
			if (c instanceof JsonProcessingException) {
				return "Le corps de la requête n'est pas un JSON valide.";
			}
			c = c.getCause();
		}
		return GENERIC_BAD_REQUEST;
	}

	private static String friendlyMismatchedInput(MismatchedInputException mie) {
		String fieldPath = mismatchedFieldPath(mie);
		Class<?> target = mie.getTargetType();
		if (target != null && isJpaEntity(target)) {
			return "Le champ '" + fieldPath + "' doit être un identifiant numérique (ex. " + fieldPath + ": 1).";
		}
		if (target != null) {
			return "Le champ '" + fieldPath + "' attend un " + simpleTypeName(target) + ".";
		}
		return "Le format du champ '" + fieldPath + "' est invalide.";
	}

	/** Messages concaténés (cause chain) pour détecter les motifs Jackson 3 sans dépendre du classpath. */
	private static String friendlyFromThrowableMessages(Throwable cause) {
		if (cause == null) {
			return null;
		}
		StringBuilder sb = new StringBuilder();
		Throwable t = cause;
		while (t != null) {
			if (t.getMessage() != null) {
				sb.append(t.getMessage()).append('\n');
			}
			t = t.getCause();
		}
		String blob = sb.toString();
		if (blob.contains("deserialize from Number value") && blob.contains("reference chain")) {
			java.util.regex.Matcher m = java.util.regex.Pattern.compile("\\[\"([^\"]+)\"]")
					.matcher(blob);
			String lastField = null;
			while (m.find()) {
				lastField = m.group(1);
			}
			if (lastField != null && lastField.startsWith("id")) {
				return "Le champ '" + lastField + "' doit être un identifiant numérique (ex. " + lastField + ": 1).";
			}
		}
		return null;
	}

	private static String mismatchedFieldPath(MismatchedInputException mie) {
		String path = mie.getPath().stream()
				.map(Reference::getFieldName)
				.filter(name -> name != null && !name.isBlank())
				.collect(Collectors.joining("."));
		return path.isBlank() ? "(racine)" : path;
	}

	private static boolean isJpaEntity(Class<?> type) {
		return type.isAnnotationPresent(jakarta.persistence.Entity.class);
	}

	private static String simpleTypeName(Class<?> type) {
		if (type == Integer.class || type == int.class) return "entier";
		if (type == Long.class || type == long.class) return "entier (long)";
		if (type == Double.class || type == double.class) return "nombre décimal";
		if (type == Boolean.class || type == boolean.class) return "booléen (true/false)";
		if (type == String.class) return "texte";
		if (type == LocalDate.class) return "date (yyyy-MM-dd)";
		if (type == LocalDateTime.class) return "date-heure (yyyy-MM-dd'T'HH:mm:ss)";
		return type.getSimpleName();
	}

	private static String formatInvalidFormatException(InvalidFormatException ife) {
		String fieldPath = ife.getPath().stream()
				.map(Reference::getFieldName)
				.filter(name -> name != null && !name.isBlank())
				.collect(Collectors.joining("."));
		if (fieldPath.isBlank()) {
			fieldPath = "(champ inconnu)";
		}
		String invalidValue = String.valueOf(ife.getValue());
		Class<?> targetType = ife.getTargetType();

		if (LocalDate.class.equals(targetType)) {
			return "Format invalide pour '" + fieldPath + "'. Valeur reçue: '" + invalidValue
					+ "'. Formats attendus: yyyy-MM-dd (ex: 2026-03-17) ou dd/MM/yyyy (ex: 17/03/2026).";
		}
		if (LocalDateTime.class.equals(targetType)) {
			return "Format invalide pour '" + fieldPath + "'. Valeur reçue: '" + invalidValue
					+ "'. Format attendu: yyyy-MM-dd'T'HH:mm:ss (ex: 2026-03-17T14:30:00).";
		}
		if (targetType != null && isJpaEntity(targetType)) {
			return "Le champ '" + fieldPath + "' doit être un identifiant numérique. Valeur reçue: '" + invalidValue + "'.";
		}
		return "Format invalide pour '" + fieldPath + "'. Valeur reçue: '" + invalidValue + "'.";
	}

	private static String friendlyIntegrityMessage(String technical) {
		if (technical == null) {
			return "Conflit d'intégrité : la donnée référence un enregistrement inexistant ou existe déjà.";
		}
		String t = technical.toLowerCase();
		if (t.contains("foreign key") || t.contains("foreignkey") || t.contains("a foreign key constraint")) {
			return "Référence invalide : un identifiant lié n'existe pas en base.";
		}
		if (t.contains("duplicate") || t.contains("unique constraint") || t.contains("unique index")) {
			return "Doublon détecté : un enregistrement avec ces valeurs existe déjà.";
		}
		if (t.contains("cannot be null") || t.contains("not-null")) {
			return "Champ obligatoire manquant.";
		}
		return "Conflit d'intégrité sur les données envoyées.";
	}

	private static ResponseEntity<Map<String, Object>> badRequest(String message, String errorId) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body(errorBody(HttpStatus.BAD_REQUEST, message, errorId));
	}

	private static Map<String, Object> errorBody(HttpStatus status, String message, String errorId) {
		Map<String, Object> body = new LinkedHashMap<>();
		body.put("timestamp", OffsetDateTime.now().toString());
		body.put("status", status.value());
		body.put("error", status.getReasonPhrase());
		body.put("message", message);
		body.put("errorId", errorId);
		return body;
	}

	private static String newErrorId() {
		return UUID.randomUUID().toString().substring(0, 8);
	}

	private static Throwable rootCause(Throwable t) {
		Throwable cur = t;
		while (cur != null && cur.getCause() != null && cur.getCause() != cur) {
			cur = cur.getCause();
		}
		return cur;
	}
}
