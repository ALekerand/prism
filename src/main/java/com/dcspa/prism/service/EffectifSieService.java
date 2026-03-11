package com.dcspa.prism.service;

import com.dcspa.prism.entity.EffectifSie;
import com.dcspa.prism.repository.EffectifSieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EffectifSieService {

	private final EffectifSieRepository repository;

	@Transactional(readOnly = true)
	public List<EffectifSie> findAll() { return repository.findAll(); }

	@Transactional(readOnly = true)
	public Optional<EffectifSie> findById(Integer id) { return repository.findById(id); }

	@Transactional
	public EffectifSie save(EffectifSie entity) { return repository.save(entity); }

	@Transactional
	public void deleteById(Integer id) { repository.deleteById(id); }
}
