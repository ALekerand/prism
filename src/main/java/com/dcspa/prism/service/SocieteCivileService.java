package com.dcspa.prism.service;

import com.dcspa.prism.entity.SocieteCivile;
import com.dcspa.prism.repository.SocieteCivileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SocieteCivileService {

	private final SocieteCivileRepository repository;

	@Transactional(readOnly = true)
	public List<SocieteCivile> findAll() { return repository.findAll(); }

	@Transactional(readOnly = true)
	public Optional<SocieteCivile> findById(Integer id) {
		return id == null ? Optional.empty() : repository.findById(id.longValue());
	}

	@Transactional
	public SocieteCivile save(SocieteCivile entity) { return repository.save(entity); }

	@Transactional
	public void deleteById(Integer id) { if (id != null) repository.deleteById(id.longValue()); }
}
