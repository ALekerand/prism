package com.dcspa.prism.service.effectif;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * Cohérence des effectifs : totaux H/F saisis ; tranches d’âge = totaux ;
 * ivoirien + non-ivoirien = même effectif (par tranche et au global) ;
 * handicap = sous-ensemble. Un des deux (ivoirien / non-ivoirien) peut être 0, pas les deux
 * si l’effectif de référence n’est pas 0.
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

	public static boolean isNonIvoirienKey(String key) {
		return key != null && NON_IVOIRIEN.matcher(key).find() && !HANDICAP.matcher(key).find();
	}

	public static boolean isIvoirienKey(String key) {
		return key != null && IVOIRIEN.matcher(key).find() && !isNonIvoirienKey(key) && !HANDICAP.matcher(key).find();
	}

	public static boolean isHandicapKey(String key) {
		return key != null && HANDICAP.matcher(key).find();
	}

	public static boolean isNationalityPairForm(Iterable<String> allKeys) {
		boolean ivoirien = false;
		boolean nonIvoirien = false;
		for (String key : toList(allKeys)) {
			if (isIvoirienKey(key)) {
				ivoirien = true;
			}
			if (isNonIvoirienKey(key)) {
				nonIvoirien = true;
			}
		}
		return ivoirien && nonIvoirien;
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
						&& (isIvoirienKey(k) || isNonIvoirienKey(k)));
		if (hasNationality) {
			return isIvoirienKey(key) || isNonIvoirienKey(key);
		}
		return !HANDICAP.matcher(key).find();
	}

	public static boolean isSubsetField(String key, Iterable<String> allKeys) {
		if (gender(key) == null || isTotalKey(key) || isPartitionPart(key, allKeys)) {
			return false;
		}
		if (isNationalityPairForm(allKeys) && (isIvoirienKey(key) || isNonIvoirienKey(key))) {
			return false;
		}
		return isHandicapKey(key)
				|| isIvoirienKey(key)
				|| isNonIvoirienKey(key)
				|| MOINS_DE_6.matcher(key).find();
	}

	/**
	 * Contrôle les totaux saisis (sans les écraser), aligne le total unique legacy H+F,
	 * refuse les partitions / nationalités / sous-ensembles incohérents.
	 */
	public static Map<String, Integer> applyTotalsAndValidate(Map<String, Integer> values) {
		Map<String, Integer> out = new LinkedHashMap<>(values);
		List<String> keys = new ArrayList<>(out.keySet());
		for (String key : keys) {
			int n = nz(out.get(key));
			if (n < 0) {
				throw new IllegalArgumentException("L’effectif « " + human(key) + " » ne peut pas être négatif.");
			}
		}
		int totalH = totalOf(out, keys, Gender.H);
		int totalF = totalOf(out, keys, Gender.F);
		for (String key : keys) {
			if (isTotalKey(key) && gender(key) == null) {
				out.put(key, totalH + totalF);
			}
		}
		assertNationalityPairs(out, keys, Gender.H, totalH);
		assertNationalityPairs(out, keys, Gender.F, totalF);
		assertPartitionEqualsTotal(out, keys, Gender.H, totalH);
		assertPartitionEqualsTotal(out, keys, Gender.F, totalF);
		for (String key : keys) {
			if (!isSubsetField(key, keys)) {
				continue;
			}
			int n = nz(out.get(key));
			Gender g = gender(key);
			int cap = capForSubset(key, g, out, keys, g == Gender.H ? totalH : totalF);
			if (n > cap) {
				throw new IllegalArgumentException(
						"L’effectif « " + human(key) + " » (" + n + ") dépasse le plafond autorisé (" + cap + "). "
								+ "Ivoiriens (sans champ non-ivoirien) et situations de handicap sont des sous-ensembles.");
			}
		}
		return out;
	}

	private static int totalOf(Map<String, Integer> values, List<String> keys, Gender g) {
		for (String key : keys) {
			if (isTotalKey(key) && gender(key) == g) {
				return nz(values.get(key));
			}
		}
		return 0;
	}

	private static void assertPartitionEqualsTotal(
			Map<String, Integer> values, List<String> keys, Gender g, int total) {
		int sum = 0;
		boolean any = false;
		for (String key : keys) {
			if (gender(key) != g || !isPartitionPart(key, keys)) {
				continue;
			}
			any = true;
			sum += nz(values.get(key));
		}
		if (!any) {
			return;
		}
		if (sum != total) {
			String gf = g == Gender.H ? "H" : "F";
			throw new IllegalArgumentException(
					"La somme des rubriques " + gf + " (" + sum + ") doit être égale à l’effectif total "
							+ gf + " (" + total + ").");
		}
	}

	private static void assertNationalityPairs(
			Map<String, Integer> values, List<String> keys, Gender g, int total) {
		List<String> ivoirien = keys.stream().filter(k -> gender(k) == g && isIvoirienKey(k)).toList();
		List<String> nonIvoirien = keys.stream().filter(k -> gender(k) == g && isNonIvoirienKey(k)).toList();
		if (ivoirien.isEmpty() || nonIvoirien.isEmpty()) {
			return;
		}
		int sumI = sumKeys(values, ivoirien);
		int sumN = sumKeys(values, nonIvoirien);
		String gf = g == Gender.H ? "H" : "F";
		if (total > 0 && sumI == 0 && sumN == 0) {
			throw new IllegalArgumentException(
					"Ivoirien " + gf + " et non-ivoirien " + gf
							+ " ne peuvent pas être tous les deux à 0 lorsque l’effectif total " + gf + " n’est pas 0.");
		}
		if (sumI + sumN != total) {
			throw new IllegalArgumentException(
					"Ivoirien " + gf + " + non-ivoirien " + gf + " (" + (sumI + sumN)
							+ ") doit être égal à l’effectif total " + gf + " (" + total + ").");
		}
		Set<String> brackets = new LinkedHashSet<>();
		for (String key : ivoirien) {
			brackets.add(stripSubsetToken(key));
		}
		for (String key : nonIvoirien) {
			brackets.add(stripSubsetToken(key));
		}
		for (String bracket : brackets) {
			String iKey = findByBracket(ivoirien, bracket);
			String nKey = findByBracket(nonIvoirien, bracket);
			int i = iKey == null ? 0 : nz(values.get(iKey));
			int n = nKey == null ? 0 : nz(values.get(nKey));
			String ageKey = findAgeForBracket(keys, g, bracket);
			if (ageKey == null) {
				continue;
			}
			int age = nz(values.get(ageKey));
			if (iKey != null && nKey != null) {
				if (age > 0 && i == 0 && n == 0) {
					throw new IllegalArgumentException(
							"Ivoirien et non-ivoirien " + gf + " ne peuvent pas être tous les deux à 0 "
									+ "sur la tranche « " + human(ageKey) + " » (" + age + ").");
				}
				if (i + n != age) {
					throw new IllegalArgumentException(
							"Ivoirien + non-ivoirien " + gf + " (" + (i + n) + ") doit être égal à « "
									+ human(ageKey) + " » (" + age + ").");
				}
			} else if (iKey != null && i > age) {
				throw new IllegalArgumentException(
						"L’effectif « " + human(iKey) + " » (" + i + ") dépasse « " + human(ageKey) + " » (" + age + ").");
			} else if (nKey != null && n > age) {
				throw new IllegalArgumentException(
						"L’effectif « " + human(nKey) + " » (" + n + ") dépasse « " + human(ageKey) + " » (" + age + ").");
			}
		}
	}

	private static String findAgeForBracket(List<String> keys, Gender g, String bracket) {
		for (String key : keys) {
			if (gender(key) == g && isAgeOnlyGender(key, keys) && stripSubsetToken(key).equals(bracket)) {
				return key;
			}
		}
		return null;
	}

	private static String findByBracket(List<String> keys, String bracket) {
		for (String key : keys) {
			if (stripSubsetToken(key).equals(bracket)) {
				return key;
			}
		}
		return null;
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
			if (isIvoirienKey(other) || isNonIvoirienKey(other)) {
				nationality += nz(values.get(other));
				hasNationality = true;
			}
		}
		if (hasAge) {
			return age;
		}
		if (isHandicapKey(key) && hasNationality) {
			return nationality;
		}
		return Math.max(genderTotal, 0);
	}

	private static boolean isAgeOnlyGender(String key, List<String> allKeys) {
		if (gender(key) == null || isTotalKey(key)) {
			return false;
		}
		if (isHandicapKey(key) || isIvoirienKey(key) || isNonIvoirienKey(key)) {
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

	static String stripSubsetToken(String key) {
		return key.replaceAll("(?i)NonIvoiri+en", "")
				.replaceAll("(?i)Ivoirien", "")
				.replaceAll("(?i)Handicap", "");
	}

	private static int sumKeys(Map<String, Integer> values, List<String> keys) {
		int sum = 0;
		for (String key : keys) {
			sum += nz(values.get(key));
		}
		return sum;
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
