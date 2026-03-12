package com.dcspa.prism.service;

import com.dcspa.prism.entity.SupportDidactiqueAlpha;
import com.dcspa.prism.repository.SupportDidactiqueAlphaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SupportDidactiqueAlphaService {

	private final SupportDidactiqueAlphaRepository repository;

	@Transactional(readOnly = true)
	public List<SupportDidactiqueAlpha> findAll() { return repository.findAll(); }

	@Transactional(readOnly = true)
	public Optional<SupportDidactiqueAlpha> findById(Integer id) {
		return id == null ? Optional.empty() : repository.findById(id.longValue());
	}

	@Transactional
	public SupportDidactiqueAlpha save(SupportDidactiqueAlpha entity) { return repository.save(entity); }

	@Transactional
	public void deleteById(Integer id) { if (id != null) repository.deleteById(id.longValue()); }
}
