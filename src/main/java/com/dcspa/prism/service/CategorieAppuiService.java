package com.dcspa.prism.service;

import com.dcspa.prism.entity.CategorieAppui;
import com.dcspa.prism.repository.CategorieAppuiRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategorieAppuiService {

	private final CategorieAppuiRepository categorieAppuiRepository;

	@Transactional(readOnly = true)
	public List<CategorieAppui> findAll() {
		return categorieAppuiRepository.findAll();
	}

	@Transactional(readOnly = true)
	public Optional<CategorieAppui> findById(Integer id) {
		return categorieAppuiRepository.findById(id);
	}

	@Transactional
	public CategorieAppui save(CategorieAppui entity) {
		return categorieAppuiRepository.save(entity);
	}

	@Transactional
	public void deleteById(Integer id) {
		categorieAppuiRepository.deleteById(id);
	}
}
