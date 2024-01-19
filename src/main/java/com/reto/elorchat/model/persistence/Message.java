package com.reto.elorchat.model.persistence;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.reto.elorchat.security.persistance.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
@Entity
@Table(name = "messages")
public class Message {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	@Column
	private String text;
	@Column
	private Date date;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "chat_id", foreignKey = @ForeignKey(name = "Fk_chat_id" ))
	@JsonManagedReference
	@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
	private Chat chat;

	@Column(name = "chat_id", insertable = false, updatable = false)
	private Integer chatId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "Fk_user_id" ))
	@JsonManagedReference
	@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
	private User user;

	@Column(name = "user_id", insertable = false, updatable = false)
	private Integer userId;

	public Message () {}

	public Message(Integer id, String text, Date date) {
		super();
		this.id = id;
		this.text = text;
		this.date = date;
	}

	public Message(String text, Date date, Chat chat, User user) {
		super();
		this.text = text;
		this.date = date;
		this.chat = chat;
		this.user = user;
	}

	public Message(String text, Date date, Chat chat, Integer chatId) {
		super();
		this.text = text;
		this.date = date;
		this.chat = chat;
		this.chatId= chatId;
	}
	public Message(Integer id, String text, Date date, Chat chat, Integer chatId) {
		super();
		this.id = id;
		this.text = text;
		this.date = date;
		this.chat = chat;
		this.chatId= chatId;
	}

	public Message(Integer id, String text, Date date, Chat chat, Integer chatId, User user, Integer userId) {
		super();
		this.id = id;
		this.text = text;
		this.date = date;
		this.chat = chat;
		this.chatId = chatId;
		this.user = user;
		this.userId = userId;
	}

	public Message(Integer id, String text, Date date, Integer chatId, Integer userId) {
		super();
		this.id = id;
		this.text = text;
		this.date = date;
		this.chatId = chatId;
		this.userId = userId;
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

	public Chat getChat() {
		return chat;
	}

	public void setChat(Chat chat) {
		this.chat = chat;
	}

	public Integer getChatId() {
		return chatId;
	}

	public void setChatId(Integer chatId) {
		this.chatId = chatId;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}


}
