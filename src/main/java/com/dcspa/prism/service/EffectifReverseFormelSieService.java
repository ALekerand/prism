package com.dcspa.prism.service;

import com.dcspa.prism.entity.EffectifReverseFormelSie;
import com.dcspa.prism.repository.EffectifReverseFormelSieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EffectifReverseFormelSieService {

	private final EffectifReverseFormelSieRepository repository;

	@Transactional(readOnly = true)
	public List<EffectifReverseFormelSie> findAll() { return repository.findAll(); }

	@Transactional(readOnly = true)
	public Optional<EffectifReverseFormelSie> findById(Integer id) { return repository.findById(id); }

	@Transactional
	public EffectifReverseFormelSie save(EffectifReverseFormelSie entity) { return repository.save(entity); }

	@Transactional
	public void deleteById(Integer id) { repository.deleteById(id); }
}
