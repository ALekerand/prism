package com.dcspa.prism.service;

import com.dcspa.prism.entity.PeriodeActivite;
import java.time.LocalDate;

/** Indique si une production est hors délai au regard de la période d'activité. */
public final class PeriodeActiviteHorsDelai {

	private PeriodeActiviteHorsDelai() {
	}

	public static boolean isHorsDelai(PeriodeActivite periode) {
		return isHorsDelai(periode, LocalDate.now());
	}

	public static boolean isHorsDelai(PeriodeActivite periode, LocalDate reference) {
		if (periode == null || reference == null) {
			return false;
		}
		LocalDate debut = periode.getDateDebut();
		LocalDate fin = periode.getDateFin();
		if (debut != null && reference.isBefore(debut)) {
			return true;
		}
		if (fin != null && reference.isAfter(fin)) {
			return true;
		}
		return false;
	}
}
