package com.dcspa.prism.service;

import com.dcspa.prism.entity.DomaineActiviteAlpha;
import com.dcspa.prism.repository.DomaineActiviteAlphaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DomaineActiviteAlphaService {

	private final DomaineActiviteAlphaRepository domaineActiviteAlphaRepository;

	@Transactional(readOnly = true)
	public List<DomaineActiviteAlpha> findAll() {
		return domaineActiviteAlphaRepository.findAll();
	}

	@Transactional(readOnly = true)
	public Optional<DomaineActiviteAlpha> findById(Integer id) {
		return domaineActiviteAlphaRepository.findById(id);
	}

	@Transactional
	public DomaineActiviteAlpha save(DomaineActiviteAlpha entity) {
		return domaineActiviteAlphaRepository.save(entity);
	}

	@Transactional
	public void deleteById(Integer id) {
		domaineActiviteAlphaRepository.deleteById(id);
	}
}
