package com.devlife.auth_service.feign.profile.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployerDto implements Serializable {

    private Long externalId;
    private Long id;
    private String name;

}
