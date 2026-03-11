package com.dcspa.prism.service;

import com.dcspa.prism.entity.EffectifPromuSie;
import com.dcspa.prism.repository.EffectifPromuSieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EffectifPromuSieService {

	private final EffectifPromuSieRepository repository;

	@Transactional(readOnly = true)
	public List<EffectifPromuSie> findAll() { return repository.findAll(); }

	@Transactional(readOnly = true)
	public Optional<EffectifPromuSie> findById(Integer id) { return repository.findById(id); }

	@Transactional
	public EffectifPromuSie save(EffectifPromuSie entity) { return repository.save(entity); }

	@Transactional
	public void deleteById(Integer id) { repository.deleteById(id); }
}
