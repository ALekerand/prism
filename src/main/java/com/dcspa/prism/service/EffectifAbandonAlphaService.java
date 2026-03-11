package com.dcspa.prism.service;

import com.dcspa.prism.entity.EffectifAbandonAlpha;
import com.dcspa.prism.repository.EffectifAbandonAlphaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EffectifAbandonAlphaService {

	private final EffectifAbandonAlphaRepository repository;

	@Transactional(readOnly = true)
	public List<EffectifAbandonAlpha> findAll() { return repository.findAll(); }

	@Transactional(readOnly = true)
	public Optional<EffectifAbandonAlpha> findById(Integer id) { return repository.findById(id); }

	@Transactional
	public EffectifAbandonAlpha save(EffectifAbandonAlpha entity) { return repository.save(entity); }

	@Transactional
	public void deleteById(Integer id) { repository.deleteById(id); }
}
