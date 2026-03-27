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
}

