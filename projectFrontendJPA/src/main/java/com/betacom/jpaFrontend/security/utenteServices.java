package com.betacom.jpaFrontend.security;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.stereotype.Service;

import com.betacom.jpaFrontend.dto.inputs.UtenteREQ;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class utenteServices {
	private final PasswordEncoder getPasswordEncoder;
	private final InMemoryUserDetailsManager inMemoryUserDetailsManager;
	public void updateUtente(UtenteREQ req) {
		if(inMemoryUserDetailsManager.userExists(req.getUserName())) {
			inMemoryUserDetailsManager.deleteUser(req.getUserName());
			log.debug("user{}deleted",req.getUserName());
		}
		inMemoryUserDetailsManager.createUser(User
				.withUsername(req.getUserName())
				.password(getPasswordEncoder.encode(req.getPwd().toString()))
				.roles(req.getRole())
				.build());
		log.debug("User{} is created",req.getUserName());
				
	}
	
	public void deleteUtente(String userName) {
		if(inMemoryUserDetailsManager.userExists(userName)) {
			inMemoryUserDetailsManager.deleteUser(userName);
			log.debug("user{}deleted",userName);
		}

	}
}
