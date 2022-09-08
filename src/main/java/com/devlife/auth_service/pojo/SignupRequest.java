package com.devlife.auth_service.pojo;

import lombok.Data;

import java.util.Set;

@Data
public class SignupRequest {
    private String username;
    private String email;
    private String phoneNumber;
    private Set<String> roles;
    private String password;
}
