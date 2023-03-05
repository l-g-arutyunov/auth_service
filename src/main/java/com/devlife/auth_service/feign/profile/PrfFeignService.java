package com.devlife.auth_service.feign.profile;

import com.devlife.auth_service.feign.profile.model.InitProfileReq;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class PrfFeignService {
    private final ProfileFeignService profileFeignService;

    public void initProfile(InitProfileReq initProfileReq, String token) {
        profileFeignService.initProfile(
                Collections.singletonMap("Authorization", token),
                initProfileReq
        );
    }
}
