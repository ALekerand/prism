package com.dcspa.prism.controller;

import com.dcspa.prism.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/admin/dashboard")
@RequiredArgsConstructor
public class AdminDashboardController {

    private final CentreRepository centreRepository;
    private final AlphaRepository alphaRepository;
    private final CecRepository cecRepository;
    private final CpRepository cpRepository;
    private final SieRepository sieRepository;
    private final PersonnelRepository personnelRepository;
    private final AppUserRepository appUserRepository;
    private final AppRoleRepository appRoleRepository;

    @GetMapping
    public ResponseEntity<Map<String, Object>> summary() {
        return ResponseEntity.ok(Map.of(
                "centresTotal", centreRepository.count(),
                "alphaTotal", alphaRepository.count(),
                "cecTotal", cecRepository.count(),
                "cpTotal", cpRepository.count(),
                "sieTotal", sieRepository.count(),
                "personnelTotal", personnelRepository.count(),
                "usersTotal", appUserRepository.count(),
                "rolesTotal", appRoleRepository.count()
        ));
    }
}

