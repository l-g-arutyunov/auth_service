package com.devlife.auth_service.pojo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SigninRequest {
    private String authItem;
    private String password;
}
