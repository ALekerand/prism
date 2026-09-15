package com.dcspa.prism.service;

import com.dcspa.prism.entity.Centre;
import com.dcspa.prism.security.AuthUser;

import java.time.LocalDate;

/** Métadonnées système à la création d'un centre. */
public final class CentreRegistrationMetadata {

	private CentreRegistrationMetadata() {
	}

	public static void applyOnCreate(Centre centre, AuthUser authUser) {
		if (centre == null || centre.getId() != null) {
			return;
		}
		if (centre.getDateEnregistrement() == null) {
			centre.setDateEnregistrement(LocalDate.now());
		}
		if (authUser != null && authUser.getUsername() != null && !authUser.getUsername().isBlank()) {
			if (centre.getEnregistrePar() == null || centre.getEnregistrePar().isBlank()) {
				centre.setEnregistrePar(authUser.getUsername().trim());
			}
		}
	}
}
