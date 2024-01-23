package com.reto.elorchat.model.service;

import java.util.Date;

public class MessageDTO {
	private Integer id;
	private String text;
	private Date sent;
	private Date saved;
	private ChatDTO chat;
	private Integer chatId;
	private UserDTO user;
	private Integer userId;

	public MessageDTO(Integer id, String text, Date sent, Date saved, ChatDTO chat, UserDTO user) {
		super();
		this.id = id;
		this.text = text;
		this.sent = sent;
		this.saved = saved;
		this.chat = chat;
		this.user = user;
	}
	public MessageDTO(Integer id, String text, Date sent, Date saved, Integer chatId,  Integer userId) {
		super();
		this.id = id;
		this.text = text;
		this.sent = sent;
		this.saved = saved;
		this.chatId = chatId;
		this.userId = userId;
	}

	public MessageDTO(Integer id, String text, Date sent, Date saved) {
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
	public ChatDTO getChat() {
		return chat;
	}

	public void setChat(ChatDTO chat) {
		this.chat = chat;
	}

	public Integer getChatId() {
		return chatId;
	}
	public void setChatId(Integer chatId) {
		this.chatId = chatId;
	}
	
	public UserDTO getUser() {
		return user;
	}

	public void setUser(UserDTO user) {
		this.user = user;
	}
	
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}


}
