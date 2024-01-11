package com.reto.elorchat.model;

public class UserPostRequest {
	private String name;
	private String surname;
	private String email;
	private int phoneNumber;
	
	public UserPostRequest() {
	}
	
	public UserPostRequest(String name, String surname, String email, int phoneNumber) {
		super();
		this.name = name;
		this.surname = surname;
		this.email = email;
		this.phoneNumber = phoneNumber;
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

	public int getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(int phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	
}
