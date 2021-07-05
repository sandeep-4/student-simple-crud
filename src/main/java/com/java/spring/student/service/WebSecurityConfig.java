package com.java.spring.student.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.java.spring.student.jwt.JwtAuthenticationEntryPoint;
import com.java.spring.student.jwt.JwtAuthenticationFilter;
import com.java.spring.student.repository.StudentRepository;
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter{
	
	@Bean
	JwtAuthenticationFilter jwtFilter() {
		return new JwtAuthenticationFilter();
	}
	
	@Autowired
	private StudentRepository studentRepository;
	
	@Autowired
	private BCryptPasswordEncoder bcrypt;
	
	@Autowired
	private StudentServiceDetails studentServiceDetails;
	
	@Autowired
	private JwtAuthenticationEntryPoint unauthorizedEntry;

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(studentServiceDetails).passwordEncoder(bcrypt);
	}

	@Override
	@Bean(BeanIds.AUTHENTICATION_MANAGER)
	protected AuthenticationManager authenticationManager() throws Exception {
		return super.authenticationManager();
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		http.csrf().disable()
		.exceptionHandling().authenticationEntryPoint(unauthorizedEntry).and()
		.sessionManagement()
		.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
		.and().headers().frameOptions().sameOrigin()
		.and()
		.authorizeRequests()
		.antMatchers("/api/student/login",
				"/api/student/register",
				"/api/student/password-change")
		.permitAll()
		.anyRequest()
		.authenticated();
		
		http.addFilterBefore(jwtFilter(), UsernamePasswordAuthenticationFilter.class);
	}

	
	
	
}
