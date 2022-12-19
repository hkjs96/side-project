package com.sideproject.security;

import com.sideproject.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Slf4j
@Service
public class TokenProvider {

    private static final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS512);

    public String create(User user) {
        Date expiryDate = Date.from(
                Instant.now().plus(1, ChronoUnit.DAYS));

        // TODO: Issuer 어떻게 할지 정의하기, payload 에 어떤 내용 담을지 정하기
        return Jwts.builder()
                .signWith(key, SignatureAlgorithm.HS512)
                .setSubject(user.getId())
                .setIssuer("Issuer 변경하기!")
                .setIssuedAt(new Date())
                .setExpiration(expiryDate)
                .compact();
    }

    public String validateAndGetUserId(String token) {
        Claims claims= Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }
}
