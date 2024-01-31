package com.reto.elorchat.model.socket;

import com.reto.elorchat.model.enums.ChatTypeEnum;

public class ChatFromClient {
	private Integer id;
	private String name;
	private ChatTypeEnum type;
	private Integer adminId;

	public ChatFromClient() {
		super();
	}

	public ChatFromClient(Integer id, String name, ChatTypeEnum type, Integer adminId) {
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
	public Integer getAdminId() {
		return adminId;
	}
	public void setAdminId(Integer adminId) {
		this.adminId = adminId;
	}

}
