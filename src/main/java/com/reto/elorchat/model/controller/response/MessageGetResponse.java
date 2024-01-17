package com.reto.elorchat.model.controller.response;

import java.util.Date;

import com.reto.elorchat.model.service.ChatDTO;
import com.reto.elorchat.model.service.UserDTO;

public class MessageGetResponse {
	private Integer id;
	private String text;
	private Date date;
	private ChatDTO chat;
	private UserDTO user;

	public MessageGetResponse(Integer id, String text, Date date, ChatDTO chat, UserDTO user) {
		super();
		this.id = id;
		this.text = text;
		this.date = date;
		this.chat = chat;
		this.user = user;
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
