package com.betacom.jpaFrontend.controllers;

import java.time.LocalDate;
import java.util.List;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.servlet.ModelAndView;

import com.betacom.jpaFrontend.dto.inputs.AbbonamentoReq;
import com.betacom.jpaFrontend.dto.inputs.AttivitaReq;
import com.betacom.jpaFrontend.dto.outputs.AttivitaDTO;
import com.betacom.jpaFrontend.dto.outputs.SocioDTO;
import com.betacom.jpaFrontend.response.Resp;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
@RequiredArgsConstructor
public class AbbonamentoController {

	private final WebClient webClient;
	
	@GetMapping("listAbbonamento")
	public ModelAndView listAbbonamento(@RequestParam(required = true)Integer id) {
		ModelAndView mav = new ModelAndView("listAbbonamento");
		
		SocioDTO soc = webClient.get()
				.uri(uriBuilder -> uriBuilder
						.path("socio/findById")
						.queryParam("id", id)
						.build()
						).retrieve()
				.bodyToMono(SocioDTO.class)
				.block();
		
		mav.addObject("socio", soc);
		mav.addObject("titolo", "Elenco abbonamento per " + soc.getNome() + " " + soc.getCognome());
		return mav;
	}
	
	@GetMapping("createAbbonamento")
	public String createAbbonamento(@RequestParam(required = true) Integer id) {
		log.debug("createAbbonamento - received id: {}", id);
		
		AbbonamentoReq req = new AbbonamentoReq();
		req.setSocioId(id);
		req.setDataIscrizione(LocalDate.now());
		
		log.debug("AbbonamentoReq created - socioId: {}, dataIscrizione: {}", req.getSocioId(), req.getDataIscrizione());
		log.debug("Creating abbonamento with request: {}", req);
		
		try {
			ResponseEntity<Resp> response = webClient.post()
					.uri("abbonamento/create")
					.bodyValue(req)
					.exchangeToMono(resp -> resp.toEntity(Resp.class) )
					.block();
			
			log.debug("Response from abbonamento/create: {}", response);
			if (response != null) {
				log.debug("Response status: {}, body: {}", response.getStatusCode(), response.getBody());
			}
		} catch (Exception e) {
			log.error("Error creating abbonamento", e);
		}
		
		return "redirect:/listAbbonamento?id=" + id;
	}
	
	@GetMapping("aggiungiAttivita")
	public ModelAndView aggiungiAttivita(@RequestParam(required = true)Integer socioId, @RequestParam(required = true)Integer abbonamentoId) {
		log.debug("aggiungiAttivita {} / {}", socioId, abbonamentoId);
		
		ModelAndView mav = new ModelAndView("aggiungiAttivita");
		
		List<AttivitaDTO> att = webClient.get()
				 .uri(uriBuilder -> uriBuilder
					    .path("attivita/list")
					    .build())
					.retrieve()
					.bodyToMono(new ParameterizedTypeReference<List<AttivitaDTO>>() {})
					.block();
		mav.addObject("socioId", socioId);
		mav.addObject("abbonamentoId", abbonamentoId);
		
		mav.addObject("listAttivita", att);
		
		AttivitaReq req = new AttivitaReq();
		req.setAbbonamentID(abbonamentoId);
		req.setSocioID(socioId);
		mav.addObject("params", req);
		return mav;
	}
	
	@PostMapping("saveAttivitaAbbonamento")
	public String saveAttivitaAbbonamento(@ModelAttribute("params") AttivitaReq req) {
		log.debug("saveAttivitaAbbonamento {}", req);

		ResponseEntity<Resp> response = webClient.post()
				.uri("attivita/createAttivitaAbbonamento")
				.bodyValue(req)
				.exchangeToMono(resp -> resp.toEntity(Resp.class) )
				.block();
		
		log.debug(response.toString());
		return "redirect:/listAbbonamento?id=" + req.getSocioID();
	}

	@GetMapping("removeAttivitaAbbonamento")
	public String removeAttivitaAbbonamento(@RequestParam(required = true) Integer abbonamentoId,
			              @RequestParam(required = true) Integer attivitaId,
			              @RequestParam(required = true) Integer socioId) {
		log.debug("removeAttivitaAbbonamento {}/{}/{}", abbonamentoId, attivitaId, socioId);
		
		
		Resp resp = webClient.delete()
				 .uri("attivita/deleteAttivitaAbbonamento/{abbonamentoId}/{attivitaId}", abbonamentoId, attivitaId)
				 .retrieve()
				 .bodyToMono(Resp.class)
				 .block();
		
		log.debug(resp.toString());

		return "redirect:/listAbbonamento?id=" + socioId;

	}


	@GetMapping("removeAbbonamento")
	public String removeAbbonamento(@RequestParam(required = true) Integer abbonamentoId, @RequestParam(required = true) Integer socioId ) {
		log.debug("removeAbbonamento {}", abbonamentoId);
		
		
		Resp resp = webClient.delete()
				 .uri("abbonamento/delete/{abbonamentoId}", abbonamentoId)
				 .retrieve()
				 .bodyToMono(Resp.class)
				 .block();
		
		log.debug(resp.toString());

		return "redirect:/listAbbonamento?id=" + socioId;

	}
}
