package com.devlife.auth_service.pojo;

import io.swagger.v3.oas.annotations.Parameter;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@Builder
public class SigninRequest {
    @Parameter(name = "authentication item", description = "email or phone number", required = true)
    @NotBlank
    private String authItem;
    @Parameter(name = "password", description = "email or phone number", required = true)
    @NotBlank
    private String password;
}
