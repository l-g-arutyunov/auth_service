package com.devlife.auth_service.pojo;

import lombok.Data;

@Data
public class SigninRequest {
    private String authItem;
    private String password;
}
