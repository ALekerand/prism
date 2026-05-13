package com.dcspa.prism.service;

import com.dcspa.prism.dto.AlphaNiveauCreatePayload;
import com.dcspa.prism.dto.CentreTypeNiveauCreatePayload;
import com.dcspa.prism.entity.Alpha;
import com.dcspa.prism.entity.AlphaNiveau;
import com.dcspa.prism.entity.AnneScolaire;
import com.dcspa.prism.entity.Cec;
import com.dcspa.prism.entity.CecNiveau;
import com.dcspa.prism.entity.Cp;
import com.dcspa.prism.entity.CpNiveau;
import com.dcspa.prism.entity.NiveauAlpha;
import com.dcspa.prism.entity.NiveauCp;
import com.dcspa.prism.entity.NiveauSieCec;
import com.dcspa.prism.entity.Sie;
import com.dcspa.prism.entity.SieNiveau;
import com.dcspa.prism.repository.AnneScolaireRepository;
import com.dcspa.prism.repository.AlphaNiveauRepository;
import com.dcspa.prism.repository.CecNiveauRepository;
import com.dcspa.prism.repository.CpNiveauRepository;
import com.dcspa.prism.repository.NiveauAlphaRepository;
import com.dcspa.prism.repository.NiveauCpRepository;
import com.dcspa.prism.repository.NiveauSieCecRepository;
import com.dcspa.prism.repository.SieNiveauRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CentreNiveauCreationService {

	private final NiveauAlphaRepository niveauAlphaRepository;
	private final AlphaNiveauRepository alphaNiveauRepository;
	private final CpNiveauRepository cpNiveauRepository;
	private final CecNiveauRepository cecNiveauRepository;
	private final SieNiveauRepository sieNiveauRepository;
	private final NiveauCpRepository niveauCpRepository;
	private final NiveauSieCecRepository niveauSieCecRepository;
	private final AnneScolaireRepository anneScolaireRepository;

	public void createAlphaNiveaux(Alpha alpha, List<AlphaNiveauCreatePayload> niveaux) {
		if (niveaux == null || niveaux.isEmpty()) {
			return;
		}
		for (AlphaNiveauCreatePayload payload : niveaux) {
			if (payload == null) {
				continue;
			}
			NiveauAlpha niveau = resolveNiveauAlpha(payload);
			if (alphaNiveauRepository.existsByIdCentre_IdAndIdNiveauAlpha_Id(alpha.getId(), niveau.getId())) {
				continue;
			}
			AlphaNiveau link = new AlphaNiveau();
			link.setIdCentre(alpha);
			link.setIdNiveauAlpha(niveau);
			alphaNiveauRepository.save(link);
		}
	}

	public void replaceAlphaNiveaux(Alpha alpha, List<AlphaNiveauCreatePayload> niveaux) {
		alphaNiveauRepository.deleteAll(alphaNiveauRepository.findByIdCentre_Id(alpha.getId()));
		createAlphaNiveaux(alpha, niveaux);
	}

	public void createCpNiveaux(Cp cp, List<CentreTypeNiveauCreatePayload> niveaux) {
		if (niveaux == null || niveaux.isEmpty()) {
			return;
		}
		for (CentreTypeNiveauCreatePayload payload : niveaux) {
			CpNiveau niveau = new CpNiveau();
			niveau.setIdCentre(cp);
			niveau.setIdAnneeScolaire(resolveAnneeScolaire(payload));
			niveau.setIdNiveauCp(resolveNiveauCp(payload));
			niveau.setNombreSalleCp(payload.getNombreSalle());
			cpNiveauRepository.save(niveau);
		}
	}

	public void replaceCpNiveaux(Cp cp, List<CentreTypeNiveauCreatePayload> niveaux) {
		cpNiveauRepository.deleteAll(cpNiveauRepository.findByIdCentre_Id(cp.getId()));
		createCpNiveaux(cp, niveaux);
	}

	public void createCecNiveaux(Cec cec, List<CentreTypeNiveauCreatePayload> niveaux) {
		if (niveaux == null || niveaux.isEmpty()) {
			return;
		}
		for (CentreTypeNiveauCreatePayload payload : niveaux) {
			CecNiveau niveau = new CecNiveau();
			niveau.setIdCentre(cec);
			niveau.setIdAnneeScolaire(resolveAnneeScolaire(payload));
			niveau.setIdNiveauSie(resolveNiveauSieCec(payload));
			niveau.setCodeNiveauCec(trimToNull(payload.getCodeNiveau()));
			niveau.setNombreSalleCec(payload.getNombreSalle());
			cecNiveauRepository.save(niveau);
		}
	}

	public void replaceCecNiveaux(Cec cec, List<CentreTypeNiveauCreatePayload> niveaux) {
		cecNiveauRepository.deleteAll(cecNiveauRepository.findByIdCentre_Id(cec.getId()));
		createCecNiveaux(cec, niveaux);
	}

	public void createSieNiveaux(Sie sie, List<CentreTypeNiveauCreatePayload> niveaux) {
		if (niveaux == null || niveaux.isEmpty()) {
			return;
		}
		for (CentreTypeNiveauCreatePayload payload : niveaux) {
			SieNiveau niveau = new SieNiveau();
			niveau.setIdCentre(sie);
			niveau.setIdAnneeScolaire(resolveAnneeScolaire(payload));
			niveau.setIdNiveauSie(resolveNiveauSieCec(payload));
			niveau.setCodeSieNiveau(trimToNull(payload.getCodeNiveau()));
			niveau.setNombreSalleSie(payload.getNombreSalle());
			sieNiveauRepository.save(niveau);
		}
	}

	public void replaceSieNiveaux(Sie sie, List<CentreTypeNiveauCreatePayload> niveaux) {
		sieNiveauRepository.deleteAll(sieNiveauRepository.findByIdCentre_Id(sie.getId()));
		createSieNiveaux(sie, niveaux);
	}

	private AnneScolaire resolveAnneeScolaire(CentreTypeNiveauCreatePayload payload) {
		Integer id = require(payload == null ? null : payload.getAnneeScolaireId(), "anneeScolaireId est obligatoire pour un niveau");
		return anneScolaireRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Annee scolaire introuvable: " + id));
	}

	private NiveauCp resolveNiveauCp(CentreTypeNiveauCreatePayload payload) {
		Integer id = require(payload == null ? null : payload.getNiveauId(), "niveauId est obligatoire pour un niveau CP");
		return niveauCpRepository.findById(id.longValue())
				.orElseThrow(() -> new IllegalArgumentException("Niveau CP introuvable: " + id));
	}

	private NiveauSieCec resolveNiveauSieCec(CentreTypeNiveauCreatePayload payload) {
		Integer id = require(payload == null ? null : payload.getNiveauId(), "niveauId est obligatoire pour un niveau CEC/SIE");
		return niveauSieCecRepository.findById(id.longValue())
				.orElseThrow(() -> new IllegalArgumentException("Niveau CEC/SIE introuvable: " + id));
	}

	private NiveauAlpha resolveNiveauAlpha(AlphaNiveauCreatePayload payload) {
		if (payload.getNiveauAlphaId() != null) {
			return niveauAlphaRepository.findById(payload.getNiveauAlphaId())
					.orElseThrow(() -> new IllegalArgumentException("Niveau Alpha introuvable: " + payload.getNiveauAlphaId()));
		}
		String libelle = trimToNull(payload.getLibelleNiveauAlpha());
		if (libelle == null) {
			throw new IllegalArgumentException("niveauAlphaId est obligatoire pour un niveau Alpha");
		}
		return niveauAlphaRepository.findByLibelleNiveauAlphaIgnoreCase(libelle)
				.orElseThrow(() -> new IllegalArgumentException("Niveau Alpha introuvable: " + libelle));
	}

	private Integer require(Integer value, String message) {
		if (value == null) {
			throw new IllegalArgumentException(message);
		}
		return value;
	}

	private String trimToNull(String value) {
		if (isBlank(value)) {
			return null;
		}
		return value.trim();
	}

	private boolean isBlank(String value) {
		return value == null || value.trim().isEmpty();
	}
}
