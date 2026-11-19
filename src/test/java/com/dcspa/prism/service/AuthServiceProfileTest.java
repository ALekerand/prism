package com.dcspa.prism.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.dcspa.prism.dto.UpdateProfileRequest;
import com.dcspa.prism.entity.AppUser;
import com.dcspa.prism.repository.AppUserRepository;
import com.dcspa.prism.security.AuthUser;
import com.dcspa.prism.security.AuthUserCache;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class AuthServiceProfileTest {

    @Mock
    private AppUserRepository appUserRepository;
    @Mock
    private com.dcspa.prism.security.JwtUtil jwtUtil;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private AdminDashboardService adminDashboardService;
    @Mock
    private AuthUserCache authUserCache;

    @InjectMocks
    private AuthService authService;

    @Test
    void changeOwnPasswordRejectsWrongCurrent() {
        AppUser user = user("admin", "hash");
        AuthUser principal = principal("admin");
        when(appUserRepository.findByUsername("admin")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("old", "hash")).thenReturn(false);

        assertThrows(IllegalArgumentException.class,
                () -> authService.changeOwnPassword(principal, "old", "newpass12"));
    }

    @Test
    void changeOwnPasswordEncodesAndInvalidatesCache() {
        AppUser user = user("admin", "hash");
        user.setMustChangePassword(true);
        AuthUser principal = principal("admin");
        when(appUserRepository.findByUsername("admin")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("oldpass1", "hash")).thenReturn(true);
        when(passwordEncoder.matches("newpass12", "hash")).thenReturn(false);
        when(passwordEncoder.encode("newpass12")).thenReturn("newhash");

        authService.changeOwnPassword(principal, "oldpass1", "newpass12");

        assertEquals("newhash", user.getPasswordHash());
        assertEquals(Boolean.FALSE, user.getMustChangePassword());
        verify(appUserRepository).save(user);
        verify(authUserCache).invalidate("admin");
    }

    @Test
    void updateOwnProfilePersistsIdentity() {
        AppUser user = user("conseiller_test", "hash");
        AuthUser principal = principal("conseiller_test");
        when(appUserRepository.findByUsername("conseiller_test")).thenReturn(Optional.of(user));
        Map<String, Object> scope = new LinkedHashMap<>();
        scope.put("nationalView", false);
        scope.put("scopeMode", "IEP");
        scope.put("scopeLabel", "IEPP");
        when(adminDashboardService.buildScopeMeta(any())).thenReturn(scope);

        UpdateProfileRequest req = new UpdateProfileRequest();
        req.setNom("Kouassi");
        req.setPrenoms("Awa");
        req.setEmail("awa@example.ci");

        Map<String, Object> payload = authService.updateOwnProfile(principal, req);
        assertEquals("Kouassi", user.getNom());
        assertEquals("Awa", user.getPrenoms());
        assertEquals("awa@example.ci", user.getEmail());
        assertEquals("Kouassi", payload.get("nom"));
        verify(authUserCache).invalidate("conseiller_test");
    }

    private static AppUser user(String username, String hash) {
        AppUser user = new AppUser();
        user.setId(7);
        user.setUsername(username);
        user.setPasswordHash(hash);
        user.setActif(true);
        return user;
    }

    private static AuthUser principal(String username) {
        return new AuthUser(7, username, "hash", true, List.of(), List.of(), null, null, null, null, null, null, null);
    }
}
