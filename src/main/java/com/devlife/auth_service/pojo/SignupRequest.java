package com.devlife.auth_service.pojo;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@Builder
public class SignupRequest {
    private String email;
    private String phoneNumber;
    @NotBlank
    private String password;
    @Size(max = 30)
    private String firstName;
    @Size(max = 30)
    private String lastName;
    @Size(max = 30)
    private String middleName;
}
