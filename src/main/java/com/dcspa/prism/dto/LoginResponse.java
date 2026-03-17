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
}

