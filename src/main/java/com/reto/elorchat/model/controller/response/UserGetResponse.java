package com.reto.elorchat.model.controller.response;

import java.util.List;

public class UserGetResponse {
	private Integer id;
	private String name;
	private String surname;
	private String email;
	private Long phoneNumber1;
	private Integer roleId;
	private List<Integer> chatId;
	private List<UserChatInfoGetResponse> userChatInfo;

	public UserGetResponse(Integer id, String name, String surname, String email, Long phoneNumber1) {
		super();
		this.id = id;
		this.name = name;
		this.surname = surname;
		this.email = email;
		this.phoneNumber1 = phoneNumber1;
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

	public Long getPhoneNumber1() {
		return phoneNumber1;
	}
	
	public Integer getRoleId() {
		return roleId;
	}

	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}

	public void setPhoneNumber1(Long phoneNumber1) {
		this.phoneNumber1 = phoneNumber1;
	}

	public List<Integer> getChatId() {
		return chatId;
	}

	public void setChatId(List<Integer> chatId) {
		this.chatId = chatId;
	}

	public List<UserChatInfoGetResponse> getUserChatInfo() {
		return userChatInfo;
	}

	public void setUserChatInfo(List<UserChatInfoGetResponse> userChatInfo) {
		this.userChatInfo = userChatInfo;
	}
	
	
}
