package com.reto.elorchat.model.controller.response;

import java.util.Date;

public class MessageGetResponse {
	private Integer id;
	private String text;
	private Date sent;
	private Date saved;
	private Integer chatId;
	private Integer userId;


	public MessageGetResponse(Integer id, String text, Date sent, Date saved, Integer chatId,  Integer userId) {
		super();
		this.id = id;
		this.text = text;
		this.sent = sent;
		this.saved = saved;
		this.chatId = chatId;
		this.userId = userId;
	}
	
	public MessageGetResponse(Integer id, String text, Date sent, Date saved) {
		super();
		this.id = id;
		this.text = text;
		this.sent = sent;
		this.saved = saved;
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

	public Date getSent() {
		return sent;
	}

	public void setSent(Date sent) {
		this.sent = sent;
	}

	public Date getSaved() {
		return saved;
	}

	public void setSaved(Date saved) {
		this.saved = saved;
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
