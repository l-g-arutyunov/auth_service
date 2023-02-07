package com.devlife.auth_service.feign.profile;

import com.devlife.auth_service.feign.profile.model.InitProfileReq;
import com.devlife.auth_service.feign.profile.model.ProfileDto;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.Map;

@FeignClient(name = "profile-service", url = "${feign-services.profile-service}/api/v1/profile")
public interface ProfileFeignService {

    @PutMapping("/init")
    @Operation(summary = "Init profile while authorization", tags = {"profile"})
    ResponseEntity<ProfileDto> initProfile(
            @RequestHeader Map<String, String> headers,
            @RequestBody InitProfileReq initProfileReq
    );
}
