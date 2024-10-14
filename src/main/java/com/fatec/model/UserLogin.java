package com.fatec.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserLogin {
    private String id;
    private String NomeResponsavel;
    private String email;
    private String senha;
    private String token;

}
