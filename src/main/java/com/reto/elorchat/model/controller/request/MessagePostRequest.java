package com.reto.elorchat.model.controller.request;

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

	public Integer getChatId() {
		return chatId;
	}

	public void setChatId(Integer chatId) {
		this.chatId = chatId;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	
		
}
