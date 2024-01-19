package com.reto.elorchat.model.socket;

public class Room {
	
	private String name;
	private Integer userId;
	
	public Room() {}
	
	public Room(String name, Integer userId) {
		super();
		this.name = name;
		this.userId = userId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	@Override
	public String toString() {
		return "Room [name=" + name + ", userId=" + userId + "]";
	}
}
