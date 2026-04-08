package com.dcspa.prism.service;

import com.dcspa.prism.dto.AppUserAdminResponse;
import com.dcspa.prism.dto.AppUserAdminUpsertRequest;
import com.dcspa.prism.dto.AppUserUpdateRolesRequest;
import com.dcspa.prism.entity.AppRole;
import com.dcspa.prism.entity.AppUser;
import com.dcspa.prism.repository.AppRoleRepository;
import com.dcspa.prism.repository.AppUserRepository;
import com.dcspa.prism.service.pagination.PageableUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AppUserAdminService {

    private final AppUserRepository appUserRepository;
    private final AppRoleRepository appRoleRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public Page<AppUserAdminResponse> findAllWithRoles(Pageable pageable) {
        Pageable p = PageableUtils.cap(pageable);
        return appUserRepository.findAll(p).map(this::toDto);
    }

    @Transactional
    public void updateUserRoles(Integer userId, AppUserUpdateRolesRequest request) {
        AppUser user = appUserRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Utilisateur introuvable: " + userId));

        List<Integer> roleIds = normalizeSingleRoleIds(request.getRoleIds());

        if (roleIds.isEmpty()) {
            user.setRoles(new HashSet<>());
            appUserRepository.save(user);
            return;
        }

        Set<AppRole> roles = new HashSet<>(appRoleRepository.findAllById(roleIds));
        if (roles.size() != roleIds.size()) {
            throw new IllegalArgumentException("Un ou plusieurs rôles sont introuvables");
        }

        user.setRoles(roles);
        appUserRepository.save(user);
    }

    @Transactional
    public AppUserAdminResponse createUser(AppUserAdminUpsertRequest request) {
        if (request == null) throw new IllegalArgumentException("Requête obligatoire");
        String username = String.valueOf(request.getUsername() == null ? "" : request.getUsername()).trim();
        if (username.isBlank()) throw new IllegalArgumentException("username est obligatoire");
        if (appUserRepository.existsByUsername(username)) throw new IllegalArgumentException("username existe déjà");
        String password = String.valueOf(request.getPassword() == null ? "" : request.getPassword());
        if (password.isBlank()) throw new IllegalArgumentException("password est obligatoire");

        AppUser u = new AppUser();
        u.setUsername(username);
        u.setEmail(request.getEmail());
        u.setActif(Boolean.TRUE.equals(request.getActif()));
        u.setPasswordHash(passwordEncoder.encode(password));
        u.setRoles(resolveRoles(request.getRoleIds()));
        return toDto(appUserRepository.save(u));
    }

    @Transactional
    public AppUserAdminResponse updateUser(Integer id, AppUserAdminUpsertRequest request) {
        if (request == null) throw new IllegalArgumentException("Requête obligatoire");
        AppUser u = appUserRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Utilisateur introuvable: " + id));

        String username = String.valueOf(request.getUsername() == null ? "" : request.getUsername()).trim();
        if (username.isBlank()) throw new IllegalArgumentException("username est obligatoire");
        if (!Objects.equals(u.getUsername(), username) && appUserRepository.existsByUsername(username)) {
            throw new IllegalArgumentException("username existe déjà");
        }

        u.setUsername(username);
        u.setEmail(request.getEmail());
        u.setActif(Boolean.TRUE.equals(request.getActif()));

        if (request.getPassword() != null && !request.getPassword().isBlank()) {
            u.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        }

        if (request.getRoleIds() != null) {
            u.setRoles(resolveRoles(request.getRoleIds()));
        }

        return toDto(appUserRepository.save(u));
    }

    @Transactional
    public void deleteUser(Integer id) {
        if (!appUserRepository.existsById(id)) {
            throw new IllegalArgumentException("Utilisateur introuvable: " + id);
        }
        appUserRepository.deleteById(id);
    }

    private Set<AppRole> resolveRoles(List<Integer> roleIds) {
        List<Integer> normalized = normalizeSingleRoleIds(roleIds);
        if (normalized.isEmpty()) return new HashSet<>();
        Set<AppRole> roles = new HashSet<>(appRoleRepository.findAllById(normalized));
        if (roles.size() != normalized.size()) {
            throw new IllegalArgumentException("Un ou plusieurs rôles sont introuvables");
        }
        return roles;
    }

    private List<Integer> normalizeSingleRoleIds(List<Integer> roleIds) {
        if (roleIds == null) return List.of();
        List<Integer> normalized = roleIds.stream()
                .filter(Objects::nonNull)
                .distinct()
                .toList();
        if (normalized.size() > 1) {
            throw new IllegalArgumentException("Un utilisateur ne peut avoir qu'un seul rôle.");
        }
        return normalized;
    }

    private AppUserAdminResponse toDto(AppUser u) {
        List<Integer> roleIds = u.getRoles()
                .stream()
                .map(AppRole::getId)
                .collect(Collectors.toList());

        return AppUserAdminResponse.builder()
                .id(u.getId())
                .username(u.getUsername())
                .email(u.getEmail())
                .actif(u.getActif())
                .roleIds(roleIds)
                .build();
    }
}

