package com.betacom.jpaFrontend.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.betacom.jpaFrontend.dto.inputs.SocioReq;
import com.betacom.jpaFrontend.dto.outputs.AttivitaDTO;
import com.betacom.jpaFrontend.dto.outputs.SocioDTO;
import com.betacom.jpaFrontend.response.Resp;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Controller
public class SocioController {
	
	private final WebClient clientWeb;

	@GetMapping(value={"/listSocio","/home"})
	public ModelAndView listSocio(
			@RequestParam(required = false) Integer id,
			@RequestParam(required = false) String nome,
			@RequestParam(required = false) String cognome,
			@RequestParam(required = false) Integer attivita			
			) {
		ModelAndView mav = new ModelAndView("listSocio");
		
		List<SocioDTO> soci = clientWeb.get()
				 .uri(uriBuilder -> uriBuilder
					    .path("socio/list")
					    .queryParamIfPresent("id", Optional.ofNullable(id))
					    .queryParamIfPresent("nome", Optional.ofNullable(nome))
					    .queryParamIfPresent("cognome", Optional.ofNullable(cognome))
					    .queryParamIfPresent("attivita", Optional.ofNullable(attivita))
					    .build())
					.retrieve()
					.bodyToMono(new ParameterizedTypeReference<List<SocioDTO>>() {})
					.block();
					
		List<AttivitaDTO> att = clientWeb.get()
				 .uri(uriBuilder -> uriBuilder
					    .path("attivita/list")
					    .build())
					.retrieve()
					.bodyToMono(new ParameterizedTypeReference<List<AttivitaDTO>>() {})
					.block();
		
		
		mav.addObject("listsocio", soci);
		mav.addObject("param", new SocioReq());
		mav.addObject("listAttivita",att);
		
		return mav;
	}
	
	
	@GetMapping("createSocio")
	public ModelAndView createSocio(Model model) {
		ModelAndView mav = new ModelAndView("createSocio");
		if (!model.containsAttribute("socio")) {
	        model.addAttribute("socio", new SocioReq());
	    }
		model.addAttribute("titolo", "Creazione nuovo socio");
		return mav;
	}
	
	@PostMapping("saveSocio")
	public String saveSocio(@ModelAttribute("socio") SocioReq req,  RedirectAttributes ra){
		
		String operation = (req.getId() == null) ? "create" : "update";
		String errorLink = (req.getId() == null) ? "createSocio" : "updateSocio";

		String url = "socio/" + operation;
		
		HttpMethod metodo = (req.getId() == null) ? HttpMethod.POST : HttpMethod.PUT;
		
		ResponseEntity<Resp> response = clientWeb.method(metodo)
				.uri(url)
				.bodyValue(req)
				.exchangeToMono(resp -> resp.toEntity(Resp.class) )
				.block();

		if (!response.getStatusCode().is2xxSuccessful()) {
			 ra.addFlashAttribute("errorMsg", response.getBody().getMsg());
			 ra.addFlashAttribute("socio", req);
			 log.debug(req.toString());
			 return "redirect:/" + errorLink;
		}

		return "redirect:/listSocio";
}	
	
	@GetMapping("updateSocio")
	public ModelAndView updateSocio(@RequestParam(required = true) Integer id, Model model) {
		ModelAndView mav = new ModelAndView("createSocio");
		SocioDTO soc = clientWeb.get()
				.uri(uriBuilder -> uriBuilder
						.path("socio/findById")
						.queryParam("id", id)
						.build()
						).retrieve()
				.bodyToMono(SocioDTO.class)
				.block();
				
		log.debug("nome {} cognome {}", soc.getNome(), soc.getCognome());
		if (!model.containsAttribute("socio")) {
			model.addAttribute("socio", new SocioReq(soc.getId(),
					soc.getCognome(),
					soc.getNome(), 
					soc.getCodiceFiscale(), 
					soc.getEmail(), null));
	    }
		
		model.addAttribute("titolo", "Aggiornamento socio");
		return mav;
	}
	
	@GetMapping("removeSocio")
	public String removeSocio(@RequestParam(required = true) Integer id) {
		log.debug("removeSocio {}", id);
		
		Resp resp = clientWeb.delete()
				.uri(uriBuilder -> uriBuilder
						.path("socio/delete/{id}")
						.build(id))
				.retrieve()
				.bodyToMono(Resp.class)
				.block();
		
		return "redirect:/listSocio";
	}
	
	
}
