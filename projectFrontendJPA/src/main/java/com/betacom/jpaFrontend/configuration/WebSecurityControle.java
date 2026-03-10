package com.betacom.jpaFrontend.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class WebSecurityControle {

	@Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http

            .csrf(csrf -> csrf.disable()) 
            
            .authorizeHttpRequests((requests) -> requests
                .requestMatchers("/admin", "/admin/**").hasRole("ADMIN") 
                
                .requestMatchers("/", "/registrazione", "/saveRegistrazione", "/error").permitAll()
                .anyRequest().authenticated()                            
            )
            .formLogin((form) -> form
                .loginPage("/login")
                
                .defaultSuccessUrl("/listSocio", true) 
                .permitAll()
            )
            .logout(logout -> logout.permitAll());

        return http.build();
    }

    @Bean
    PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}