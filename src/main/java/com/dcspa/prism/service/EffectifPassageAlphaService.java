package com.dcspa.prism.service;

import com.dcspa.prism.entity.EffectifPassageAlpha;
import com.dcspa.prism.repository.EffectifPassageAlphaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EffectifPassageAlphaService {

	private final EffectifPassageAlphaRepository repository;

	@Transactional(readOnly = true)
	public List<EffectifPassageAlpha> findAll() { return repository.findAll(); }

	@Transactional(readOnly = true)
	public Optional<EffectifPassageAlpha> findById(Integer id) { return repository.findById(id); }

	@Transactional
	public EffectifPassageAlpha save(EffectifPassageAlpha entity) { return repository.save(entity); }

	@Transactional
	public void deleteById(Integer id) { repository.deleteById(id); }
}
