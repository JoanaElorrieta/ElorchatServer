package com.reto.elorchat.model.socket;

public class Room {
	
	private Integer roomId;
	private Integer userId;
	private Integer adminId;
	private String userName;
	private String adminName;
	
	public Room() {}
	
	public Room(Integer roomId, Integer userId, String userName) {
		super();
		this.roomId = roomId;
		this.userId = userId;
		this.userName = userName;
	}

	public Room(Integer roomId, Integer userId, Integer adminId, String userName, String adminName) {
		super();
		this.roomId = roomId;
		this.userId = userId;
		this.adminId = adminId;
		this.userName = userName;
		this.adminName = adminName;
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

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	public String getAdminName() {
		return adminName;
	}

	public void setAdminName(String adminName) {
		this.adminName = adminName;
	}
	@Override
	public String toString() {
		return "Room [roomId=" + roomId + ", userId=" + userId + "]";
	}
}
