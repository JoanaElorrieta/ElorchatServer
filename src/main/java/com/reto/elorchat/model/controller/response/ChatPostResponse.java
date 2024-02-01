package com.reto.elorchat.model.controller.response;

import java.util.ArrayList;
import java.util.List;

import com.reto.elorchat.model.enums.ChatTypeEnum;

public class ChatPostResponse {
	private Integer id;
	private String name;
	private ChatTypeEnum type;
	private Integer adminId;
	private List<UserGetResponse> users = new ArrayList<>();
	private List<MessageGetResponse> messages = new ArrayList<>();
	private Long created;
	private Long deleted;
	
	public ChatPostResponse(Integer id, String name, ChatTypeEnum type, Integer adminId, Long created, Long deleted) {
		super();
		this.id = id;
		this.name = name;
		this.type = type;
		this.adminId = adminId;
		this.created = created;
		this.deleted = deleted;
	}
	
	public ChatPostResponse(Integer id, String name, ChatTypeEnum type, Integer adminId, Long created) {
		super();
		this.id = id;
		this.name = name;
		this.type = type;
		this.adminId = adminId;
		this.created = created;
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
	public List<UserGetResponse> getUsers() {
		return users;
	}
	public void setUsers(List<UserGetResponse> users) {
		this.users = users;
	}
	public List<MessageGetResponse> getMessages() {
		return messages;
	}
	public void setMessages(List<MessageGetResponse> messages) {
		this.messages = messages;
	}

	public Long getCreated() {
		return created;
	}

	public void setCreated(Long created) {
		this.created = created;
	}

	public Long getDeleted() {
		return deleted;
	}

	public void setDeleted(Long deleted) {
		this.deleted = deleted;
	}

}
