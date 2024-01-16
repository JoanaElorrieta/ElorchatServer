package com.reto.elorchat.model.socket;

import com.reto.elorchat.model.enums.MessageType;

public class MessageFromServer {

	private MessageType messageType;
    private String room;
    private String message;
    private String authorName;
    private Integer authorId;

    
    public MessageFromServer() {
    	super();
    }
    
	public MessageFromServer(MessageType messageType, String room, String message, String authorName, Integer authorId) {
		super();
		this.messageType = messageType;
		this.room = room;
		this.message = message;
		this.authorName = authorName;
		this.authorId = authorId;
	}

	public String getRoom() {
		return room;
	}

	public void setRoom(String room) {
		this.room = room;
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