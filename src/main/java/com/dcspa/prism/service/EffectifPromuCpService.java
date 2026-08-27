package com.dcspa.prism.service;

import com.dcspa.prism.entity.Cp;
import com.dcspa.prism.entity.EffectifPromuCp;
import com.dcspa.prism.repository.CpRepository;
import com.dcspa.prism.repository.EffectifPromuCpRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EffectifPromuCpService {

	private final EffectifPromuCpRepository repository;
	private final CpRepository cpRepository;

	@Transactional(readOnly = true)
	public List<EffectifPromuCp> findAll() {
		return repository.findAll();
	}

	@Transactional(readOnly = true)
	public Optional<EffectifPromuCp> findById(Integer id) {
		return repository.findById(id);
	}

	@Transactional
	public EffectifPromuCp save(EffectifPromuCp entity) {
		EffectifPromuCp saved = repository.save(entity);
		markCentrePromu(saved.getIdCentre());
		return saved;
	}

	@Transactional
	public void deleteById(Integer id) {
		repository.deleteById(id);
	}

	private void markCentrePromu(Cp cp) {
		if (cp == null || cp.getId() == null) {
			return;
		}
		cpRepository.findById(cp.getId()).ifPresent(existing -> {
			if (!Boolean.TRUE.equals(existing.getEstPromu())) {
				existing.setEstPromu(true);
				cpRepository.save(existing);
			}
		});
	}
}
