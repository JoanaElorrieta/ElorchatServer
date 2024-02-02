package com.reto.elorchat.model.socket;

import com.reto.elorchat.model.enums.TextTypeEnum;

public class MessageFromClient {
	
    private Integer room;
    private Integer localId;
    private String message;
    private Long sent;
    private TextTypeEnum type;

    public MessageFromClient() {
        super();
    }
    public MessageFromClient(Integer room, Integer localId, String message, Long sent, TextTypeEnum type) {
    	super();
        this.message = message;
        this.room = room;
        this.sent = sent;
        this.type = type;
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