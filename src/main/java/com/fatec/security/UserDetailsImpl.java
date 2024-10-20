package com.fatec.security;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fatec.model.User;

/**
 * Implementação da interface UserDetails do Spring Security,
 * representando os detalhes do usuário autenticado.
 */
public class UserDetailsImpl implements UserDetails {

    private static final long serialVersionUID = 1L;

    // E-mail do usuário.
    private String email;

    // Senha do usuário.
    private String senha;

    // Autoridades (permissões) concedidas ao usuário.
    private List<GrantedAuthority> authorities;

    /**
     * Construtor que inicializa os detalhes do usuário a partir de um objeto User.
     *
     * @param user O objeto User contendo informações do usuário.
     */
    public UserDetailsImpl(User user) {
        this.email = user.getEmail();
        this.senha = user.getSenha();
    }

    // Construtor padrão.
    public UserDetailsImpl() {}

    /**
     * Retorna as autoridades (permissões) do usuário.
     *
     * @return Coleção de autoridades concedidas ao usuário.
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    /**
     * Retorna a senha do usuário.
     *
     * @return A senha do usuário.
     */
    @Override
    public String getPassword() {
        return senha;
    }

    /**
     * Retorna o nome de usuário (neste caso, o e-mail).
     *
     * @return O e-mail do usuário.
     */
    @Override
    public String getUsername() {
        return email;
    }

    /**
     * Indica se a conta do usuário não está expirada.
     *
     * @return true, já que não há lógica de expiração implementada.
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * Indica se a conta do usuário não está bloqueada.
     *
     * @return true, já que não há lógica de bloqueio implementada.
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * Indica se as credenciais do usuário (senha) não estão expiradas.
     *
     * @return true, já que não há lógica de expiração de credenciais implementada.
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * Indica se a conta do usuário está habilitada.
     *
     * @return true, já que não há lógica de habilitação implementada.
     */
    @Override
    public boolean isEnabled() {
        return true;
    }
}
