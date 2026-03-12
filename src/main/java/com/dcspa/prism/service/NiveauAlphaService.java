package com.dcspa.prism.service;

import com.dcspa.prism.entity.NiveauAlpha;
import com.dcspa.prism.repository.NiveauAlphaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NiveauAlphaService {

	private final NiveauAlphaRepository repository;

	@Transactional(readOnly = true)
	public List<NiveauAlpha> findAll() { return repository.findAll(); }

	@Transactional(readOnly = true)
	public Optional<NiveauAlpha> findById(Integer id) {
		return id == null ? Optional.empty() : repository.findById(id.longValue());
	}

	@Transactional
	public NiveauAlpha save(NiveauAlpha entity) { return repository.save(entity); }

	@Transactional
	public void deleteById(Integer id) { if (id != null) repository.deleteById(id.longValue()); }
}
