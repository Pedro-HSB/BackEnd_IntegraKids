package com.fatec.security;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fatec.model.User;

public class UserDetailsImpl implements UserDetails{

    private static final long serialVersionUID = 1L;

    private String email;
    private String senha;
    private List<GrantedAuthority> authorities;

    public UserDetailsImpl(User user) {
        this.email = user.getEmail();
        this.senha = user.getSenha();
    }

    public UserDetailsImpl() {	}

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        return authorities;
    }

    @Override
    public String getPassword() {

        return senha;
    }

    @Override
    public String getUsername() {

        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}
