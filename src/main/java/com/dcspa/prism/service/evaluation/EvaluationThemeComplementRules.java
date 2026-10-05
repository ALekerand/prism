package com.dcspa.prism.service.evaluation;

import com.dcspa.prism.dto.EvaluationRequest;
import com.dcspa.prism.entity.ThemeEvaluation;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * Couple « sait … » / « ne sait pas … » : le complémentaire est déduit du positif
 * (même total évalué, obtenu complémentaire = total − obtenu positif).
 */
public final class EvaluationThemeComplementRules {
	private EvaluationThemeComplementRules() {
	}

	public static void applyComplements(
			List<EvaluationRequest.ThemeTauxRequest> items,
			Function<Integer, ThemeEvaluation> resolveTheme) {
		if (items == null || items.size() < 2) {
			return;
		}
		record Slot(int index, boolean complementary, String family) {
		}
		List<Slot> slots = new ArrayList<>();
		for (int i = 0; i < items.size(); i++) {
			EvaluationRequest.ThemeTauxRequest item = items.get(i);
			if (item == null || item.getIdThemeEvaluation() == null) {
				continue;
			}
			ThemeEvaluation theme = resolveTheme.apply(item.getIdThemeEvaluation());
			String libelle = theme == null ? null : theme.getLibelleThemeEvaluation();
			String code = theme == null ? null : theme.getCodeThemeEvaluation();
			slots.add(new Slot(i, isComplementary(libelle, code), familyKey(libelle, code)));
		}
		Map<String, List<Slot>> byFamily = new LinkedHashMap<>();
		for (Slot slot : slots) {
			byFamily.computeIfAbsent(slot.family(), key -> new ArrayList<>()).add(slot);
		}
		for (List<Slot> family : byFamily.values()) {
			Slot positive = family.stream().filter(slot -> !slot.complementary()).findFirst().orElse(null);
			Slot negative = family.stream().filter(Slot::complementary).findFirst().orElse(null);
			if (positive == null || negative == null) {
				continue;
			}
			EvaluationRequest.ThemeTauxRequest source = items.get(positive.index());
			EvaluationRequest.ThemeTauxRequest target = items.get(negative.index());
			if (source.getNombreTotalEvalue() == null || source.getNombreResultatObtenu() == null) {
				continue;
			}
			int total = source.getNombreTotalEvalue();
			int obtenu = Math.min(Math.max(source.getNombreResultatObtenu(), 0), total);
			source.setNombreResultatObtenu(obtenu);
			target.setNombreTotalEvalue(total);
			target.setNombreResultatObtenu(Math.max(0, total - obtenu));
		}
	}

	static boolean isComplementary(String libelle, String code) {
		return token(libelle, code).startsWith("NE_");
	}

	static String familyKey(String libelle, String code) {
		String token = token(libelle, code);
		if (token.startsWith("NE_")) {
			return token.substring(3).replace("_PAS_", "_");
		}
		return token;
	}

	private static String token(String libelle, String code) {
		String fromLibelle = normalize(libelle);
		if (!fromLibelle.isEmpty()) {
			return fromLibelle;
		}
		return normalize(code).replaceFirst("^TEV_(N1|N2|PA)_", "");
	}

	private static String normalize(String value) {
		if (value == null) {
			return "";
		}
		String noAccent = Normalizer.normalize(value, Normalizer.Form.NFD).replaceAll("\\p{M}", "");
		return noAccent.toUpperCase()
				.replaceAll("[^A-Z0-9]+", "_")
				.replaceAll("(^_|_$)", "");
	}
}
