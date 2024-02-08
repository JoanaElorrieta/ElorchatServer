package com.reto.elorchat.model.controller.response;

public class UserChatInfoGetResponse {

	private Integer userId;
	private Integer chatId;
	private Long joined;
	private Long deleted;


	public UserChatInfoGetResponse() {
		super();
	}

	public UserChatInfoGetResponse(Integer userId,  Integer chatId, Long joined, Long deleted) {
		super();
		this.userId = userId;
		this.chatId = chatId;
		this.joined = joined;
		this.deleted = deleted;
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

	public Long getJoined() {
		return joined;
	}

	public void setJoined(Long joined) {
		this.joined = joined;
	}

	public Long getDeleted() {
		return deleted;
	}

	public void setDeleted(Long deleted) {
		this.deleted = deleted;
	}

	@Override
	public String toString() {
		return "UserChatInfoGetResponse [userId=" + userId + ", chatId=" + chatId + ", joined=" + joined + ", deleted="
				+ deleted + "]";
	}
	
}
