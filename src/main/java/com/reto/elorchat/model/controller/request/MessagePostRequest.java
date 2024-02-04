package com.reto.elorchat.model.controller.request;

import com.reto.elorchat.model.enums.TextTypeEnum;

public class MessagePostRequest {
	
    private Integer room;
    private Integer userId;
    private Integer localId;
    private String message;
    private Long sent;
    private TextTypeEnum type;

    public MessagePostRequest() {
        super();
    }
    public MessagePostRequest(Integer room, Integer userId, Integer localId, String message, Long sent, TextTypeEnum type) {
    	super();
        this.room = room;
        this.userId = userId;
        this.localId = localId;
        this.message = message;
        this.sent = sent;
        this.type = type;
    }

	public Integer getRoom() {
		return room;
	}

	public void setRoom(Integer room) {
		this.room = room;
	}

	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public Integer getLocalId() {
		return localId;
	}
	public void setLocalId(Integer localId) {
		this.localId = localId;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}

	public Long getSent() {
		return sent;
	}
	public void setSent(Long sent) {
		this.sent = sent;
	}
	
	public TextTypeEnum getType() {
		return type;
	}
	public void setType(TextTypeEnum type) {
		this.type = type;
	}
	@Override
	public String toString() {
		return "MessageFromClient [room=" + room + ", message=" + message + "]";
	}

		
}
