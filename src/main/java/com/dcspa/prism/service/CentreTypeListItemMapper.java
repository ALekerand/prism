package com.dcspa.prism.service;

import com.dcspa.prism.dto.CentreTypeListItem;
import com.dcspa.prism.entity.Alpha;
import com.dcspa.prism.entity.Cec;
import com.dcspa.prism.entity.Centre;
import com.dcspa.prism.entity.Cp;
import com.dcspa.prism.entity.Sie;

// Conversion entité → DTO liste unifié pour les écrans centres.
public final class CentreTypeListItemMapper {

    // Constructeur privé : utilitaire statique uniquement.
    private CentreTypeListItemMapper() {
    }

    private static Boolean actifFromCentre(Centre centre) {
        if (centre == null) {
            return Boolean.TRUE;
        }
        Boolean actif = centre.getActif();
        return actif == null ? Boolean.TRUE : actif;
    }

    /** Variante explicite : évite d'accéder à la relation lazy {@code centre}. */
    public static CentreTypeListItem fromAlpha(Alpha a, Boolean actif) {
        return new CentreTypeListItem(
                a.getId(),
                a.getCodeCentre(),
                a.getCodeAlpha(),
                a.getLibelleAlpha(),
                a.getIdLocalite(),
                a.getIdIep(),
                a.getIdNaturecentre(),
                a.getIdPeriodicite(),
                a.getIdAutoriteAutorisation(),
                a.getAutorisation(),
                a.getEstElectrifie(),
                a.getADeLeau(),
                a.getNombreVisite(),
                a.getTotalApprenants(),
                a.getTotalHommes(),
                a.getTotalFemmes(),
                a.getLatitudeGps(),
                a.getLongitudeGps(),
                a.getGpsValide(),
                a.getStructurePartenaire(),
                a.getNomPartenaire(),
                a.getLocalisationCentre(),
                a.getNomMilieuImplentation(),
                a.getEncadreurNonMena(),
                a.getEncadrerParMena(),
                a.getIdPromoteur(),
                actif == null ? Boolean.TRUE : actif
        );
    }

    // Mappe une ligne Alpha vers le DTO affiché en liste.
    public static CentreTypeListItem fromAlpha(Alpha a) {
        return fromAlpha(a, actifFromCentre(a.getCentre()));
    }

    public static CentreTypeListItem fromCec(Cec c, Boolean actif) {
        return new CentreTypeListItem(
                c.getId(),
                c.getCodeCentre(),
                null,
                c.getLibelleCec(),
                c.getIdLocalite(),
                c.getIdIep(),
                c.getIdNaturecentre(),
                c.getIdPeriodicite(),
                c.getIdAutoriteAutorisation(),
                c.getAutorisation(),
                c.getEstElectrifie(),
                c.getADeLeau(),
                c.getNombreVisite(),
                c.getTotalApprenants(),
                c.getTotalHommes(),
                c.getTotalFemmes(),
                c.getLatitudeGps(),
                c.getLongitudeGps(),
                c.getGpsValide(),
                c.getStructurePartenaire(),
                c.getNomPartenaire(),
                c.getLocalisationCentre(),
                c.getNomMilieuImplentation(),
                c.getEncadreurNonMena(),
                c.getEncadrerParMena(),
                c.getIdPromoteur(),
                actif == null ? Boolean.TRUE : actif
        );
    }

    // Mappe un CEC vers le DTO (pas de code type spécifique).
    public static CentreTypeListItem fromCec(Cec c) {
        return fromCec(c, actifFromCentre(c.getCentre()));
    }

    public static CentreTypeListItem fromCp(Cp c, Boolean actif) {
        return new CentreTypeListItem(
                c.getId(),
                c.getCodeCentre(),
                null,
                c.getLibellleCp(),
                c.getIdLocalite(),
                c.getIdIep(),
                c.getIdNaturecentre(),
                c.getIdPeriodicite(),
                c.getIdAutoriteAutorisation(),
                c.getAutorisation(),
                c.getEstElectrifie(),
                c.getADeLeau(),
                c.getNombreVisite(),
                c.getTotalApprenants(),
                c.getTotalHommes(),
                c.getTotalFemmes(),
                c.getLatitudeGps(),
                c.getLongitudeGps(),
                c.getGpsValide(),
                c.getStructurePartenaire(),
                c.getNomPartenaire(),
                c.getLocalisationCentre(),
                c.getNomMilieuImplentation(),
                c.getEncadreurNonMena(),
                c.getEncadrerParMena(),
                c.getIdPromoteur(),
                actif == null ? Boolean.TRUE : actif
        );
    }

    // Mappe un CP vers le DTO.
    public static CentreTypeListItem fromCp(Cp c) {
        return fromCp(c, actifFromCentre(c.getCentre()));
    }

    public static CentreTypeListItem fromSie(Sie s, Boolean actif) {
        return new CentreTypeListItem(
                s.getId(),
                s.getCodeCentre(),
                null,
                s.getLibelleSie(),
                s.getIdLocalite(),
                s.getIdIep(),
                s.getIdNaturecentre(),
                s.getIdPeriodicite(),
                s.getIdAutoriteAutorisation(),
                s.getAutorisation(),
                s.getEstElectrifie(),
                s.getADeLeau(),
                s.getNombreVisite(),
                s.getTotalApprenants(),
                s.getTotalHommes(),
                s.getTotalFemmes(),
                s.getLatitudeGps(),
                s.getLongitudeGps(),
                s.getGpsValide(),
                s.getStructurePartenaire(),
                s.getNomPartenaire(),
                s.getLocalisationCentre(),
                s.getNomMilieuImplentation(),
                s.getEncadreurNonMena(),
                s.getEncadrerParMena(),
                s.getIdPromoteur(),
                actif == null ? Boolean.TRUE : actif
        );
    }

    // Mappe un SIE vers le DTO.
    public static CentreTypeListItem fromSie(Sie s) {
        return fromSie(s, actifFromCentre(s.getCentre()));
    }
}
