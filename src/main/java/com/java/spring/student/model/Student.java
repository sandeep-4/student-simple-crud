package com.java.spring.student.model;

import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Email;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Entity
@Data
public class Student implements UserDetails{

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@NotBlank(message = "Name is required")
	private String name;
	@NotBlank(message = "Standred is required")
	private String standred;
	@Column(unique = true)
	private String username;
	@Email(message = "Enter valid email")
	@NotBlank(message = "Email is required")
	private String email;
	@NotBlank(message = "Password is required")
	private String password;
	private String address;
	@Max(value = 10)
	@Min(value = 7)
	private Long contact;
	@NotBlank(message = "Stream is required")
	private String stream;
	@NotBlank(message = "house is required")
	private String house;
	
	private byte[] picByte;
	
	private String photo;
	
	
	
	
	public Student() {
		super();
	}
	public Student(Long id, @NotBlank(message = "Name is required") String name,
			@NotBlank(message = "Standred is required") String standred, String username,
			@Email(message = "Enter valid email") @NotBlank(message = "Email is required") String email,
			@NotBlank(message = "Password is required") String password, String address, @Max(10) @Min(7) Long contact,
			@NotBlank(message = "Stream is required") String stream,
			@NotBlank(message = "house is required") String house, byte[] picByte, String photo) {
		super();
		this.id = id;
		this.name = name;
		this.standred = standred;
		this.username = username;
		this.email = email;
		this.password = password;
		this.address = address;
		this.contact = contact;
		this.stream = stream;
		this.house = house;
		this.picByte = picByte;
		this.photo = photo;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getStandred() {
		return standred;
	}
	public void setStandred(String standred) {
		this.standred = standred;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public Long getContact() {
		return contact;
	}
	public void setContact(Long contact) {
		this.contact = contact;
	}
	public String getStream() {
		return stream;
	}
	public void setStream(String stream) {
		this.stream = stream;
	}
	public String getHouse() {
		return house;
	}
	public void setHouse(String house) {
		this.house = house;
	}
	public byte[] getPicByte() {
		return picByte;
	}
	public void setPicByte(byte[] picByte) {
		this.picByte = picByte;
	}
	public String getPhoto() {
		return photo;
	}
	public void setPhoto(String photo) {
		this.photo = photo;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	@JsonIgnore
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return null;
	}
	@JsonIgnore
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}
	@Override
	@JsonIgnore
	public boolean isAccountNonLocked() {
		return true;
	}
	@Override
	@JsonIgnore
	public boolean isCredentialsNonExpired() {
		return true;
	}
	@Override
	@JsonIgnore
	public boolean isEnabled() {
		return true;
	}
}
