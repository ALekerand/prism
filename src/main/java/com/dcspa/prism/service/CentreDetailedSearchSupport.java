package com.dcspa.prism.service;

import com.dcspa.prism.dto.CentreWithPromoteurItem;

import java.util.Map;
import java.util.Objects;

public final class CentreDetailedSearchSupport {

    private CentreDetailedSearchSupport() {
    }

    public static boolean matchesCriteria(CentreWithPromoteurItem item, Map<String, String> criteria) {
        if (criteria == null || criteria.isEmpty()) {
            return true;
        }
        return criteria.entrySet().stream()
                .filter(e -> e.getKey() != null && e.getValue() != null)
                .allMatch(e -> matches(item, e.getKey().trim().toLowerCase(), e.getValue().trim().toLowerCase()));
    }

    private static boolean matches(CentreWithPromoteurItem item, String key, String expected) {
        String actual = valueByKey(item, key);
        if (actual == null) {
            return false;
        }
        return actual.toLowerCase().contains(expected);
    }

    private static String valueByKey(CentreWithPromoteurItem item, String key) {
        return switch (key) {
            case "idcentre", "id" -> stringify(item.getIdCentre());
            case "codecentre" -> item.getCodeCentre();
            case "codetype" -> item.getCodeType();
            case "libelle" -> item.getLibelle();
            case "idlocalite" -> stringify(item.getIdLocalite());
            case "idiep" -> stringify(item.getIdIep());
            case "idnaturecentre" -> stringify(item.getIdNaturecentre());
            case "idperiodicite" -> stringify(item.getIdPeriodicite());
            case "idautoriteautorisation" -> stringify(item.getIdAutoriteAutorisation());
            case "localite.code", "codelocalite" ->
                    item.getLocalite() != null ? item.getLocalite().getCode() : null;
            case "localite.libelle", "nomlocalite", "localite.nomlocalite" ->
                    item.getLocalite() != null ? item.getLocalite().getLibelle() : null;
            case "iep.code", "codeiep" ->
                    item.getIep() != null ? item.getIep().getCode() : null;
            case "iep.libelle", "nomiep" ->
                    item.getIep() != null ? item.getIep().getLibelle() : null;
            case "naturecentre.code", "codenaturecentre" ->
                    item.getNaturecentre() != null ? item.getNaturecentre().getCode() : null;
            case "naturecentre.libelle", "libellenaturecentre" ->
                    item.getNaturecentre() != null ? item.getNaturecentre().getLibelle() : null;
            case "periodicite.code", "codeperiodicite" ->
                    item.getPeriodicite() != null ? item.getPeriodicite().getCode() : null;
            case "periodicite.libelle", "libelleperiodicite" ->
                    item.getPeriodicite() != null ? item.getPeriodicite().getLibelle() : null;
            case "autoriteautorisation.code", "codeautorisation" ->
                    item.getAutoriteAutorisation() != null ? item.getAutoriteAutorisation().getCode() : null;
            case "autoriteautorisation.libelle", "libelleautoriteautorisation" ->
                    item.getAutoriteAutorisation() != null ? item.getAutoriteAutorisation().getLibelle() : null;
            case "autorisation" -> stringify(item.getAutorisation());
            case "estelectrifie" -> stringify(item.getEstElectrifie());
            case "adeleau" -> stringify(item.getADeLeau());
            case "nombrevisite" -> stringify(item.getNombreVisite());
            case "localisationcentre" -> item.getLocalisationCentre();
            case "nommilieuimplentation" -> item.getNomMilieuImplentation();
            case "encadreurnonmena" -> item.getEncadreurNonMena();
            case "encadrerparmena" -> stringify(item.getEncadrerParMena());
            case "idpromoteur", "promoteur.id" -> stringify(item.getPromoteur() != null ? item.getPromoteur().getIdPromoteur() : null);
            case "codepromoteur", "promoteur.codepromoteur" -> item.getPromoteur() != null ? item.getPromoteur().getCodePromoteur() : null;
            case "libellepromoteur", "promoteur.libellepromoteur" -> item.getPromoteur() != null ? item.getPromoteur().getLibellePromoteur() : null;
            case "typepromoteur", "promoteur.typepromoteur" ->
                    item.getPromoteur() != null && item.getPromoteur().getTypePromoteur() != null ? item.getPromoteur().getTypePromoteur().name() : null;
            case "personnephysique.nom", "nom" ->
                    item.getPromoteur() != null && item.getPromoteur().getPersonnePhysique() != null ? item.getPromoteur().getPersonnePhysique().getNom() : null;
            case "personnephysique.prenom", "prenom" ->
                    item.getPromoteur() != null && item.getPromoteur().getPersonnePhysique() != null ? item.getPromoteur().getPersonnePhysique().getPrenom() : null;
            case "personnephysique.contact" ->
                    item.getPromoteur() != null && item.getPromoteur().getPersonnePhysique() != null ? item.getPromoteur().getPersonnePhysique().getContact() : null;
            case "personnemorale.denomination", "denomination" ->
                    item.getPromoteur() != null && item.getPromoteur().getPersonneMorale() != null ? item.getPromoteur().getPersonneMorale().getDenomination() : null;
            case "personnemorale.nomprogramme", "nomprogramme" ->
                    item.getPromoteur() != null && item.getPromoteur().getPersonneMorale() != null ? item.getPromoteur().getPersonneMorale().getNomProgramme() : null;
            case "personnemorale.nomrepresentant", "nomrepresentant" ->
                    item.getPromoteur() != null && item.getPromoteur().getPersonneMorale() != null ? item.getPromoteur().getPersonneMorale().getNomRepresentant() : null;
            case "personnemorale.mail", "mail" ->
                    item.getPromoteur() != null && item.getPromoteur().getPersonneMorale() != null ? item.getPromoteur().getPersonneMorale().getMail() : null;
            case "personnemorale.libelletypepersonnemorale", "libelletypepersonnemorale" ->
                    item.getPromoteur() != null && item.getPromoteur().getPersonneMorale() != null ? item.getPromoteur().getPersonneMorale().getLibelleTypePersonneMorale() : null;
            default -> null;
        };
    }

    private static String stringify(Object v) {
        return Objects.toString(v, null);
    }
}
