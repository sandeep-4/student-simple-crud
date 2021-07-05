package com.java.spring.student.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

import org.apache.tomcat.util.http.fileupload.FileUpload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.java.spring.student.exception.StudentException;
import com.java.spring.student.file.FileUploadUtil;
import com.java.spring.student.model.Student;
import com.java.spring.student.repository.StudentRepository;
import com.java.spring.student.utils.PasswordChange;

@Service
public class StudentService {

	@Autowired
	private StudentRepository studentRepoitory;

	@Autowired
	private BCryptPasswordEncoder bcrypt;

	public Student saveUser(Student student) {
		student.setUsername(student.getName().substring(0, 2) + UUID.randomUUID().toString().substring(0, 5));
		if(student.getPassword().length() >8) {
			throw new StudentException("Length must be atleast 8 chars");
		}
		student.setPassword(bcrypt.encode(student.getPassword()));

		Student dublicatecheck = studentRepoitory.findByEmail(student.getEmail());
		if (dublicatecheck != null) {
			throw new StudentException("Email already exists");
		}

		Student saveStudent = studentRepoitory.save(student);
		if (saveStudent == null) {
			throw new StudentException("Student sent empty");
		}
		return saveStudent;
	}

	public List<Student> listOfAllStudent() {
		List<Student> students = studentRepoitory.findAll();
		if (students.isEmpty()) {
			throw new StudentException("Student list empty");
		}
		return students;
	}

	public Student findByUsername(String username) {
		Student student = studentRepoitory.findByUsername(username);
		if (student == null) {
			throw new StudentException("Student not found");
		}
		return student;
	}

	public List<Student> searchUsingFilter(String filter) {
		List<Student> students = studentRepoitory.searchStudentByFilter(filter);
		if (students.isEmpty()) {
			throw new StudentException("Student filter empty");
		}
		return students;
	}

	public List<Student> findByStream(String stream) {
		List<Student> students = studentRepoitory.findByStream(stream);
		if (students.isEmpty()) {
			throw new StudentException("Student stream list empty");
		}
		return students;
	}

	public List<Student> findByHouse(String house) {
		List<Student> students = studentRepoitory.findByHouse(house);
		if (students.isEmpty()) {
			throw new StudentException("Student house list empty");
		}
		return students;
	}

	public Student findByEmail(String email) {
		Student student = studentRepoitory.findByEmail(email);
		if (student == null) {
			throw new StudentException("User with email not found");
		}
		return student;
	}

	public ResponseEntity<?> updateStudent(String username, Student student) {
		Student update = studentRepoitory.findByUsername(username);
		if (update == null) {
			throw new StudentException("User with username not found");
		}
		
		update.setUsername(username);
		update.setPassword(update.getPassword());
		
		Student dublicatecheck = studentRepoitory.findByEmail(student.getEmail());
		if (dublicatecheck != null) {
			throw new StudentException("Email already exists");
		}
		update.setEmail(student.getEmail());
		update.setAddress(student.getAddress());
		update.setContact(student.getContact());
		update.setHouse(student.getHouse());
		update.setName(student.getName());
		update.setStandred(student.getStandred());
		update.setStream(student.getStream());

		studentRepoitory.save(update);
		return new ResponseEntity<>(update, HttpStatus.OK);
	}

	public void deleteStudent(String username) {
		Student std = studentRepoitory.findByUsername(username);
		if (std == null) {
			throw new StudentException("User with username not found");
		}
		studentRepoitory.delete(std);
	}

	// send email with token
	
	public Student passwordChange(PasswordChange change) {
		Student std = studentRepoitory.findByEmail(change.getEmail());
		if (std == null) {
			throw new StudentException("User with username not found");
		}
		if (!(change.getPassword().equals(change.getConPassword()))) {
			throw new StudentException("Passwords doesnt match");
		}
		std.setPassword(bcrypt.encode(change.getPassword()));
		studentRepoitory.save(std);
		return std;

	}
	
	//photo upload
	public ResponseEntity<?> uploadPic(MultipartFile file,String username)throws IOException{
		Student student=studentRepoitory.findByUsername(username);
		System.out.println("original size " +file.getSize());
//		compressByte(file.getBytes());
		student.setPicByte(compressByte(file.getBytes()));
		studentRepoitory.save(student);
		
		return new ResponseEntity<>("pic uploaded sucessfully",HttpStatus.OK);
	}

	//photo display
	public ResponseEntity<?> display(String username){
		Student student=studentRepoitory.findByUsername(username);
//		decompressByte(student.getPicByte());
		return ResponseEntity.ok().body(decompressByte(student.getPicByte()));
	}
	
	
	
	//compress before updating
	public static byte[] compressByte(byte[] data) {
		Deflater deflator=new Deflater();
		deflator.setInput(data);
		deflator.finish();
		
		ByteArrayOutputStream outputStream=new ByteArrayOutputStream(data.length);
		byte [] buffer=new byte[1024];
		while(deflator.finished()) {
			int count=deflator.deflate(buffer);
			outputStream.write(buffer,0,count);
		}
		try {
			outputStream.close();
		} catch (Exception e) {
			System.out.println(e);
		}
		System.out.println("Compressed --> "+outputStream.toByteArray().length);
		return outputStream.toByteArray();
	}
	
	//decompressing before downloaoding or showing
	public static byte[] decompressByte(byte[] data) {
		Inflater inflator=new Inflater();
		inflator.setInput(data);
		
		ByteArrayOutputStream outputStream=new ByteArrayOutputStream(data.length);
		byte [] buffer=new byte[1024];
		try {
		while(!inflator.finished()) {
			int count=inflator.inflate(buffer);
			outputStream.write(buffer,0,count);
		}
		
			outputStream.close();
		} catch (Exception e) {
			System.out.println(e);
		}
		System.out.println("Decompressed --> ");
		return outputStream.toByteArray();
	}
	
	
	//another way of uploaad
	public ResponseEntity<?> alternativeUpload(String username,MultipartFile file) throws IOException{
		Student student=studentRepoitory.findByUsername(username);
		String fileName=StringUtils.cleanPath(file.getOriginalFilename());
		student.setPhoto(fileName);
		studentRepoitory.save(student);
		
		String uploadDir="student-pics/"+student.getUsername();
		FileUploadUtil.saveFile(uploadDir, fileName, file);
		
		return ResponseEntity.ok().body("File uploaded in local directory"); 
	}
}
