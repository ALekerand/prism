package com.dcspa.prism.service;

import com.dcspa.prism.dto.CentreCreatePayload;
import com.dcspa.prism.dto.CentreWithPromoteurItem;
import com.dcspa.prism.dto.UpdateCentreTypeInfosRequest;
import com.dcspa.prism.entity.Alpha;
import com.dcspa.prism.support.NumericSanitizer;
import com.dcspa.prism.entity.Cec;
import com.dcspa.prism.entity.Centre;
import com.dcspa.prism.entity.Cp;
import com.dcspa.prism.entity.Sie;

/**
 * Champs complémentaires import/création qui ne font pas partie des référentiels obligatoires du centre.
 */
final class CentreSupplementalFields {

    private CentreSupplementalFields() {
    }

    static void applyToCentre(Centre centre, CentreCreatePayload payload) {
        Integer hommes = NumericSanitizer.nonNegativeOrNull(payload.getTotalHommes());
        Integer femmes = NumericSanitizer.nonNegativeOrNull(payload.getTotalFemmes());
        centre.setTotalHommes(hommes);
        centre.setTotalFemmes(femmes);
        centre.setTotalApprenants(NumericSanitizer.totalApprenantsFromGenres(hommes, femmes));
        centre.setLatitudeGps(trimToNull(payload.getLatitudeGps()));
        centre.setLongitudeGps(trimToNull(payload.getLongitudeGps()));
        centre.setGpsValide(payload.getGpsValide());
        centre.setStructurePartenaire(trimToNull(payload.getStructurePartenaire()));
        centre.setNomPartenaire(trimToNull(payload.getNomPartenaire()));
        if (payload.getActif() != null) {
            centre.setActif(payload.getActif());
        }
        if (payload.getDateCreationDaaje() != null) {
            centre.setDateCreationDaaje(payload.getDateCreationDaaje());
        }
    }

    static void applyActifToCentre(Centre centre, Boolean actif) {
        if (centre == null || actif == null) {
            return;
        }
        centre.setActif(actif);
    }

    static Boolean actifForApi(Centre centre) {
        if (centre == null) {
            return Boolean.TRUE;
        }
        Boolean value = centre.getActif();
        return value == null ? Boolean.TRUE : value;
    }

    static void copyToAlpha(Alpha alpha, Centre centre) {
        alpha.setTotalApprenants(centre.getTotalApprenants());
        alpha.setTotalHommes(centre.getTotalHommes());
        alpha.setTotalFemmes(centre.getTotalFemmes());
        alpha.setLatitudeGps(centre.getLatitudeGps());
        alpha.setLongitudeGps(centre.getLongitudeGps());
        alpha.setGpsValide(centre.getGpsValide());
        alpha.setStructurePartenaire(centre.getStructurePartenaire());
        alpha.setNomPartenaire(centre.getNomPartenaire());
    }

    static void copyToCp(Cp cp, Centre centre) {
        cp.setTotalApprenants(centre.getTotalApprenants());
        cp.setTotalHommes(centre.getTotalHommes());
        cp.setTotalFemmes(centre.getTotalFemmes());
        cp.setLatitudeGps(centre.getLatitudeGps());
        cp.setLongitudeGps(centre.getLongitudeGps());
        cp.setGpsValide(centre.getGpsValide());
        cp.setStructurePartenaire(centre.getStructurePartenaire());
        cp.setNomPartenaire(centre.getNomPartenaire());
    }

    static void copyToCec(Cec cec, Centre centre) {
        cec.setTotalApprenants(centre.getTotalApprenants());
        cec.setTotalHommes(centre.getTotalHommes());
        cec.setTotalFemmes(centre.getTotalFemmes());
        cec.setLatitudeGps(centre.getLatitudeGps());
        cec.setLongitudeGps(centre.getLongitudeGps());
        cec.setGpsValide(centre.getGpsValide());
        cec.setStructurePartenaire(centre.getStructurePartenaire());
        cec.setNomPartenaire(centre.getNomPartenaire());
    }

    static void copyToSie(Sie sie, Centre centre) {
        sie.setTotalApprenants(centre.getTotalApprenants());
        sie.setTotalHommes(centre.getTotalHommes());
        sie.setTotalFemmes(centre.getTotalFemmes());
        sie.setLatitudeGps(centre.getLatitudeGps());
        sie.setLongitudeGps(centre.getLongitudeGps());
        sie.setGpsValide(centre.getGpsValide());
        sie.setStructurePartenaire(centre.getStructurePartenaire());
        sie.setNomPartenaire(centre.getNomPartenaire());
    }

    static void applyUpdate(Alpha alpha, UpdateCentreTypeInfosRequest req) {
        Integer hommes = NumericSanitizer.nonNegativeOrNull(req.getTotalHommes());
        Integer femmes = NumericSanitizer.nonNegativeOrNull(req.getTotalFemmes());
        alpha.setTotalHommes(hommes);
        alpha.setTotalFemmes(femmes);
        alpha.setTotalApprenants(NumericSanitizer.totalApprenantsFromGenres(hommes, femmes));
        alpha.setLatitudeGps(trimToNull(req.getLatitudeGps()));
        alpha.setLongitudeGps(trimToNull(req.getLongitudeGps()));
        alpha.setGpsValide(req.getGpsValide());
        alpha.setStructurePartenaire(trimToNull(req.getStructurePartenaire()));
        alpha.setNomPartenaire(trimToNull(req.getNomPartenaire()));
    }

    static void applyUpdate(Cp cp, UpdateCentreTypeInfosRequest req) {
        Integer hommes = NumericSanitizer.nonNegativeOrNull(req.getTotalHommes());
        Integer femmes = NumericSanitizer.nonNegativeOrNull(req.getTotalFemmes());
        cp.setTotalHommes(hommes);
        cp.setTotalFemmes(femmes);
        cp.setTotalApprenants(NumericSanitizer.totalApprenantsFromGenres(hommes, femmes));
        cp.setLatitudeGps(trimToNull(req.getLatitudeGps()));
        cp.setLongitudeGps(trimToNull(req.getLongitudeGps()));
        cp.setGpsValide(req.getGpsValide());
        cp.setStructurePartenaire(trimToNull(req.getStructurePartenaire()));
        cp.setNomPartenaire(trimToNull(req.getNomPartenaire()));
    }

    static void applyUpdate(Cec cec, UpdateCentreTypeInfosRequest req) {
        Integer hommes = NumericSanitizer.nonNegativeOrNull(req.getTotalHommes());
        Integer femmes = NumericSanitizer.nonNegativeOrNull(req.getTotalFemmes());
        cec.setTotalHommes(hommes);
        cec.setTotalFemmes(femmes);
        cec.setTotalApprenants(NumericSanitizer.totalApprenantsFromGenres(hommes, femmes));
        cec.setLatitudeGps(trimToNull(req.getLatitudeGps()));
        cec.setLongitudeGps(trimToNull(req.getLongitudeGps()));
        cec.setGpsValide(req.getGpsValide());
        cec.setStructurePartenaire(trimToNull(req.getStructurePartenaire()));
        cec.setNomPartenaire(trimToNull(req.getNomPartenaire()));
    }

    static void applyUpdate(Sie sie, UpdateCentreTypeInfosRequest req) {
        Integer hommes = NumericSanitizer.nonNegativeOrNull(req.getTotalHommes());
        Integer femmes = NumericSanitizer.nonNegativeOrNull(req.getTotalFemmes());
        sie.setTotalHommes(hommes);
        sie.setTotalFemmes(femmes);
        sie.setTotalApprenants(NumericSanitizer.totalApprenantsFromGenres(hommes, femmes));
        sie.setLatitudeGps(trimToNull(req.getLatitudeGps()));
        sie.setLongitudeGps(trimToNull(req.getLongitudeGps()));
        sie.setGpsValide(req.getGpsValide());
        sie.setStructurePartenaire(trimToNull(req.getStructurePartenaire()));
        sie.setNomPartenaire(trimToNull(req.getNomPartenaire()));
    }

    static void fillItem(CentreWithPromoteurItem item, Alpha alpha) {
        item.setTotalApprenants(alpha.getTotalApprenants());
        item.setTotalHommes(alpha.getTotalHommes());
        item.setTotalFemmes(alpha.getTotalFemmes());
        item.setLatitudeGps(alpha.getLatitudeGps());
        item.setLongitudeGps(alpha.getLongitudeGps());
        item.setGpsValide(alpha.getGpsValide());
        item.setStructurePartenaire(alpha.getStructurePartenaire());
        item.setNomPartenaire(alpha.getNomPartenaire());
    }

    static void fillItem(CentreWithPromoteurItem item, Cp cp) {
        item.setTotalApprenants(cp.getTotalApprenants());
        item.setTotalHommes(cp.getTotalHommes());
        item.setTotalFemmes(cp.getTotalFemmes());
        item.setLatitudeGps(cp.getLatitudeGps());
        item.setLongitudeGps(cp.getLongitudeGps());
        item.setGpsValide(cp.getGpsValide());
        item.setStructurePartenaire(cp.getStructurePartenaire());
        item.setNomPartenaire(cp.getNomPartenaire());
    }

    static void fillItem(CentreWithPromoteurItem item, Cec cec) {
        item.setTotalApprenants(cec.getTotalApprenants());
        item.setTotalHommes(cec.getTotalHommes());
        item.setTotalFemmes(cec.getTotalFemmes());
        item.setLatitudeGps(cec.getLatitudeGps());
        item.setLongitudeGps(cec.getLongitudeGps());
        item.setGpsValide(cec.getGpsValide());
        item.setStructurePartenaire(cec.getStructurePartenaire());
        item.setNomPartenaire(cec.getNomPartenaire());
    }

    static void fillItem(CentreWithPromoteurItem item, Sie sie) {
        item.setTotalApprenants(sie.getTotalApprenants());
        item.setTotalHommes(sie.getTotalHommes());
        item.setTotalFemmes(sie.getTotalFemmes());
        item.setLatitudeGps(sie.getLatitudeGps());
        item.setLongitudeGps(sie.getLongitudeGps());
        item.setGpsValide(sie.getGpsValide());
        item.setStructurePartenaire(sie.getStructurePartenaire());
        item.setNomPartenaire(sie.getNomPartenaire());
    }

    private static String trimToNull(String value) {
        if (value == null) {
            return null;
        }
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }
}
