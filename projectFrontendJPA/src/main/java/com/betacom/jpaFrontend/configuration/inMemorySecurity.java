package com.betacom.jpaFrontend.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import lombok.RequiredArgsConstructor;
import security.CustomUserDetailsServices;
@RequiredArgsConstructor
@Configuration
public class inMemorySecurity {
	private final CustomUserDetailsServices customUserDetailsServices;
	@Bean
	InMemoryUserDetailsManager InMemoryUserDetailsManager();
		return customUserDetailsServices.loadUsers();
}
