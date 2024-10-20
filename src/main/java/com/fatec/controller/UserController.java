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

/**
 * Controlador responsável por gerenciar as requisições relacionadas a usuários.
 * Mapeado na rota "/usuarios".
 */
@RequestMapping("/usuarios")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@AllArgsConstructor
public class UserController {

    // Injeta a dependência do serviço de usuário.
    @Autowired
    private UserService usuarioService;

    // Injeta a dependência do repositório de usuários.
    @Autowired
    private UserRepository usuarioRepository;

    /**
     * Retorna uma lista de todos os usuários.
     * Rota: GET /usuarios/all
     *
     * @return Lista de todos os usuários com status 200 (OK).
     */
    @GetMapping("/all")
    public ResponseEntity<List<User>> getAll() {
        return ResponseEntity.ok(usuarioRepository.findAll());
    }

    /**
     * Busca um usuário pelo seu ID.
     * Rota: GET /usuarios/{id}
     *
     * @param id ID do usuário a ser buscado.
     * @return Usuário encontrado com status 200 (OK) ou 404 (Not Found) se não encontrado.
     */
    @GetMapping("/{id}")
    public ResponseEntity<User> getById(@PathVariable Long id) {
        return usuarioRepository.findById(String.valueOf(id))
                .map(resposta -> ResponseEntity.ok(resposta))
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Atualiza as informações de um usuário.
     * Rota: PUT /usuarios/atualizar
     *
     * @param usuario Objeto User contendo os dados atualizados.
     * @return Usuário atualizado com status 200 (OK) ou 404 (Not Found) se o usuário não existir.
     */
    @PutMapping("/atualizar")
    public ResponseEntity<User> putUsuario(@Valid @RequestBody User usuario) {
        return usuarioService.atualizarUser(usuario)
                .map(resposta -> ResponseEntity.status(HttpStatus.OK).body(resposta))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    /**
     * Cadastra um novo usuário.
     * Rota: POST /usuarios/cadastrar
     *
     * @param usuario Objeto User contendo os dados do novo usuário.
     * @return Usuário criado com status 201 (Created) ou 400 (Bad Request) se os dados forem inválidos.
     */
    @PostMapping("/cadastrar")
    public ResponseEntity<User> postUsuario(@Valid @RequestBody User usuario) {
        return usuarioService.cadastrarUser(usuario)
                .map(resposta -> ResponseEntity.status(HttpStatus.CREATED).body(resposta))
                .orElse(ResponseEntity.status(HttpStatus.BAD_REQUEST).build());
    }

    /**
     * Autentica um usuário.
     * Rota: POST /usuarios/logar
     *
     * @param usuarioLogin Objeto Optional<UserLogin> contendo as credenciais do usuário.
     * @return Dados de autenticação do usuário com status 200 (OK) ou 401 (Unauthorized) se as credenciais forem inválidas.
     */
    @PostMapping("/logar")
    public ResponseEntity<UserLogin> autenticarUsuario(@Valid @RequestBody Optional<UserLogin> usuarioLogin) {
        return usuarioService.autenticarUser(usuarioLogin)
                .map(resposta -> ResponseEntity.status(HttpStatus.OK).body(resposta))
                .orElse(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
    }
}
