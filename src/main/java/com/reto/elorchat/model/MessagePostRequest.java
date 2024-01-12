package com.reto.elorchat.model;

import java.util.Date;

public class MessagePostRequest {

	private String text;
	private Date date;
	private Integer chatId;
	private Integer userId;

	public MessagePostRequest() {}
	
	public MessagePostRequest(String text, Date date, Integer chatId, Integer userId) {
		super();
		this.text = text;
		this.date = date;
		this.chatId = chatId;
		this.userId = userId;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Integer getGroupId() {
		return chatId;
	}

	public void setGroupId(Integer groupId) {
		this.chatId = groupId;
	}

	public Integer getUseId() {
		return userId;
	}

	public void setUser_id(Integer userId) {
		this.userId = userId;
	}
	
		
}
