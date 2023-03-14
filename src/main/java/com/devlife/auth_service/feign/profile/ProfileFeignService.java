package com.devlife.auth_service.feign.profile;

import com.devlife.auth_service.feign.profile.model.AuthorizationDto;
import com.devlife.auth_service.feign.profile.model.InitProfileReq;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@FeignClient(name = "profile-service", url = "${feign-services.profile-service}/api/v1/")
public interface ProfileFeignService {

    @PutMapping("profile/init")
    @Operation(summary = "Init profile while authorization", tags = {"profile"})
    ResponseEntity<AuthorizationDto> initProfile(
            @RequestHeader Map<String, String> headers,
            @RequestBody InitProfileReq initProfileReq
    );

    @Operation(summary = "Get external user id by auth user id from auth service", tags = {"user"})
    @GetMapping("user/auth/{id}")
    Long getUserIdByAuthId(
            @RequestHeader Map<String, String> headers,
            @PathVariable("id") Long id);
}