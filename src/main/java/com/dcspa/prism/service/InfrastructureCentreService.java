package com.dcspa.prism.service;

import com.dcspa.prism.entity.InfrastructureCentre;
import com.dcspa.prism.repository.InfrastructureCentreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class InfrastructureCentreService {

	private final InfrastructureCentreRepository infrastructureCentreRepository;

	@Transactional(readOnly = true)
	public List<InfrastructureCentre> findAll() {
		return infrastructureCentreRepository.findAll();
	}

	@Transactional(readOnly = true)
	public Optional<InfrastructureCentre> findById(Integer id) {
		return infrastructureCentreRepository.findById(id);
	}

	@Transactional
	public InfrastructureCentre save(InfrastructureCentre entity) {
		return infrastructureCentreRepository.save(entity);
	}

	@Transactional
	public void deleteById(Integer id) {
		infrastructureCentreRepository.deleteById(id);
	}
}
