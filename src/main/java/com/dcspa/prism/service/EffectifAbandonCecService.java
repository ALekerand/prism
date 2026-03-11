package com.dcspa.prism.service;

import com.dcspa.prism.entity.EffectifAbandonCec;
import com.dcspa.prism.repository.EffectifAbandonCecRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EffectifAbandonCecService {

	private final EffectifAbandonCecRepository repository;

	@Transactional(readOnly = true)
	public List<EffectifAbandonCec> findAll() { return repository.findAll(); }

	@Transactional(readOnly = true)
	public Optional<EffectifAbandonCec> findById(Integer id) { return repository.findById(id); }

	@Transactional
	public EffectifAbandonCec save(EffectifAbandonCec entity) { return repository.save(entity); }

	@Transactional
	public void deleteById(Integer id) { repository.deleteById(id); }
}
