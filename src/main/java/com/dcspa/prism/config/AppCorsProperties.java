package com.dcspa.prism.config;

import java.util.List;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Origines CORS autorisées par profil ({@code application-local.properties},
 * {@code application-staging.properties}, etc.).
 */
@ConfigurationProperties(prefix = "app.cors")
public record AppCorsProperties(List<String> allowedOriginPatterns) {

	public List<String> effectivePatterns() {
		if (allowedOriginPatterns == null || allowedOriginPatterns.isEmpty()) {
			return List.of("http://localhost:*", "http://127.0.0.1:*");
		}
		return allowedOriginPatterns;
	}
}
