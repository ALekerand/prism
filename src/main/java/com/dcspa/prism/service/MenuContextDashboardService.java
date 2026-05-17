package com.dcspa.prism.service;

import com.dcspa.prism.entity.Centre;
import com.dcspa.prism.entity.EffectifAbandonAlpha;
import com.dcspa.prism.entity.EffectifAbandonCec;
import com.dcspa.prism.entity.EffectifAbandonCp;
import com.dcspa.prism.entity.EffectifAbondanSie;
import com.dcspa.prism.entity.EffectifAdmisIntegrationCp;
import com.dcspa.prism.entity.EffectifAlpha;
import com.dcspa.prism.entity.EffectifCepeCec;
import com.dcspa.prism.entity.EffectifCepeCp;
import com.dcspa.prism.entity.EffectifCec;
import com.dcspa.prism.entity.EffectifCp;
import com.dcspa.prism.entity.EffectifIntegrationFormelCp;
import com.dcspa.prism.entity.EffectifPassageAlpha;
import com.dcspa.prism.entity.EffectifPromuCec;
import com.dcspa.prism.entity.EffectifPromuSie;
import com.dcspa.prism.entity.EffectifReverseFormelSie;
import com.dcspa.prism.entity.EffectifSie;
import com.dcspa.prism.entity.EffectifSituationHandicapAlpha;
import com.dcspa.prism.entity.EffectifSituationHandicapCec;
import com.dcspa.prism.entity.EffectifSituationHandicapCp;
import com.dcspa.prism.entity.EffectifSituationHandicapSie;
import com.dcspa.prism.entity.Personnel;
import com.dcspa.prism.entity.Promoteur;
import com.dcspa.prism.entity.TypePromoteur;
import com.dcspa.prism.repository.AlphaRepository;
import com.dcspa.prism.repository.AppRoleRepository;
import com.dcspa.prism.repository.AppUserRepository;
import com.dcspa.prism.repository.AppuiPartenaireRepository;
import com.dcspa.prism.repository.CecRepository;
import com.dcspa.prism.repository.CentreRepository;
import com.dcspa.prism.repository.ControleRepository;
import com.dcspa.prism.repository.CpRepository;
import com.dcspa.prism.repository.EffectifAbandonAlphaRepository;
import com.dcspa.prism.repository.EffectifAbandonCecRepository;
import com.dcspa.prism.repository.EffectifAbandonCpRepository;
import com.dcspa.prism.repository.EffectifAbondanSieRepository;
import com.dcspa.prism.repository.EffectifAdmisIntegrationCpRepository;
import com.dcspa.prism.repository.EffectifAlphaRepository;
import com.dcspa.prism.repository.EffectifCepeCecRepository;
import com.dcspa.prism.repository.EffectifCepeCpRepository;
import com.dcspa.prism.repository.EffectifCecRepository;
import com.dcspa.prism.repository.EffectifCpRepository;
import com.dcspa.prism.repository.EffectifIntegrationFormelCpRepository;
import com.dcspa.prism.repository.EffectifPassageAlphaRepository;
import com.dcspa.prism.repository.EffectifPromuCecRepository;
import com.dcspa.prism.repository.EffectifPromuSieRepository;
import com.dcspa.prism.repository.EffectifReverseFormelSieRepository;
import com.dcspa.prism.repository.EffectifSieRepository;
import com.dcspa.prism.repository.EffectifSituationHandicapAlphaRepository;
import com.dcspa.prism.repository.EffectifSituationHandicapCecRepository;
import com.dcspa.prism.repository.EffectifSituationHandicapCpRepository;
import com.dcspa.prism.repository.EffectifSituationHandicapSieRepository;
import com.dcspa.prism.repository.EvaluationRepository;
import com.dcspa.prism.repository.PerformanceRepository;
import com.dcspa.prism.repository.PersonnelRepository;
import com.dcspa.prism.repository.PromoteurRepository;
import com.dcspa.prism.repository.SieRepository;
import com.dcspa.prism.repository.VisiteRepository;
import com.dcspa.prism.repository.spec.CentreCirconscriptionSpecifications;
import com.dcspa.prism.security.AuthUser;
import com.dcspa.prism.service.circonscription.CirconscriptionAttachement;
import com.dcspa.prism.service.circonscription.CirconscriptionLevel;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MenuContextDashboardService {

	private final CirconscriptionResolver circonscriptionResolver;
	private final AdminDashboardService adminDashboardService;
	private final PersonnelAdminService personnelAdminService;
	private final CentreRepository centreRepository;
	private final AlphaRepository alphaRepository;
	private final CecRepository cecRepository;
	private final CpRepository cpRepository;
	private final SieRepository sieRepository;
	private final PersonnelRepository personnelRepository;
	private final PromoteurRepository promoteurRepository;
	private final VisiteRepository visiteRepository;
	private final ControleRepository controleRepository;
	private final EvaluationRepository evaluationRepository;
	private final PerformanceRepository performanceRepository;
	private final AppuiPartenaireRepository appuiPartenaireRepository;
	private final AppUserRepository appUserRepository;
	private final AppRoleRepository appRoleRepository;
	private final EffectifAlphaRepository effectifAlphaRepository;
	private final EffectifCecRepository effectifCecRepository;
	private final EffectifCpRepository effectifCpRepository;
	private final EffectifSieRepository effectifSieRepository;
	private final EffectifAbandonAlphaRepository effectifAbandonAlphaRepository;
	private final EffectifAbandonCecRepository effectifAbandonCecRepository;
	private final EffectifAbandonCpRepository effectifAbandonCpRepository;
	private final EffectifPassageAlphaRepository effectifPassageAlphaRepository;
	private final EffectifSituationHandicapAlphaRepository effectifSituationHandicapAlphaRepository;
	private final EffectifSituationHandicapCecRepository effectifSituationHandicapCecRepository;
	private final EffectifSituationHandicapCpRepository effectifSituationHandicapCpRepository;
	private final EffectifSituationHandicapSieRepository effectifSituationHandicapSieRepository;
	private final EffectifCepeCpRepository effectifCepeCpRepository;
	private final EffectifCepeCecRepository effectifCepeCecRepository;
	private final EffectifAbondanSieRepository effectifAbondanSieRepository;
	private final EffectifAdmisIntegrationCpRepository effectifAdmisIntegrationCpRepository;
	private final EffectifIntegrationFormelCpRepository effectifIntegrationFormelCpRepository;
	private final EffectifPromuSieRepository effectifPromuSieRepository;
	private final EffectifPromuCecRepository effectifPromuCecRepository;
	private final EffectifReverseFormelSieRepository effectifReverseFormelSieRepository;

	@Transactional(readOnly = true)
	public Map<String, Object> build(
			AuthUser user,
			String module,
			String centreType,
			Integer centreId,
			String subModule,
			String apiPath) {
		String mod = module == null ? "" : module.trim().toUpperCase(Locale.ROOT);
		CirconscriptionAttachement att = circonscriptionResolver.resolve(user);
		Map<String, Object> summary = adminDashboardService.buildSummary(user);
		String scopeLabel = String.valueOf(summary.getOrDefault("scopeLabel", "Vue nationale"));
		boolean nationalView = att.level() == CirconscriptionLevel.NONE;
		String scopeMode = String.valueOf(summary.getOrDefault("scopeMode", nationalView ? "NATIONAL" : att.level().name()));

		List<Map<String, Object>> cards = new ArrayList<>();
		String subtitle = null;

		switch (mod) {
			case "PERSONNEL" -> {
				if (centreId != null) {
					cards.addAll(personnelCardsFromMap(personnelAdminService.buildCentreDashboard(centreId)));
					subtitle = "Centre sélectionné";
				} else if (centreType != null && !centreType.isBlank()) {
					cards.addAll(personnelCardsFromMap(personnelAdminService.buildTypeSummary(centreType, att)));
					subtitle = centreTypeLabel(normalizeCentreType(centreType));
				} else {
					long personnel = count(personnelRepository, CentreCirconscriptionSpecifications.forPersonnel(att));
					long centres = count(centreRepository, CentreCirconscriptionSpecifications.forCentre(att));
					cards.add(card("Personnel", personnel, scopeLabel, "users", null));
					cards.add(card("Centres", centres, "Affinez par type ou par centre", "building", "mint"));
					subtitle = scopeLabel;
				}
			}
			case "CENTRES" -> {
				String type = normalizeCentreType(centreType);
				long centres = countCentresByType(att, type);
				long personnel = countPersonnelForCentreType(att, type);
				subtitle = centreTypeLabel(type);
				cards.add(card("Centres", centres, subtitle, "building", null));
				cards.add(card("Personnel rattaché", personnel, "Agents sur les centres " + type, "users", "mint"));
				if ("ALPHA".equals(type)) {
					long alphaRows = count(alphaRepository, CentreCirconscriptionSpecifications.forAlpha(att));
					cards.add(card("Fiches Alpha", alphaRows, "Centres alphabétisation enregistrés", "school", null));
				}
			}
			case "PROMOTEUR" -> {
				subtitle = nationalView
						? "Promoteurs — vue nationale (tout le territoire)"
						: "Promoteurs et rattachement aux centres";
				var promoteurStats = countPromoteursInScope(att);
				long centres = count(centreRepository, CentreCirconscriptionSpecifications.forCentre(att));
				cards.add(card("Promoteurs", promoteurStats.total(), "Structures enregistrées", "handshake", null));
				cards.add(card("Personnes physiques", promoteurStats.physiques(), null, "user", null));
				cards.add(card("Personnes morales", promoteurStats.morales(), null, "building", "mint"));
				cards.add(card("Centres (périmètre)", centres, scopeLabel, "map-marker-alt", null));
			}
			case "APPRENANT" -> {
				String path = normalizeApiPath(apiPath);
				long records = countApprenantRecords(path, att);
				String type = normalizeCentreTypeOrNull(centreType);
				subtitle = apprenantSubtitle(subModule, path);
				cards.add(card("Fiches saisies", records, subtitle, "clipboard-list", null));
				if (type != null) {
					long centres = countCentresByType(att, type);
					cards.add(card("Centres " + type, centres, scopeLabel, "building", "mint"));
				} else {
					cards.add(card("Centres (périmètre)", count(centreRepository, CentreCirconscriptionSpecifications.forCentre(att)), scopeLabel, "building", "mint"));
				}
			}
			case "ACTIVITES" -> {
				subtitle = activitesSubtitle(subModule, apiPath);
				cards.addAll(activitesCards(att, subModule, apiPath));
			}
			case "ADMIN" -> {
				subtitle = adminSubtitle(subModule);
				cards.addAll(adminCards(att, subModule, summary));
			}
			default -> throw new IllegalArgumentException("Module inconnu: " + module);
		}

		Map<String, Object> payload = new LinkedHashMap<>();
		payload.put("module", mod);
		payload.put("scopeLabel", scopeLabel);
		payload.put("scopeMode", scopeMode);
		payload.put("nationalView", nationalView);
		if (subtitle != null) {
			payload.put("subtitle", subtitle);
		}
		payload.put("cards", cards);
		return payload;
	}

	private record PromoteurScopeStats(long total, long physiques, long morales) {}

	private PromoteurScopeStats countPromoteursInScope(CirconscriptionAttachement att) {
		if (att == null || att.level() == CirconscriptionLevel.NONE) {
			var promoteurs = promoteurRepository.findAll();
			long physiques = promoteurs.stream().filter(p -> p.getTypePromoteur() == TypePromoteur.PHYSIQUE).count();
			long morales = promoteurs.stream().filter(p -> p.getTypePromoteur() == TypePromoteur.MORALE).count();
			return new PromoteurScopeStats(promoteurs.size(), physiques, morales);
		}
		Specification<Centre> centreScope = CentreCirconscriptionSpecifications.forCentre(att);
		List<Centre> centres = centreRepository.findAll(centreScope);
		List<Promoteur> promoteurs = centres.stream()
				.map(Centre::getIdPromoteur)
				.filter(Objects::nonNull)
				.collect(Collectors.toMap(Promoteur::getId, p -> p, (a, b) -> a))
				.values()
				.stream()
				.toList();
		long physiques = promoteurs.stream().filter(p -> p.getTypePromoteur() == TypePromoteur.PHYSIQUE).count();
		long morales = promoteurs.stream().filter(p -> p.getTypePromoteur() == TypePromoteur.MORALE).count();
		return new PromoteurScopeStats(promoteurs.size(), physiques, morales);
	}

	private List<Map<String, Object>> personnelCardsFromMap(Map<String, Object> raw) {
		List<Map<String, Object>> cards = new ArrayList<>();
		String scope = String.valueOf(raw.getOrDefault("scope", ""));
		if ("TYPE".equals(scope)) {
			cards.add(card("Centres", raw.get("centresCount"), String.valueOf(raw.get("centreTypeLabel")), "building", null));
			cards.add(card("Personnel", raw.get("personnelTotal"), "Effectif total sur le type", "users", "mint"));
			return cards;
		}
		cards.add(card("Total", raw.get("total"), null, "users", null));
		cards.add(card("Certifiés", raw.get("certifiedTotal"), null, "certificate", "mint"));
		cards.add(card("Hommes", raw.get("hommesTotal"), null, "male", null));
		cards.add(card("Femmes", raw.get("femmesTotal"), null, "female", null));
		cards.add(card("Fonctions distinctes", raw.get("fonctionsDistinctes"), null, "briefcase", null));
		Object top = raw.get("topFonctionLabel");
		if (top != null && !"—".equals(String.valueOf(top))) {
			cards.add(card("Fonction majoritaire", top, raw.get("topFonctionCount") + " agent(s)", "star", null));
		}
		return cards;
	}

	private List<Map<String, Object>> activitesCards(CirconscriptionAttachement att, String subModule, String apiPath) {
		String sub = subModule == null ? "" : subModule.trim().toLowerCase(Locale.ROOT);
		String path = normalizeApiPath(apiPath);
		List<Map<String, Object>> cards = new ArrayList<>();
		switch (sub) {
			case "controle" -> cards.add(card("Contrôles", count(controleRepository, CentreCirconscriptionSpecifications.forControle(att)), "Suivis pédagogiques Alpha", "clipboard-check", null));
			case "evaluation" -> cards.add(card("Évaluations", count(evaluationRepository, CentreCirconscriptionSpecifications.forEvaluation(att)), "Évaluations périodiques", "chart-line", "mint"));
			case "visite" -> cards.add(card("Visites", count(visiteRepository, CentreCirconscriptionSpecifications.forVisite(att)), "Points de visite enregistrés", "map-pin", null));
			case "dossier" -> {
				long centres = count(centreRepository, CentreCirconscriptionSpecifications.forCentre(att));
				cards.add(card("Centres", centres, "Dossiers centre à compléter", "folder-open", null));
				cards.add(card("Partenariats", count(appuiPartenaireRepository, CentreCirconscriptionSpecifications.forAppuiPartenaire(att)), "Appuis partenaires", "hands-helping", "mint"));
			}
			case "performance" -> cards.add(card("Performances", count(performanceRepository, CentreCirconscriptionSpecifications.forPerformance(att)), "Fiches performance Alpha", "chart-bar", "mint"));
			case "partenariat" -> cards.add(card("Partenariats", count(appuiPartenaireRepository, CentreCirconscriptionSpecifications.forAppuiPartenaire(att)), "Appuis et partenariats", "handshake", null));
			default -> {
				if (path.contains("performance")) {
					cards.add(card("Performances", count(performanceRepository, CentreCirconscriptionSpecifications.forPerformance(att)), null, "chart-bar", "mint"));
				} else if (path.contains("appui-partenaire")) {
					cards.add(card("Partenariats", count(appuiPartenaireRepository, CentreCirconscriptionSpecifications.forAppuiPartenaire(att)), null, "handshake", null));
				} else {
					cards.add(card("Visites", count(visiteRepository, CentreCirconscriptionSpecifications.forVisite(att)), null, "map-pin", null));
					cards.add(card("Contrôles", count(controleRepository, CentreCirconscriptionSpecifications.forControle(att)), null, "clipboard-check", null));
					cards.add(card("Évaluations", count(evaluationRepository, CentreCirconscriptionSpecifications.forEvaluation(att)), null, "chart-line", "mint"));
				}
			}
		}
		return cards;
	}

	private List<Map<String, Object>> adminCards(
			CirconscriptionAttachement att, String subModule, Map<String, Object> summary) {
		String sub = subModule == null ? "" : subModule.trim().toLowerCase(Locale.ROOT);
		List<Map<String, Object>> cards = new ArrayList<>();
		switch (sub) {
			case "acteurs", "roles", "role-permissions" -> {
				long roles = appRoleRepository.count();
				cards.add(card("Rôles", roles, "Profils et acteurs configurés", "user-tag", null));
			}
			case "utilisateurs" -> {
				Object users = summary.get("usersTotal");
				cards.add(card("Utilisateurs", users, String.valueOf(summary.get("scopeLabel")), "user-shield", null));
			}
			default -> {
				Object users = summary.get("usersTotal");
				cards.add(card("Utilisateurs", users, null, "user-shield", null));
				if (summary.get("rolesTotal") != null) {
					cards.add(card("Rôles", summary.get("rolesTotal"), null, "user-tag", "mint"));
				}
			}
		}
		return cards;
	}

	private long countApprenantRecords(String path, CirconscriptionAttachement att) {
		String p = normalizeApiPath(path);
		if (p.isEmpty()) {
			return count(effectifAlphaRepository, CentreCirconscriptionSpecifications.forCentreBacked(EffectifAlpha.class, att));
		}
		return switch (p) {
			case "effectif-alpha", "effectifalpha" -> count(effectifAlphaRepository, CentreCirconscriptionSpecifications.forCentreBacked(EffectifAlpha.class, att));
			case "effectif-cec" -> count(effectifCecRepository, CentreCirconscriptionSpecifications.forCentreBacked(EffectifCec.class, att));
			case "effectif-cp" -> count(effectifCpRepository, CentreCirconscriptionSpecifications.forCentreBacked(EffectifCp.class, att));
			case "effectif-sie" -> count(effectifSieRepository, CentreCirconscriptionSpecifications.forCentreBacked(EffectifSie.class, att));
			case "effectif-abandon-alpha" -> count(effectifAbandonAlphaRepository, CentreCirconscriptionSpecifications.forCentreBacked(EffectifAbandonAlpha.class, att));
			case "effectif-abandon-cec" -> count(effectifAbandonCecRepository, CentreCirconscriptionSpecifications.forCentreBacked(EffectifAbandonCec.class, att));
			case "effectif-abandon-cp" -> count(effectifAbandonCpRepository, CentreCirconscriptionSpecifications.forCentreBacked(EffectifAbandonCp.class, att));
			case "effectif-abondan-sie" -> count(effectifAbondanSieRepository, CentreCirconscriptionSpecifications.forCentreBacked(EffectifAbondanSie.class, att));
			case "effectif-passage-alpha" -> count(effectifPassageAlphaRepository, CentreCirconscriptionSpecifications.forCentreBacked(EffectifPassageAlpha.class, att));
			case "effectif-situation-handicap-alpha" -> count(effectifSituationHandicapAlphaRepository, CentreCirconscriptionSpecifications.forCentreBacked(EffectifSituationHandicapAlpha.class, att));
			case "effectif-situation-handicap-cec" -> count(effectifSituationHandicapCecRepository, CentreCirconscriptionSpecifications.forCentreBacked(EffectifSituationHandicapCec.class, att));
			case "effectif-situation-handicap-cp" -> count(effectifSituationHandicapCpRepository, CentreCirconscriptionSpecifications.forCentreBacked(EffectifSituationHandicapCp.class, att));
			case "effectif-situation-handicap-sie" -> count(effectifSituationHandicapSieRepository, CentreCirconscriptionSpecifications.forCentreBacked(EffectifSituationHandicapSie.class, att));
			case "effectif-cepe-cp" -> count(effectifCepeCpRepository, CentreCirconscriptionSpecifications.forCentreBacked(EffectifCepeCp.class, att));
			case "effectif-cepe-cec" -> count(effectifCepeCecRepository, CentreCirconscriptionSpecifications.forCentreBacked(EffectifCepeCec.class, att));
			case "effectif-admis-integration-cp" -> count(effectifAdmisIntegrationCpRepository, CentreCirconscriptionSpecifications.forCentreBacked(EffectifAdmisIntegrationCp.class, att));
			case "effectif-integration-formel-cp" -> count(effectifIntegrationFormelCpRepository, CentreCirconscriptionSpecifications.forCentreBacked(EffectifIntegrationFormelCp.class, att));
			case "effectif-promu-sie" -> count(effectifPromuSieRepository, CentreCirconscriptionSpecifications.forCentreBacked(EffectifPromuSie.class, att));
			case "effectif-promu-cec" -> count(effectifPromuCecRepository, CentreCirconscriptionSpecifications.forCentreBacked(EffectifPromuCec.class, att));
			case "effectif-reverse-formel-sie" -> count(effectifReverseFormelSieRepository, CentreCirconscriptionSpecifications.forCentreBacked(EffectifReverseFormelSie.class, att));
			default -> 0L;
		};
	}

	private long countCentresByType(CirconscriptionAttachement att, String type) {
		return switch (type) {
			case "ALPHA" -> count(alphaRepository, CentreCirconscriptionSpecifications.forAlpha(att));
			case "CEC" -> count(cecRepository, CentreCirconscriptionSpecifications.forCec(att));
			case "CP" -> count(cpRepository, CentreCirconscriptionSpecifications.forCp(att));
			case "SIE" -> count(sieRepository, CentreCirconscriptionSpecifications.forSie(att));
			default -> count(centreRepository, CentreCirconscriptionSpecifications.forCentre(att));
		};
	}

	private long countPersonnelForCentreType(CirconscriptionAttachement att, String type) {
		Specification<Centre> centreScope = CentreCirconscriptionSpecifications.forCentre(att);
		List<Centre> centres = centreScope == null
				? centreRepository.findAll()
				: centreRepository.findAll(centreScope);
		List<Integer> centreIds = centres.stream()
				.filter(c -> matchesCentreType(c.getCodeCentre(), type))
				.map(Centre::getId)
				.toList();
		if (centreIds.isEmpty()) {
			return 0L;
		}
		Specification<Personnel> base = (root, query, cb) -> root.get("idCentre").get("id").in(centreIds);
		Specification<Personnel> scoped = CentreCirconscriptionSpecifications.forPersonnel(att);
		if (scoped == null) {
			return personnelRepository.count(base);
		}
		return personnelRepository.count(base.and(scoped));
	}

	private static boolean matchesCentreType(String codeCentre, String centreType) {
		if (centreType == null || centreType.isBlank()) {
			return true;
		}
		return centreTypeFromCode(codeCentre).equals(centreType);
	}

	private static String centreTypeFromCode(String code) {
		if (code == null || code.isBlank()) {
			return "AUTRE";
		}
		String c = code.toUpperCase(Locale.ROOT);
		if (c.contains("ALP") || c.contains("ALPHA")) {
			return "ALPHA";
		}
		if (c.contains("CEC")) {
			return "CEC";
		}
		if (c.contains("SIE")) {
			return "SIE";
		}
		if (c.contains("CP")) {
			return "CP";
		}
		return "AUTRE";
	}

	private static String normalizeCentreType(String centreType) {
		if (centreType == null || centreType.isBlank()) {
			return "ALPHA";
		}
		String t = centreType.trim().toUpperCase(Locale.ROOT);
		if ("ALPHA".equals(t) || "CEC".equals(t) || "CP".equals(t) || "SIE".equals(t)) {
			return t;
		}
		return switch (centreType.trim().toLowerCase(Locale.ROOT)) {
			case "alpha" -> "ALPHA";
			case "cec" -> "CEC";
			case "cp" -> "CP";
			case "sie" -> "SIE";
			default -> t;
		};
	}

	private static String normalizeCentreTypeOrNull(String centreType) {
		if (centreType == null || centreType.isBlank()) {
			return null;
		}
		return normalizeCentreType(centreType);
	}

	private static String centreTypeLabel(String centreType) {
		return switch (centreType) {
			case "ALPHA" -> "Centres Alpha";
			case "CEC" -> "Centres CEC";
			case "CP" -> "Centres CP";
			case "SIE" -> "Centres SIE";
			default -> "Centres";
		};
	}

	private static String normalizeApiPath(String apiPath) {
		if (apiPath == null || apiPath.isBlank()) {
			return "";
		}
		String p = apiPath.trim().toLowerCase(Locale.ROOT);
		if (p.startsWith("/api/")) {
			return p.substring(5);
		}
		if (p.startsWith("api/")) {
			return p.substring(4);
		}
		return p.startsWith("/") ? p.substring(1) : p;
	}

	private static String apprenantSubtitle(String subModule, String apiPath) {
		if (subModule != null && !subModule.isBlank()) {
			return "Apprenant · " + subModule;
		}
		if (apiPath != null && !apiPath.isBlank()) {
			return apiPath;
		}
		return "Effectifs apprenants";
	}

	private static String activitesSubtitle(String subModule, String apiPath) {
		if (subModule != null && !subModule.isBlank()) {
			return "Activités centre · " + subModule;
		}
		return apiPath != null ? apiPath : "Activités centre";
	}

	private static String adminSubtitle(String subModule) {
		return subModule != null && !subModule.isBlank() ? "Administration · " + subModule : "Administration";
	}

	private static Map<String, Object> card(String label, Object value, String hint, String icon, String variant) {
		Map<String, Object> m = new LinkedHashMap<>();
		m.put("label", label);
		m.put("value", value);
		if (hint != null && !hint.isBlank()) {
			m.put("hint", hint);
		}
		if (icon != null && !icon.isBlank()) {
			m.put("icon", icon);
		}
		if (variant != null && !variant.isBlank()) {
			m.put("variant", variant);
		}
		return m;
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
		return repository.count();
	}
}
