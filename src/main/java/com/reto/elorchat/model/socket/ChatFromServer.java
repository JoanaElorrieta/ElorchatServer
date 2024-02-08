package com.reto.elorchat.model.socket;

import com.reto.elorchat.model.enums.ChatTypeEnum;

public class ChatFromServer {
	private Integer id;
	private String name;
	private ChatTypeEnum type;
	private Long created;
	private Long deleted;
	private Integer adminId;
	
    public ChatFromServer() {
        super();
    }
	public ChatFromServer(Integer id, String name, ChatTypeEnum type, Long created, Long deleted,  Integer adminId) {
		super();
		this.id = id;
		this.name = name;
		this.type = type;
		this.created = created;
		this.deleted = deleted;
		this.adminId = adminId;
	}
	
	public ChatFromServer(Integer id, String name, ChatTypeEnum type, Integer adminId) {
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
