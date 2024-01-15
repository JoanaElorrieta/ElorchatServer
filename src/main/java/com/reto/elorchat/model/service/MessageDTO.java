package com.reto.elorchat.model.service;

import java.util.Date;

public class MessageDTO {
	private Integer id;
	private String text;
	private Date date;
	private ChatDTO chat;
	private UserDTO user;

	public MessageDTO(Integer id, String text, Date date, ChatDTO chat, UserDTO user) {
		super();
		this.id = id;
		this.text = text;
		this.date = date;
		this.chat = chat;
		this.user = user;
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

	public ChatDTO getChat() {
		return chat;
	}

	public void setChat(ChatDTO chat) {
		this.chat = chat;
	}

	public UserDTO getUser() {
		return user;
	}

	public void setUser(UserDTO user) {
		this.user = user;
	}


}
