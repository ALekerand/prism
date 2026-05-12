package com.dcspa.prism.dto;

import lombok.Data;

import java.util.List;

@Data
public class AppUserAdminUpsertRequest {
    private String username;
    private String email;
    private Boolean actif;

    /** Mot de passe en clair à l'admin (sera hashé). Optionnel en update. */
    private String password;

    /** Rôles à associer (optionnel). */
    private List<Integer> roleIds;

    private Integer idRegion;
    private Integer idDrena;
    private Integer idIep;
    private Integer idDepartement;
    private Integer idSousPrefecture;
    private Integer idCommune;
    private Integer idLocalite;
}

