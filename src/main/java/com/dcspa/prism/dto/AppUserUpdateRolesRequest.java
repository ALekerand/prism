package com.dcspa.prism.dto;

import lombok.Data;

import java.util.List;

@Data
public class AppUserUpdateRolesRequest {
    private List<Integer> roleIds;
}

