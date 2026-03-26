package com.dcspa.prism.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

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
}

