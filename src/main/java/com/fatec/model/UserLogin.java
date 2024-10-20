package com.fatec.model;

import lombok.Builder;
import lombok.Data;

/**
 * Classe que representa os dados de login de um usuário.
 * Utilizada para autenticação e troca de informações de login.
 */
@Data
@Builder
public class UserLogin {

    // Identificador único do usuário.
    private String id;

    // Nome do responsável pelo usuário.
    private String NomeResponsavel;

    // Email do usuário.
    private String email;

    // Senha do usuário.
    private String senha;

    // Token de autenticação gerado após o login.
    // Usado para autenticar futuras requisições.
    private String token;
}
