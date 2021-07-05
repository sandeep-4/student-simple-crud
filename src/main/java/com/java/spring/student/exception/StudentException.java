package com.java.spring.student.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class StudentException extends RuntimeException{


	private static final long serialVersionUID = 1L;

	public StudentException(String msg) {
		super(msg);
	
	}

	
	
	
}
