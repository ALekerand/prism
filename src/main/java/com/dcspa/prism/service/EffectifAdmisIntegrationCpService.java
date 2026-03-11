package com.dcspa.prism.service;

import com.dcspa.prism.entity.EffectifAdmisIntegrationCp;
import com.dcspa.prism.repository.EffectifAdmisIntegrationCpRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EffectifAdmisIntegrationCpService {

	private final EffectifAdmisIntegrationCpRepository repository;

	@Transactional(readOnly = true)
	public List<EffectifAdmisIntegrationCp> findAll() { return repository.findAll(); }

	@Transactional(readOnly = true)
	public Optional<EffectifAdmisIntegrationCp> findById(Integer id) { return repository.findById(id); }

	@Transactional
	public EffectifAdmisIntegrationCp save(EffectifAdmisIntegrationCp entity) { return repository.save(entity); }

	@Transactional
	public void deleteById(Integer id) { repository.deleteById(id); }
}
