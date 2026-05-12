package com.dcspa.prism.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppUserAdminResponse {
    private Integer id;
    private String username;
    private String email;
    private Boolean actif;
    private List<Integer> roleIds;
    private Integer idRegion;
    private Integer idDrena;
    private Integer idIep;
    private Integer idDepartement;
    private Integer idSousPrefecture;
    private Integer idCommune;
    private Integer idLocalite;
    private Map<String, Object> region;
    private Map<String, Object> drena;
    private Map<String, Object> iep;
    private Map<String, Object> departement;
    private Map<String, Object> sousPrefecture;
    private Map<String, Object> commune;
    private Map<String, Object> localite;
}

