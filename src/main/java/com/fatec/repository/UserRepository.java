package com.fatec.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.fatec.model.User;

/**
 * Repositório responsável por gerenciar as operações de persistência dos usuários.
 * Estende MongoRepository para fornecer métodos CRUD e operações customizadas.
 */
@Repository
public interface UserRepository extends MongoRepository<User, String> {

    /**
     * Busca um usuário pelo seu email.
     *
     * @param email O email do usuário a ser buscado.
     * @return Um Optional contendo o usuário, caso encontrado.
     */
    Optional<User> findByEmail(String email);
}
