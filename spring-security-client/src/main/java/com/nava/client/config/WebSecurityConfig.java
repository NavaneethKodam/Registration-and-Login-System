package com.nava.client.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
public class WebSecurityConfig {     // this class is all about spring security and allowing api's which are mentioned

    private  static final String[] WHITE_LIST_URLS = {"/Navaneeth","/clientRegister","/verifyRegistration","/savePassword","/resetPassword","/resendVerifyToken*"};


@Bean
    public PasswordEncoder passwordEncoder()
    {
        return new BCryptPasswordEncoder(11);
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors()
                .and()
                .csrf()
                .disable()
                .authorizeHttpRequests()
                .antMatchers(WHITE_LIST_URLS).permitAll();

        return http.build();


    }


}
