package com.dcspa.prism.service;

import com.dcspa.prism.dto.LangueLiaisonSyncRequest;
import com.dcspa.prism.dto.LiaisonCatalogSyncRequest;
import com.dcspa.prism.dto.LiaisonCatalogUpdateRequest;
import com.dcspa.prism.entity.Alpha;
import com.dcspa.prism.entity.Centre;
import com.dcspa.prism.entity.Competence;
import com.dcspa.prism.entity.CompetenceCentre;
import com.dcspa.prism.entity.Designation;
import com.dcspa.prism.entity.Difficulte;
import com.dcspa.prism.entity.DifficulteAlpha;
import com.dcspa.prism.entity.Impact;
import com.dcspa.prism.entity.ImpactAlpha;
import com.dcspa.prism.entity.Infrastructure;
import com.dcspa.prism.entity.InfrastructureCentre;
import com.dcspa.prism.entity.LangueApprentissage;
import com.dcspa.prism.entity.MaterielAlpha;
import com.dcspa.prism.entity.MaterielsPedagogique;
import com.dcspa.prism.entity.RessourceFinanciereMateriel;
import com.dcspa.prism.repository.AlphaRepository;
import com.dcspa.prism.repository.CentreRepository;
import com.dcspa.prism.repository.CompetenceCentreRepository;
import com.dcspa.prism.repository.CompetenceRepository;
import com.dcspa.prism.repository.DesignationRepository;
import com.dcspa.prism.repository.DifficulteAlphaRepository;
import com.dcspa.prism.repository.DifficulteRepository;
import com.dcspa.prism.repository.ImpactAlphaRepository;
import com.dcspa.prism.repository.ImpactRepository;
import com.dcspa.prism.repository.InfrastructureCentreRepository;
import com.dcspa.prism.repository.InfrastructureRepository;
import com.dcspa.prism.repository.LangueApprentissageRepository;
import com.dcspa.prism.repository.MaterielAlphaRepository;
import com.dcspa.prism.repository.MaterielsPedagogiqueRepository;
import com.dcspa.prism.repository.RessourceFinanciereMaterielRepository;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CentreLiaisonSyncService {

	private final AlphaRepository alphaRepository;
	private final CentreRepository centreRepository;
	private final DifficulteRepository difficulteRepository;
	private final DifficulteAlphaRepository difficulteAlphaRepository;
	private final ImpactRepository impactRepository;
	private final ImpactAlphaRepository impactAlphaRepository;
	private final CompetenceRepository competenceRepository;
	private final CompetenceCentreRepository competenceCentreRepository;
	private final InfrastructureRepository infrastructureRepository;
	private final InfrastructureCentreRepository infrastructureCentreRepository;
	private final DesignationRepository designationRepository;
	private final RessourceFinanciereMaterielRepository ressourceFinanciereMaterielRepository;
	private final MaterielsPedagogiqueRepository materielsPedagogiqueRepository;
	private final MaterielAlphaRepository materielAlphaRepository;
	private final LangueApprentissageRepository langueApprentissageRepository;

	@Transactional
	public void syncDifficulteAlpha(LiaisonCatalogSyncRequest req) {
		Alpha centre = requireAlpha(req.getIdCentre());
		deleteIntegerIds(difficulteAlphaRepository::deleteById, req.getDeleteLiaisonIds());
		for (Integer catalogId : safeIds(req.getCreateCatalogIds())) {
			Difficulte difficulte = difficulteRepository.findById(catalogId)
					.orElseThrow(() -> new IllegalArgumentException("Difficulté introuvable: " + catalogId));
			DifficulteAlpha row = new DifficulteAlpha();
			row.setIdCentre(centre);
			row.setIdDifficulte(difficulte);
			difficulteAlphaRepository.save(row);
		}
		applyDifficulteUpdates(req);
	}

	@Transactional
	public void syncImpactAlpha(LiaisonCatalogSyncRequest req) {
		Alpha centre = requireAlpha(req.getIdCentre());
		deleteIntegerIds(impactAlphaRepository::deleteById, req.getDeleteLiaisonIds());
		for (Integer catalogId : safeIds(req.getCreateCatalogIds())) {
			Impact impact = impactRepository.findById(catalogId)
					.orElseThrow(() -> new IllegalArgumentException("Impact introuvable: " + catalogId));
			ImpactAlpha row = new ImpactAlpha();
			row.setIdCentre(centre);
			row.setIdImpact(impact);
			impactAlphaRepository.save(row);
		}
		applyImpactUpdates(req);
	}

	@Transactional
	public void syncCompetenceCentre(LiaisonCatalogSyncRequest req) {
		Alpha centre = requireAlpha(req.getIdCentre());
		deleteIntegerIds(competenceCentreRepository::deleteById, req.getDeleteLiaisonIds());
		for (Integer catalogId : safeIds(req.getCreateCatalogIds())) {
			Competence competence = competenceRepository.findById(catalogId)
					.orElseThrow(() -> new IllegalArgumentException("Compétence introuvable: " + catalogId));
			CompetenceCentre row = new CompetenceCentre();
			row.setIdCentre(centre);
			row.setIdCompetence(competence);
			competenceCentreRepository.save(row);
		}
	}

	@Transactional
	public void syncInfrastructureCentre(LiaisonCatalogSyncRequest req) {
		Centre centre = requireCentre(req.getIdCentre());
		deleteIntegerIds(infrastructureCentreRepository::deleteById, req.getDeleteLiaisonIds());
		for (Integer catalogId : safeIds(req.getCreateCatalogIds())) {
			Infrastructure infrastructure = infrastructureRepository.findById(catalogId)
					.orElseThrow(() -> new IllegalArgumentException("Infrastructure introuvable: " + catalogId));
			InfrastructureCentre row = new InfrastructureCentre();
			row.setIdCentre(centre);
			row.setIdInfrastructure(infrastructure);
			updateForCatalog(req, catalogId).ifPresent(u -> applyInfrastructureExtra(row, u));
			infrastructureCentreRepository.save(row);
		}
		applyInfrastructureUpdates(req);
	}

	@Transactional
	public void syncRessourceFinanciereMateriel(LiaisonCatalogSyncRequest req) {
		Centre centre = requireCentre(req.getIdCentre());
		deleteLongIds(ressourceFinanciereMaterielRepository::deleteById, req.getDeleteLiaisonIds());
		for (Integer catalogId : safeIds(req.getCreateCatalogIds())) {
			Designation designation = designationRepository.findById(catalogId)
					.orElseThrow(() -> new IllegalArgumentException("Désignation introuvable: " + catalogId));
			RessourceFinanciereMateriel row = new RessourceFinanciereMateriel();
			row.setIdCentre(centre);
			row.setIdDesignation(designation);
			ressourceFinanciereMaterielRepository.save(row);
		}
		applyRessourceUpdates(req);
	}

	@Transactional
	public void syncMaterielAlpha(LiaisonCatalogSyncRequest req) {
		Alpha centre = requireAlpha(req.getIdCentre());
		deleteIntegerIds(materielAlphaRepository::deleteById, req.getDeleteLiaisonIds());
		for (Integer catalogId : safeIds(req.getCreateCatalogIds())) {
			MaterielsPedagogique mp = materielsPedagogiqueRepository.findById(catalogId)
					.orElseThrow(() -> new IllegalArgumentException("Matériel pédagogique introuvable: " + catalogId));
			MaterielAlpha row = new MaterielAlpha();
			row.setIdCentre(centre);
			row.setIdMaterielPedagogique(mp);
			updateForCatalog(req, catalogId).ifPresent(u -> applyMaterielExtra(row, u));
			materielAlphaRepository.save(row);
		}
		applyMaterielUpdates(req);
	}

	@Transactional
	public void syncLangueApprentissage(LangueLiaisonSyncRequest req) {
		Centre centre = requireCentre(req.getIdCentre());
		deleteIntegerIds(langueApprentissageRepository::deleteById, req.getDeleteLiaisonIds());
		for (String label : safeLabels(req.getCreateLabels())) {
			LangueApprentissage row = new LangueApprentissage();
			row.setIdCentre(centre);
			row.setLibelleLangue(label);
			langueApprentissageRepository.save(row);
		}
	}

	private void applyDifficulteUpdates(LiaisonCatalogSyncRequest req) {
		for (LiaisonCatalogUpdateRequest u : safeUpdates(req)) {
			if (u.getLiaisonId() == null) {
				continue;
			}
			difficulteAlphaRepository.findById(u.getLiaisonId()).ifPresent(row -> {
				// pas de champs extra sur difficulte_alpha
				difficulteAlphaRepository.save(row);
			});
		}
	}

	private void applyImpactUpdates(LiaisonCatalogSyncRequest req) {
		for (LiaisonCatalogUpdateRequest u : safeUpdates(req)) {
			if (u.getLiaisonId() == null) {
				continue;
			}
			impactAlphaRepository.findById(u.getLiaisonId()).ifPresent(impactAlphaRepository::save);
		}
	}

	private void applyInfrastructureUpdates(LiaisonCatalogSyncRequest req) {
		Integer idCentre = req.getIdCentre();
		for (LiaisonCatalogUpdateRequest u : safeUpdates(req)) {
			if (u.getLiaisonId() != null) {
				infrastructureCentreRepository.findById(u.getLiaisonId()).ifPresent(row -> {
					applyInfrastructureExtra(row, u);
					infrastructureCentreRepository.save(row);
				});
				continue;
			}
			if (idCentre == null || u.getCatalogId() == null) {
				continue;
			}
			infrastructureCentreRepository.findAll().stream()
					.filter(row -> row.getIdCentre() != null && Objects.equals(row.getIdCentre().getId(), idCentre))
					.filter(row -> row.getIdInfrastructure() != null
							&& Objects.equals(row.getIdInfrastructure().getId(), u.getCatalogId()))
					.findFirst()
					.ifPresent(row -> {
						applyInfrastructureExtra(row, u);
						infrastructureCentreRepository.save(row);
					});
		}
	}

	private void applyInfrastructureExtra(InfrastructureCentre row, LiaisonCatalogUpdateRequest u) {
		if (u.getLibelleAutreInfrastructure() != null) {
			row.setLibelleAutreInfrastructure(u.getLibelleAutreInfrastructure().trim());
		}
	}

	private void applyMaterielExtra(MaterielAlpha row, LiaisonCatalogUpdateRequest u) {
		if (u.getLibelleAutreMateriel() != null) {
			row.setLibelleAutreMateriel(u.getLibelleAutreMateriel().trim());
		}
	}

	private static java.util.Optional<LiaisonCatalogUpdateRequest> updateForCatalog(
			LiaisonCatalogSyncRequest req, Integer catalogId) {
		return safeUpdates(req).stream()
				.filter(u -> Objects.equals(u.getCatalogId(), catalogId))
				.findFirst();
	}

	private void applyRessourceUpdates(LiaisonCatalogSyncRequest req) {
		Integer idCentre = req.getIdCentre();
		for (LiaisonCatalogUpdateRequest u : safeUpdates(req)) {
			if (u.getLiaisonId() != null) {
				ressourceFinanciereMaterielRepository.findById(u.getLiaisonId().longValue()).ifPresent(row -> {
					applyRessourceExtraFields(row, u);
					ressourceFinanciereMaterielRepository.save(row);
				});
				continue;
			}
			if (idCentre == null || u.getCatalogId() == null) {
				continue;
			}
			ressourceFinanciereMaterielRepository.findAll().stream()
					.filter(row -> row.getIdCentre() != null && Objects.equals(row.getIdCentre().getId(), idCentre))
					.filter(row -> row.getIdDesignation() != null
							&& Objects.equals(row.getIdDesignation().getId(), u.getCatalogId()))
					.findFirst()
					.ifPresent(row -> {
						applyRessourceExtraFields(row, u);
						ressourceFinanciereMaterielRepository.save(row);
					});
		}
	}

	private void applyRessourceExtraFields(
			com.dcspa.prism.entity.RessourceFinanciereMateriel row, LiaisonCatalogUpdateRequest u) {
		if (u.getSourceFinancement() != null) {
			row.setSourceFinancement(u.getSourceFinancement().trim());
		}
		if (u.getMontant() != null) {
			row.setMontant(u.getMontant());
		}
	}

	private void applyMaterielUpdates(LiaisonCatalogSyncRequest req) {
		Integer idCentre = req.getIdCentre();
		for (LiaisonCatalogUpdateRequest u : safeUpdates(req)) {
			if (u.getLiaisonId() != null) {
				materielAlphaRepository.findById(u.getLiaisonId()).ifPresent(row -> {
					applyMaterielExtra(row, u);
					materielAlphaRepository.save(row);
				});
				continue;
			}
			if (idCentre == null || u.getCatalogId() == null) {
				continue;
			}
			materielAlphaRepository.findAll().stream()
					.filter(row -> row.getIdCentre() != null && Objects.equals(row.getIdCentre().getId(), idCentre))
					.filter(row -> row.getIdMaterielPedagogique() != null
							&& Objects.equals(row.getIdMaterielPedagogique().getId(), u.getCatalogId()))
					.findFirst()
					.ifPresent(row -> {
						applyMaterielExtra(row, u);
						materielAlphaRepository.save(row);
					});
		}
	}

	private Alpha requireAlpha(Integer idCentre) {
		if (idCentre == null) {
			throw new IllegalArgumentException("idCentre est obligatoire.");
		}
		return alphaRepository.findById(idCentre)
				.orElseThrow(() -> new IllegalArgumentException("Centre alpha introuvable: " + idCentre));
	}

	private Centre requireCentre(Integer idCentre) {
		if (idCentre == null) {
			throw new IllegalArgumentException("idCentre est obligatoire.");
		}
		return centreRepository.findById(idCentre)
				.orElseThrow(() -> new IllegalArgumentException("Centre introuvable: " + idCentre));
	}

	private static Iterable<Integer> safeIds(java.util.List<Integer> ids) {
		if (ids == null) {
			return java.util.List.of();
		}
		return ids.stream().filter(Objects::nonNull).distinct().toList();
	}

	private static Iterable<String> safeLabels(java.util.List<String> labels) {
		if (labels == null) {
			return java.util.List.of();
		}
		return labels.stream()
				.filter(Objects::nonNull)
				.map(String::trim)
				.filter(s -> !s.isEmpty())
				.distinct()
				.toList();
	}

	private static java.util.List<LiaisonCatalogUpdateRequest> safeUpdates(LiaisonCatalogSyncRequest req) {
		return req.getUpdates() != null ? req.getUpdates() : java.util.List.of();
	}

	private static void deleteIntegerIds(java.util.function.Consumer<Integer> deleteById, java.util.List<Integer> ids) {
		if (ids == null) {
			return;
		}
		for (Integer id : ids) {
			if (id != null) {
				deleteById.accept(id);
			}
		}
	}

	private static void deleteLongIds(java.util.function.Consumer<Long> deleteById, java.util.List<Integer> ids) {
		if (ids == null) {
			return;
		}
		for (Integer id : ids) {
			if (id != null) {
				deleteById.accept(id.longValue());
			}
		}
	}
}
