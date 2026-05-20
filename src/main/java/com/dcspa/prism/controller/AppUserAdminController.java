package com.dcspa.prism.controller;

import com.dcspa.prism.controller.support.PermissionGuard;
import com.dcspa.prism.dto.AppUserAdminResponse;
import com.dcspa.prism.dto.AppUserAdminUpsertRequest;
import com.dcspa.prism.dto.AppUserUpdateRolesRequest;
import com.dcspa.prism.security.AuthUser;
import com.dcspa.prism.service.AppUserAdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/app-users")
@RequiredArgsConstructor
public class AppUserAdminController {

    private static final String FEATURE = "ADMIN_UTILISATEURS";

    private final AppUserAdminService appUserAdminService;

    @GetMapping
    public ResponseEntity<?> findAll(
            @RequestParam(required = false) String q,
            @RequestParam(required = false) Integer roleId,
            @RequestParam(required = false) Boolean actif,
            @PageableDefault(size = 20, sort = "id") Pageable pageable,
            @AuthenticationPrincipal AuthUser user) {
        ResponseEntity<?> denied = PermissionGuard.require(user, FEATURE, "LIRE");
        if (denied != null) {
            return denied;
        }
        return ResponseEntity.ok(appUserAdminService.findAllWithRoles(pageable, q, roleId, actif, user));
    }

    @PostMapping
    public ResponseEntity<?> create(
            @RequestBody AppUserAdminUpsertRequest request,
            @AuthenticationPrincipal AuthUser user) {
        ResponseEntity<?> denied = PermissionGuard.require(user, FEATURE, "CREER");
        if (denied != null) {
            return denied;
        }
        return ResponseEntity.status(201).body(appUserAdminService.createUser(request, user));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(
            @PathVariable Integer id,
            @RequestBody AppUserAdminUpsertRequest request,
            @AuthenticationPrincipal AuthUser user) {
        ResponseEntity<?> denied = PermissionGuard.require(user, FEATURE, "MODIFIER");
        if (denied != null) {
            return denied;
        }
        return ResponseEntity.ok(appUserAdminService.updateUser(id, request, user));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(
            @PathVariable Integer id,
            @AuthenticationPrincipal AuthUser user) {
        ResponseEntity<?> denied = PermissionGuard.require(user, FEATURE, "SUPPRIMER");
        if (denied != null) {
            return denied;
        }
        if (!appUserAdminService.deleteUserIfExists(id, user)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Utilisateur introuvable.");
        }
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/roles")
    public ResponseEntity<?> updateRoles(
            @PathVariable Integer id,
            @RequestBody AppUserUpdateRolesRequest request,
            @AuthenticationPrincipal AuthUser user) {
        ResponseEntity<?> denied = PermissionGuard.require(user, FEATURE, "MODIFIER");
        if (denied != null) {
            return denied;
        }
        appUserAdminService.updateUserRoles(id, request, user);
        return ResponseEntity.noContent().build();
    }
}
