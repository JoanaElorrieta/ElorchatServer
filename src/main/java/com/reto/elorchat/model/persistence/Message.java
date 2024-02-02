package com.reto.elorchat.model.persistence;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.reto.elorchat.model.enums.TextTypeEnum;
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
	private Date sent;
	@Column
	private Date saved;
	
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
	
	@Column(name = "type")
	private TextTypeEnum textType;
	
	public Message () {}

	public Message(Integer id, String text, Date sent, Date saved, TextTypeEnum textType) {
		super();
		this.id = id;
		this.text = text;
		this.sent = sent;
		this.saved = saved;
		this.textType = textType;
	}

	public Message(String text, Date sent, Date saved, Chat chat, User user, TextTypeEnum textType) {
		super();
		this.text = text;
		this.sent = sent;
		this.saved = saved;
		this.chat = chat;
		this.user = user;
		this.textType = textType;
	}

	public Message(String text, Date sent, Date saved, Chat chat, Integer chatId, TextTypeEnum textType) {
		super();
		this.text = text;
		this.sent = sent;
		this.saved = saved;
		this.chat = chat;
		this.chatId= chatId;
		this.textType = textType;
	}
	public Message(Integer id, String text, Date sent, Date saved, Chat chat, Integer chatId, TextTypeEnum textType) {
		super();
		this.id = id;
		this.text = text;
		this.sent = sent;
		this.saved = saved;
		this.chat = chat;
		this.chatId= chatId;
		this.textType = textType;
	}

	public Message(Integer id, String text, Date sent, Date saved, Chat chat, Integer chatId, User user, Integer userId, TextTypeEnum textType) {
		super();
		this.id = id;
		this.text = text;
		this.sent = sent;
		this.saved = saved;
		this.chat = chat;
		this.chatId = chatId;
		this.user = user;
		this.userId = userId;
		this.textType = textType;
	}

	public Message(Integer id, String text, Date sent, Date saved, Integer chatId, Integer userId, TextTypeEnum textType) {
		super();
		this.id = id;
		this.text = text;
		this.sent = sent;
		this.saved = saved;
		this.chatId = chatId;
		this.userId = userId;
		this.textType = textType;
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

	public void setSaved(Date sent) {
		this.sent = sent;
	}

	public Date getSaved() {
		return saved;
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

	public TextTypeEnum getTextType() {
		return textType;
	}

	public void setTextType(TextTypeEnum textType) {
		this.textType = textType;
	}
}
