package com.dcspa.prism.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
public class AdminDashboardService {

	private static final String SUMMARY_SQL = """
			SELECT
				(SELECT COUNT(*) FROM centre) AS centres_total,
				(SELECT COUNT(*) FROM alpha) AS alpha_total,
				(SELECT COUNT(*) FROM cec) AS cec_total,
				(SELECT COUNT(*) FROM cp) AS cp_total,
				(SELECT COUNT(*) FROM sie) AS sie_total,
				(SELECT COUNT(*) FROM personnel) AS personnel_total,
				(SELECT COUNT(*) FROM app_user) AS users_total,
				(SELECT COUNT(*) FROM app_role) AS roles_total
			""";

	@PersistenceContext
	private EntityManager entityManager;

	/** Un aller-retour SQL pour les compteurs du tableau de bord (au lieu de neuf requêtes séparées). */
	@Transactional(readOnly = true)
	public Map<String, Object> buildSummary() {
		var list = entityManager.createNativeQuery(SUMMARY_SQL).getResultList();
		if (list.isEmpty()) {
			throw new IllegalStateException("Résumé tableau de bord : aucune ligne retournée");
		}
		Object first = list.getFirst();
		if (!(first instanceof Object[] cells) || cells.length != 8) {
			throw new IllegalStateException("Résumé tableau de bord : forme de résultat SQL inattendue");
		}
		return Map.of(
				"centresTotal", toLong(cells[0]),
				"alphaTotal", toLong(cells[1]),
				"cecTotal", toLong(cells[2]),
				"cpTotal", toLong(cells[3]),
				"sieTotal", toLong(cells[4]),
				"personnelTotal", toLong(cells[5]),
				"usersTotal", toLong(cells[6]),
				"rolesTotal", toLong(cells[7]));
	}

	private static long toLong(Object cell) {
		if (cell instanceof Number n) {
			return n.longValue();
		}
		return 0L;
	}
}
