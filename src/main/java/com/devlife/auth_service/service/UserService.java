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
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;
import java.util.function.UnaryOperator;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final TokenProvider tokenProvider;
    private final UserDetailsServiceImpl userDetailsService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    public JwtResponse signin(SigninRequest signinRequest) {
        String authItem = signinRequest.getAuthItem();
        UserDetailsImpl userDetails = null;
        if (isPhoneNumber(authItem)) {
            userDetails = (UserDetailsImpl) getUserByPhoneNumber(authItem, signinRequest.getPassword());
        }
        if (userDetails != null) {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userDetails.getUsername(), signinRequest.getPassword()));
            JwtResponse jwtResponse = JwtResponse.builder()
                    .token(tokenProvider.createToken(userDetails))
                    .userDetails(userDetails)
                    .build();
            return jwtResponse;
        }
        return null;
    }

    private UserDetails getUserByPhoneNumber(String phoneNumber, String password) {
        Optional<UserEntity> userOpt = userRepository.findByPhoneNumber(phoneNumber);
        if (userOpt.isPresent()) {
            return userDetailsService.loadUserByUsername(userOpt.get().getUsername());
        }
        return null;
    }

    /**
     * Проверка на то что передано цифровое значение
     *
     * @param authItem
     * @return boolean
     */
    private boolean isPhoneNumber(String authItem) {
        Pattern pattern = Pattern.compile("\\d+"); //TODO возможно стоит добавить "+", для +7...
        return pattern.matcher(authItem).matches();
    }

    public JwtResponse signup(SignupRequest signupRequest) {
        RoleEntity role = roleRepository.findByName(RoleEnum.USER)
                .orElseGet(() -> roleRepository.saveAndFlush(new RoleEntity().builder().name(RoleEnum.USER).build()));

        UserEntity user = UserEntity.builder()
                .username(signupRequest.getUsername())
                .email(signupRequest.getEmail())
                .phoneNumber(signupRequest.getPhoneNumber())
                .password(passwordEncoder.encode(signupRequest.getPassword()))
                .roles(Set.of(role))
                .enabled(true)
                .build();

        checkUniqueData(user);

        UserDetailsImpl userDetails = UserDetailsImpl.build(userRepository.save(user));
        JwtResponse jwtResponse = JwtResponse.builder()
                .token(tokenProvider.createToken(userDetails))
                .userDetails(userDetails)
                .build();
        return jwtResponse;
    }

    private void checkUniqueData(UserEntity user) {
        final UnaryOperator<String> nonUnique = param -> {
            throw new BadCredentialsException(
                    String.format("Field %s1 is not unique", param)
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
