package com.dcspa.prism.controller;

import com.dcspa.prism.dto.DocumentListFilter;
import com.dcspa.prism.dto.DocumentListItem;
import com.dcspa.prism.security.AuthUser;
import com.dcspa.prism.service.DocumentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Consultation des fiches de suivi rattachées aux centres Alpha ({@code document}).
 * Liste filtrée par circonscription (IEP / DRENA) selon le profil connecté.
 */
@RestController
@RequestMapping("/api/visites")
@RequiredArgsConstructor
public class VisitesController {

	private final DocumentService documentService;

	@GetMapping
	public ResponseEntity<Page<DocumentListItem>> list(
			@PageableDefault(size = 20, sort = "id") Pageable pageable,
			@ModelAttribute DocumentListFilter filter,
			@AuthenticationPrincipal AuthUser user) {
		return ResponseEntity.ok(documentService.findAllListItems(pageable, filter, user));
	}
}
