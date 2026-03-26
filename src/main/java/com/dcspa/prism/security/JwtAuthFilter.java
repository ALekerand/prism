package com.dcspa.prism.security;

import com.dcspa.prism.service.AuthService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

public class JwtAuthFilter implements WebFilter {

    private static final String BEARER_PREFIX = "Bearer ";

    private final JwtUtil jwtUtil;
    private final AuthService authService;

    public JwtAuthFilter(JwtUtil jwtUtil, AuthService authService) {
        this.jwtUtil = jwtUtil;
        this.authService = authService;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        String token = extractToken(exchange.getRequest());
        if (!StringUtils.hasText(token)) {
            return chain.filter(exchange);
        }
        if (!jwtUtil.validateToken(token)) {
            return chain.filter(exchange);
        }
        String username = jwtUtil.getUsernameFromToken(token);
        // chain.filter → Mono<Void>: no onNext, so switchIfEmpty after flatMap would run the chain twice (204 then UOE).
        return Mono.fromCallable(() -> authService.loadUserWithPermissions(username))
                .subscribeOn(Schedulers.boundedElastic())
                .flatMap(authUser -> {
                    Authentication auth = new JwtAuthenticationToken(authUser, authUser.getAuthorities());
                    return chain.filter(exchange)
                            .contextWrite(ReactiveSecurityContextHolder.withAuthentication(auth));
                });
    }

    private String extractToken(ServerHttpRequest request) {
        String header = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (StringUtils.hasText(header) && header.startsWith(BEARER_PREFIX)) {
            return header.substring(BEARER_PREFIX.length()).trim();
        }
        return null;
    }
}
