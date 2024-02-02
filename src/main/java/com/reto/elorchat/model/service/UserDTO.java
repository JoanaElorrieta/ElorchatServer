package com.reto.elorchat.model.service;

import java.util.List;

public class UserDTO {

	private Integer id;
	private String name;
	private String surname;
	private String email;
	private Long phoneNumber1;
	private String photo;
	private Integer roleId;
	private List<ChatDTO> chats;
	private List<MessageDTO> messages;
	private List<UserChatInfoDTO> userChatInfo;

	public UserDTO(Integer id, String name, String surname, String email, Long phoneNumber1,
			String photo) {
		super();
		this.id = id;
		this.name = name;
		this.surname = surname;
		this.email = email;
		this.phoneNumber1 = phoneNumber1;
		this.photo = photo;
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
	public void setPhoneNumber1(Long phoneNumber1) {
		this.phoneNumber1 = phoneNumber1;
	}

	public String getPhoto() {
		return photo;
	}
	public void setPhoto(String photo) {
		this.photo = photo;
	}

	public Integer getRoleId() {
		return roleId;
	}
	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}
	public List<ChatDTO> getChats() {
		return chats;
	}
	
	public void setChats(List<ChatDTO> chats) {
		this.chats = chats;
	}
	
	public List<MessageDTO> getMessages() {
		return messages;
	}
	public void setMessages(List<MessageDTO> messages) {
		this.messages = messages;
	}

	public List<UserChatInfoDTO> getUserChatInfo() {
		return userChatInfo;
	}
	public void setUserChatInfo(List<UserChatInfoDTO> userChatInfo) {
		this.userChatInfo = userChatInfo;
	}
	@Override
	public String toString() {
		return "UserDTO [id=" + id + ", name=" + name + ", surname=" + surname + ", email=" + email + ", phoneNumber1="
				+ phoneNumber1 + ", photo=" + photo + ", chats=" + chats + ", messages=" + messages + "]";
	}


}
