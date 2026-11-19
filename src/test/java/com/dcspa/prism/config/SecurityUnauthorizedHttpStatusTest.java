package com.dcspa.prism.config;

import static org.assertj.core.api.Assertions.assertThat;

import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Vérifie la règle métier : accès anonyme → 401 (pas 403) pour que le front déconnecte proprement.
 */
class SecurityUnauthorizedHttpStatusTest {

	@Test
	void anonymousAccessDeniedMapsToUnauthorized() {
		SecurityContextHolder.clearContext();
		int status = resolveDeniedStatusForCurrentContext();
		assertThat(status).isEqualTo(HttpServletResponse.SC_UNAUTHORIZED);
	}

	@Test
	void authenticatedAccessDeniedMapsToForbidden() {
		SecurityContextHolder.getContext().setAuthentication(
				new TestingAuthenticationToken("user", "n/a", new SimpleGrantedAuthority("ROLE_USER")));
		int status = resolveDeniedStatusForCurrentContext();
		assertThat(status).isEqualTo(HttpServletResponse.SC_FORBIDDEN);
	}

	private static int resolveDeniedStatusForCurrentContext() {
		var authentication = SecurityContextHolder.getContext().getAuthentication();
		boolean anonymous = authentication == null
				|| !authentication.isAuthenticated()
				|| authentication instanceof AnonymousAuthenticationToken;
		return anonymous ? HttpServletResponse.SC_UNAUTHORIZED : HttpServletResponse.SC_FORBIDDEN;
	}
}