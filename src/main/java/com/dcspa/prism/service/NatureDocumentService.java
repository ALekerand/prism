package com.dcspa.prism.service;

import com.dcspa.prism.entity.NatureDocument;
import com.dcspa.prism.repository.NatureDocumentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NatureDocumentService {

	private final NatureDocumentRepository repository;

	@Transactional(readOnly = true)
	public List<NatureDocument> findAll() { return repository.findAll(); }

	@Transactional(readOnly = true)
	public Optional<NatureDocument> findById(Integer id) {
		return id == null ? Optional.empty() : repository.findById(id.longValue());
	}

	@Transactional
	public NatureDocument save(NatureDocument entity) { return repository.save(entity); }

	@Transactional
	public void deleteById(Integer id) { if (id != null) repository.deleteById(id.longValue()); }
}
