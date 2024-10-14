package com.fatec.controller;


import com.fatec.model.User;
import com.fatec.model.UserLogin;
import com.fatec.repository.UserRepository;
import com.fatec.service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RequestMapping("/usuarios")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@AllArgsConstructor
public class UserController {
    @Autowired
    private UserService usuarioService;

    @Autowired
    private UserRepository usuarioRepository;

    @GetMapping("/all")
    public ResponseEntity <List<User>> getAll(){

        return ResponseEntity.ok(usuarioRepository.findAll());

    }
    //change because the model dont have the ID
    @GetMapping("/{id}")
    public ResponseEntity<User> getById(@PathVariable Long id) {
        return usuarioRepository.findById(String.valueOf(id))
                .map(resposta -> ResponseEntity.ok(resposta))
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/atualizar")
    public ResponseEntity<User> putUsuario(@Valid @RequestBody User usuario){
        return usuarioService.atualizarUser(usuario)
                .map(resposta -> ResponseEntity.status(HttpStatus.OK).body(resposta))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PostMapping("/cadastrar")
    public ResponseEntity<User> postUsuario(@Valid @RequestBody User usuario){
        return usuarioService.cadastrarUser(usuario)
                .map(resposta -> ResponseEntity.status(HttpStatus.CREATED).body(resposta))
                .orElse(ResponseEntity.status(HttpStatus.BAD_REQUEST).build());
    }

    @PostMapping("/logar")
    public ResponseEntity<UserLogin> autenticarUsuario(@Valid @RequestBody Optional<UserLogin> usuarioLogin){
        return usuarioService.autenticarUser(usuarioLogin)
                .map(resposta -> ResponseEntity.status(HttpStatus.OK).body(resposta))
                .orElse(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
    }
}
