package com.devlife.auth_service.feign.profile;

import com.devlife.auth_service.feign.profile.model.AuthorizationDto;
import com.devlife.auth_service.feign.profile.model.InitProfileReq;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class PrfFeignService {
    private final ProfileFeignService profileFeignService;

    public AuthorizationDto initProfile(InitProfileReq initProfileReq, String token) {
        ResponseEntity<AuthorizationDto> authorization = profileFeignService.initProfile(
                Collections.singletonMap("Authorization", token),
                initProfileReq
        );

        return authorization.getBody();
    }

    public Long getUserIdByAuthId(Long userAuthId) {
        return profileFeignService.getUserIdByAuthId(userAuthId);
    }
}
