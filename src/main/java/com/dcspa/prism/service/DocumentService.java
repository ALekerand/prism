package com.dcspa.prism.service;

import com.dcspa.prism.dto.DocumentListFilter;
import com.dcspa.prism.dto.DocumentListItem;
import com.dcspa.prism.entity.Document;
import com.dcspa.prism.repository.DocumentRepository;
import com.dcspa.prism.repository.spec.DocumentSpecifications;
import com.dcspa.prism.service.pagination.PageableUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DocumentService {

	private final DocumentRepository documentRepository;

	@Transactional(readOnly = true)
	public List<Document> findAll() {
		return documentRepository.findAll();
	}

	/** Liste paginée pour le module Visites (même donnée que {@code Document}, forme aplatie). */
	@Transactional(readOnly = true)
	public Page<DocumentListItem> findAllListItems(Pageable pageable, DocumentListFilter filter) {
		Pageable p = PageableUtils.cap(pageable);
		Specification<Document> spec = DocumentSpecifications.fromFilter(filter);
		return documentRepository.findAll(spec, p).map(DocumentListItemMapper::fromDocument);
	}

	@Transactional(readOnly = true)
	public Optional<Document> findById(Integer id) {
		return documentRepository.findById(id);
	}

	@Transactional
	public Document save(Document entity) {
		validateRequiredFields(entity);
		return documentRepository.save(entity);
	}

	@Transactional
	public void deleteById(Integer id) {
		documentRepository.deleteById(id);
	}

	private void validateRequiredFields(Document entity) {
		if (entity.getIdNatureDocument() == null) {
			throw new IllegalArgumentException("La nature du document est obligatoire.");
		}
		if (entity.getIdTypeDocument() == null) {
			throw new IllegalArgumentException("Le type de document est obligatoire.");
		}
		if (entity.getIdCentre() == null) {
			throw new IllegalArgumentException("Le centre (Alpha) est obligatoire pour un document.");
		}
	}
}
