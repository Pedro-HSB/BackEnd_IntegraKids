package com.fatec.security;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

/**
 * Serviço para manipulação de tokens JWT, incluindo geração, validação e extração de informações dos tokens.
 */
@Component
public class JwtService {

    // Segredo utilizado para assinar e validar tokens JWT, lido das configurações da aplicação.
    @Value("${jwt.secret}")
    private String secret;

    /**
     * Retorna a chave de assinatura utilizada para assinar o token.
     *
     * @return uma instância de Key gerada a partir do segredo.
     */
    private Key getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * Extrai todas as claims (informações) do token JWT.
     *
     * @param token o token JWT.
     * @return as claims presentes no token.
     */
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Extrai uma claim específica do token utilizando uma função resolvente.
     *
     * @param <T> O tipo da claim a ser retornada.
     * @param token o token JWT.
     * @param claimsResolver função que define qual claim extrair.
     * @return a claim extraída.
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Extrai o nome de usuário (subject) do token.
     *
     * @param token o token JWT.
     * @return o nome de usuário presente no token.
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Extrai a data de expiração do token.
     *
     * @param token o token JWT.
     * @return a data de expiração do token.
     */
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * Verifica se o token JWT está expirado.
     *
     * @param token o token JWT.
     * @return true se o token estiver expirado, caso contrário false.
     */
    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * Valida o token, verificando se pertence ao usuário e se não está expirado.
     *
     * @param token o token JWT.
     * @param userDetails os detalhes do usuário a serem validados.
     * @return true se o token for válido, caso contrário false.
     */
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    /**
     * Cria um novo token JWT com as claims especificadas e o nome de usuário.
     *
     * @param claims um mapa contendo as claims adicionais.
     * @param userName o nome de usuário a ser incluído no token.
     * @return o token JWT gerado.
     */
    private String createToken(Map<String, Object> claims, String userName) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userName)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)) // Expira em 1 hora
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Gera um token JWT para o nome de usuário fornecido.
     *
     * @param userName o nome de usuário.
     * @return o token JWT gerado.
     */
    public String generateToken(String userName) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, userName);
    }
}
