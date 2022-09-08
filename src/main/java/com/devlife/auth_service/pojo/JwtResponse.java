package com.devlife.auth_service.pojo;

import com.devlife.auth_service.model.RoleEntity;
import lombok.Data;

import java.util.Set;

@Data
public class JwtResponse {
    private String token;
    private String type = "Bearer";
    private Long userId;
    private String username;
    private Set<RoleEntity> roles;
}
