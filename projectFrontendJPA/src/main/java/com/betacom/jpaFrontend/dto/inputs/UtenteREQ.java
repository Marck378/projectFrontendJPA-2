package com.betacom.jpaFrontend.dto.inputs;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UtenteREQ {
    private String userName;
    private String pwd;
    private String email;
    private String role;
}