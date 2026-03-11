package com.dcspa.prism.service;

import com.dcspa.prism.entity.Document;
import com.dcspa.prism.repository.DocumentRepository;
import lombok.RequiredArgsConstructor;
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
