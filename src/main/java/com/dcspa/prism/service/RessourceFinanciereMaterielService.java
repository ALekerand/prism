package com.dcspa.prism.service;

import com.dcspa.prism.entity.RessourceFinanciereMateriel;
import com.dcspa.prism.repository.RessourceFinanciereMaterielRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RessourceFinanciereMaterielService {

	private final RessourceFinanciereMaterielRepository repository;

	@Transactional(readOnly = true)
	public List<RessourceFinanciereMateriel> findAll() { return repository.findAll(); }

	@Transactional(readOnly = true)
	public Optional<RessourceFinanciereMateriel> findById(Integer id) {
		return id == null ? Optional.empty() : repository.findById(id.longValue());
	}

	@Transactional
	public RessourceFinanciereMateriel save(RessourceFinanciereMateriel entity) { return repository.save(entity); }

	@Transactional
	public void deleteById(Integer id) { if (id != null) repository.deleteById(id.longValue()); }
}
