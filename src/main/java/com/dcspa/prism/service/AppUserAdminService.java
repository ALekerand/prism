package com.dcspa.prism.service;

import com.dcspa.prism.dto.AppUserAdminResponse;
import com.dcspa.prism.dto.AppUserUpdateRolesRequest;
import com.dcspa.prism.entity.AppRole;
import com.dcspa.prism.entity.AppUser;
import com.dcspa.prism.repository.AppRoleRepository;
import com.dcspa.prism.repository.AppUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AppUserAdminService {

    private final AppUserRepository appUserRepository;
    private final AppRoleRepository appRoleRepository;

    @Transactional(readOnly = true)
    public List<AppUserAdminResponse> findAllWithRoles() {
        // AppUserRepository.findAll() est surchargé avec @EntityGraph(roles)
        return appUserRepository.findAll()
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public void updateUserRoles(Integer userId, AppUserUpdateRolesRequest request) {
        AppUser user = appUserRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Utilisateur introuvable: " + userId));

        List<Integer> roleIds = request.getRoleIds();
        if (roleIds == null) {
            roleIds = List.of();
        }

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

