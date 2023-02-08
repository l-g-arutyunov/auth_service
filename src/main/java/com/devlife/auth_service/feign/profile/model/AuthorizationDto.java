package com.devlife.auth_service.feign.profile.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthorizationDto implements Serializable {

    private Long AuthUser;
    private LocalDate DateOfRegistration;
    private ProfileDto Profile;

}
