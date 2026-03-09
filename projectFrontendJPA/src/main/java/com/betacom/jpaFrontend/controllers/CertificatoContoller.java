package com.betacom.jpaFrontend.controllers;


import static com.betacom.jpaFrontend.utils.Utils.dateToString;
import static com.betacom.jpaFrontend.utils.Utils.stringToDate;

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

import com.betacom.jpaFrontend.dto.inputs.CertificatoReq;
import com.betacom.jpaFrontend.dto.outputs.CertificatoDTO;
import com.betacom.jpaFrontend.response.Resp;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Controller
public class CertificatoContoller {

	private final WebClient webclient;
	
	@GetMapping("updateCertificato")
	public ModelAndView listCertificato(@RequestParam (required = true) Integer id,   Model model) {
		
		ModelAndView mav = new ModelAndView("listCertificato");
		

		CertificatoDTO cer = webclient.get()
				 .uri(uriBuilder -> uriBuilder
					.path("certificato/findById")
					.queryParam("id", id)
					.build())
				 .retrieve()
				 .bodyToMono(CertificatoDTO.class)
				 .block();

		log.debug("certificato {} / {}", cer.getTipo(), cer.getDataCertificato());
		
		if (!model.containsAttribute("certificato")) {
			model.addAttribute("certificato", new CertificatoReq(
					cer.getId(),
					cer.getTipo(),
					cer.getDataCertificato(),
					null,
					dateToString(cer.getDataCertificato()))); 
	    }
		
		model.addAttribute("titolo", "Aggiornamento certificato medico");
		return mav;
	}

	
	@GetMapping("createCertificato")
	public ModelAndView createCertificato(@RequestParam (required = true) Integer id) {
		
		ModelAndView mav = new ModelAndView("listCertificato");
		
		mav.addObject("certificato",new CertificatoReq(null, null, null, id, null));
		mav.addObject("titolo", "Creazione certificato medico");
		return mav;
	}

	@PostMapping("saveCertificato")
	public String saveCertificato(@ModelAttribute("certificato") CertificatoReq req) {
		log.debug("saveCertificato {}", req);
		
		String operation = (req.getId() == null) ? "create" : "update";
		String errorLink = (req.getId() == null) ? "createCertificato" : "updateCertificato";
		
		String url = "certificato/" + operation;
		req.setDataCertificato(stringToDate(req.getDataCertificatoString()));
		
		HttpMethod metodo = (req.getId() == null) ? HttpMethod.POST : HttpMethod.PUT;
		
		ResponseEntity<Resp> response = webclient.method(metodo)
				.uri(url)
				.bodyValue(req)
				.exchangeToMono(resp -> resp.toEntity(Resp.class) )
				.block();
		
		return "redirect:/listSocio";
	}
}
