package com.reto.elorchat.model.socket;

public class Room {
	
	private Integer roomId;
	private Integer userId;
	
	public Room() {}
	
	public Room(Integer roomId, Integer userId) {
		super();
		this.roomId = roomId;
		this.userId = userId;
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

	@Override
	public String toString() {
		return "Room [roomId=" + roomId + ", userId=" + userId + "]";
	}
}
