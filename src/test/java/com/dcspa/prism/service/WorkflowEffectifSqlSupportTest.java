package com.dcspa.prism.service;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class WorkflowEffectifSqlSupportTest {

	@Test
	void reverseFormelSieUsesLegacyPkAndNoCentreJoin() {
		var meta = WorkflowEffectifSqlSupport.meta("/api/effectif-reverse-formel-sie");
		assertThat(meta).isNotNull();
		assertThat(meta.pkColumn()).isEqualTo("id_effectif_debut23");
		assertThat(meta.centreLink()).isEqualTo(WorkflowEffectifSqlSupport.CentreLink.NONE);
		String sql = WorkflowEffectifSqlSupport.scopeSql(meta);
		assertThat(sql).contains("id_effectif_debut23");
		assertThat(sql).doesNotContain("id_effectif_reverse_formel_sie");
		assertThat(sql).doesNotContain("e.id_centre");
	}

	@Test
	void effectifSieUsesDebut20Pk() {
		var meta = WorkflowEffectifSqlSupport.meta("/api/effectif-sie");
		assertThat(meta).isNotNull();
		assertThat(meta.pkColumn()).isEqualTo("id_effectif_debut20");
	}
}