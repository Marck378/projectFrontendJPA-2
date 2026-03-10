package com.betacom.jpaFrontend.dto.outputs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

public class UtenteDTO {
	@Getter
	@Setter
	@NoArgsConstructor
	@AllArgsConstructor
	public class UtenteREQ {
			private String userName;
				private String pwd;
				private String email;
				private String role;
			}
}
