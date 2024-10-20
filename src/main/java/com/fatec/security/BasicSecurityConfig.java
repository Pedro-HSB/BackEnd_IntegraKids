package com.fatec.security;

import static org.springframework.security.config.Customizer.withDefaults;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Classe de configuração de segurança para a aplicação.
 * Define como a autenticação e a autorização são gerenciadas.
 */
@Configuration
@EnableWebSecurity
public class BasicSecurityConfig {

    // Filtro de autenticação JWT para verificar os tokens nas requisições.
    @Autowired
    private JwtAuthFilter authFilter;

    /**
     * Configura o serviço de busca de detalhes do usuário.
     *
     * @return uma instância de UserDetailsService.
     */
    @Bean
    UserDetailsService userDetailsService() {
        return new UserDetailsServiceImpl();
    }

    /**
     * Define o encoder de senha utilizado para encriptar e comparar senhas.
     * Utiliza o BCrypt.
     *
     * @return uma instância de PasswordEncoder.
     */
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Configura o provedor de autenticação usando o serviço de detalhes do usuário
     * e o encoder de senha.
     *
     * @return uma instância de AuthenticationProvider.
     */
    @Bean
    AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService());
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

    /**
     * Configura o gerenciador de autenticação.
     *
     * @param authenticationConfiguration configuração da autenticação.
     * @return uma instância de AuthenticationManager.
     * @throws Exception em caso de erro na configuração.
     */
    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
            throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    /**
     * Configura a cadeia de filtros de segurança, incluindo políticas de sessão,
     * permissões de acesso e integração do filtro de autenticação JWT.
     *
     * @param http objeto HttpSecurity para configurar as regras de segurança.
     * @return uma instância de SecurityFilterChain.
     * @throws Exception em caso de erro na configuração.
     */
    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                // Define que a política de sessão é sem estado (stateless) para uso com JWT.
                .sessionManagement(management -> management
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // Desabilita CSRF pois tokens JWT já oferecem proteção.
                .csrf(csrf -> csrf.disable())
                // Ativa CORS (Cross-Origin Resource Sharing) com as configurações padrão.
                .cors(withDefaults());

        http
                // Define as regras de autorização para as rotas da aplicação.
                .authorizeHttpRequests((auth) -> auth
                        // Permite acesso público às rotas de login, cadastro, e algumas outras.
                        .requestMatchers("/usuarios/logar").permitAll()
                        .requestMatchers("/usuarios/cadastrar").permitAll()
                        .requestMatchers("/error/**").permitAll()
                        .requestMatchers(HttpMethod.OPTIONS).permitAll()
                        // Qualquer outra requisição precisa estar autenticada.
                        .anyRequest().authenticated())
                // Configura o provedor de autenticação.
                .authenticationProvider(authenticationProvider())
                // Adiciona o filtro de autenticação JWT antes do filtro padrão de autenticação por username e senha.
                .addFilterBefore(authFilter, UsernamePasswordAuthenticationFilter.class)
                // Configura a autenticação HTTP básica.
                .httpBasic(withDefaults());

        return http.build();
    }
}
