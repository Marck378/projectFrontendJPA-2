package com.betacom.jpaFrontend.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/test")
public class ControllerTest {

	@GetMapping("/hello")

	public ModelAndView hello(@RequestParam("nome") String nome) {
		ModelAndView mav = new ModelAndView("test");
		log.debug("ControllerTest:" + nome);
		
		mav.addObject("nome", nome);
		return mav;
		
	}
}
