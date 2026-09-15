package com.dcspa.prism.service.effectif;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Règles métier d’effectifs : totaux H/F = somme des rubriques de partition
 * (tranches d’âge, ou nationalité si pas de tranche), ivoirien/handicap = sous-ensembles.
 */
public final class EffectifCoherenceRules {

	private static final Pattern TOTAL_KEY = Pattern.compile(
			"(NiveauHomme|NiveauFemme|NiveauH|NiveauF|alphaNiveauFemme)$|(Niveau(Cec|Cp|Sie))$",
			Pattern.CASE_INSENSITIVE);
	private static final Pattern CANDIDAT = Pattern.compile("Candidat", Pattern.CASE_INSENSITIVE);
	private static final Pattern ADMIS = Pattern.compile("Admis", Pattern.CASE_INSENSITIVE);
	private static final Pattern HANDICAP = Pattern.compile("Handicap", Pattern.CASE_INSENSITIVE);
	private static final Pattern IVOIRIEN = Pattern.compile("Ivoirien", Pattern.CASE_INSENSITIVE);
	private static final Pattern NON_IVOIRIEN = Pattern.compile("NonIvoiri+en", Pattern.CASE_INSENSITIVE);
	private static final Pattern MOINS_DE_6 = Pattern.compile("MoinsDe6", Pattern.CASE_INSENSITIVE);

	private EffectifCoherenceRules() {
	}

	public enum Gender { H, F }

	public static Gender gender(String key) {
		if (key == null || key.isBlank()) {
			return null;
		}
		if (key.matches("(?i).*(Homme|Garcon|Garçon).*")) {
			return Gender.H;
		}
		if (key.matches("(?i).*(Femme|Fille).*")) {
			return Gender.F;
		}
		if (key.matches("(?i).*(NiveauH|H(Cp|Cec|Sie)?)$")) {
			return Gender.H;
		}
		if (key.matches("(?i).*(NiveauF|alphaNiveauFemme|F(Cp|Cec|Sie)?)$")) {
			return Gender.F;
		}
		return null;
	}

	public static boolean isTotalKey(String key) {
		return key != null && TOTAL_KEY.matcher(key).find();
	}

	public static boolean isPartitionPart(String key, Iterable<String> allKeys) {
		if (gender(key) == null || isTotalKey(key)) {
			return false;
		}
		List<String> keys = toList(allKeys);
		boolean hasCandidat = keys.stream().anyMatch(k ->
				CANDIDAT.matcher(k).find() && gender(k) != null && !HANDICAP.matcher(k).find());
		if (hasCandidat) {
			return CANDIDAT.matcher(key).find()
					&& !HANDICAP.matcher(key).find()
					&& !IVOIRIEN.matcher(key).find()
					&& !(ADMIS.matcher(key).find() && !CANDIDAT.matcher(key).find());
		}
		boolean hasAgeOnly = keys.stream().anyMatch(k -> isAgeOnlyGender(k, keys));
		if (hasAgeOnly) {
			return isAgeOnlyGender(key, keys);
		}
		boolean hasNationality = keys.stream().anyMatch(k ->
				gender(k) != null && !isTotalKey(k) && !HANDICAP.matcher(k).find()
						&& (IVOIRIEN.matcher(k).find() || NON_IVOIRIEN.matcher(k).find()));
		if (hasNationality) {
			return !HANDICAP.matcher(key).find()
					&& (IVOIRIEN.matcher(key).find() || NON_IVOIRIEN.matcher(key).find());
		}
		return !HANDICAP.matcher(key).find();
	}

	public static boolean isSubsetField(String key, Iterable<String> allKeys) {
		if (gender(key) == null || isTotalKey(key) || isPartitionPart(key, allKeys)) {
			return false;
		}
		return HANDICAP.matcher(key).find()
				|| IVOIRIEN.matcher(key).find()
				|| NON_IVOIRIEN.matcher(key).find()
				|| MOINS_DE_6.matcher(key).find();
	}

	/**
	 * Recalcule les totaux H/F (et totaux legacy H+F) à partir de la partition, puis
	 * refuse les sous-ensembles qui dépassent la rubrique ou le total du même genre.
	 */
	public static Map<String, Integer> applyTotalsAndValidate(Map<String, Integer> values) {
		Map<String, Integer> out = new LinkedHashMap<>(values);
		List<String> keys = new ArrayList<>(out.keySet());
		int sumH = 0;
		int sumF = 0;
		for (String key : keys) {
			if (!isPartitionPart(key, keys)) {
				continue;
			}
			int n = nz(out.get(key));
			if (n < 0) {
				throw new IllegalArgumentException("L’effectif « " + key + " » ne peut pas être négatif.");
			}
			if (gender(key) == Gender.H) {
				sumH += n;
			} else if (gender(key) == Gender.F) {
				sumF += n;
			}
		}
		for (String key : keys) {
			if (!isTotalKey(key) || gender(key) == null) {
				continue;
			}
			out.put(key, gender(key) == Gender.H ? sumH : sumF);
		}
		int totalH = sumH;
		int totalF = sumF;
		for (String key : keys) {
			if (isTotalKey(key) && gender(key) == null) {
				out.put(key, totalH + totalF);
			}
		}
		for (String key : keys) {
			if (!isSubsetField(key, keys)) {
				continue;
			}
			int n = nz(out.get(key));
			if (n < 0) {
				throw new IllegalArgumentException("L’effectif « " + key + " » ne peut pas être négatif.");
			}
			Gender g = gender(key);
			int cap = capForSubset(key, g, out, keys, g == Gender.H ? totalH : totalF);
			if (n > cap) {
				throw new IllegalArgumentException(
						"L’effectif « " + human(key) + " » (" + n + ") dépasse le plafond autorisé (" + cap + "). "
								+ "Ivoiriens, non-ivoiriens et situations de handicap sont des sous-ensembles des totaux H/F.");
			}
		}
		return out;
	}

	private static int capForSubset(
			String key, Gender gender, Map<String, Integer> values, List<String> keys, int genderTotal) {
		String bracket = stripSubsetToken(key);
		int age = 0;
		boolean hasAge = false;
		int nationality = 0;
		boolean hasNationality = false;
		for (String other : keys) {
			if (gender(other) != gender || isTotalKey(other)) {
				continue;
			}
			if (!stripSubsetToken(other).equals(bracket)) {
				continue;
			}
			if (isAgeOnlyGender(other, keys)) {
				age += nz(values.get(other));
				hasAge = true;
			}
			if ((IVOIRIEN.matcher(other).find() || NON_IVOIRIEN.matcher(other).find())
					&& !HANDICAP.matcher(other).find()) {
				nationality += nz(values.get(other));
				hasNationality = true;
			}
		}
		if (hasAge) {
			return age;
		}
		if (HANDICAP.matcher(key).find() && hasNationality) {
			return nationality;
		}
		return Math.max(genderTotal, 0);
	}

	private static boolean isAgeOnlyGender(String key, List<String> allKeys) {
		if (gender(key) == null || isTotalKey(key)) {
			return false;
		}
		if (HANDICAP.matcher(key).find() || IVOIRIEN.matcher(key).find() || NON_IVOIRIEN.matcher(key).find()) {
			return false;
		}
		if (CANDIDAT.matcher(key).find() || ADMIS.matcher(key).find()) {
			return false;
		}
		if (MOINS_DE_6.matcher(key).find() && allKeys.stream().anyMatch(k ->
				k.matches("(?i).*(Moins3|Cec35).*") && !IVOIRIEN.matcher(k).find() && !HANDICAP.matcher(k).find())) {
			return false;
		}
		return true;
	}

	private static String stripSubsetToken(String key) {
		return key.replaceAll("(?i)NonIvoiri+en", "")
				.replaceAll("(?i)Ivoirien", "")
				.replaceAll("(?i)Handicap", "");
	}

	private static String human(String key) {
		return key.replaceAll("([a-z])([A-Z])", "$1 $2").toLowerCase(Locale.ROOT);
	}

	private static int nz(Integer value) {
		return value == null ? 0 : value;
	}

	private static List<String> toList(Iterable<String> allKeys) {
		List<String> keys = new ArrayList<>();
		if (allKeys == null) {
			return keys;
		}
		for (String key : allKeys) {
			if (key != null) {
				keys.add(key);
			}
		}
		return keys;
	}
}
