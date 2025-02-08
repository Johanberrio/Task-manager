package com.example.taskmanager.services;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.Base64;
import java.security.Key;

@Service
public class JwtService {

    @Value("${app.jwt.secret}")  // Obtiene la clave del properties
    private String secretKey;

    public String generateToken(String username) {
        Key key = Keys.hmacShaKeyFor(Base64.getDecoder().decode(secretKey));

        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 3600000)) // Expira en 1 hora
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Key key = Keys.hmacShaKeyFor(Base64.getDecoder().decode(secretKey));

            Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token);

            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
