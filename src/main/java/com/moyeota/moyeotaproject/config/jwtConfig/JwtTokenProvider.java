package com.moyeota.moyeotaproject.config.jwtConfig;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Collections;
import java.util.Date;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.moyeota.moyeotaproject.config.exception.ApiException;
import com.moyeota.moyeotaproject.config.exception.ErrorCode;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Slf4j
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

	public Authentication getAuthentication(String accessToken) {
		Claims claims = parseClaims(accessToken);
		UserDetails principal = new User(claims.getSubject(), "", Collections.emptyList());
		return new UsernamePasswordAuthenticationToken(principal, "", Collections.emptyList());
	}

	public Long extractSubjectFromJwt(String accessToken) {
		try {
			String token = getToken(accessToken);
			Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();

			String subject = claims.getSubject();
			log.info("AccessToken으로 알아낸 UserId = {}", subject);
			return Long.parseLong(subject);
		} catch (IllegalArgumentException e) {
			throw new ApiException(ErrorCode.WRONG_TYPE_TOKEN);
		} catch (NullPointerException e) {
			throw new ApiException(ErrorCode.UNKNOWN_ERROR);
		}
	}

	public boolean validateToken(String token) {
		log.info("token = {}", token);
		token = getToken(token);
		Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
		return true;
	}

	public String getToken(String token) {
		if(token.contains("BEARER")){
			token = token.substring(7).trim();
		}
		return token.trim();
	}
}