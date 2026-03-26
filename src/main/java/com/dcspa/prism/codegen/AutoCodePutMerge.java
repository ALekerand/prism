package com.dcspa.prism.codegen;

import java.lang.reflect.Field;

/**
 * Lors d'un PUT partiel, les champs absents du JSON sont souvent null sur l'entité désérialisée.
 * Pour les entités annotées {@link AutoCode}, le code ne doit pas être écrasé par null.
 */
public final class AutoCodePutMerge {

	private AutoCodePutMerge() {
	}

	/** Garde la valeur de requête si elle est non vide ; sinon la valeur persistée. */
	public static String mergeCodeString(String fromRequest, String fromDatabase) {
		if (fromRequest != null && !fromRequest.isBlank()) {
			return fromRequest;
		}
		return fromDatabase;
	}

	/**
	 * Si la classe porte {@link AutoCode}, recopie le code de l'existant sur l'entité entrante
	 * lorsque la requête n'a pas fourni de code non vide.
	 */
	public static void preserveAutoCodeFromExisting(Object existing, Object incoming) {
		if (existing == null || incoming == null) {
			return;
		}
		if (existing.getClass() != incoming.getClass()) {
			return;
		}
		AutoCode cfg = existing.getClass().getAnnotation(AutoCode.class);
		if (cfg == null) {
			return;
		}
		String fieldName = cfg.field();
		if (fieldName == null || fieldName.isBlank()) {
			return;
		}
		try {
			Field f = existing.getClass().getDeclaredField(fieldName.trim());
			f.setAccessible(true);
			if (f.getType() != String.class) {
				return;
			}
			String inc = (String) f.get(incoming);
			String ex = (String) f.get(existing);
			f.set(incoming, mergeCodeString(inc, ex));
		} catch (ReflectiveOperationException e) {
			throw new IllegalStateException("Fusion code auto: " + existing.getClass().getName(), e);
		}
	}
}
