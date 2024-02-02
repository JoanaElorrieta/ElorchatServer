package com.reto.elorchat.model.service;

import java.util.Date;

public class UserChatInfoDTO {

	private Integer user_Id;
	private Integer chat_Id;
	private Date joined;
	private Date deleted;


	public UserChatInfoDTO() {
		super();
	}

	public UserChatInfoDTO(Integer userId,  Integer chatId, Date joined, Date deleted) {
		super();
		this.user_Id = userId;
		this.chat_Id = chatId;
		this.joined = joined;
		this.deleted = deleted;
	}

	public Integer getUserId() {
		return user_Id;
	}

	public void setUserId(Integer userId) {
		this.user_Id = userId;
	}

	public Integer getChatId() {
		return chat_Id;
	}

	public void setChatId(Integer chatId) {
		this.chat_Id = chatId;
	}

	public Date getJoined() {
		return joined;
	}

	public void setJoined(Date joined) {
		this.joined = joined;
	}

	public Date getDeleted() {
		return deleted;
	}

	public void setDeleted(Date deleted) {
		this.deleted = deleted;
	}
}
