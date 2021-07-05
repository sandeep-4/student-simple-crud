package com.java.spring.student.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.java.spring.student.exception.StudentException;
import com.java.spring.student.model.Student;
import com.java.spring.student.repository.StudentRepository;

@Service
public class StudentServiceDetails implements UserDetailsService{
	
	@Autowired
	private StudentRepository studentRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Student student=studentRepository.findByEmail(username);
		if(student==null) {
			throw new StudentException("No any student with this email");
		}
//		System.out.println( (UserDetails) student);

		return (UserDetails) student;
		
	}

}
