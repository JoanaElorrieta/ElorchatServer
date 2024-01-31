package com.reto.elorchat.model.socket;

import com.reto.elorchat.model.enums.MessageType;

public class MessageFromServer {

	private MessageType messageType;
	private Integer room;
	private Integer messageServerId;
	private Integer localId;
	private String message;
	private String authorName;
	private Integer authorId;

	private Long sent;

	private Long saved;


	public MessageFromServer() {
		super();
	}

	public MessageFromServer(MessageType messageType, Integer room, Integer messageServerId, Integer localId, String message, String authorName, Integer authorId, Long sent, Long saved) {
		super();
		this.messageType = messageType;
		this.room = room;
		this.messageServerId = messageServerId;
		this.localId = localId;
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

	public Integer getLocalId() {
		return localId;
	}

	public void setLocalId(Integer localId) {
		this.localId = localId;
	}

	public Integer getMessageServerId() {
		return messageServerId;
	}

	public void setMessageServerId(Integer messageServerId) {
		this.messageServerId = messageServerId;
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