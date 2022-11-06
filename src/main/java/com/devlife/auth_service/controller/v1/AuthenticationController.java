package com.devlife.auth_service.controller.v1;

import com.devlife.auth_service.pojo.JwtResponse;
import com.devlife.auth_service.pojo.SigninRequest;
import com.devlife.auth_service.pojo.SignupRequest;
import com.devlife.auth_service.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/auth/")
@RequiredArgsConstructor
public class AuthenticationController {
    private final UserService userService;

    @PostMapping("signin")
    public ResponseEntity<JwtResponse> signin(@RequestBody SigninRequest signinRequest) {
        JwtResponse jwtResponse = userService.signin(signinRequest);
        return ResponseEntity.ok(jwtResponse);
    }

    @PutMapping("signup")
    public ResponseEntity<JwtResponse> signup(@RequestBody SignupRequest signupRequest) {
        JwtResponse jwtResponse = userService.signup(signupRequest);
        return ResponseEntity.ok(jwtResponse);
    }
}
