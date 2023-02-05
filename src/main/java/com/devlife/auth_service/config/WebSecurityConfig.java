package com.devlife.auth_service.config;

import com.devlife.auth_service.security.jwt.AuthTokenFilter;
import com.devlife.auth_service.security.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class WebSecurityConfig {
    //private final UserDetailsService userDetailsService; //TODO Описание ниже
    private final TokenProvider tokenProvider;

    @Value("${security.cors.enable:true}")
    boolean enableCors;
    @Value("${security.cors.allowall:true}")
    boolean allowAllCors;

    @Value("${security.cors.allowedorigin:null}")
    String allowedOrigin;

    @Value("${security.cors.allowedheader:null}")
    String allowedHeader;

    @Value("${security.cors.allowallmethods:false}")
    boolean allowAllCorsMethods;

    @Value("${security.cors.allowedmethods:null}")
    String allowedMethods;

    @Bean
    public AuthTokenFilter authenticationJwtTokenFilter() {
        return new AuthTokenFilter(tokenProvider);
    }

//    @Bean
//    public DaoAuthenticationProvider authenticationProvider() { //TODO Разобраться нужно ли?
//        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
//        authenticationProvider.setUserDetailsService(userDetailsService);
//        authenticationProvider.setPasswordEncoder(passwordEncoder());
//        return authenticationProvider;
//    }
//
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .csrf().disable()
                .cors().configurationSource(corsConfiguration())
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers("/api/v1/auth/**").permitAll()
                .antMatchers("/swagger-ui/**").permitAll()
                .antMatchers("/v3/api-docs/**").permitAll()
                .antMatchers("/health/**", "/info", "/error/**").permitAll()
                .antMatchers("/api/v1/adm/**").authenticated()
                .anyRequest().authenticated();

        http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    public CorsConfigurationSource corsConfiguration() {
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        if (!enableCors) {
            return source;
        }
        final CorsConfiguration config = new CorsConfiguration();
        if (allowAllCors) {
            config.applyPermitDefaultValues();
            config.setAllowedMethods(List.of("*"));
        } else {
            if (allowedOrigin != null) {
                for (String origin : allowedOrigin.split(",")) {
                    config.addAllowedOrigin(origin);
                }
            }
            if (allowedHeader != null) {
                config.setAllowedHeaders(Arrays.asList(allowedHeader.split(",")));
            }
            if (allowAllCorsMethods) {
                config.setAllowedMethods(List.of("*"));
            } else {
                if (allowedMethods != null) {
                    config.setAllowedMethods(Arrays.asList(allowedMethods.split(",")));
                }
            }
        }
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
