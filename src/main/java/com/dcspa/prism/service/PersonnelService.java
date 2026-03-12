package com.dcspa.prism.service;

import com.dcspa.prism.entity.Personnel;
import com.dcspa.prism.repository.PersonnelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PersonnelService {

	private final PersonnelRepository repository;

	@Transactional(readOnly = true)
	public List<Personnel> findAll() { return repository.findAll(); }

	@Transactional(readOnly = true)
	public Optional<Personnel> findById(Integer id) {
		return id == null ? Optional.empty() : repository.findById(id.longValue());
	}

	@Transactional
	public Personnel save(Personnel entity) { return repository.save(entity); }

	@Transactional
	public void deleteById(Integer id) { if (id != null) repository.deleteById(id.longValue()); }
}
