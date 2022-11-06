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
import com.devlife.auth_service.security.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.function.UnaryOperator;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final TokenProvider tokenProvider;
    private final UserDetailsService userDetailsService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    public static final boolean ENABLED = true;
    public static final String FIELD_S_1_IS_NOT_UNIQUE = "Field %s1 is not unique";

    public JwtResponse signin(SigninRequest signinRequest) {
        String authItem = signinRequest.getAuthItem();
        UserDetailsImpl userDetails = null;
        if (isPhoneNumber(authItem)) {
            userDetails = (UserDetailsImpl) getUserByPhoneNumber(authItem);
        } else if (isEmail(authItem)) {
            userDetails = (UserDetailsImpl) getUserByEmail(authItem);
        }
        if (userDetails != null) {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userDetails.getUsername(), signinRequest.getPassword()));
            return JwtResponse.builder()
                    .token(tokenProvider.createToken(userDetails))
                    .userDetails(userDetails)
                    .build();
        }
        return null;
    }

    private UserDetails getUserByPhoneNumber(String phoneNumber) {
        Optional<UserEntity> userOpt = userRepository.findByPhoneNumber(phoneNumber);
        return userOpt.map(i -> userDetailsService.loadUserByUsername(i.getUsername())).orElse(null);
    }

    private UserDetails getUserByEmail(String email) {
        Optional<UserEntity> userOpt = userRepository.findByEmail(email);
        return userOpt.map(i -> userDetailsService.loadUserByUsername(i.getUsername())).orElse(null);
    }

    /**
     * Проверка на то что передан номер телефона
     *
     * @param authItem
     * @return Boolean
     */
    private Boolean isPhoneNumber(String authItem) {
        Pattern pattern = Pattern.compile("\\+?\\d+");
        return pattern.matcher(authItem).matches();
    }

    /**
     * Проверка на то что передан email
     *
     * @param authItem
     * @return Boolean
     */
    private Boolean isEmail(String authItem) {
        Pattern pattern = Pattern.compile(".+@.+\\..+");
        return pattern.matcher(authItem).matches();
    }


    public JwtResponse signup(SignupRequest signupRequest) {
        RoleEntity role = roleRepository.findByName(RoleEnum.USER)
                .orElseGet(() -> roleRepository.saveAndFlush(new RoleEntity().builder().name(RoleEnum.USER).build()));

        UserEntity user = UserEntity.builder()
                .username(UUID.randomUUID().toString())
                .email(signupRequest.getEmail())
                .phoneNumber(signupRequest.getPhoneNumber())
                .password(passwordEncoder.encode(signupRequest.getPassword()))
                .roles(Set.of(role))
                .enabled(ENABLED)
                .build();

        checkUniqueData(user);


        UserDetailsImpl userDetails = UserDetailsImpl.build(userRepository.save(user));
        return JwtResponse.builder()
                .token(tokenProvider.createToken(userDetails))
                .userDetails(userDetails)
                .build();
    }

    private void checkUniqueData(UserEntity user) {
        final UnaryOperator<String> nonUnique = param -> {
            throw new BadCredentialsException(
                    String.format(FIELD_S_1_IS_NOT_UNIQUE, param)
            );
        };
        if (user.getPhoneNumber() != null && userRepository.existsByPhoneNumber(user.getPhoneNumber())) {
            nonUnique.apply("phoneNumber");
        }
        if (user.getPhoneNumber() != null && userRepository.existsByEmail(user.getEmail())) {
            nonUnique.apply("email");
        }
        if (user.getPhoneNumber() != null && userRepository.existsByUsername(user.getUsername())) {
            nonUnique.apply("username");
        }
    }

    public UserDetailsImpl decodeJwt(String jwt) {
        Authentication authentication = tokenProvider.getAuthentication(jwt);
        authentication.getPrincipal();
        return (UserDetailsImpl) authentication.getPrincipal();
    }
}
