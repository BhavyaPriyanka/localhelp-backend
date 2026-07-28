package com.localhelp.backend.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
public class JwtService {

    private final SecretKey key = Keys.hmacShaKeyFor(
            "localhelp-localhelp-secret-key-localhelp-secret-key"
            .getBytes()
    );

    private final long expirationTime = 1000 * 60 * 60; // 1 hour


    public String generateToken(String username) {

        String token = Jwts.builder()
                .subject(username)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(key)
                .compact();

        System.out.println("GENERATED TOKEN: " + token);

        return token;
    }


    public String extractUsername(String token) {

        System.out.println("VALIDATING TOKEN: " + token);

        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }
}