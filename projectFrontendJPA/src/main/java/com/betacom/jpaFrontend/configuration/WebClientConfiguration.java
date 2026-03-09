package com.betacom.jpaFrontend.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfiguration {
	
	@Bean
	WebClient webClient(@Value("${jpa.backend}") String backendUrl) {
		return WebClient.builder()
				.baseUrl(backendUrl)
				.build();
	}

}
