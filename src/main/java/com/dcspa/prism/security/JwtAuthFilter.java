package com.dcspa.prism.security;

import com.dcspa.prism.service.AuthService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

public class JwtAuthFilter extends OncePerRequestFilter {

    private static final String BEARER_PREFIX = "Bearer ";

    private final JwtUtil jwtUtil;
    private final AuthService authService;
    private final AuthUserCache authUserCache;

    public JwtAuthFilter(JwtUtil jwtUtil, AuthService authService, AuthUserCache authUserCache) {
        this.jwtUtil = jwtUtil;
        this.authService = authService;
        this.authUserCache = authUserCache;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        if (HttpMethod.OPTIONS.matches(request.getMethod())) {
            return true;
        }
        String uri = request.getRequestURI();
        return uri != null && uri.endsWith("/api/auth/login");
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {
        String token = extractToken(request);
        if (StringUtils.hasText(token) && jwtUtil.validateToken(token)) {
            String username = jwtUtil.getUsernameFromToken(token);
            Authentication existing = SecurityContextHolder.getContext().getAuthentication();
            if (!(existing instanceof JwtAuthenticationToken jwtAuth)
                    || !username.equals(jwtAuth.getName())) {
                AuthUser authUser = authUserCache.get(username);
                if (authUser == null) {
                    authUser = authService.loadUserWithPermissions(username);
                    authUserCache.put(username, authUser);
                }
                Authentication auth = new JwtAuthenticationToken(authUser, authUser.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        }
        filterChain.doFilter(request, response);
    }

    private String extractToken(HttpServletRequest request) {
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (StringUtils.hasText(header) && header.startsWith(BEARER_PREFIX)) {
            return header.substring(BEARER_PREFIX.length()).trim();
        }
        return null;
    }
}
