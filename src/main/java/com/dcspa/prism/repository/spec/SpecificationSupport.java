package com.dcspa.prism.repository.spec;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import java.util.ArrayList;
import java.util.List;

// Prédicats réutilisables pour les filtres « contient » / égalité.
public final class SpecificationSupport {

	private SpecificationSupport() {
	}

	public static Predicate eq(CriteriaBuilder cb, Expression<Integer> path, Integer value) {
		if (value == null) {
			return cb.conjunction();
		}
		return cb.equal(path, value);
	}

	public static Predicate eq(CriteriaBuilder cb, Expression<Boolean> path, Boolean value) {
		if (value == null) {
			return cb.conjunction();
		}
		return cb.equal(path, value);
	}

	/** Recherche insensible à la casse (LIKE %terme%). */
	public static Predicate containsIgnoreCase(CriteriaBuilder cb, Expression<String> path, String raw) {
		if (raw == null || raw.isBlank()) {
			return cb.conjunction();
		}
		String term = raw.trim();
		String escaped = term.replace("\\", "\\\\").replace("%", "\\%").replace("_", "\\_");
		return cb.like(cb.lower(path), "%" + escaped.toLowerCase() + "%", '\\');
	}

	/**
	 * Recherche globale : le terme correspond à au moins une colonne texte (OU logique),
	 * ou à l’identifiant numérique si {@code q} est un entier valide.
	 * Si {@code q} est vide, ne restreint pas (conjunction).
	 */
	public static Predicate globalTextOrId(CriteriaBuilder cb, Root<?> root, String q, String... stringAttributes) {
		if (q == null || q.isBlank()) {
			return cb.conjunction();
		}
		String t = q.trim();
		List<Predicate> ors = new ArrayList<>();
		for (String attr : stringAttributes) {
			ors.add(containsIgnoreCase(cb, root.get(attr), t));
		}
		try {
			ors.add(cb.equal(root.get("id"), Integer.valueOf(t)));
		} catch (NumberFormatException ignored) {
			// q n’est pas un id entier
		}
		return cb.or(ors.toArray(Predicate[]::new));
	}
}
