package com.dcspa.prism.repository.spec;

import com.dcspa.prism.entity.AppRole;
import com.dcspa.prism.entity.AppUser;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

/**
 * Filtres JPA pour la liste administration des utilisateurs ({@code /api/app-users}).
 */
public final class AppUserSpecifications {

	private AppUserSpecifications() {
	}

	public static Specification<AppUser> forAdminSearch(String q, Integer roleId, Boolean actif) {
		return (root, query, cb) -> {
			if (query != null) {
				query.distinct(true);
			}
			List<Predicate> predicates = new ArrayList<>();

			if (roleId != null) {
				Join<AppUser, AppRole> roles = root.join("roles", JoinType.LEFT);
				predicates.add(cb.equal(roles.get("id"), roleId));
			}

			if (q != null && !q.isBlank()) {
				String pattern = "%" + q.toLowerCase() + "%";
				Predicate usernameMatch = cb.like(cb.lower(root.get("username")), pattern);
				Predicate emailMatch = cb.and(
						cb.isNotNull(root.get("email")),
						cb.like(cb.lower(root.get("email")), pattern));
				predicates.add(cb.or(usernameMatch, emailMatch));
			}

			if (actif != null) {
				predicates.add(cb.equal(root.get("actif"), actif));
			}

			return cb.and(predicates.toArray(Predicate[]::new));
		};
	}
}
