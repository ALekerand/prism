package com.dcspa.prism.controller;

import com.dcspa.prism.dto.DocumentListFilter;
import com.dcspa.prism.dto.DocumentListItem;
import com.dcspa.prism.service.DocumentService;
import lombok.RequiredArgsConstructor;
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
 * Les écrans « conseiller / superviseur / IEPP » peuvent réutiliser cette liste en filtrant côté client
 * jusqu’à l’arrivée de règles métier dédiées.
 */
@RestController
@RequestMapping("/api/visites")
@RequiredArgsConstructor
public class VisitesController {

	private final DocumentService documentService;

	@GetMapping
	public ResponseEntity<Page<DocumentListItem>> list(
			@PageableDefault(size = 20, sort = "id") Pageable pageable,
			@ModelAttribute DocumentListFilter filter) {
		return ResponseEntity.ok(documentService.findAllListItems(pageable, filter));
	}
}
