package com.reto.elorchat.model.controller.response;

import com.reto.elorchat.model.enums.TextTypeEnum;

public class MessageGetResponse {
	
	private Integer id;
	private String text;
	private Long sent;
	private Long saved;
	private Integer chatId;
	private Integer userId;
	private TextTypeEnum type;


	public MessageGetResponse(Integer id, String text, Long sent, Long saved, Integer chatId,  Integer userId, TextTypeEnum type) {
		super();
		this.id = id;
		this.text = text;
		this.sent = sent;
		this.saved = saved;
		this.chatId = chatId;
		this.userId = userId;
		this.type = type;
	}

	public MessageGetResponse(Integer id, String text, Long sent, Long saved, TextTypeEnum type) {
		super();
		this.id = id;
		this.text = text;
		this.sent = sent;
		this.saved = saved;
		this.type = type;
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

	public Long getSent() {
		return sent;
	}

	public void setSent(Long sent) {
		this.sent = sent;
	}

	public Long getSaved() {
		return saved;
	}

	public void setSaved(Long saved) {
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

	public TextTypeEnum getType() {
		return type;
	}

	public void setType(TextTypeEnum type) {
		this.type = type;
	}
	
}
