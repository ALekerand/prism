package com.dcspa.prism.dto;

import java.util.List;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LoginResponse {

    private final String token;
    private final String type;
    private final Integer userId;
    private final String username;
    private final String email;
    private final List<String> roles;
    private final List<String> permissions;
    private final Integer idRegion;
    private final Integer idDrena;
    private final Integer idIep;
    private final Integer idDepartement;
    private final Integer idSousPrefecture;
    private final Integer idCommune;
    private final Integer idLocalite;
}

