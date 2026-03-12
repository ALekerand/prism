package com.dcspa.prism.service;

import com.dcspa.prism.entity.Ong;
import com.dcspa.prism.repository.OngRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OngService {

	private final OngRepository repository;

	@Transactional(readOnly = true)
	public List<Ong> findAll() { return repository.findAll(); }

	@Transactional(readOnly = true)
	public Optional<Ong> findById(Integer id) {
		return id == null ? Optional.empty() : repository.findById(id.longValue());
	}

	@Transactional
	public Ong save(Ong entity) { return repository.save(entity); }

	@Transactional
	public void deleteById(Integer id) { if (id != null) repository.deleteById(id.longValue()); }
}
