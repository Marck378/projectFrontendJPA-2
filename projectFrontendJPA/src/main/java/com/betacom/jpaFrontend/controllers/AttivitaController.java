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

import com.betacom.jpaFrontend.dto.inputs.AttivitaReq;
import com.betacom.jpaFrontend.dto.outputs.AttivitaDTO;
import com.betacom.jpaFrontend.response.Resp;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Controller
public class AttivitaController {
	
	private final WebClient clientWeb;

	@GetMapping("/listAttivita")
	public ModelAndView listAttivita(
			@RequestParam(required = false) Integer id,
			@RequestParam(required = false) String description			
			) {
		ModelAndView mav = new ModelAndView("listAttivita");
		
		List<AttivitaDTO> attivita = clientWeb.get()
				 .uri(uriBuilder -> uriBuilder
					    .path("attivita/list")
					    .queryParamIfPresent("id", Optional.ofNullable(id))
					    .queryParamIfPresent("description", Optional.ofNullable(description))
					    .build())
					.retrieve()
					.bodyToMono(new ParameterizedTypeReference<List<AttivitaDTO>>() {})
					.block();
		
		mav.addObject("listattivita", attivita);
		mav.addObject("param", new AttivitaReq());
		
		return mav;
	}
	
	
	@GetMapping("createAttivita")
	public ModelAndView createAttivita(Model model) {
		ModelAndView mav = new ModelAndView("createAttivita");
		if (!model.containsAttribute("attivita")) {
	        model.addAttribute("attivita", new AttivitaReq());
	    }
		model.addAttribute("titolo", "Creazione nuova attivita");
		return mav;
	}
	
	@PostMapping("saveAttivita")
	public String saveAttivita(@ModelAttribute("attivita") AttivitaReq req,  RedirectAttributes ra){
		
		String operation = (req.getId() == null) ? "create" : "update";
		String errorLink = (req.getId() == null) ? "createAttivita" : "updateAttivita";

		String url = "attivita/" + operation;
		
		HttpMethod metodo = (req.getId() == null) ? HttpMethod.POST : HttpMethod.PUT;
		
		ResponseEntity<Resp> response = clientWeb.method(metodo)
				.uri(url)
				.bodyValue(req)
				.exchangeToMono(resp -> resp.toEntity(Resp.class) )
				.block();

		if (!response.getStatusCode().is2xxSuccessful()) {
			 ra.addFlashAttribute("errorMsg", response.getBody().getMsg());
			 ra.addFlashAttribute("attivita", req);
			 log.debug(req.toString());
			 return "redirect:/" + errorLink;
		}

		return "redirect:/listAttivita";
}	
	
	@GetMapping("updateAttivita")
	public ModelAndView updateAttivita(@RequestParam(required = true) Integer id, Model model) {
		ModelAndView mav = new ModelAndView("createAttivita");
		AttivitaDTO att = clientWeb.get()
				.uri(uriBuilder -> uriBuilder
						.path("attivita/findById")
						.queryParam("id", id)
						.build()
						).retrieve()
				.bodyToMono(AttivitaDTO.class)
				.block();
				
		log.debug("description {}", att.getDescription());
		if (!model.containsAttribute("attivita")) {
			AttivitaReq attivitaReq = new AttivitaReq();
			attivitaReq.setId(att.getId());
			attivitaReq.setDescription(att.getDescription());
			model.addAttribute("attivita", attivitaReq);
	    }
		
		model.addAttribute("titolo", "Aggiornamento attivita");
		return mav;
	}
	
	@GetMapping("removeAttivita")
	public String removeAttivita(@RequestParam(required = true) Integer id) {
		log.debug("removeAttivita {}", id);
		
		Resp resp = clientWeb.delete()
				.uri(uriBuilder -> uriBuilder
						.path("attivita/delete/{id}")
						.build(id))
				.retrieve()
				.bodyToMono(Resp.class)
				.block();
		
		return "redirect:/listAttivita";
	}
}