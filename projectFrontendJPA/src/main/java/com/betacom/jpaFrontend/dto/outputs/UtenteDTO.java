package com.betacom.jpaFrontend.dto.outputs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UtenteDTO {

    private String userName;
    private String pwd;
    private String email;
    private String role; 

}