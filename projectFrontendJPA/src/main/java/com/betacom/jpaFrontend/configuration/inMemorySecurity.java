package com.betacom.jpaFrontend.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import com.betacom.jpaFrontend.security.CustomUserDetailsServices;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Configuration
public class inMemorySecurity {
    
    private final CustomUserDetailsServices customUserDetailsServices;
    
    @Bean
    public InMemoryUserDetailsManager inMemoryUserDetailsManager() {
        return customUserDetailsServices.loadUser();
    }
}