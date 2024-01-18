package com.reto.elorchat.model.controller.response;

import java.util.Date;

public class MessageGetResponse {
	private Integer id;
	private String text;
	private Date date;
	private Integer chatId;
	private Integer userId;


	public MessageGetResponse(Integer id, String text, Date date, Integer chatId,  Integer userId) {
		super();
		this.id = id;
		this.text = text;
		this.date = date;
		this.chatId = chatId;
		this.userId = userId;
	}
	
	public MessageGetResponse(Integer id, String text, Date date) {
		super();
		this.id = id;
		this.text = text;
		this.date = date;
	}
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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
