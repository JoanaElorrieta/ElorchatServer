package com.reto.elorchat.model.controller.request;

public class UserChatInfoPostRequest {
	private Integer userId;
	private Integer chatId;
	
	public UserChatInfoPostRequest() {
		super();
	}
	
	public UserChatInfoPostRequest(Integer userId, Integer chatId) {
		super();
		this.userId = userId;
		this.chatId = chatId;
	}
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public Integer getChatId() {
		return chatId;
	}
	public void setChatId(Integer chatId) {
		this.chatId = chatId;
	}

	@Override
	public String toString() {
		return "UserChatInfoPostRequest [userId=" + userId + ", chatId=" + chatId + "]";
	}
	
}
