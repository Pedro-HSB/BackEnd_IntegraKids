package com.fatec.service;

import java.util.Optional;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.fatec.model.User;
import com.fatec.model.UserLogin;
import com.fatec.repository.UserRepository;
import com.fatec.security.JwtService;

/**
 * Serviço responsável pela lógica de negócio relacionada aos usuários.
 */
@Service
public class UserService {

    // Repositório para operações de acesso a dados de usuários.
    @Autowired
    private UserRepository usuarioRepository;

    // Serviço para geração e validação de tokens JWT.
    @Autowired
    private JwtService jwtService;

    // Gerenciador de autenticação para autenticar usuários.
    @Autowired
    private AuthenticationManager authenticationManager;

    /**
     * Cadastra um novo usuário no sistema.
     *
     * @param usuario O objeto User a ser cadastrado.
     * @return Um Optional contendo o usuário cadastrado ou vazio se o e-mail já existir.
     */
    public Optional<User> cadastrarUser(@NotNull User usuario) {
        // Verifica se o e-mail já está cadastrado.
        if (usuarioRepository.findByEmail(usuario.getEmail()).isPresent())
            return Optional.empty();

        // Criptografa a senha antes de salvar.
        usuario.setSenha(criptografarSenha(usuario.getSenha()));

        // Salva o usuário no repositório e retorna o usuário salvo.
        return Optional.of(usuarioRepository.save(usuario));
    }

    /**
     * Atualiza as informações de um usuário existente.
     *
     * @param usuario O objeto User com as informações a serem atualizadas.
     * @return Um Optional contendo o usuário atualizado ou vazio se o usuário não existir.
     */
    public Optional<User> atualizarUser(@NotNull User usuario) {
        // Verifica se o usuário existe no repositório.
        if (usuarioRepository.findById(usuario.getId()).isPresent()) {
            // Verifica se já existe outro usuário com o mesmo nome.
            Optional<User> buscaUsuario = usuarioRepository.findByEmail(usuario.getNomeResponsavel());

            if (buscaUsuario.isPresent() && buscaUsuario.get().getId() != usuario.getId())
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Usuário já existe!", null);

            // Criptografa a nova senha antes de salvar.
            usuario.setSenha(criptografarSenha(usuario.getSenha()));

            // Salva o usuário atualizado e retorna.
            return Optional.ofNullable(usuarioRepository.save(usuario));
        }

        return Optional.empty();
    }

    /**
     * Autentica um usuário com base nas credenciais fornecidas.
     *
     * @param usuarioLogin Um Optional contendo as credenciais do usuário.
     * @return Um Optional contendo as informações do usuário autenticado ou vazio se a autenticação falhar.
     */
    public Optional<UserLogin> autenticarUser(@NotNull Optional<UserLogin> usuarioLogin) {
        // Gera o objeto de autenticação com as credenciais fornecidas.
        var credenciais = new UsernamePasswordAuthenticationToken(usuarioLogin.get().getEmail(), usuarioLogin.get().getSenha());

        // Autentica o usuário.
        Authentication authentication = authenticationManager.authenticate(credenciais);

        // Se a autenticação foi bem-sucedida.
        if (authentication.isAuthenticated()) {
            // Busca os dados do usuário pelo e-mail.
            Optional<User> usuario = usuarioRepository.findByEmail(usuarioLogin.get().getEmail());

            // Se o usuário foi encontrado.
            if (usuario.isPresent()) {
                // Preenche o objeto usuarioLogin com os dados encontrados.
                usuarioLogin.get().setId(usuario.get().getId());
                usuarioLogin.get().setNomeResponsavel(usuario.get().getNomeResponsavel());
                usuarioLogin.get().setToken(gerarToken(usuarioLogin.get().getEmail()));
                usuarioLogin.get().setSenha(""); // Limpa a senha.

                // Retorna o objeto usuarioLogin preenchido.
                return usuarioLogin;
            }
        }

        return Optional.empty(); // Retorna vazio se a autenticação falhar.
    }

    /**
     * Criptografa a senha usando BCrypt.
     *
     * @param senha A senha a ser criptografada.
     * @return A senha criptografada.
     */
    private String criptografarSenha(String senha) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder.encode(senha);
    }

    /**
     * Gera um token JWT para o usuário autenticado.
     *
     * @param usuario O e-mail do usuário.
     * @return O token JWT gerado.
     */
    private String gerarToken(String usuario) {
        return "Bearer " + jwtService.generateToken(usuario);
    }
}
