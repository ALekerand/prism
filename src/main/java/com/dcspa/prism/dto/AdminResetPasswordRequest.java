package com.dcspa.prism.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminResetPasswordRequest {

    @NotBlank
    private String newPassword;
}
