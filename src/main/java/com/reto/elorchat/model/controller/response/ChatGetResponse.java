package com.reto.elorchat.model.controller.response;

import java.util.ArrayList;
import java.util.List;

import com.reto.elorchat.model.persistence.User;

public class ChatGetResponse {
	
	private Integer id;
	private String name;
	private String type;
	private List<UserGetResponse> users= new ArrayList<>();
	private User admin;
	private Integer adminId;
	private List<MessageGetResponse> messages;
	public ChatGetResponse(Integer id, String name, String type, Integer adminId) {
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
	public List<UserGetResponse> getUsers() {
		return users;
	}
	public void setUsers(List<UserGetResponse> users) {
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
	public List<MessageGetResponse> getMessages() {
		return messages;
	}
	public void setMessages(List<MessageGetResponse> messages) {
		this.messages = messages;
	}

}
