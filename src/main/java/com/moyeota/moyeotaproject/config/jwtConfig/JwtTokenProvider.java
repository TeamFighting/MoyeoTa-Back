package com.moyeota.moyeotaproject.config.jwtConfig;


import com.moyeota.moyeotaproject.controller.dto.TokenInfoDto;
import com.moyeota.moyeotaproject.domain.users.Entity.Users;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Collections;
import java.util.Date;

@Component
public class JwtTokenProvider {

    private final Key key;

    @Value("${jwt.secret}")
    String salt;

    public JwtTokenProvider(@Value("${jwt.secret}") String salt) {
        this.key = Keys.hmacShaKeyFor(salt.getBytes(StandardCharsets.UTF_8));
    }

    public String generateToken(String subject, Date expiredAt) {
        return Jwts.builder()
                .setSubject(subject)
                .setExpiration(expiredAt)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    private Claims parseClaims(String accessToken) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(accessToken)
                    .getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

    // JWT 토큰을 복호화하여 토큰에 들어있는 정보를 꺼내는 메서드
    public Authentication getAuthentication(String accessToken) {
        // 토큰 복호화
        System.out.println("accessToken = " + accessToken);
        Claims claims = parseClaims(accessToken);

        // UserDetails 객체를 만들어서 Authentication 리턴
        UserDetails principal = new User(claims.getSubject(), "", Collections.emptyList());
        return new UsernamePasswordAuthenticationToken(principal, "", Collections.emptyList());
    }

    public Long extractSubjectFromJwt(String accessToken) {
        try {
            String token = getToken(accessToken);
            Claims claims = Jwts.parser()
                    .setSigningKey(salt)
                    .parseClaimsJws(token)
                    .getBody();
            String subject = claims.getSubject();
            System.out.println("subject = " + subject);
            return Long.parseLong(subject);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // 토큰 정보를 검증하는 메서드
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (RuntimeException e) {
            System.out.println("e = " + e);
            throw new RuntimeException(e);
        }
    }

    // Bearer 제외부분
    public String getToken(String token) {
        if (token.length() < 7) {
            throw new RuntimeException("토큰에 오류가 있습니다.");
        }
        token = token.substring(7).trim();
        return token;
    }

}
