package com.dcspa.prism.dto;

import com.dcspa.prism.entity.TypePromoteur;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class PromoteurUpsertRequest {
    /**
     * Si renseigné, le promoteur existant sera réutilisé.
     * Sinon un nouveau promoteur sera créé avec les champs ci-dessous.
     */
    private Integer id;

    private String codePromoteur;
    private String libellePromoteur;
    private TypePromoteur typePromoteur;
    private PersonnePhysiquePayload personnePhysique;
    private PersonneMoralePayload personneMorale;

    @Getter
    @Setter
    public static class PersonnePhysiquePayload {
        private String libellePersonnePhysique;
        private String nom;
        private String prenom;
        private String contact;
        private String fonction;
        private String sexe;
        private LocalDate dateNaissance;
        private String anciennete;
        private String boitePostale;
        private String niveauEtudes;
        private String civilite;
    }

    @Getter
    @Setter
    public static class PersonneMoralePayload {
        private String denomination;
        private String nomProgramme;
        private String nomRepresentant;
        private String contact;
        private String boitePostale;
        private String mail;
        private Integer idTypePersonneMorale;
    }
}

