package com.reto.elorchat.model.controller.request;

public class UserPostRequest {
	private String name;
	private String surname;
	private String email;
	private Integer phoneNumber1;
	private Integer phoneNumber2;
	
	public UserPostRequest() {
	}
	
	public UserPostRequest(String name, String surname, String email, Integer phoneNumber1) {
		super();
		this.name = name;
		this.surname = surname;
		this.email = email;
		this.phoneNumber1 = phoneNumber1;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Integer getPhoneNumber1() {
		return phoneNumber1;
	}

	public void setPhoneNumber1(Integer phoneNumber1) {
		this.phoneNumber1 = phoneNumber1;
	}

	public Integer getPhoneNumber2() {
		return phoneNumber2;
	}

	public void setPhoneNumber2(Integer phoneNumber2) {
		this.phoneNumber2 = phoneNumber2;
	}
	
}
