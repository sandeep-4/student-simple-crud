package com.java.spring.student.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.java.spring.student.model.Student;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long>{

	public Student findByEmail(String email);
	
	public Student findByUsername(String username);
	
	public List<Student> findByStream(String stream);
	
	public List<Student> findByHouse(String house);
	
	@Query("SELECT s from Student s WHERE s.name LIKE %?1% "
			+ "OR s.username LIKE %?1%")
	public List<Student> searchStudentByFilter(String filter);
	
}
