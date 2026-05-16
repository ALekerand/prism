package com.dcspa.prism.controller;

import com.dcspa.prism.dto.AppUserAdminResponse;
import com.dcspa.prism.dto.AppUserAdminUpsertRequest;
import com.dcspa.prism.dto.AppUserUpdateRolesRequest;
import com.dcspa.prism.service.AppUserAdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
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

    private final AppUserAdminService appUserAdminService;

    @GetMapping
    public ResponseEntity<Page<AppUserAdminResponse>> findAll(
            @RequestParam(required = false) String q,
            @RequestParam(required = false) Integer roleId,
            @RequestParam(required = false) Boolean actif,
            @PageableDefault(size = 20, sort = "id") Pageable pageable) {
        return ResponseEntity.ok(appUserAdminService.findAllWithRoles(pageable, q, roleId, actif));
    }

    @PostMapping
    public ResponseEntity<AppUserAdminResponse> create(@RequestBody AppUserAdminUpsertRequest request) {
        return ResponseEntity.status(201).body(appUserAdminService.createUser(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AppUserAdminResponse> update(
            @PathVariable Integer id,
            @RequestBody AppUserAdminUpsertRequest request) {
        return ResponseEntity.ok(appUserAdminService.updateUser(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        if (!appUserAdminService.deleteUserIfExists(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Utilisateur introuvable.");
        }
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/roles")
    public ResponseEntity<Void> updateRoles(
            @PathVariable Integer id,
            @RequestBody AppUserUpdateRolesRequest request) {
        appUserAdminService.updateUserRoles(id, request);
        return ResponseEntity.noContent().build();
    }
}
