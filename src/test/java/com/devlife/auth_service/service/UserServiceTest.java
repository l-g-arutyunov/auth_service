package com.devlife.auth_service.service;

import com.devlife.auth_service.enums.RoleEnum;
import com.devlife.auth_service.model.RoleEntity;
import com.devlife.auth_service.model.UserEntity;
import com.devlife.auth_service.pojo.JwtResponse;
import com.devlife.auth_service.pojo.SigninRequest;
import com.devlife.auth_service.pojo.SignupRequest;
import com.devlife.auth_service.repository.RoleRepository;
import com.devlife.auth_service.repository.UserRepository;
import com.devlife.auth_service.security.UserDetailsImpl;
import com.devlife.auth_service.security.UserDetailsServiceImpl;
import com.devlife.auth_service.security.jwt.TokenProvider;
import lombok.SneakyThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.lang.reflect.Method;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@DisplayName("UserService test")
class UserServiceTest {
    private final UserRepository userRepository = mock(UserRepository.class);
    private final RoleRepository roleRepository = mock(RoleRepository.class);
    private final TokenProvider tokenProvider = mock(TokenProvider.class);
    private final UserDetailsService userDetailsService = mock(UserDetailsServiceImpl.class);
    private final AuthenticationManager authenticationManager = mock(AuthenticationManager.class);
    private final PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);

    private final UserService userService = new UserService(
            userRepository,
            roleRepository,
            tokenProvider,
            userDetailsService,
            authenticationManager,
            passwordEncoder
    );

    @Test
    void signin() {
        SigninRequest signinRequest = SigninRequest.builder()
                .authItem("test@test.com")
                .password("password")
                .build();

        UserDetails userDetails = new UserDetailsImpl(1L,
                "test",
                "test@test.com",
                "88005553535",
                "1",
                true,
                null);

        UserEntity userEntity = UserEntity.builder()
                .id(1L)
                .username("test")
                .build();

        doReturn(userDetails).when(userDetailsService).loadUserByUsername(any());
        doReturn(Optional.of(userEntity)).when(userRepository).findByEmail("test@test.com");
        doReturn("test_jwt").when(tokenProvider).createToken(userDetails);

        JwtResponse jwtResponse = userService.signin(signinRequest);

        JwtResponse referenceJwtResponse = JwtResponse.builder()
                .token("test_jwt")
                .userDetails((UserDetailsImpl) userDetails)
                .build();

        assertEquals(jwtResponse, referenceJwtResponse);

        verify(userRepository, times(1)).findByEmail(any());
        verify(userDetailsService, times(1)).loadUserByUsername(any());
        verify(tokenProvider, times(1)).createToken(any());
    }

    @Test
    void signup() {
        SignupRequest signupRequest = SignupRequest.builder()
                .email("test@test.com")
                .phoneNumber("88005553535")
                .password("password")
                .build();

        UserDetails userDetails = new UserDetailsImpl(1L,
                "test",
                "test@test.com",
                "88005553535",
                "1",
                true,
                null);

        RoleEntity roleEntity = RoleEntity.builder()
                .name(RoleEnum.USER).build();

        UserEntity userEntity = UserEntity.builder()
                .id(1L)
                .username("test")
                .phoneNumber("88005553535")
                .email("test@test.com")
                .password("1")
                .enabled(true)
                .build();

        doReturn(Optional.of(roleEntity)).when(roleRepository).findByName(RoleEnum.USER);
        doReturn(false).when(userRepository).existsByPhoneNumber(any());
        doReturn(false).when(userRepository).existsByEmail(any());
        doReturn(false).when(userRepository).existsByUsername(any());
        doReturn(userEntity).when(userRepository).save(any());
        doReturn("test_jwt").when(tokenProvider).createToken(userDetails);


        JwtResponse jwtResponse = userService.signup(signupRequest);

        JwtResponse referenceJwtResponse = JwtResponse.builder()
                .token("test_jwt")
                .userDetails((UserDetailsImpl) userDetails)
                .build();

        assertEquals(jwtResponse, referenceJwtResponse);

        verify(roleRepository, times(1)).findByName(any());
        verify(userRepository, times(1)).existsByPhoneNumber(any());
        verify(userRepository, times(1)).existsByEmail(any());
        verify(userRepository, times(1)).existsByUsername(any());
        verify(userRepository, times(1)).save(any());
        verify(tokenProvider, times(1)).createToken(any());
    }

    @Test
    void decodeJwt() {
    }

    @SneakyThrows
    @Test
    void isPhoneNumber() {
        String phoneNumber1 = "8908123123";
        String phoneNumber2 = "+7908123123";
        String phoneNumber3 = "+phoneNumber";

        Method method_isPhoneNumber = UserService.class.getDeclaredMethod("isPhoneNumber", String.class);
        method_isPhoneNumber.setAccessible(true);

        assertTrue((Boolean) method_isPhoneNumber.invoke(userService, phoneNumber1));
        assertTrue((Boolean) method_isPhoneNumber.invoke(userService, phoneNumber2));
        assertFalse((Boolean) method_isPhoneNumber.invoke(userService, phoneNumber3));
    }

    @SneakyThrows
    @Test
    void isEmail() {
        String email1 = "123@gmail.com";
        String email2 = "@mail.ru";
        String email3 = "123.com";
        String email4 = "test@gmail";

        Method method_isEmail = UserService.class.getDeclaredMethod("isEmail", String.class);
        method_isEmail.setAccessible(true);

        assertTrue((Boolean) method_isEmail.invoke(userService, email1));
        assertFalse((Boolean) method_isEmail.invoke(userService, email2));
        assertFalse((Boolean) method_isEmail.invoke(userService, email3));
        assertFalse((Boolean) method_isEmail.invoke(userService, email4));
    }
}