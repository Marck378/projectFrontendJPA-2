package com.betacom.jpaFrontend.dto.inputs;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CertificatoReq {
	private Integer id;
	private Boolean tipo; // false normale true agosnistico
	private LocalDate dataCertificato;
	private Integer socioID;
	private String dataCertificatoString;
}
