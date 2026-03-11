package com.dcspa.prism.service;

import com.dcspa.prism.entity.EffectifSituationHandicapCp;
import com.dcspa.prism.repository.EffectifSituationHandicapCpRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EffectifSituationHandicapCpService {

	private final EffectifSituationHandicapCpRepository repository;

	@Transactional(readOnly = true)
	public List<EffectifSituationHandicapCp> findAll() { return repository.findAll(); }

	@Transactional(readOnly = true)
	public Optional<EffectifSituationHandicapCp> findById(Integer id) { return repository.findById(id); }

	@Transactional
	public EffectifSituationHandicapCp save(EffectifSituationHandicapCp entity) { return repository.save(entity); }

	@Transactional
	public void deleteById(Integer id) { repository.deleteById(id); }
}
