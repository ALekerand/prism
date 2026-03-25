package com.dcspa.prism.service;

import com.dcspa.prism.entity.SousPrefecture;
import com.dcspa.prism.repository.SousPrefectureRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SousPrefectureService {

	private final SousPrefectureRepository repository;

	@Transactional(readOnly = true)
	public List<SousPrefecture> findAll() { return repository.findAll(); }

	@Transactional(readOnly = true)
	public Optional<SousPrefecture> findById(Integer id) {
		return id == null ? Optional.empty() : repository.findById(id);
	}

	@Transactional
	public SousPrefecture save(SousPrefecture entity) { return repository.save(entity); }

	@Transactional
	public void deleteById(Integer id) { if (id != null) repository.deleteById(id); }
}
