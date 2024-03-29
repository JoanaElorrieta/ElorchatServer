package com.reto.elorchat.model.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.reto.elorchat.model.enums.ChatTypeEnum;

public class ChatDTO {
	private Integer id;
	private String name;
	private ChatTypeEnum type;
	private List<UserDTO> users = new ArrayList<>();
	private UserDTO admin;
	private Integer adminId;
	//FIXME NECESITO ESTO?
	private List<MessageDTO> messages = new ArrayList<>();
	private Date created;
	private Date deleted;

	public ChatDTO(Integer id, String name, ChatTypeEnum type, Integer adminId, Date created, Date deleted) {
		super();
		this.id = id;
		this.name = name;
		this.type = type;
		this.adminId = adminId;
		this.created = created;
		this.deleted = deleted;
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
	public List<UserDTO> getUsers() {
		return users; 
	}
	public void setUsers(List<UserDTO> users) {
		this.users = users;
	}
	public UserDTO getAdmin() {
		return admin;
	}
	public void setAdmin(UserDTO admin) {
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

	public Date getCreated() {
		return created;
	}
	public void setCreated(Date created) {
		this.created = created;
	}
	public Date getDeleted() {
		return deleted;
	}
	public void setDeleted(Date deleted) {
		this.deleted = deleted;
	}

	public boolean isChatPrivate(ChatDTO chatDTO) {
		if (chatDTO.getType() == ChatTypeEnum.PRIVATE) {
			System.out.println("Es privado");
			return true;
		}
		return false;
	}
	@Override
	public String toString() {
		return "ChatDTO [id=" + id + ", name=" + name + ", type=" + type + ", users=" + users + ", admin=" + admin
				+ ", adminId=" + adminId + ", messages=" + messages + "]";
	}

}
