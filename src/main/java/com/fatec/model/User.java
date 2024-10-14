package com.fatec.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Document("user")
public class User {
    @Id
    private String id;

    @NotBlank(message = "O atributo Nome do Responsável é obrigatório.")
    private String nomeResponsavel;

    @NotBlank(message = "O atributo email é obrigatório.")
    @Email(message = "O atributo email deve ser um email válido.")
    private String email;

    @NotBlank(message = "O atributo senha é obrigatório.")
    @Size(min = 8)
    private String senha;
}
