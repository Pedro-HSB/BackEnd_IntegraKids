package com.fatec.entity;

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

    @Indexed
    private String username;

    private String password;

    private boolean active;
}
