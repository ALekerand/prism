package com.dcspa.prism.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

/** Mise à jour du compte par l’utilisateur connecté (pas le rôle ni la circonscription). */
@Getter
@Setter
public class UpdateProfileRequest {

    private String email;
    private String nom;
    private String prenoms;
    private LocalDate dateNaissance;
    private String lieuNaissance;
}
