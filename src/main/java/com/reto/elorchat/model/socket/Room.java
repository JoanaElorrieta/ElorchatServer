package com.reto.elorchat.model.socket;

public class Room {
	
	private Integer roomId;
	private Integer userId;
	private String userName;
	
	public Room() {}
	
	public Room(Integer roomId, Integer userId, String userName) {
		super();
		this.roomId = roomId;
		this.userId = userId;
		this.userName = userName;
	}

	public Integer getRoomId() {
		return roomId;
	}

	public void setRoomId(Integer name) {
		this.roomId = name;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	@Override
	public String toString() {
		return "Room [roomId=" + roomId + ", userId=" + userId + "]";
	}
}
