package com.reto.elorchat.model.controller.response;

import java.util.List;

public class UserGetResponse {
	private Integer id;
	private String name;
	private String surname;
	private String email;
	private String password;
	private int phone_Number1;
	private int phone_Number2;
	private String address;
	private String photo;
	private Boolean FCTDUAL;
	private List<ChatGetResponse> chats;
	private List<MessageGetResponse> messages;

	public UserGetResponse(Integer id, String name, String surname, String email, String password, int phone_Number1,
			int phone_Number2, String address, String photo, Boolean fCTDUAL) {
		super();
		this.id = id;
		this.name = name;
		this.surname = surname;
		this.email = email;
		this.password = password;
		this.phone_Number1 = phone_Number1;
		this.phone_Number2 = phone_Number2;
		this.address = address;
		this.photo = photo;
		FCTDUAL = fCTDUAL;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
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
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public int getPhone_Number1() {
		return phone_Number1;
	}
	public void setPhone_Number1(int phone_Number1) {
		this.phone_Number1 = phone_Number1;
	}
	public int getPhone_Number2() {
		return phone_Number2;
	}
	public void setPhone_Number2(int phone_Number2) {
		this.phone_Number2 = phone_Number2;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getPhoto() {
		return photo;
	}
	public void setPhoto(String photo) {
		this.photo = photo;
	}
	public Boolean getFCTDUAL() {
		return FCTDUAL;
	}
	public void setFCTDUAL(Boolean fCTDUAL) {
		FCTDUAL = fCTDUAL;
	}
	public List<ChatGetResponse> getChats() {
		return chats;
	}
	public void setChats(List<ChatGetResponse> chats) {
		this.chats = chats;
	}
	public List<MessageGetResponse> getMessages() {
		return messages;
	}
	public void setMessages(List<MessageGetResponse> messages) {
		this.messages = messages;
	}


}
