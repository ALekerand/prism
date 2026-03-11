package com.dcspa.prism.service;

import com.dcspa.prism.entity.EffectifIntegrationFormelCp;
import com.dcspa.prism.repository.EffectifIntegrationFormelCpRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EffectifIntegrationFormelCpService {

	private final EffectifIntegrationFormelCpRepository repository;

	@Transactional(readOnly = true)
	public List<EffectifIntegrationFormelCp> findAll() { return repository.findAll(); }

	@Transactional(readOnly = true)
	public Optional<EffectifIntegrationFormelCp> findById(Integer id) { return repository.findById(id); }

	@Transactional
	public EffectifIntegrationFormelCp save(EffectifIntegrationFormelCp entity) { return repository.save(entity); }

	@Transactional
	public void deleteById(Integer id) { repository.deleteById(id); }
}
