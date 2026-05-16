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
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Bean
	public JwtAuthFilter jwtAuthFilter(JwtUtil jwtUtil, AuthService authService) {
		return new JwtAuthFilter(jwtUtil, authService);
	}

	@Bean
	public SaisieWorkflowEditGuardFilter saisieWorkflowEditGuardFilter(SaisieWorkflowService workflowService) {
		return new SaisieWorkflowEditGuardFilter(workflowService);
	}

	@Bean
	public SecurityFilterChain securityFilterChain(
			HttpSecurity http,
			JwtAuthFilter jwtAuthFilter,
			SaisieWorkflowEditGuardFilter saisieWorkflowEditGuardFilter) throws Exception {
		return http
				.csrf(AbstractHttpConfigurer::disable)
				.cors(Customizer.withDefaults())
				.httpBasic(AbstractHttpConfigurer::disable)
				.formLogin(AbstractHttpConfigurer::disable)
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.authorizeHttpRequests(auth -> auth
						.requestMatchers(HttpMethod.POST, "/api/auth/login").permitAll()
						.requestMatchers("/api/auth/**").permitAll()
						.requestMatchers("/v3/api-docs", "/v3/api-docs/**").permitAll()
						.requestMatchers("/swagger-ui.html", "/swagger-ui/**").permitAll()
						.anyRequest().authenticated())
				.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
				.addFilterAfter(saisieWorkflowEditGuardFilter, JwtAuthFilter.class)
				.build();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

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
