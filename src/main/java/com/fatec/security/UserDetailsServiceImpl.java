package com.fatec.security;

import java.util.Optional;

import com.fatec.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.fatec.model.User;

/**
 * Implementação do serviço UserDetailsService, responsável por carregar
 * os detalhes do usuário com base no e-mail fornecido.
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    // Repositório para acesso aos dados dos usuários.
    @Autowired
    private UserRepository usuarioRepository;

    /**
     * Carrega os detalhes do usuário pelo nome de usuário (e-mail).
     *
     * @param email O e-mail do usuário a ser carregado.
     * @return Uma instância de UserDetails representando o usuário.
     * @throws UsernameNotFoundException Se o usuário não for encontrado.
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        // Tenta encontrar o usuário pelo e-mail no repositório.
        Optional<User> usuario = usuarioRepository.findByEmail(email);

        // Se o usuário for encontrado, retorna seus detalhes.
        if (usuario.isPresent())
            return new UserDetailsImpl(usuario.get());
        else
            // Se não encontrado, lança uma exceção de status FORBIDDEN.
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
    }
}
