package com.dcspa.prism.service;

import com.dcspa.prism.entity.TypeAlpha;
import com.dcspa.prism.repository.TypeAlphaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TypeAlphaService {

	private final TypeAlphaRepository repository;

	@Transactional(readOnly = true)
	public List<TypeAlpha> findAll() { return repository.findAll(); }

	@Transactional(readOnly = true)
	public Optional<TypeAlpha> findById(Integer id) {
		return id == null ? Optional.empty() : repository.findById(id.longValue());
	}

	@Transactional
	public TypeAlpha save(TypeAlpha entity) { return repository.save(entity); }

	@Transactional
	public void deleteById(Integer id) { if (id != null) repository.deleteById(id.longValue()); }
}
