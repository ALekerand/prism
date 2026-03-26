package com.dcspa.prism.controller;

import com.dcspa.prism.dto.AppUserAdminResponse;
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

    @PutMapping("/{id}/roles")
    public ResponseEntity<Void> updateRoles(
            @PathVariable Integer id,
            @RequestBody AppUserUpdateRolesRequest request
    ) {
        appUserAdminService.updateUserRoles(id, request);
        return ResponseEntity.noContent().build();
    }
}

