package com.dcspa.prism.service.effectif;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class EffectifCoherenceRulesTest {

	@Test
	void alphaAgeBracketsArePartitionAndIvoirienIsSubset() {
		List<String> keys = List.of(
				"effectifAlphaNiveauH",
				"effectifAlphaNiveauF",
				"effectifAlphaMoins15H",
				"effectifAlphaMoins15F",
				"effectifAlphaMoins15IvoirienH",
				"effectifAlphaMoins15HandicapH");
		assertThat(EffectifCoherenceRules.isPartitionPart("effectifAlphaMoins15H", keys)).isTrue();
		assertThat(EffectifCoherenceRules.isPartitionPart("effectifAlphaMoins15IvoirienH", keys)).isFalse();
		assertThat(EffectifCoherenceRules.isPartitionPart("effectifAlphaMoins15HandicapH", keys)).isFalse();
		assertThat(EffectifCoherenceRules.isNationalityPairForm(keys)).isFalse();
	}

	@Test
	void keepsEnteredTotalsWhenAgeBracketsMatch() {
		Map<String, Integer> values = new LinkedHashMap<>();
		values.put("effectifAlphaNiveauH", 4);
		values.put("effectifAlphaNiveauF", 2);
		values.put("effectifAlphaMoins15H", 4);
		values.put("effectifAlphaMoins15F", 2);
		values.put("effectifAlphaMoins15IvoirienH", 3);
		values.put("effectifAlphaMoins15HandicapH", 1);

		Map<String, Integer> out = EffectifCoherenceRules.applyTotalsAndValidate(values);
		assertThat(out.get("effectifAlphaNiveauH")).isEqualTo(4);
		assertThat(out.get("effectifAlphaNiveauF")).isEqualTo(2);
	}

	@Test
	void rejectsAgeSumDifferentFromEnteredTotal() {
		Map<String, Integer> values = new LinkedHashMap<>();
		values.put("effectifAlphaNiveauH", 30);
		values.put("effectifAlphaNiveauF", 25);
		values.put("effectifAlphaMoins15H", 4);
		values.put("effectifAlphaMoins15F", 2);

		assertThatThrownBy(() -> EffectifCoherenceRules.applyTotalsAndValidate(values))
				.isInstanceOf(IllegalArgumentException.class)
				.hasMessageContaining("effectif total H");
	}

	@Test
	void rejectsIvoirienAboveSameAgeBracket() {
		Map<String, Integer> values = new LinkedHashMap<>();
		values.put("effectifAlphaNiveauH", 2);
		values.put("effectifAlphaNiveauF", 0);
		values.put("effectifAlphaMoins15H", 2);
		values.put("effectifAlphaMoins15F", 0);
		values.put("effectifAlphaMoins15IvoirienH", 5);

		assertThatThrownBy(() -> EffectifCoherenceRules.applyTotalsAndValidate(values))
				.isInstanceOf(IllegalArgumentException.class)
				.hasMessageContaining("sous-ensembles");
	}

	@Test
	void cpNationalityIsPartitionAndHandicapIsSubset() {
		List<String> keys = List.of(
				"effectifCpNiveauH",
				"effectifCpNiveauF",
				"effectifCp911IvoirienH",
				"effectifCp911NonIvoirienH",
				"effectifCp911HandicapH");
		assertThat(EffectifCoherenceRules.isPartitionPart("effectifCp911IvoirienH", keys)).isTrue();
		assertThat(EffectifCoherenceRules.isPartitionPart("effectifCp911NonIvoirienH", keys)).isTrue();
		assertThat(EffectifCoherenceRules.isPartitionPart("effectifCp911HandicapH", keys)).isFalse();
		assertThat(EffectifCoherenceRules.isNationalityPairForm(keys)).isTrue();
	}

	@Test
	void cpAcceptsAllIvoirienAndZeroNonIvoirien() {
		Map<String, Integer> values = new LinkedHashMap<>();
		values.put("effectifCpNiveauH", 25);
		values.put("effectifCpNiveauF", 0);
		values.put("effectifCp911IvoirienH", 25);
		values.put("effectifCp911NonIvoirienH", 0);
		values.put("effectifCp911HandicapH", 3);

		Map<String, Integer> out = EffectifCoherenceRules.applyTotalsAndValidate(values);
		assertThat(out.get("effectifCpNiveauH")).isEqualTo(25);
	}

	@Test
	void cpRejectsBothNationalitiesZeroWhenTotalPositive() {
		Map<String, Integer> values = new LinkedHashMap<>();
		values.put("effectifCpNiveauH", 25);
		values.put("effectifCpNiveauF", 0);
		values.put("effectifCp911IvoirienH", 0);
		values.put("effectifCp911NonIvoirienH", 0);

		assertThatThrownBy(() -> EffectifCoherenceRules.applyTotalsAndValidate(values))
				.isInstanceOf(IllegalArgumentException.class)
				.hasMessageContaining("tous les deux à 0");
	}

	@Test
	void cpRejectsNationalitySumDifferentFromTotal() {
		Map<String, Integer> values = new LinkedHashMap<>();
		values.put("effectifCpNiveauH", 25);
		values.put("effectifCpNiveauF", 0);
		values.put("effectifCp911IvoirienH", 10);
		values.put("effectifCp911NonIvoirienH", 5);

		assertThatThrownBy(() -> EffectifCoherenceRules.applyTotalsAndValidate(values))
				.isInstanceOf(IllegalArgumentException.class)
				.hasMessageContaining("non-ivoirien H");
	}

	@Test
	void ageFormWithIvoirienAndNonIvoirienMustMatchTheAgeBand() {
		Map<String, Integer> values = new LinkedHashMap<>();
		values.put("effectifAlphaNiveauH", 5);
		values.put("effectifAlphaNiveauF", 0);
		values.put("effectifAlphaMoins15H", 5);
		values.put("effectifAlphaMoins15F", 0);
		values.put("effectifAlphaMoins15IvoirienH", 2);
		values.put("effectifAlphaMoins15NonIvoirienH", 1);

		assertThatThrownBy(() -> EffectifCoherenceRules.applyTotalsAndValidate(values))
				.isInstanceOf(IllegalArgumentException.class)
				.hasMessageContaining("non-ivoirien H");
	}

	@Test
	void fillsLegacyCombinedTotalFromEnteredHf() {
		Map<String, Integer> values = new LinkedHashMap<>();
		values.put("effectifCpNiveauH", 10);
		values.put("effectifCpNiveauF", 5);
		values.put("effectifCpNiveauCp", 0);
		values.put("effectifCp911IvoirienH", 10);
		values.put("effectifCp911NonIvoirienH", 0);
		values.put("effectifCp911IvoirienF", 5);
		values.put("effectifCp911NonIvoirienF", 0);

		Map<String, Integer> out = EffectifCoherenceRules.applyTotalsAndValidate(values);
		assertThat(out.get("effectifCpNiveauCp")).isEqualTo(15);
	}
}
