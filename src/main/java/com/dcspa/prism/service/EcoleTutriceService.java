package com.dcspa.prism.service;

import com.dcspa.prism.entity.EcoleTutrice;
import com.dcspa.prism.repository.EcoleTutriceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EcoleTutriceService {

	private final EcoleTutriceRepository repository;

	@Transactional(readOnly = true)
	public List<EcoleTutrice> findAll() {
		return repository.findAll();
	}

	@Transactional(readOnly = true)
	public Optional<EcoleTutrice> findById(Integer id) {
		return repository.findById(id);
	}

	@Transactional
	public EcoleTutrice save(EcoleTutrice entity) {
		return repository.save(entity);
	}

	@Transactional
	public void deleteById(Integer id) {
		repository.deleteById(id);
	}
}
