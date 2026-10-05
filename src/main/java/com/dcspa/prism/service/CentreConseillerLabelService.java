package com.dcspa.prism.service;

import com.dcspa.prism.dto.CentreAuteurScope;
import com.dcspa.prism.dto.CentreTypeListItem;
import com.dcspa.prism.dto.CentreWithPromoteurItem;
import com.dcspa.prism.entity.AppUser;
import com.dcspa.prism.entity.Centre;
import com.dcspa.prism.repository.AppUserRepository;
import com.dcspa.prism.repository.CentreRepository;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

/**
 * Noms affichés des auteurs : conseiller ({@code enregistrePar} / workflow) et coordonnateur de l’IEP.
 */
@Service
@RequiredArgsConstructor
public class CentreConseillerLabelService {

	private static final String ROLE_COORDONNATEUR = "COORDONNATEUR";

	private final AppUserRepository appUserRepository;
	private final CentreRepository centreRepository;

	public Page<CentreTypeListItem> enrich(Page<CentreTypeListItem> page) {
		if (page == null) {
			return page;
		}
		enrich(page.getContent());
		return page;
	}

	public CentreTypeListItem enrich(CentreTypeListItem item) {
		if (item != null) {
			enrich(List.of(item));
		}
		return item;
	}

	public void enrich(Collection<CentreTypeListItem> items) {
		if (items == null || items.isEmpty()) {
			return;
		}
		Map<String, String> labels = labelsFor(items.stream()
				.map(CentreTypeListItem::getEnregistrePar)
				.toList());
		Map<Integer, String> coords = coordonnateurLabelsFor(items.stream()
				.map(CentreTypeListItem::getIdIep)
				.toList());
		for (CentreTypeListItem item : items) {
			if (item == null) {
				continue;
			}
			item.setConseillerNom(labels.getOrDefault(normalize(item.getEnregistrePar()), display(null, item.getEnregistrePar())));
			item.setCoordonnateurNom(coords.get(item.getIdIep()));
		}
	}

	public void fill(CentreWithPromoteurItem item, Centre centre) {
		if (item == null) {
			return;
		}
		Integer idIep = item.getIdIep();
		if (centre != null) {
			item.setEnregistrePar(centre.getEnregistrePar());
			if (idIep == null && centre.getIdIep() != null) {
				idIep = centre.getIdIep().getId();
				item.setIdIep(idIep);
			}
		}
		String login = item.getEnregistrePar();
		item.setConseillerNom(labelFor(login));
		item.setCoordonnateurNom(idIep == null ? null : coordonnateurLabelsFor(List.of(idIep)).get(idIep));
	}

	/**
	 * Ajoute {@code enregistrePar}, {@code conseillerNom}, {@code coordonnateurNom} (et {@code idIep} si manquant)
	 * sur les lignes de saisie (effectifs, visites, contrôles, évaluations…).
	 */
	public void enrichMaps(List<Map<String, Object>> rows) {
		if (rows == null || rows.isEmpty()) {
			return;
		}
		fillMissingFromCentres(rows);
		Map<String, String> conseillerLabels = labelsFor(rows.stream()
				.map(CentreConseillerLabelService::conseillerLoginOf)
				.toList());
		Map<Integer, String> coordLabels = coordonnateurLabelsFor(rows.stream()
				.map(row -> intId(row.get("idIep")))
				.toList());
		for (Map<String, Object> row : rows) {
			String login = conseillerLoginOf(row);
			if (login != null) {
				row.put("enregistrePar", login);
				row.put("conseillerNom", conseillerLabels.getOrDefault(login, login));
			}
			Integer idIep = intId(row.get("idIep"));
			if (idIep != null) {
				row.put("coordonnateurNom", coordLabels.get(idIep));
			}
		}
	}

	public String labelFor(String login) {
		String key = normalize(login);
		if (key == null) {
			return null;
		}
		return appUserRepository.findByUsername(key)
				.map(u -> display(u, key))
				.orElse(key);
	}

	/**
	 * Noms affichés des validateurs workflow ({@code workflowValide*Par} → {@code valide*Nom}).
	 */
	public void enrichWorkflowValidatorLabels(Map<String, Object> row) {
		if (row == null) {
			return;
		}
		enrichValidatorNom(row, "workflowValideCoordPar", "valideCoordNom");
		enrichValidatorNom(row, "workflowValideSupPar", "valideSupNom");
		enrichValidatorNom(row, "workflowValideCentralPar", "valideCentralNom");
	}

	private void enrichValidatorNom(Map<String, Object> row, String loginKey, String nomKey) {
		String login = stringOf(row.get(loginKey));
		if (login == null) {
			return;
		}
		row.put(nomKey, labelFor(login));
	}

	private void fillMissingFromCentres(List<Map<String, Object>> rows) {
		List<Integer> centreIds = rows.stream()
				.map(CentreConseillerLabelService::centreIdOf)
				.filter(Objects::nonNull)
				.distinct()
				.toList();
		if (centreIds.isEmpty()) {
			return;
		}
		Map<Integer, CentreAuteurScope> scopes = new HashMap<>();
		for (CentreAuteurScope scope : centreRepository.findAuteurScopesByIdIn(centreIds)) {
			if (scope != null && scope.idCentre() != null) {
				scopes.put(scope.idCentre(), scope);
			}
		}
		for (Map<String, Object> row : rows) {
			Integer centreId = centreIdOf(row);
			CentreAuteurScope scope = centreId == null ? null : scopes.get(centreId);
			if (scope == null) {
				continue;
			}
			if (row.get("idIep") == null && scope.idIep() != null) {
				row.put("idIep", scope.idIep());
			}
			if (stringOf(row.get("enregistrePar")) == null && scope.enregistrePar() != null) {
				row.put("enregistrePar", scope.enregistrePar());
			}
		}
	}

	Map<Integer, String> coordonnateurLabelsFor(Collection<Integer> iepIds) {
		List<Integer> ids = iepIds.stream().filter(Objects::nonNull).distinct().toList();
		Map<Integer, String> out = new LinkedHashMap<>();
		if (ids.isEmpty()) {
			return out;
		}
		Map<Integer, String> acc = new LinkedHashMap<>();
		for (Object[] row : appUserRepository.findIdentityByRoleAndIepIdIn(ROLE_COORDONNATEUR, ids)) {
			if (row == null || row.length < 4 || !(row[3] instanceof Number n)) {
				continue;
			}
			int idIep = n.intValue();
			String label = displayParts((String) row[0], (String) row[1], (String) row[2]);
			if (label == null) {
				continue;
			}
			String previous = acc.get(idIep);
			acc.put(idIep, previous == null ? label : previous + " / " + label);
		}
		out.putAll(acc);
		return out;
	}

	private Map<String, String> labelsFor(Collection<String> logins) {
		List<String> keys = logins.stream().map(CentreConseillerLabelService::normalize).filter(Objects::nonNull).distinct().toList();
		Map<String, String> out = new HashMap<>();
		if (keys.isEmpty()) {
			return out;
		}
		for (AppUser user : appUserRepository.findByUsernameIn(keys)) {
			if (user.getUsername() != null) {
				out.put(user.getUsername().trim(), display(user, user.getUsername()));
			}
		}
		for (String key : keys) {
			out.putIfAbsent(key, key);
		}
		return out;
	}

	static String display(AppUser user, String fallback) {
		if (user == null) {
			return fallback == null || fallback.isBlank() ? null : fallback.trim();
		}
		return displayParts(user.getPrenoms(), user.getNom(), fallback == null ? user.getUsername() : fallback);
	}

	private static String displayParts(String prenoms, String nom, String fallback) {
		String p = prenoms == null ? "" : prenoms.trim();
		String n = nom == null ? "" : nom.trim();
		String composed = (p + " " + n).trim();
		if (!composed.isEmpty()) {
			return composed;
		}
		return fallback == null || fallback.isBlank() ? null : fallback.trim();
	}

	private static String conseillerLoginOf(Map<String, Object> row) {
		String login = stringOf(row.get("enregistrePar"));
		if (login != null) {
			return login;
		}
		login = stringOf(row.get("workflowProprietaire"));
		if (login != null) {
			return login;
		}
		return stringOf(row.get("workflowSoumisPar"));
	}

	private static Integer centreIdOf(Map<String, Object> row) {
		Integer id = intId(row.get("idCentre"));
		if (id != null) {
			return id;
		}
		id = intId(row.get("idAlpha"));
		if (id != null) {
			return id;
		}
		for (String key : List.of("alpha", "cec", "cp", "sie", "centre")) {
			id = intId(nestedId(row.get(key)));
			if (id != null) {
				return id;
			}
		}
		return null;
	}

	private static Object nestedId(Object ref) {
		if (ref instanceof Map<?, ?> map) {
			return map.get("id");
		}
		return null;
	}

	private static Integer intId(Object value) {
		if (value instanceof Integer i) {
			return i;
		}
		if (value instanceof Number n) {
			return n.intValue();
		}
		return null;
	}

	private static String stringOf(Object value) {
		if (value == null) {
			return null;
		}
		String s = value.toString().trim();
		return s.isEmpty() ? null : s;
	}

	private static String normalize(String login) {
		if (login == null) {
			return null;
		}
		String trimmed = login.trim();
		return trimmed.isEmpty() ? null : trimmed;
	}
}
