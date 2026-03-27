package com.dcspa.prism.controller;

import com.dcspa.prism.dto.AppUserAdminResponse;
import com.dcspa.prism.dto.AppUserAdminUpsertRequest;
import com.dcspa.prism.dto.AppUserUpdateRolesRequest;
import com.dcspa.prism.service.AppUserAdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/app-users")
@RequiredArgsConstructor
public class AppUserAdminController {

    private final AppUserAdminService appUserAdminService;

    @GetMapping
    public ResponseEntity<List<AppUserAdminResponse>> findAll() {
        return ResponseEntity.ok(appUserAdminService.findAllWithRoles());
    }

    @PostMapping
    public ResponseEntity<AppUserAdminResponse> create(@RequestBody AppUserAdminUpsertRequest request) {
        return ResponseEntity.status(201).body(appUserAdminService.createUser(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AppUserAdminResponse> update(
            @PathVariable Integer id,
            @RequestBody AppUserAdminUpsertRequest request
    ) {
        return ResponseEntity.ok(appUserAdminService.updateUser(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        appUserAdminService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/roles")
    public ResponseEntity<Void> updateRoles(
            @PathVariable Integer id,
            @RequestBody AppUserUpdateRolesRequest request
    ) {
        appUserAdminService.updateUserRoles(id, request);
        return ResponseEntity.noContent().build();
    }
}

