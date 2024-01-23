package com.reto.elorchat.model.socket;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.reto.elorchat.model.enums.MessageType;

public class MessageFromServer {

	private MessageType messageType;
	private Integer room;
	private Integer messageId;
	private String message;
	private String authorName;
	private Integer authorId;

	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date sent;

	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date saved;


	public MessageFromServer() {
		super();
	}

	public MessageFromServer(MessageType messageType, Integer room, Integer messageId, String message, String authorName, Integer authorId, Date sent, Date saved) {
		super();
		this.messageType = messageType;
		this.room = room;
		this.messageId = messageId;
		this.message = message;
		this.authorName = authorName;
		this.authorId = authorId;
		this.sent = sent;
		this.saved = saved;
	}

	public Integer getRoom() {
		return room;
	}

	public void setRoom(Integer room) {
		this.room = room;
	}

	public Integer getMessageId() {
		return messageId;
	}

	public void setMessageId(Integer messageId) {
		this.messageId = messageId;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getAuthorName() {
		return authorName;
	}

	public void setAuthorName(String authorName) {
		this.authorName = authorName;
	}

	public Integer getAuthorId() {
		return authorId;
	}

	public void setAuthorId(Integer authorId) {
		this.authorId = authorId;
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

	public MessageType getMessageType() {
		return messageType;
	}

	public void setMessageType(MessageType messageType) {
		this.messageType = messageType;
	}

	@Override
	public String toString() {
		return "MessageFromServer [room=" + room + ", message=" + message + ", authorName=" + authorName + ", authorId="
				+ authorId + ", messageType=" + messageType + "]";
	}


}