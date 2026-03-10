package com.betacom.jpaFrontend.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.betacom.jpaFrontend.dto.inputs.UtenteREQ;
import com.betacom.jpaFrontend.response.Resp;
import com.betacom.jpaFrontend.security.utenteServices;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Controller
public class RegistraController {

    private final WebClient webClient;
    private final utenteServices uS;
    @GetMapping("/registrazione")
    public ModelAndView registrazione(Model model) {
        ModelAndView mav = new ModelAndView("registrazione");
        log.debug("registra...");
        
    
        if (!model.containsAttribute("req")) {
            mav.addObject("req", new UtenteREQ());
        }
        
        return mav;
    }

    @PostMapping("/saveRegistrazione")
    public String saveRegistrazione(@ModelAttribute("req") UtenteREQ req, RedirectAttributes ra) {
        
        req.setRole("USER");

        ResponseEntity<Resp> response = webClient.post()
                .uri("utente/create")
                .bodyValue(req)
                .exchangeToMono(resp -> resp.toEntity(Resp.class))
                .block();

        if (!response.getStatusCode().is2xxSuccessful()) {
            ra.addFlashAttribute("errorMsg", response.getBody().getMsg());
            ra.addFlashAttribute("req", req);
            return "redirect:/registrazione";
        }
        uS.updateUtente(req);
        return "redirect:/login";
    }
}