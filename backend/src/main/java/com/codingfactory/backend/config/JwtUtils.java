package com.codingfactory.backend.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtils {

    @Value("${jwt.secret:Y29kaW5nZmFjdG9yeV9qd3Rfc2VjcmV0X2tleV8yMDI2X3N1cGVyX3NlY3VyZQ==}")
    private String jwtSecret;

    @Value("${jwt.expiration-ms:86400000}") // 24 hours
    private long jwtExpirationMs;

    public String generateToken(String email, String role, String prenom, String nom) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + jwtExpirationMs);

        Map<String, Object> claims = new HashMap<>();
        claims.put("role", role);
        claims.put("prenom", prenom);
        claims.put("nom", nom);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(email)
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(SignatureAlgorithm.HS256, jwtSecret)
                .compact();
    }

    public Claims parseClaims(String token) {
        return Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody();
    }

    public boolean validateToken(String token) {
        try {
            parseClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
