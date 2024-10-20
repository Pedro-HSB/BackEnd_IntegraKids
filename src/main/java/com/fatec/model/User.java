package com.fatec.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Builder;
import lombok.Data;

/**
 * Classe que representa o modelo de dados do Usuário no sistema.
 * Anotada como um documento do MongoDB com a coleção "user".
 */
@Data
@Builder
@Document("user")
public class User {

    // Identificador único do usuário, gerado automaticamente pelo MongoDB.
    @Id
    private String id;

    // Nome do responsável pelo usuário.
    // Campo obrigatório.
    @NotBlank(message = "O atributo Nome do Responsável é obrigatório.")
    private String nomeResponsavel;

    // Email do usuário.
    // Campo obrigatório e deve ser um email válido.
    @NotBlank(message = "O atributo email é obrigatório.")
    @Email(message = "O atributo email deve ser um email válido.")
    private String email;

    // Senha do usuário.
    // Campo obrigatório e deve ter no mínimo 8 caracteres.
    @NotBlank(message = "O atributo senha é obrigatório.")
    @Size(min = 8, message = "A senha deve ter no mínimo 8 caracteres.")
    private String senha;
}
