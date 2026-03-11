package com.dcspa.prism.service;

import com.dcspa.prism.entity.EffectifSituationHandicapCec;
import com.dcspa.prism.repository.EffectifSituationHandicapCecRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EffectifSituationHandicapCecService {

	private final EffectifSituationHandicapCecRepository repository;

	@Transactional(readOnly = true)
	public List<EffectifSituationHandicapCec> findAll() { return repository.findAll(); }

	@Transactional(readOnly = true)
	public Optional<EffectifSituationHandicapCec> findById(Integer id) { return repository.findById(id); }

	@Transactional
	public EffectifSituationHandicapCec save(EffectifSituationHandicapCec entity) { return repository.save(entity); }

	@Transactional
	public void deleteById(Integer id) { repository.deleteById(id); }
}
