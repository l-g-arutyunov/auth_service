package com.devlife.auth_service.service;

import com.devlife.auth_service.model.UserEntity;
import com.devlife.auth_service.pojo.LoginRequest;
import com.devlife.auth_service.repository.UserRepository;
import com.devlife.auth_service.security.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final TokenProvider tokenProvider;
    private final UserDetailsService userDetailsService;

    public void login(LoginRequest loginRequest) {
        String authItem = loginRequest.getAuthItem();
        if (isPhoneNumber(authItem)) {
            loginByPhoneNumber(authItem, loginRequest.getPassword());
        }

    }

    private void loginByPhoneNumber(String phoneNumber, String password) {
        Optional<UserEntity> userOpt = userRepository.findByPhoneNumber(phoneNumber);
        String token = "1";
        userOpt.ifPresent(user -> {
            token = tokenProvider.createToken(userDetailsService.loadUserByUsername(user.getUsername()));
        });

    }

    /**
     * Проверка на то что передано цифровое значение
     * @param authItem
     * @return boolean
     */
    private boolean isPhoneNumber(String authItem) {
        Pattern pattern = Pattern.compile("\\d+"); //TODO возможно стоит добавить "+", для +7...
        return pattern.matcher(authItem).matches();
    }
}
