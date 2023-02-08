package com.devlife.auth_service.feign.profile.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class InitProfileReq {
    private Long externalId;
    private String firstName;
    private String lastName;
    private String middleName;
    private String nickname;
    private List<ContactInfo> contactInfoList;

    @Data
    @AllArgsConstructor
    public static class ContactInfo {
        private Integer contactType;
        private String contactValue;
    }
}
