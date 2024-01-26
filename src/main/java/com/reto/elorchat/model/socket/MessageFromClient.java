package com.reto.elorchat.model.socket;

public class MessageFromClient {
	
    private Integer room;
    private String message;
    private Long sent;

    public MessageFromClient() {
        super();
    }
    public MessageFromClient(Integer room, String message, Long sent) {
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

	public Long getSent() {
		return sent;
	}
	public void setSent(Long sent) {
		this.sent = sent;
	}
	@Override
	public String toString() {
		return "MessageFromClient [room=" + room + ", message=" + message + "]";
	}

    
}