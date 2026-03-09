package com.betacom.jpaFrontend.dto.outputs;

import java.time.LocalDate;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AbbonamentoDTO {
	private Integer id;
	private LocalDate dataIscrizione;
	private List<AttivitaDTO> attivita;
}
