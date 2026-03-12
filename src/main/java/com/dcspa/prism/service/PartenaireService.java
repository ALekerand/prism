package com.dcspa.prism.service;

import com.dcspa.prism.entity.Partenaire;
import com.dcspa.prism.repository.PartenaireRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PartenaireService {

	private final PartenaireRepository repository;

	@Transactional(readOnly = true)
	public List<Partenaire> findAll() { return repository.findAll(); }

	@Transactional(readOnly = true)
	public Optional<Partenaire> findById(Integer id) {
		return id == null ? Optional.empty() : repository.findById(id.longValue());
	}

	@Transactional
	public Partenaire save(Partenaire entity) { return repository.save(entity); }

	@Transactional
	public void deleteById(Integer id) { if (id != null) repository.deleteById(id.longValue()); }
}
