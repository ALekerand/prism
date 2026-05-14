package com.dcspa.prism.config;

import com.dcspa.prism.security.JwtAuthFilter;
import com.dcspa.prism.security.JwtUtil;
import com.dcspa.prism.security.SaisieWorkflowEditGuardFilter;
import com.dcspa.prism.service.AuthService;
import com.dcspa.prism.service.SaisieWorkflowService;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

	@Bean
	public SaisieWorkflowEditGuardFilter saisieWorkflowEditGuardFilter(SaisieWorkflowService workflowService) {
		return new SaisieWorkflowEditGuardFilter(workflowService);
	}

	@Bean
	public SecurityWebFilterChain securityWebFilterChain(
			ServerHttpSecurity http,
			JwtUtil jwtUtil,
			AuthService authService,
			SaisieWorkflowEditGuardFilter saisieWorkflowEditGuardFilter) {
		JwtAuthFilter jwtAuthFilter = new JwtAuthFilter(jwtUtil, authService);

		return http
				.csrf(ServerHttpSecurity.CsrfSpec::disable)
				.cors(Customizer.withDefaults())
				.httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
				.formLogin(ServerHttpSecurity.FormLoginSpec::disable)
				.securityContextRepository(NoOpServerSecurityContextRepository.getInstance())
				.authorizeExchange(exchange -> exchange
						.pathMatchers(HttpMethod.POST, "/api/auth/login").permitAll()
						.pathMatchers("/api/auth/**").permitAll()
						.pathMatchers("/v3/api-docs", "/v3/api-docs/**").permitAll()
						.pathMatchers("/swagger-ui.html", "/swagger-ui/**").permitAll()
						.anyExchange().authenticated()
				)
				.addFilterAt(jwtAuthFilter, SecurityWebFiltersOrder.AUTHENTICATION)
				.addFilterAfter(saisieWorkflowEditGuardFilter, SecurityWebFiltersOrder.AUTHENTICATION)
				.build();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	/**
	 * Autorise le front Angular (dev et variantes localhost) à appeler l'API depuis le navigateur.
	 */
	@Bean
	public CorsConfigurationSource corsConfigurationSource(AppCorsProperties appCorsProperties) {
		CorsConfiguration config = new CorsConfiguration();
		config.setAllowedOriginPatterns(appCorsProperties.effectivePatterns());
		config.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
		config.setAllowedHeaders(List.of("*"));
		config.setAllowCredentials(false);
		config.setMaxAge(3600L);
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", config);
		return source;
	}
}
