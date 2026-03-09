package com.betacom.jpaFrontend.controllers;

import java.util.List;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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
@RequestMapping("/admin")
public class AdminController {

	private final WebClient webclient; // Collegamento al backend
	
	// 1. MOSTRA LA LISTA
	@GetMapping("/listAttivita")
	public ModelAndView listAttivita() {
		ModelAndView mav = new ModelAndView("admin/listAttivita");
		
		// Chiamata al backend per avere tutte le attività
		List<AttivitaDTO> att = webclient.get()
				 .uri(uriBuilder -> uriBuilder.path("attivita/list").build())
				 .retrieve()
				 .bodyToMono(new ParameterizedTypeReference<List<AttivitaDTO>>() {})
				 .block();
		
		mav.addObject("listAttivita", att); // Mettiamo la lista nella "scatola"
		return mav;
	}
	
	// 2. FORM NUOVA ATTIVITÀ
	@GetMapping("/createAttivita")
	public ModelAndView createAttivita(Model model) {
		ModelAndView mav = new ModelAndView("admin/createAttivita");
		if (!model.containsAttribute("attivita")) {
			model.addAttribute("attivita", new AttivitaReq());
		}
		model.addAttribute("titolo", "Creazione nuova attività");
		return mav;
	}
	
	// 3. FORM MODIFICA ATTIVITÀ
	@GetMapping("/updateAttivita")
	public ModelAndView updateAttivita(@RequestParam(required = true) Integer id, @RequestParam(required = true) String description, Model model) {
		ModelAndView mav = new ModelAndView("admin/createAttivita");
		
		AttivitaReq req = new AttivitaReq();
		req.setId(id);
		req.setDescription(description);

		if (!model.containsAttribute("attivita")) {
			model.addAttribute("attivita", req);
		}
		model.addAttribute("titolo", "Aggiornamento Attività");
		return mav;
	}

	// 4. SALVA NEL DATABASE (Creazione o Modifica)
	@PostMapping("/saveAttivita")
	public String saveAttivita(@ModelAttribute("attivita") AttivitaReq req, RedirectAttributes ra) {
		String operation = (req.getId() == null) ? "create" : "update";
		String url = "attivita/" + operation;
		HttpMethod metodo = (req.getId() == null) ? HttpMethod.POST : HttpMethod.PUT;
		
		ResponseEntity<Resp> response = webclient.method(metodo)
				.uri(url)
				.bodyValue(req)
				.exchangeToMono(resp -> resp.toEntity(Resp.class))
				.block();

		if (!response.getStatusCode().is2xxSuccessful()) {
			 ra.addFlashAttribute("errorMsg", response.getBody().getMsg());
			 ra.addFlashAttribute("attivita", req);
			 return (req.getId() == null) ? "redirect:/admin/createAttivita" : "redirect:/admin/updateAttivita?id=" + req.getId() + "&description=" + req.getDescription();
		}
		return "redirect:/admin/listAttivita";
	}
	
	// 5. ELIMINA ATTIVITÀ
	@GetMapping("/removeAttivita")
	public String removeAttivita(@RequestParam(required = true) Integer id) {
		Resp resp = webclient.delete()
				 .uri("attivita/delete/{id}", id)
				 .retrieve()
				 .bodyToMono(Resp.class)
				 .block();
		return "redirect:/admin/listAttivita";
	}
}