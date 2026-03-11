package com.dcspa.prism.service;

import com.dcspa.prism.entity.EffectifCepeCp;
import com.dcspa.prism.repository.EffectifCepeCpRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EffectifCepeCpService {

	private final EffectifCepeCpRepository repository;

	@Transactional(readOnly = true)
	public List<EffectifCepeCp> findAll() { return repository.findAll(); }

	@Transactional(readOnly = true)
	public Optional<EffectifCepeCp> findById(Integer id) { return repository.findById(id); }

	@Transactional
	public EffectifCepeCp save(EffectifCepeCp entity) { return repository.save(entity); }

	@Transactional
	public void deleteById(Integer id) { repository.deleteById(id); }
}
