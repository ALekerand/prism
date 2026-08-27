package com.dcspa.prism.service;

import com.dcspa.prism.entity.TypeSie;
import com.dcspa.prism.repository.TypeSieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TypeSieService {

	private final TypeSieRepository repository;

	@Transactional(readOnly = true)
	public List<TypeSie> findAll() {
		return repository.findAll();
	}

	@Transactional(readOnly = true)
	public Optional<TypeSie> findById(Integer id) {
		return id == null ? Optional.empty() : repository.findById(id);
	}

	@Transactional
	public TypeSie save(TypeSie entity) {
		return repository.save(entity);
	}

	@Transactional
	public void deleteById(Integer id) {
		if (id != null) {
			repository.deleteById(id);
		}
	}
}
