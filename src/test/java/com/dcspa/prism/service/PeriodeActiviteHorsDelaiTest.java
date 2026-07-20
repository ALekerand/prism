package com.dcspa.prism.service;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.dcspa.prism.entity.PeriodeActivite;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;

class PeriodeActiviteHorsDelaiTest {

	@Test
	void horsDelai_apresFin() {
		PeriodeActivite p = new PeriodeActivite();
		p.setDateDebut(LocalDate.of(2026, 1, 1));
		p.setDateFin(LocalDate.of(2026, 6, 30));
		assertTrue(PeriodeActiviteHorsDelai.isHorsDelai(p, LocalDate.of(2026, 7, 1)));
	}

	@Test
	void dansDelai() {
		PeriodeActivite p = new PeriodeActivite();
		p.setDateDebut(LocalDate.of(2026, 1, 1));
		p.setDateFin(LocalDate.of(2026, 12, 31));
		assertFalse(PeriodeActiviteHorsDelai.isHorsDelai(p, LocalDate.of(2026, 7, 13)));
	}
}
