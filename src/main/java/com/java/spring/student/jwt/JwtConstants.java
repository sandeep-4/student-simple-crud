package com.java.spring.student.jwt;

public class JwtConstants {
	public static final String SIGN_UP_URLS="/api/student/register";
	public static final String H2_CONSOLE="h2-console/**";
	public static final String TOKEN_PREFIX="Bearer";
	public static final String SECRET="secretKey";
	public static final String HEADER_STRING="Authorization";
	public static final long EXPIRATION_TIME= 300000000;
}
