package com.devlife.auth_service.feign.profile;

import com.devlife.auth_service.feign.profile.model.InitProfileReq;
import com.devlife.auth_service.feign.profile.model.ProfileDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PrfFeignService {
    private final ProfileFeignService profileFeignService;

    public void initProfile(Long userId, String nickname, Map<Integer, String> contactInfo, String token) {
        profileFeignService.initProfile(
                Collections.singletonMap("Authorization", token),
                InitProfileReq.builder()
                        .externalId(userId)
                        .nickname(nickname)
                        .contactInfoList(contactInfo.entrySet().stream()
                                .map(entry -> new InitProfileReq.ContactInfo(entry.getKey(), entry.getValue()))
                                .collect(Collectors.toList()))
                        .build()
        );
    }
}
