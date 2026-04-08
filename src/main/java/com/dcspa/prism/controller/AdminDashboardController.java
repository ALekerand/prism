package com.dcspa.prism.controller;

import com.dcspa.prism.service.AdminDashboardService;
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

    private final AdminDashboardService adminDashboardService;

    // Expose les compteurs agrégés (centres, effectifs, utilisateurs, etc.).
    @GetMapping
    public ResponseEntity<Map<String, Object>> summary() {
        return ResponseEntity.ok(adminDashboardService.buildSummary());
    }
}

