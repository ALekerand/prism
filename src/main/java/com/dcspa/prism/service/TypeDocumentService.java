package com.dcspa.prism.service;

import com.dcspa.prism.entity.TypeDocument;
import com.dcspa.prism.repository.TypeDocumentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TypeDocumentService {

	private final TypeDocumentRepository repository;

	@Transactional(readOnly = true)
	public List<TypeDocument> findAll() { return repository.findAll(); }

	@Transactional(readOnly = true)
	public Optional<TypeDocument> findById(Integer id) {
		return id == null ? Optional.empty() : repository.findById(id.longValue());
	}

	@Transactional
	public TypeDocument save(TypeDocument entity) { return repository.save(entity); }

	@Transactional
	public void deleteById(Integer id) { if (id != null) repository.deleteById(id.longValue()); }
}
