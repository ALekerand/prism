package com.dcspa.prism.service;

import com.dcspa.prism.entity.Ptf;
import com.dcspa.prism.repository.PtfRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PtfService {

	private final PtfRepository repository;

	@Transactional(readOnly = true)
	public List<Ptf> findAll() { return repository.findAll(); }

	@Transactional(readOnly = true)
	public Optional<Ptf> findById(Integer id) {
		return id == null ? Optional.empty() : repository.findById(id.longValue());
	}

	@Transactional
	public Ptf save(Ptf entity) { return repository.save(entity); }

	@Transactional
	public void deleteById(Integer id) { if (id != null) repository.deleteById(id.longValue()); }
}
