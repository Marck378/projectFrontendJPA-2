package com.betacom.jpaFrontend.dto.outputs;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SocioDTO {
	private Integer id;
	private String cognome;
	private String nome;
	private String codiceFiscale;
	private String email;
	private CertificatoDTO certificato;
	private List<AbbonamentoDTO> abbonamento;
}
