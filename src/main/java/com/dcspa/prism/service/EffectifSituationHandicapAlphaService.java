package com.dcspa.prism.service;

import com.dcspa.prism.entity.EffectifSituationHandicapAlpha;
import com.dcspa.prism.repository.EffectifSituationHandicapAlphaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EffectifSituationHandicapAlphaService {

	private final EffectifSituationHandicapAlphaRepository repository;

	@Transactional(readOnly = true)
	public List<EffectifSituationHandicapAlpha> findAll() { return repository.findAll(); }

	@Transactional(readOnly = true)
	public Optional<EffectifSituationHandicapAlpha> findById(Integer id) { return repository.findById(id); }

	@Transactional
	public EffectifSituationHandicapAlpha save(EffectifSituationHandicapAlpha entity) { return repository.save(entity); }

	@Transactional
	public void deleteById(Integer id) { repository.deleteById(id); }
}
