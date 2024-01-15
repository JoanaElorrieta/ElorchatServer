package com.reto.elorchat.model.controller.request;

import com.reto.elorchat.model.enums.ChatTypeEnum;

public class ChatPostRequest {
	
	private Integer id;
	private String name;
	private ChatTypeEnum type;
	private Integer adminId;
	
	public ChatPostRequest() {}

	public ChatPostRequest(Integer id, String name, ChatTypeEnum type, Integer adminId) {
		super();
		this.id = id;
		this.name = name;
		this.type = type;
		this.adminId = adminId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public ChatTypeEnum getType() {
		return type;
	}

	public void setType(ChatTypeEnum type) {
		this.type = type;
	}

	public Integer getAdminId() {
		return adminId;
	}

	public void setAdminId(Integer adminId) {
		this.adminId = adminId;
	}
	
	
}
