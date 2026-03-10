package com.dcspa.prism.service;

import com.dcspa.prism.entity.Campagne;
import com.dcspa.prism.repository.CampagneRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CampagneService {

	private final CampagneRepository campagneRepository;

	@Transactional(readOnly = true)
	public List<Campagne> findAll() {
		return campagneRepository.findAll();
	}

	@Transactional(readOnly = true)
	public Optional<Campagne> findById(Integer id) {
		return campagneRepository.findById(id);
	}

	@Transactional
	public Campagne save(Campagne campagne) {
		return campagneRepository.save(campagne);
	}

	@Transactional
	public void deleteById(Integer id) {
		campagneRepository.deleteById(id);
	}
}
