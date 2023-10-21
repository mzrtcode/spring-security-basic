package com.courses.springsecuritycourse.service;

import com.courses.springsecuritycourse.models.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Map;

@Service
public class JwtService {

@Value("${security.jwt.secret-key}")
    private String SECRET_KEY;

    @Value("${security.jwt.expiration-minutes}")
    private Long EXPIRATION_MINUTES;

    public String generateToken(User user, Map<String, Object> extraClaims) {

        Date issuedAt = new Date(System.currentTimeMillis());
        Date expiration = new Date(issuedAt.getTime() + (EXPIRATION_MINUTES * 60 * 1000));

        return Jwts.builder()
                .claims(extraClaims)
                .subject(user.getUsername())
                .issuedAt(issuedAt)
                .expiration(expiration)
                .header().add("typ", "JWT").and()
                .signWith(generateKey(), SignatureAlgorithm.HS256)
                .compact();

    }

    private SecretKey generateKey() {

        byte[] secretAsBytes = Decoders.BASE64.decode(SECRET_KEY);
        System.out.println("Mi clave es: " + new String(secretAsBytes));
        return Keys.hmacShaKeyFor(secretAsBytes);
    }

    public String extractUsername(String jwt) {
        return getPayload(jwt).getSubject();
    }

    private Claims getPayload(String jwt) {
        return Jwts.parser().verifyWith(generateKey()).build()
                .parseSignedClaims(jwt).getPayload();
    }
}
