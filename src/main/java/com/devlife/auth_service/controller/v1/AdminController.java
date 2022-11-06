package com.devlife.auth_service.controller.v1;

import com.devlife.auth_service.security.UserDetailsImpl;
import com.devlife.auth_service.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/adm/")
@RequiredArgsConstructor
public class AdminController {
    private final UserService userService;

    @GetMapping("decode")
    @PreAuthorize("hasAuthority('ROOT')")
    public ResponseEntity<UserDetailsImpl> decodeJwt(@RequestParam("jwt") String jwt) {
        return ResponseEntity.ok(userService.decodeJwt(jwt));
    }
}
