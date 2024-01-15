package com.reto.elorchat.model.controller.response;

import java.util.ArrayList;
import java.util.List;

import com.reto.elorchat.model.persistence.User;
import com.reto.elorchat.model.service.MessageDTO;
import com.reto.elorchat.model.service.UserDTO;

public class ChatPostResponse {
	private Integer id;
	private String name;
	private String type;
	private List<UserDTO> users= new ArrayList<>();
	private User admin;
	private Integer adminId;
	private List<MessageDTO> messages;
	
	public ChatPostResponse(Integer id, String name, String type, Integer adminId) {
		super();
		this.id = id;
		this.name = name;
		this.type = type;
		this.adminId = adminId;
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
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public List<UserDTO> getUsers() {
		return users;
	}
	public void setUsers(List<UserDTO> users) {
		this.users = users;
	}
	public User getAdmin() {
		return admin;
	}
	public void setAdmin(User admin) {
		this.admin = admin;
	}
	public Integer getAdminId() {
		return adminId;
	}
	public void setAdminId(Integer adminId) {
		this.adminId = adminId;
	}
	public List<MessageDTO> getMessages() {
		return messages;
	}
	public void setMessages(List<MessageDTO> messages) {
		this.messages = messages;
	}

}
