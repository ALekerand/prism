package com.dcspa.prism.config;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import com.dcspa.prism.entity.AppRole;
import com.dcspa.prism.entity.AppUser;
import com.dcspa.prism.entity.Fonctionnalite;
import com.dcspa.prism.entity.Permission;
import com.dcspa.prism.repository.AppRoleRepository;
import com.dcspa.prism.repository.AppUserRepository;
import com.dcspa.prism.repository.FonctionnaliteRepository;
import com.dcspa.prism.repository.PermissionRepository;
import com.dcspa.prism.repository.IeppRepository;
import com.dcspa.prism.repository.RoleFonctionnalitePermissionRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class ActivitesCentreRbacInitializerTest {

    @Mock
    private AppRoleRepository appRoleRepository;

    @Mock
    private AppUserRepository appUserRepository;

    @Mock
    private FonctionnaliteRepository fonctionnaliteRepository;

    @Mock
    private PermissionRepository permissionRepository;

    @Mock
    private RoleFonctionnalitePermissionRepository roleFonctionnalitePermissionRepository;

    @Mock
    private IeppRepository ieppRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    private final List<AppUser> savedUsers = new ArrayList<>();

    @BeforeEach
    void setUp() {
        when(permissionRepository.findByCodePermission(anyString()))
                .thenAnswer(invocation -> Optional.of(permission(invocation.getArgument(0))));
        when(fonctionnaliteRepository.findByCodeFonctionnalite(anyString()))
                .thenAnswer(invocation -> Optional.of(fonctionnalite(invocation.getArgument(0))));
        when(appRoleRepository.findByCodeRole(anyString()))
                .thenAnswer(invocation -> Optional.of(role(invocation.getArgument(0))));
        when(roleFonctionnalitePermissionRepository.existsByRole_IdAndFonctionnalite_IdAndPermission_Id(
                any(), any(), any())).thenReturn(true);
        when(permissionRepository.findAll()).thenReturn(List.of(permission("LIRE"), permission("CREER"), permission("MODIFIER"), permission("VALIDER")));
        when(fonctionnaliteRepository.findAll()).thenReturn(List.of(fonctionnalite("POINTS_VISITES")));
        when(appUserRepository.findByUsername(anyString())).thenReturn(Optional.empty());
        when(passwordEncoder.encode("123456")).thenReturn("encoded-123456");
        when(appUserRepository.save(any(AppUser.class))).thenAnswer(invocation -> {
            AppUser user = invocation.getArgument(0);
            savedUsers.add(user);
            return user;
        });
        when(ieppRepository.findAll()).thenReturn(List.of());
    }

    @Test
    void createsValidationWorkflowTestUsersWithExpectedRoles() {
        initializer().initActivitesCentrePermissions();

        assertThat(savedUsers)
                .extracting(AppUser::getUsername)
                .containsExactlyInAnyOrderElementsOf(expectedDemoUsernames());
        assertTestUser("conseiller_test", "CONSEILLER");
        assertTestUser("coordonnateur_test", "COORDONNATEUR");
        assertTestUser("iepp_test", "IEPP");
        assertTestUser("superviseur_test", "SUPERVISEUR");
        assertTestUser("superviseur_aenf_test", "SUPERVISEUR_AENF");
    }

    private static List<String> expectedDemoUsernames() {
        return List.of(
                "conseiller_test",
                "coordonnateur_test",
                "iepp_test",
                "superviseur_test",
                "superviseur_aenf_test");
    }

    private ActivitesCentreRbacInitializer initializer() {
        return new ActivitesCentreRbacInitializer(
                appRoleRepository,
                appUserRepository,
                fonctionnaliteRepository,
                permissionRepository,
                roleFonctionnalitePermissionRepository,
                ieppRepository,
                passwordEncoder
        );
    }

    private void assertTestUser(String username, String roleCode) {
        AppUser user = savedUsers.stream()
                .filter(saved -> username.equals(saved.getUsername()))
                .findFirst()
                .orElseThrow();
        assertThat(user.getPasswordHash()).isEqualTo("encoded-123456");
        assertThat(user.getActif()).isTrue();
        assertThat(user.getRoles()).singleElement().extracting(AppRole::getCodeRole).isEqualTo(roleCode);
    }

    private Permission permission(String code) {
        Permission permission = new Permission();
        permission.setId(Math.abs(code.hashCode()));
        permission.setCodePermission(code);
        permission.setLibellePermission(code);
        return permission;
    }

    private Fonctionnalite fonctionnalite(String code) {
        Fonctionnalite fonctionnalite = new Fonctionnalite();
        fonctionnalite.setId(Math.abs(code.hashCode()));
        fonctionnalite.setCodeFonctionnalite(code);
        fonctionnalite.setLibelleFonctionnalite(code);
        return fonctionnalite;
    }

    private AppRole role(String code) {
        AppRole role = new AppRole();
        role.setId(Math.abs(code.hashCode()));
        role.setCodeRole(code);
        role.setLibelleRole(code);
        return role;
    }
}
