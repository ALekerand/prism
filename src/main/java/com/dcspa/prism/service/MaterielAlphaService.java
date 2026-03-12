package com.dcspa.prism.service;

import com.dcspa.prism.entity.MaterielAlpha;
import com.dcspa.prism.repository.MaterielAlphaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MaterielAlphaService {

	private final MaterielAlphaRepository repository;

	@Transactional(readOnly = true)
	public List<MaterielAlpha> findAll() { return repository.findAll(); }

	@Transactional(readOnly = true)
	public Optional<MaterielAlpha> findById(Integer id) { return repository.findById(id); }

	@Transactional
	public MaterielAlpha save(MaterielAlpha entity) { return repository.save(entity); }

	@Transactional
	public void deleteById(Integer id) { repository.deleteById(id); }
}
