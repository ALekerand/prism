package com.dcspa.prism.service;

import com.dcspa.prism.entity.EffectifAbondanSie;
import com.dcspa.prism.repository.EffectifAbondanSieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EffectifAbondanSieService {

	private final EffectifAbondanSieRepository repository;

	@Transactional(readOnly = true)
	public List<EffectifAbondanSie> findAll() { return repository.findAll(); }

	@Transactional(readOnly = true)
	public Optional<EffectifAbondanSie> findById(Integer id) { return repository.findById(id); }

	@Transactional
	public EffectifAbondanSie save(EffectifAbondanSie entity) { return repository.save(entity); }

	@Transactional
	public void deleteById(Integer id) { repository.deleteById(id); }
}
