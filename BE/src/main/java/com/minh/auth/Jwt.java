package com.minh.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import javax.crypto.SecretKey;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;

public final  class Jwt {
    private static SecretKey key;

    public static void init(byte[] secret) {
        key =  Keys.hmacShaKeyFor(secret);
    }

    public static String issue(String userId, long ttlSeconds) {
        long now = System.currentTimeMillis();

        return Jwts.builder()
                .setSubject(userId)
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(now + ttlSeconds * 1000))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public static String verifyAndGetUserId(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            return claims.getSubject();
        } catch (JwtException e) {
            return null;
        }
    }

    public static Long getUserId(HttpServletRequest request) {
        String auth = request.getHeader("Authorization");

        if (auth == null || !auth.startsWith("Bearer ")) {
            throw new ResponseStatusException(HttpStatus.NON_AUTHORITATIVE_INFORMATION, "Authorization header is invalid");
        }

        String token = auth.substring(7);
        String userId = verifyAndGetUserId(token);

        if (userId == null) {
            throw new ResponseStatusException(HttpStatus.NON_AUTHORITATIVE_INFORMATION, "Authorization header is invalid");
        }
        return Long.valueOf(userId);
    }
}
