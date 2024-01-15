package com.reto.elorchat.model.controller.response;

import com.reto.elorchat.model.enums.ChatTypeEnum;
import com.reto.elorchat.security.persistance.User;

public class ChatPostResponse {
	private Integer id;
	private String name;
	private ChatTypeEnum type;
	private User admin;
	private Integer adminId;
	
	public ChatPostResponse(Integer id, String name, ChatTypeEnum type, Integer adminId) {
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
	public ChatTypeEnum getType() {
		return type;
	}
	public void setType(ChatTypeEnum type) {
		this.type = type;
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

}
