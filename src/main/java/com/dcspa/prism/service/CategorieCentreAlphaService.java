package com.dcspa.prism.service;

import com.dcspa.prism.entity.CategorieCentreAlpha;
import com.dcspa.prism.repository.CategorieCentreAlphaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategorieCentreAlphaService {

	private final CategorieCentreAlphaRepository repository;

	@Transactional(readOnly = true)
	public List<CategorieCentreAlpha> findAll() { return repository.findAll(); }

	@Transactional(readOnly = true)
	public Optional<CategorieCentreAlpha> findById(Integer id) {
		return id == null ? Optional.empty() : repository.findById(id);
	}

	@Transactional
	public CategorieCentreAlpha save(CategorieCentreAlpha entity) { return repository.save(entity); }

	@Transactional
	public void deleteById(Integer id) { if (id != null) repository.deleteById(id); }
}
