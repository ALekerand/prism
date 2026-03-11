package com.dcspa.prism.service;

import com.dcspa.prism.entity.EffectifPromuCec;
import com.dcspa.prism.repository.EffectifPromuCecRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EffectifPromuCecService {

	private final EffectifPromuCecRepository repository;

	@Transactional(readOnly = true)
	public List<EffectifPromuCec> findAll() { return repository.findAll(); }

	@Transactional(readOnly = true)
	public Optional<EffectifPromuCec> findById(Integer id) { return repository.findById(id); }

	@Transactional
	public EffectifPromuCec save(EffectifPromuCec entity) { return repository.save(entity); }

	@Transactional
	public void deleteById(Integer id) { repository.deleteById(id); }
}
