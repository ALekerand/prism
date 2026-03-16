package com.dcspa.prism.service;

import com.dcspa.prism.dto.LoginRequest;
import com.dcspa.prism.dto.LoginResponse;
import com.dcspa.prism.entity.AppRole;
import com.dcspa.prism.entity.AppUser;
import com.dcspa.prism.entity.RoleFonctionnalitePermission;
import com.dcspa.prism.repository.AppUserRepository;
import com.dcspa.prism.security.AuthUser;
import com.dcspa.prism.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthService implements UserDetailsService {

    private final AppUserRepository appUserRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser user = appUserRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé: " + username));
        return toAuthUser(user);
    }

    @Transactional(readOnly = true)
    public AuthUser loadUserWithPermissions(String username) {
        AppUser user = appUserRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé: " + username));
        return toAuthUser(user);
    }

    public LoginResponse login(LoginRequest request) {
        UserDetails userDetails = loadUserByUsername(request.getUsername());
        if (!passwordEncoder.matches(request.getPassword(), userDetails.getPassword())) {
            throw new IllegalArgumentException("Identifiants incorrects");
        }
        AuthUser authUser = (AuthUser) userDetails;
        if (!authUser.isEnabled()) {
            throw new IllegalArgumentException("Compte désactivé");
        }
        String token = jwtUtil.generateToken(authUser.getUsername());
        AppUser user = appUserRepository.findByUsername(authUser.getUsername()).orElseThrow();
        List<String> roleCodes = user.getRoles().stream()
                .map(AppRole::getCodeRole)
                .collect(Collectors.toList());
        return LoginResponse.builder()
                .token(token)
                .type("Bearer")
                .userId(authUser.getUserId())
                .username(authUser.getUsername())
                .email(user.getEmail())
                .roles(roleCodes)
                .permissions(authUser.getPermissions())
                .build();
    }

    private AuthUser toAuthUser(AppUser user) {
        List<String> permissions = new ArrayList<>();
        for (AppRole role : user.getRoles()) {
            for (RoleFonctionnalitePermission rfp : role.getRoleFonctionnalitePermissions()) {
                String key = rfp.getFonctionnalite().getCodeFonctionnalite() + ":"
                        + rfp.getPermission().getCodePermission();
                if (!permissions.contains(key)) {
                    permissions.add(key);
                }
            }
        }
        return new AuthUser(
                user.getId(),
                user.getUsername(),
                user.getPasswordHash(),
                Boolean.TRUE.equals(user.getActif()),
                permissions
        );
    }
}
