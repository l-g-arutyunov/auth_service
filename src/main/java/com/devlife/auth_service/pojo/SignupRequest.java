package com.devlife.auth_service.pojo;

import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
public class SignupRequest {
    private String email;
    private String phoneNumber;
    private Set<String> roles;
    private String password;
}
