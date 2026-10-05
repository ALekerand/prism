package com.dcspa.prism.service;

import com.dcspa.prism.entity.AppRole;
import com.dcspa.prism.entity.AppUser;
import com.dcspa.prism.entity.Drena;
import com.dcspa.prism.entity.Iep;
import com.dcspa.prism.entity.Region;
import com.dcspa.prism.repository.AlphaRepository;
import com.dcspa.prism.repository.AppRoleRepository;
import com.dcspa.prism.repository.AppUserRepository;
import com.dcspa.prism.repository.CecRepository;
import com.dcspa.prism.repository.CentreRepository;
import com.dcspa.prism.repository.ControleRepository;
import com.dcspa.prism.repository.CpRepository;
import com.dcspa.prism.repository.DrenaRepository;
import com.dcspa.prism.repository.EvaluationRepository;
import com.dcspa.prism.repository.IeppRepository;
import com.dcspa.prism.repository.PersonnelRepository;
import com.dcspa.prism.repository.RegionRepository;
import com.dcspa.prism.repository.SieRepository;
import com.dcspa.prism.repository.VisiteRepository;
import com.dcspa.prism.repository.spec.CentreCirconscriptionSpecifications;
import com.dcspa.prism.repository.spec.CentreListScopeSpecifications;
import com.dcspa.prism.security.AuthUser;
import com.dcspa.prism.service.circonscription.CirconscriptionAttachement;
import com.dcspa.prism.service.circonscription.CirconscriptionLevel;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdminDashboardService {

	private final CirconscriptionResolver circonscriptionResolver;
	private final CentreRepository centreRepository;
	private final AlphaRepository alphaRepository;
	private final CecRepository cecRepository;
	private final CpRepository cpRepository;
	private final SieRepository sieRepository;
	private final PersonnelRepository personnelRepository;
	private final VisiteRepository visiteRepository;
	private final ControleRepository controleRepository;
	private final EvaluationRepository evaluationRepository;
	private final AppUserRepository appUserRepository;
	private final AppRoleRepository appRoleRepository;
	private final IeppRepository iepRepository;
	private final DrenaRepository drenaRepository;
	private final RegionRepository regionRepository;
	private final SaisieWorkflowReturnAlertService workflowReturnAlertService;

	/** Métadonnées de périmètre sans agrégats SQL (login, /me). */
	@Transactional(readOnly = true)
	public Map<String, Object> buildScopeMeta(AuthUser user) {
		CirconscriptionAttachement att = circonscriptionResolver.resolve(user);
		boolean national = att.level() == CirconscriptionLevel.NONE;
		Map<String, Object> payload = new LinkedHashMap<>();
		payload.put("scopeMode", national ? "NATIONAL" : att.level().name());
		payload.put("scopeLabel", resolveScopeLabel(att, national));
		payload.put("nationalView", national);
		return payload;
	}

	@Transactional(readOnly = true)
	public Map<String, Object> buildSummary(AuthUser user) {
		CirconscriptionAttachement att = circonscriptionResolver.resolve(user);
		boolean national = att.level() == CirconscriptionLevel.NONE;
		Map<String, Object> payload = buildScopeMeta(user);

		long alphaTotal = count(alphaRepository, CentreListScopeSpecifications.andOwnTypedCentres(
				CentreCirconscriptionSpecifications.forAlphaStats(att), user));
		long cecTotal = count(cecRepository, CentreListScopeSpecifications.andOwnTypedCentres(
				CentreCirconscriptionSpecifications.forCecStats(att), user));
		long cpTotal = count(cpRepository, CentreListScopeSpecifications.andOwnTypedCentres(
				CentreCirconscriptionSpecifications.forCpStats(att), user));
		long sieTotal = count(sieRepository, CentreListScopeSpecifications.andOwnTypedCentres(
				CentreCirconscriptionSpecifications.forSieStats(att), user));
		long centresTotal = count(centreRepository, CentreListScopeSpecifications.andOwnCentre(
				CentreCirconscriptionSpecifications.forCentreStats(att), user));
		long personnelTotal = count(personnelRepository, CentreListScopeSpecifications.andOwnViaIdCentre(
				CentreCirconscriptionSpecifications.forPersonnelStats(att), user));
		long visitesTotal = count(visiteRepository, CentreListScopeSpecifications.andOwnViaAlpha(
				CentreCirconscriptionSpecifications.forVisiteStats(att), user));
		long controlesTotal = count(controleRepository, CentreListScopeSpecifications.andOwnViaAlpha(
				CentreCirconscriptionSpecifications.forControleStats(att), user));
		long evaluationsTotal = count(evaluationRepository, CentreListScopeSpecifications.andOwnViaAlpha(
				CentreCirconscriptionSpecifications.forEvaluationStats(att), user));

		payload.put("centresTotal", centresTotal);
		payload.put("alphaTotal", alphaTotal);
		payload.put("cecTotal", cecTotal);
		payload.put("cpTotal", cpTotal);
		payload.put("sieTotal", sieTotal);
		payload.put("personnelTotal", personnelTotal);
		payload.put("visitesTotal", visitesTotal);
		payload.put("controlesTotal", controlesTotal);
		payload.put("evaluationsTotal", evaluationsTotal);

		if (national) {
			payload.put("usersTotal", appUserRepository.count());
			payload.put("rolesTotal", appRoleRepository.count());
		} else if (user != null && user.hasRole("CONSEILLER")) {
			payload.put("usersTotal", null);
			payload.put("rolesTotal", null);
		} else {
			Specification<com.dcspa.prism.entity.AppUser> userScope = CentreCirconscriptionSpecifications.forAppUser(att);
			payload.put("usersTotal", userScope == null ? appUserRepository.count() : appUserRepository.count(userScope));
			payload.put("rolesTotal", null);
		}

		payload.put("workflowReturns", workflowReturnAlertService.buildAlertPayload(user));
		return payload;
	}

	@Transactional(readOnly = true)
	public long countUsersInScopeWithRoles(CirconscriptionAttachement att, String... roleCodes) {
		if (roleCodes == null || roleCodes.length == 0) {
			return 0L;
		}
		List<String> codes = Arrays.stream(roleCodes).filter(c -> c != null && !c.isBlank()).toList();
		if (codes.isEmpty()) {
			return 0L;
		}
		Specification<AppUser> roleSpec = (root, query, cb) -> {
			if (query != null) {
				query.distinct(true);
			}
			Join<AppUser, AppRole> roles = root.join("roles", JoinType.INNER);
			return roles.get("codeRole").in(codes);
		};
		Specification<AppUser> scope = CentreCirconscriptionSpecifications.forAppUser(att);
		if (scope == null) {
			return appUserRepository.count(roleSpec);
		}
		return appUserRepository.count(scope.and(roleSpec));
	}

	private String resolveScopeLabel(CirconscriptionAttachement att, boolean national) {
		if (national) {
			return "Vue nationale";
		}
		return switch (att.level()) {
			case IEP -> iepRepository.findById(att.scopeId())
					.map(AdminDashboardService::iepLabel)
					.orElse("IEP #" + att.scopeId());
			case DRENA -> drenaRepository.findById(att.scopeId())
					.map(AdminDashboardService::drenaLabel)
					.orElse("DRENA #" + att.scopeId());
			case REGION -> regionRepository.findById(att.scopeId())
					.map(AdminDashboardService::regionLabel)
					.orElse("Région #" + att.scopeId());
			case NONE -> "Vue nationale";
		};
	}

	private static String iepLabel(Iep iep) {
		String nom = iep.getNomIep();
		if (nom != null && !nom.isBlank()) {
			return "IEP · " + nom.trim();
		}
		String code = iep.getCodeIep();
		if (code != null && !code.isBlank()) {
			return "IEP · " + code.trim();
		}
		return "IEP #" + iep.getId();
	}

	private static String drenaLabel(Drena drena) {
		String nom = drena.getNomDrena();
		if (nom != null && !nom.isBlank()) {
			return "DRENA · " + nom.trim();
		}
		String code = drena.getCodeDrena();
		if (code != null && !code.isBlank()) {
			return "DRENA · " + code.trim();
		}
		return "DRENA #" + drena.getId();
	}

	private static String regionLabel(Region region) {
		String libelle = region.getLibelleRegion();
		if (libelle != null && !libelle.isBlank()) {
			return "Région · " + libelle.trim();
		}
		return "Région #" + region.getId();
	}

	private static <T> long count(JpaRepository<T, ?> repository, Specification<T> spec) {
		if (spec == null) {
			return repository.count();
		}
		if (repository instanceof JpaSpecificationExecutor<?> specRepo) {
			@SuppressWarnings("unchecked")
			JpaSpecificationExecutor<T> executor = (JpaSpecificationExecutor<T>) specRepo;
			return executor.count(spec);
		}
		throw new IllegalStateException("Repository sans support Specification: " + repository.getClass().getName());
	}
}
