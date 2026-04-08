package com.dcspa.prism.controller;

import com.dcspa.prism.dto.LoginRequest;
import com.dcspa.prism.dto.LoginResponse;
import com.dcspa.prism.security.AuthUser;
import com.dcspa.prism.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    // Authentifie l’utilisateur et renvoie le jeton JWT.
    @PostMapping("/login")
    public Mono<ResponseEntity<LoginResponse>> login(@Valid @RequestBody LoginRequest request) {
        return Mono.fromCallable(() -> authService.login(request))
                .map(ResponseEntity::ok)
                .onErrorResume(e -> e instanceof IllegalArgumentException || e instanceof UsernameNotFoundException,
                        e -> Mono.just(ResponseEntity.status(401).build()));
    }

    // Retourne l’identité et les permissions du jeton courant.
    @GetMapping("/me")
    public Mono<ResponseEntity<Map<String, Object>>> me(@AuthenticationPrincipal AuthUser user) {
        if (user == null) {
            return Mono.just(ResponseEntity.status(401).build());
        }
        return Mono.just(ResponseEntity.ok(authService.buildAuthenticatedUserPayload(user)));
    }
}
