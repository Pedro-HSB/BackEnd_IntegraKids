package com.fatec.repository;

import com.fatec.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthUserRepository extends MongoRepository<User, String> {
    Optional<User> findByUsername(String username);
}