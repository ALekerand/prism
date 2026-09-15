package com.dcspa.prism.service;

import com.dcspa.prism.dto.CentreTypeListItem;
import com.dcspa.prism.dto.CentreWithPromoteurItem;
import com.dcspa.prism.entity.AppUser;
import com.dcspa.prism.entity.Centre;
import com.dcspa.prism.repository.AppUserRepository;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

/**
 * Résout le nom affiché du conseiller à partir de {@code centre.enregistrePar}.
 */
@Service
@RequiredArgsConstructor
public class CentreConseillerLabelService {

	private final AppUserRepository appUserRepository;

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
		for (CentreTypeListItem item : items) {
			if (item == null) {
				continue;
			}
			item.setConseillerNom(labels.getOrDefault(normalize(item.getEnregistrePar()), display(null, item.getEnregistrePar())));
		}
	}

	public void fill(CentreWithPromoteurItem item, Centre centre) {
		if (item == null) {
			return;
		}
		if (centre != null) {
			item.setEnregistrePar(centre.getEnregistrePar());
		}
		String login = item.getEnregistrePar();
		item.setConseillerNom(labelFor(login));
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
		String prenoms = user.getPrenoms() == null ? "" : user.getPrenoms().trim();
		String nom = user.getNom() == null ? "" : user.getNom().trim();
		String composed = (prenoms + " " + nom).trim();
		if (!composed.isEmpty()) {
			return composed;
		}
		return fallback == null || fallback.isBlank() ? user.getUsername() : fallback.trim();
	}

	private static String normalize(String login) {
		if (login == null) {
			return null;
		}
		String trimmed = login.trim();
		return trimmed.isEmpty() ? null : trimmed;
	}
}
