package com.betacom.jpaFrontend.dto.inputs;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class AttivitaReq {
	private Integer id;
	private String description;
	private Integer abbonamentID;
	private Integer socioID;
}
