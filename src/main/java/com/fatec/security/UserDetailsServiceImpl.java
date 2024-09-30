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

@Service
public class UserDetailsServiceImpl implements UserDetailsService {


    @Autowired
    private UserRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        Optional<User> usuario = usuarioRepository.findByEmail(email);

        if (usuario.isPresent())
            return new UserDetailsImpl(usuario.get());
        else
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);

    }
}