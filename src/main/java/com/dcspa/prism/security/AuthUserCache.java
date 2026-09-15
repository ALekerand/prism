package com.dcspa.prism.security;

import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Component;

/**
 * Cache court du principal authentifié. Évite de recharger le graphe rôles/permissions
 * à chaque requête HTTP (filtre JWT).
 */
@Component
public class AuthUserCache {

	private static final long TTL_MS = 60_000L;

	private final ConcurrentHashMap<String, Cached> values = new ConcurrentHashMap<>();

	private record Cached(AuthUser user, long expiresAt) {}

	public AuthUser get(String username) {
		if (username == null || username.isBlank()) {
			return null;
		}
		Cached cached = values.get(username);
		if (cached == null) {
			return null;
		}
		if (cached.expiresAt < System.currentTimeMillis()) {
			values.remove(username, cached);
			return null;
		}
		return cached.user;
	}

	public void put(String username, AuthUser user) {
		if (username == null || username.isBlank() || user == null) {
			return;
		}
		values.put(username, new Cached(user, System.currentTimeMillis() + TTL_MS));
	}

	public void invalidate(String username) {
		if (username != null) {
			values.remove(username);
		}
	}
}
