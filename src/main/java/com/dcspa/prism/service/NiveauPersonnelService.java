package com.dcspa.prism.service;

import com.dcspa.prism.entity.NiveauPersonnel;
import com.dcspa.prism.repository.NiveauPersonnelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NiveauPersonnelService {

	private final NiveauPersonnelRepository repository;

	@Transactional(readOnly = true)
	public List<NiveauPersonnel> findAll() { return repository.findAll(); }

	@Transactional(readOnly = true)
	public Optional<NiveauPersonnel> findById(Integer id) {
		return id == null ? Optional.empty() : repository.findById(id.longValue());
	}

	@Transactional
	public NiveauPersonnel save(NiveauPersonnel entity) { return repository.save(entity); }

	@Transactional
	public void deleteById(Integer id) { if (id != null) repository.deleteById(id.longValue()); }
}
