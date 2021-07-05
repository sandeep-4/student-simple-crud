package com.java.spring.student.controller;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.java.spring.student.exception.StudentException;
import com.java.spring.student.jwt.JwtTokenProvider;
import com.java.spring.student.model.Student;
import com.java.spring.student.service.ErrorMapping;
import com.java.spring.student.service.StudentService;
import com.java.spring.student.utils.LoggedIn;
import com.java.spring.student.utils.Login;
import com.java.spring.student.utils.PasswordChange;

@RestController
@RequestMapping("/api/student")
public class StudentController {

	@Autowired
	private StudentService studentService;
	
	@Autowired
	private JwtTokenProvider jwtTokenProvider;
	
	@Autowired
	private AuthenticationManager authManager;
	
	@Autowired
	private ErrorMapping errorMapService;
	
	
	@PostMapping("/register")
	public ResponseEntity<?> register(@Valid @RequestBody Student student,BindingResult result){
		ResponseEntity<?> mapError=errorMapService.errorMapping(result);
		if(mapError!=null) {
			return mapError;
		}
		Student std=studentService.saveUser(student);
		return new ResponseEntity<>(std,HttpStatus.OK);
	}
	
	@PostMapping("/login")
	public ResponseEntity<?> login(@Valid @RequestBody Login login,BindingResult result){
		ResponseEntity<?> mapError=errorMapService.errorMapping(result);
		if(mapError!=null) {
			return mapError;
		}
		Authentication authentication=authManager.authenticate(
				new UsernamePasswordAuthenticationToken(login.getEmail(),login.getPassword())
				);
		SecurityContextHolder.getContext().setAuthentication(authentication);
		String token="Bearer "+jwtTokenProvider.generateToken(authentication);
		
		return ResponseEntity.ok(new LoggedIn(true,token));
		
	}
	
	@GetMapping("/all")
	public ResponseEntity<?> allTheStudents(Principal principal){
		System.out.println(".....");
		System.out.println(principal.getName());
		System.out.println(".....");
		List<Student> student=studentService.listOfAllStudent();
		return ResponseEntity.ok().body(student);
	}
	
	@GetMapping("/search/{filter}")
	public ResponseEntity<?> allStudentByFilterNameAndUsername(@PathVariable(name = "filter") String filter){
	List<Student> students=studentService.searchUsingFilter(filter);
	return ResponseEntity.ok().body(students);
	}
	
	@GetMapping("/stream/{stream}")
	public ResponseEntity<?> allStudentByStream(@PathVariable(name = "stream") String stream){
		List<Student> streams=studentService.findByStream(stream);
		return ResponseEntity.ok().body(streams);
	}
	
	@GetMapping("/house/{house}")
	public ResponseEntity<?> allStudentByHouse(@PathVariable(name = "house") String house){
		List<Student> std=studentService.findByStream(house);
		return ResponseEntity.ok().body(std);
	}
	
	
	//username here is slug
	
	@PutMapping("/update/{username}")
	public ResponseEntity<?> updateStudent(@PathVariable(name="username") String username,
			@Valid @RequestBody Student student,
			Principal principal){
		String authUsername=principal.getName();
		if(authUsername.equals(username)) {
		ResponseEntity<?> updated=studentService.updateStudent(username, student);
		if(updated==null) {
			throw new StudentException("Unable to update");
		}
		return updated;
		}
		return new ResponseEntity<>("This not your account",HttpStatus.UNAUTHORIZED);
	}
	
	@DeleteMapping("/delete/{username}")
	public ResponseEntity<?> deleteStudent(@PathVariable(name = "username") String username,Principal principal){	
		String logedUsername=principal.getName();
		if(logedUsername.equals(username)) {
			studentService.deleteStudent(username);
			return ResponseEntity.ok("Deleted sucessfully !!!");
		}
		return ResponseEntity.ok("You are not acessed to delete");
	}
	
	@PutMapping("/password-reset")
	public ResponseEntity<?> updateStudent(@Valid @RequestBody PasswordChange change){
		Student pass=studentService.passwordChange(change);
		if(pass==null) {
			return ResponseEntity.ok("Unable to reset password");
		}
		return ResponseEntity.ok("Reseted Password sucessfully");

	}
	@PutMapping("/uploadImage/{username}")
	public ResponseEntity<?> uploadImage(@PathVariable(name = "username")String username,@RequestBody MultipartFile image) throws IOException{
		return studentService.uploadPic(image, username);
	}
	
	@GetMapping("/image/{username}")
	public ResponseEntity<?> seeImage(@PathVariable(name = "username")String username){
		return studentService.display(username);
	}
	
	@PutMapping("/localUpload/{username}")
	public ResponseEntity<?> localUploadFile(@PathVariable(name = "username")String username,@RequestBody MultipartFile file){
		try {
			return studentService.alternativeUpload(username, file);
		} catch (IOException e) {
			throw new StudentException("Student pic not able to upload");
		}
	}
	
	
}
