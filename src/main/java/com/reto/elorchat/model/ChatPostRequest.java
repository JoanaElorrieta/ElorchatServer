package com.reto.elorchat.model;

public class ChatPostRequest {
	
	private String name;
	private String type;
	private Integer userId;
	
	public ChatPostRequest() {}

	public ChatPostRequest(String name, String type, Integer userId) {
		super();
		this.name = name;
		this.type = type;
		this.userId = userId;
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

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	
	
}
