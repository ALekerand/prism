package com.dcspa.prism.service;

import com.dcspa.prism.entity.EffectifCec;
import com.dcspa.prism.repository.EffectifCecRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EffectifCecService {

	private final EffectifCecRepository repository;

	@Transactional(readOnly = true)
	public List<EffectifCec> findAll() { return repository.findAll(); }

	@Transactional(readOnly = true)
	public Optional<EffectifCec> findById(Integer id) { return repository.findById(id); }

	@Transactional
	public EffectifCec save(EffectifCec entity) { return repository.save(entity); }

	@Transactional
	public void deleteById(Integer id) { repository.deleteById(id); }
}
