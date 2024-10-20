package com.fatec.security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.server.ResponseStatusException;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Filtro de autenticação JWT que intercepta cada requisição e valida o token JWT.
 * Se o token for válido, autentica o usuário no contexto de segurança do Spring.
 */
@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    // Serviço para manipular e validar tokens JWT.
    @Autowired
    private JwtService jwtService;

    // Serviço para carregar os detalhes do usuário a partir do nome de usuário.
    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    /**
     * Método que intercepta cada requisição HTTP para verificar a presença e validade
     * de um token JWT no cabeçalho "Authorization".
     *
     * @param request  O objeto HttpServletRequest da requisição.
     * @param response O objeto HttpServletResponse da resposta.
     * @param filterChain A cadeia de filtros que deve ser executada.
     * @throws ServletException Em caso de erro na execução do filtro.
     * @throws IOException Em caso de erro de entrada/saída.
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // Extrai o cabeçalho "Authorization" da requisição.
        String authHeader = request.getHeader("Authorization");
        String token = null;
        String username = null;

        try {
            // Verifica se o cabeçalho contém um token JWT e extrai o nome de usuário do token.
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                token = authHeader.substring(7); // Remove "Bearer " do início do token.
                username = jwtService.extractUsername(token);
            }

            // Verifica se o nome de usuário foi extraído e se ainda não há uma autenticação no contexto.
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                // Carrega os detalhes do usuário a partir do nome de usuário.
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                // Valida o token usando o serviço JWT.
                if (jwtService.validateToken(token, userDetails)) {
                    // Cria um objeto de autenticação e define no contexto de segurança.
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }

            // Continua com o próximo filtro na cadeia.
            filterChain.doFilter(request, response);

        } catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException | SignatureException
                 | ResponseStatusException e) {
            // Define o status HTTP como 403 (FORBIDDEN) se ocorrer qualquer exceção de token.
            response.setStatus(HttpStatus.FORBIDDEN.value());
            return;
        }
    }
}
