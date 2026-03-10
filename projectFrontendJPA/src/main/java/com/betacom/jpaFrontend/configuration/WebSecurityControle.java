package com.betacom.jpaFrontend.configuration;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class WebSecurityControle {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests((requests) -> requests
            .requestMatchers("/admin", "/admin/**").hasRole("ADMIN") // url with admin only for user admin
            .requestMatchers("/").permitAll()                        // other urls are not protected
            .anyRequest().authenticated()                            // all requests must be authenticated
        )
        .formLogin((form) -> form
            .loginPage("/login")
            .permitAll()
        )
        .logout(logout -> logout.permitAll());

        return http.build();
    }

    @Bean
    PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    UserDetailsService userDetailsService() {

        List<UserDetails> userDetailsList = new ArrayList<>();

        UserDetails user = User.withUsername("user")
            .password(getPasswordEncoder().encode("pwd"))
            .roles("USER")
            .build();

        userDetailsList.add(user);

        UserDetails admin = User.withUsername("admin")
            .password(getPasswordEncoder().encode("admin"))
            .roles("ADMIN")
            .build();

        userDetailsList.add(admin);
        
        return new InMemoryUserDetailsManager(userDetailsList);
    }
}