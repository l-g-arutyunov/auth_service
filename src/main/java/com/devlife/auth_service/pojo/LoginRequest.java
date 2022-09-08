package com.devlife.auth_service.pojo;

import lombok.Data;

@Data
public class LoginRequest {
    private String authItem;
    private String password;
}
