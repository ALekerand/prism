package com.dcspa.prism.service;

import com.dcspa.prism.dto.UpdateCentreTypeInfosRequest;
import com.dcspa.prism.entity.Centre;
import com.dcspa.prism.entity.Promoteur;
import com.dcspa.prism.repository.CentreRepository;
import com.dcspa.prism.repository.PromoteurRepository;

import java.util.function.Consumer;

/**
 * Met à jour le promoteur d’un {@link Centre} et la colonne dénormalisée {@code id_promoteur} sur la fiche typée.
 */
public final class CentrePromoteurSync {

	private CentrePromoteurSync() {
	}

	public static void applyPromoteurChange(
			UpdateCentreTypeInfosRequest req,
			Integer centreId,
			CentreRepository centreRepository,
			PromoteurRepository promoteurRepository,
			Consumer<Integer> setTypedRowPromoteurId) {
		if (req == null || req.getIdPromoteur() == null) {
			return;
		}
		Integer pid = req.getIdPromoteur();
		Promoteur p = promoteurRepository.findById(pid)
				.orElseThrow(() -> new IllegalArgumentException("Promoteur introuvable: " + pid));
		Centre centre = centreRepository.findById(centreId)
				.orElseThrow(() -> new IllegalStateException("Centre introuvable: " + centreId));
		centre.setIdPromoteur(p);
		centreRepository.save(centre);
		setTypedRowPromoteurId.accept(pid);
	}
}
