package com.dcspa.prism.service;

import com.dcspa.prism.entity.PeriodeActivite;
import com.dcspa.prism.repository.PeriodeActiviteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PeriodeActiviteService {

	private final PeriodeActiviteRepository repository;

	@Transactional(readOnly = true)
	public List<PeriodeActivite> findAll() { return repository.findAll(); }

	@Transactional(readOnly = true)
	public Optional<PeriodeActivite> findById(Integer id) {
		return id == null ? Optional.empty() : repository.findById(id.longValue());
	}

	@Transactional
	public PeriodeActivite save(PeriodeActivite entity) { return repository.save(entity); }

	@Transactional
	public void deleteById(Integer id) { if (id != null) repository.deleteById(id.longValue()); }
}
