package com.dcspa.prism.service;

import com.dcspa.prism.entity.Alpha;
import com.dcspa.prism.repository.AlphaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AlphaService {

	private final AlphaRepository alphaRepository;

	@Transactional(readOnly = true)
	public List<Alpha> findAll() {
		return alphaRepository.findAll();
	}

	@Transactional(readOnly = true)
	public Optional<Alpha> findById(Integer id) {
		return alphaRepository.findById(id);
	}

	@Transactional
	public Alpha save(Alpha alpha) {
		return alphaRepository.save(alpha);
	}

	@Transactional
	public void deleteById(Integer id) {
		alphaRepository.deleteById(id);
	}
}
