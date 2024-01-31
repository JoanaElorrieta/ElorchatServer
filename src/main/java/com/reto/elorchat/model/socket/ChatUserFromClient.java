package com.reto.elorchat.model.socket;

public class ChatUserFromClient {
	private Integer roomId;
	private Integer userId;
	private Integer adminId;

	public ChatUserFromClient() {}

	public ChatUserFromClient(Integer roomId) {
		super();
		this.roomId = roomId;
	}

	public ChatUserFromClient(Integer roomId, Integer userId) {
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

	public Integer getAdminId() {
		return adminId;
	}

	public void setAdminId(Integer adminId) {
		this.adminId = adminId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	@Override
	public String toString() {
		return "Room [roomId=" + roomId + ", userId=" + userId + "]";
	}
}
