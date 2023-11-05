package com.wanted.yamyam.global.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.ArrayList;
import java.util.Date;

@Component
public class JwtTokenProvider implements InitializingBean {

    private final String accessSecret;
    private final String refreshSecret;
    private final long accessTokenValidity;
    private final long refreshTokenValidity;
    private Key accessSecretKey;
    private Key refreshSecretKey;

    public JwtTokenProvider(
            @Value("${jwt.access-secret}") String accessSecret,
            @Value("${jwt.refresh-secret}") String refreshSecret,
            @Value("${jwt.access-token-validity-in-seconds}") long accessTokenValidityInSeconds,
            @Value("${jwt.refresh-token-validity-in-seconds}") long refreshTokenValidityInSeconds) {
        this.accessSecret = accessSecret;
        this.refreshSecret = refreshSecret;
        this.accessTokenValidity = accessTokenValidityInSeconds * 1000;
        this.refreshTokenValidity = refreshTokenValidityInSeconds * 1000;
    }

    @Override
    public void afterPropertiesSet() {
        byte[] keyBytes = Decoders.BASE64.decode(accessSecret);
        this.accessSecretKey = Keys.hmacShaKeyFor(keyBytes);
        keyBytes = Decoders.BASE64.decode(refreshSecret);
        this.refreshSecretKey = Keys.hmacShaKeyFor(keyBytes);
    }

    public String createAccessToken(Long id) {
        return buildJwtToken(id, accessSecretKey, accessTokenValidity);
    }

    public String createRefreshToken(Long id) {
        return buildJwtToken(id, refreshSecretKey, refreshTokenValidity);
    }

    private String buildJwtToken(Long id, Key secretKey, long validityPeriod) {
        long now = (new Date()).getTime();
        Date validity = new Date(now + validityPeriod);

        return Jwts.builder()
                .setSubject(String.valueOf(id))
                .signWith(secretKey, SignatureAlgorithm.HS512)
                .setExpiration(validity)
                .compact();
    }

    public Authentication getAuthentication(String token) {
        Claims claims = Jwts
                .parserBuilder()
                .setSigningKey(accessSecretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();

        User principal = new User(claims.getSubject(), "N/A", new ArrayList<>());

        return new UsernamePasswordAuthenticationToken(principal, token, new ArrayList<>());
    }

    public void validateToken(String token) {
        Jwts.parserBuilder().setSigningKey(accessSecretKey).build().parseClaimsJws(token);
    }

    public Claims parseToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(refreshSecretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}