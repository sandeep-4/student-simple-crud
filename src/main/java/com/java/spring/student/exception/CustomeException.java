package com.java.spring.student.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@RestController
public class CustomeException extends ResponseEntityExceptionHandler{

	@ExceptionHandler
	public ResponseEntity<?> handleStudentException(StudentException e,WebRequest request){
		StudentException studentException=new StudentException(e.getMessage());
		return new ResponseEntity<>(studentException,HttpStatus.BAD_REQUEST);
	}
}
