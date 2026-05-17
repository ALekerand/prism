package com.dcspa.prism.controller;

import com.dcspa.prism.security.AuthUser;
import com.dcspa.prism.service.AdminDashboardService;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/dashboard")
@RequiredArgsConstructor
public class AdminDashboardController {

    private final AdminDashboardService adminDashboardService;

    @GetMapping
    public ResponseEntity<Map<String, Object>> summary(@AuthenticationPrincipal AuthUser user) {
        return ResponseEntity.ok(adminDashboardService.buildSummary(user));
    }
}
