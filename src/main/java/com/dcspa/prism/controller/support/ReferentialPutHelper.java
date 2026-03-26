package com.dcspa.prism.controller.support;

import com.dcspa.prism.codegen.AutoCodePutMerge;
import org.springframework.http.ResponseEntity;

import java.util.Optional;
import java.util.function.Function;

/**
 * PUT référentiel : préserve le champ {@link com.dcspa.prism.codegen.AutoCode} si absent du body.
 */
public final class ReferentialPutHelper {

	private ReferentialPutHelper() {
	}

	public static <T> ResponseEntity<T> putPreservingAutoCode(
			Integer id,
			T body,
			Function<Integer, Optional<T>> findById,
			Function<T, T> save) {
		Optional<T> opt = findById.apply(id);
		if (opt.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		AutoCodePutMerge.preserveAutoCodeFromExisting(opt.get(), body);
		invokeSetId(body, id);
		return ResponseEntity.ok(save.apply(body));
	}

	private static void invokeSetId(Object body, Integer id) {
		try {
			body.getClass().getMethod("setId", Integer.class).invoke(body, id);
		} catch (ReflectiveOperationException e) {
			throw new IllegalStateException("setId(Integer) introuvable sur " + body.getClass().getName(), e);
		}
	}
}
