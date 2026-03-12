package com.dcspa.prism.service;

import com.dcspa.prism.entity.SupportDidactique;
import com.dcspa.prism.repository.SupportDidactiqueRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SupportDidactiqueService {

	private final SupportDidactiqueRepository repository;

	@Transactional(readOnly = true)
	public List<SupportDidactique> findAll() { return repository.findAll(); }

	@Transactional(readOnly = true)
	public Optional<SupportDidactique> findById(Integer id) {
		return id == null ? Optional.empty() : repository.findById(id.longValue());
	}

	@Transactional
	public SupportDidactique save(SupportDidactique entity) { return repository.save(entity); }

	@Transactional
	public void deleteById(Integer id) { if (id != null) repository.deleteById(id.longValue()); }
}
