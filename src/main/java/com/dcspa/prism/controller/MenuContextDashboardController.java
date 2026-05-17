package com.dcspa.prism.controller;

import com.dcspa.prism.security.AuthUser;
import com.dcspa.prism.service.MenuContextDashboardService;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/menu-dashboard")
@RequiredArgsConstructor
public class MenuContextDashboardController {

	private final MenuContextDashboardService menuContextDashboardService;

	@GetMapping
	public ResponseEntity<Map<String, Object>> dashboard(
			@AuthenticationPrincipal AuthUser user,
			@RequestParam String module,
			@RequestParam(required = false) String centreType,
			@RequestParam(required = false) Integer centreId,
			@RequestParam(required = false) String subModule,
			@RequestParam(required = false) String apiPath) {
		return ResponseEntity.ok(
				menuContextDashboardService.build(user, module, centreType, centreId, subModule, apiPath));
	}
}
