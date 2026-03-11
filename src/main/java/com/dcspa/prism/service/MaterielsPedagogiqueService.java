package com.dcspa.prism.service;

import com.dcspa.prism.entity.MaterielsPedagogique;
import com.dcspa.prism.repository.MaterielsPedagogiqueRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MaterielsPedagogiqueService {

	private final MaterielsPedagogiqueRepository materielsPedagogiqueRepository;

	@Transactional(readOnly = true)
	public List<MaterielsPedagogique> findAll() {
		return materielsPedagogiqueRepository.findAll();
	}

	@Transactional(readOnly = true)
	public Optional<MaterielsPedagogique> findById(Integer id) {
		return materielsPedagogiqueRepository.findById(id);
	}

	@Transactional
	public MaterielsPedagogique save(MaterielsPedagogique entity) {
		return materielsPedagogiqueRepository.save(entity);
	}

	@Transactional
	public void deleteById(Integer id) {
		materielsPedagogiqueRepository.deleteById(id);
	}
}
