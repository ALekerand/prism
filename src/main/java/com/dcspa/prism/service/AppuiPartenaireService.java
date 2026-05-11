package com.dcspa.prism.service;

import com.dcspa.prism.entity.AppuiPartenaire;
import com.dcspa.prism.repository.AppuiPartenaireRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AppuiPartenaireService {

	private final AppuiPartenaireRepository appuiPartenaireRepository;

	@Transactional(readOnly = true)
	public List<AppuiPartenaire> findAll() {
		return appuiPartenaireRepository.findAllWithAssociations();
	}

	@Transactional(readOnly = true)
	public Optional<AppuiPartenaire> findById(Integer id) {
		return appuiPartenaireRepository.findByIdWithAssociations(id);
	}

	@Transactional
	public AppuiPartenaire save(AppuiPartenaire entity) {
		return appuiPartenaireRepository.save(entity);
	}

	@Transactional
	public void deleteById(Integer id) {
		appuiPartenaireRepository.deleteById(id);
	}
}
