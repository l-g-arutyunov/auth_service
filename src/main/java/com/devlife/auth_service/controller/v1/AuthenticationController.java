package com.devlife.auth_service.controller.v1;

import com.devlife.auth_service.pojo.LoginRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.naming.AuthenticationException;

@RestController
@RequestMapping(name = "/api/v1/auth/")
@RequiredArgsConstructor
public class AuthenticationController {

    public ResponseEntity login(@RequestBody LoginRequest loginRequest) {
        try {
            return
        } catch (AuthenticationException e) {
            throw new  BadCredentialsException("Invalid login or password");
        }

    }

}
