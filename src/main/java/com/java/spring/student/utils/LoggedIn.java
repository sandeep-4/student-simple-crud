package com.java.spring.student.utils;

import lombok.Data;

@Data
public class LoggedIn {

	private boolean sucess;
	private String token;
	
	public LoggedIn(boolean sucess, String token) {
		this.sucess = sucess;
		this.token = token;
	}
	
	
}
