package com.devlife.auth_service.feign.profile.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.OffsetDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthorizationDto implements Serializable {
    private Long id;
    private Long authUserId;
    private OffsetDateTime dateOfRegistration;
    private ProfileDto profile;
}
