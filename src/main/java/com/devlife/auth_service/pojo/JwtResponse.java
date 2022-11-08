package com.devlife.auth_service.pojo;

import com.devlife.auth_service.security.UserDetailsImpl;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class JwtResponse {
    private final String type = "Bearer";
    private String token;
    private UserDetailsImpl userDetails;
}
