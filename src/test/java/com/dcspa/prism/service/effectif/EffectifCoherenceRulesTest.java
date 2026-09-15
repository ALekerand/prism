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
		assertThat(EffectifCoherenceRules.isPartitionPart("effectifAlphaNiveauH", keys)).isFalse();
	}

	@Test
	void applyTotalsFromAgeBracketsAndRejectsIvoirienAboveAge() {
		Map<String, Integer> values = new LinkedHashMap<>();
		values.put("effectifAlphaNiveauH", 1);
		values.put("effectifAlphaNiveauF", 0);
		values.put("effectifAlphaMoins15H", 4);
		values.put("effectifAlphaMoins15F", 2);
		values.put("effectifAlphaMoins15IvoirienH", 3);
		values.put("effectifAlphaMoins15HandicapH", 1);

		Map<String, Integer> out = EffectifCoherenceRules.applyTotalsAndValidate(values);
		assertThat(out.get("effectifAlphaNiveauH")).isEqualTo(4);
		assertThat(out.get("effectifAlphaNiveauF")).isEqualTo(2);
	}

	@Test
	void rejectsIvoirienAboveSameAgeBracket() {
		Map<String, Integer> values = new LinkedHashMap<>();
		values.put("effectifAlphaNiveauH", 0);
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
	}
}
