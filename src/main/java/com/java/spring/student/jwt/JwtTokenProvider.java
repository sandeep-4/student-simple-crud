package com.java.spring.student.jwt;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.java.spring.student.model.Student;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

@Component
public class JwtTokenProvider {

	public String generateToken(Authentication authentication) {
		Student student = (Student) authentication.getPrincipal();
		Date now = new Date(System.currentTimeMillis());
		Date expirary = new Date(System.currentTimeMillis() + JwtConstants.EXPIRATION_TIME);

//		String stdId=Long.toString(student.getId());
//		String username = student.getUsername();
		String email=student.getEmail();
		Map<String, Object> claims = new HashMap<>();
		claims.put("id", Long.toString(student.getId()));
		claims.put("name", student.getName());
		claims.put("email", student.getEmail());

//		return Jwts.builder().setSubject(username).setClaims(claims).setIssuedAt(now).setExpiration(expirary)
//				.signWith(SignatureAlgorithm.HS512, JwtConstants.SECRET).compact();
		
		return Jwts.builder().setSubject(email).setClaims(claims).setIssuedAt(now).setExpiration(expirary)
				.signWith(SignatureAlgorithm.HS512, JwtConstants.SECRET).compact();
	}

	public Boolean validateToken(String token) {
		try {
			Jwts.parser().setSigningKey(JwtConstants.SECRET).parseClaimsJws(token);
			return true;
		} catch (SignatureException e) {
			System.out.println("Signature exception");
		} catch (MalformedJwtException e) {
			System.out.println("Jwt token exception" + e);
		} catch (ExpiredJwtException e) {
			System.out.println("expired token");
		} catch (UnsupportedJwtException e) {
			System.out.println("token not supported");
		} catch (IllegalArgumentException ex) {
			System.out.println("illegal args");
		}
		return false;
	}
	
	public String getEmailFromToken(String token) {
		Claims claims=Jwts.parser().setSigningKey(JwtConstants.SECRET).parseClaimsJws(token).getBody();
		String email=(String) claims.get("email");
		System.out.println(email);
		return email;
	}
}
