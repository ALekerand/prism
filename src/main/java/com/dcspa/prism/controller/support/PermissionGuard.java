package com.dcspa.prism.controller.support;

import com.dcspa.prism.security.AuthUser;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public final class PermissionGuard {
	private PermissionGuard() {
	}

	public static ResponseEntity<?> require(AuthUser user, String fonctionnalite, String permission) {
		if (user != null && user.hasPermission(fonctionnalite, permission)) {
			return null;
		}
		return ResponseEntity.status(HttpStatus.FORBIDDEN)
				.body(Map.of("message", "Accès refusé: permission " + fonctionnalite + ":" + permission + " requise"));
	}
}
