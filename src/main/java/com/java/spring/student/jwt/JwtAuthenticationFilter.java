package com.java.spring.student.jwt;

import java.io.IOException;
import java.util.Collections;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.java.spring.student.model.Student;
import com.java.spring.student.service.StudentServiceDetails;

public class JwtAuthenticationFilter extends OncePerRequestFilter{

	@Autowired
	private JwtTokenProvider jwtTokenProvider;
	
	@Autowired
	private StudentServiceDetails studentDetails;
	
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		try {
			String jwt=getJwtFromRequest(request);
			
			if(StringUtils.hasText(jwt) && jwtTokenProvider.validateToken(jwt)) {
				String email=jwtTokenProvider.getEmailFromToken(jwt);
				Student student=(Student) studentDetails.loadUserByUsername(email);
				
				 UsernamePasswordAuthenticationToken authentication=new UsernamePasswordAuthenticationToken
						(student,null, Collections.emptyList());
				 
				 authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				 
				 SecurityContextHolder.getContext().setAuthentication(authentication);
				
				
			}
		} catch (Exception e) {
			logger.error("Unable to secure student ---> "+e);
		}
		
		filterChain.doFilter(request, response);
	}
	
	public String getJwtFromRequest(HttpServletRequest request) {
		String bearerToken=request.getHeader(JwtConstants.HEADER_STRING);
		if(StringUtils.hasText(bearerToken) && bearerToken.startsWith(JwtConstants.TOKEN_PREFIX)) {
			return bearerToken.substring(7,bearerToken.length());
		}
		return null;
	}

}
