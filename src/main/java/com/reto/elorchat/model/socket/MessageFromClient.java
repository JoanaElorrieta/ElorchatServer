package com.reto.elorchat.model.socket;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class MessageFromClient {
    private Integer room;
    private String message;
    
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date sent;

    public MessageFromClient() {
        super();
    }
    public MessageFromClient(Integer room, String message, Date sent) {
    	super();
        this.message = message;
        this.room = room;
        this.sent = sent;
    }

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Integer getRoom() {
		return room;
	}

	public void setRoom(Integer room) {
		this.room = room;
	}

	public Date getSent() {
		return sent;
	}
	public void setSent(Date sent) {
		this.sent = sent;
	}
	@Override
	public String toString() {
		return "MessageFromClient [room=" + room + ", message=" + message + "]";
	}

    
}