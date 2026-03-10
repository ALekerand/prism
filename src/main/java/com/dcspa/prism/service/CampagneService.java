package com.dcspa.prism.service;

import com.dcspa.prism.entity.Campagne;
import com.dcspa.prism.repository.CampagneRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CampagneService {

	private final CampagneRepository campagneRepository;

	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

	@Transactional(readOnly = true)
	public List<Campagne> findAll() {
		return campagneRepository.findAll();
	}

	@Transactional(readOnly = true)
	public Optional<Campagne> findById(Integer id) {
		return campagneRepository.findById(id);
	}

	@Transactional
	public Campagne save(Campagne campagne) {
		validateRequiredFields(campagne);
		// dateFormat.parse(campagne.getDateDebutCampagne());
		return campagneRepository.save(campagne);
	}

	@Transactional
	public void deleteById(Integer id) {
		campagneRepository.deleteById(id);
	}

	private void validateRequiredFields(Campagne campagne) {
		if (campagne.getDateDebutCampagne() == null) {
			throw new IllegalArgumentException("La date de début ne peut pas être nulle.");
		}
		if (campagne.getDateFinCampagne() == null) {
			throw new IllegalArgumentException("La date de fin ne peut pas être nulle.");
		}	
		// if (campagne == null) {
		// 	throw new IllegalArgumentException("L'instance Campagne ne peut pas être nulle.");
		// }
		// if (campagne.getCodeCampagne() == null || campagne.getCodeCampagne().isBlank()) {
		// 	throw new IllegalArgumentException("Le code campagne est obligatoire.");
		// }
	}
}
