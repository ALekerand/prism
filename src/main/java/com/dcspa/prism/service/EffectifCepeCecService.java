package com.dcspa.prism.service;

import com.dcspa.prism.entity.EffectifCepeCec;
import com.dcspa.prism.repository.EffectifCepeCecRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EffectifCepeCecService {

	private final EffectifCepeCecRepository repository;

	@Transactional(readOnly = true)
	public List<EffectifCepeCec> findAll() { return repository.findAll(); }

	@Transactional(readOnly = true)
	public Optional<EffectifCepeCec> findById(Integer id) { return repository.findById(id); }

	@Transactional
	public EffectifCepeCec save(EffectifCepeCec entity) { return repository.save(entity); }

	@Transactional
	public void deleteById(Integer id) { repository.deleteById(id); }
}
