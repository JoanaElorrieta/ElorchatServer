package com.reto.elorchat.model.socket;

public class MessageFromClient {
    private String room;
    private String message;


    public MessageFromClient() {
        super();
    }
    public MessageFromClient(String room, String message) {
    	super();
        this.message = message;
        this.room = room;
    }

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getRoom() {
		return room;
	}

	public void setRoom(String room) {
		this.room = room;
	}
	
	@Override
	public String toString() {
		return "MessageFromClient [room=" + room + ", message=" + message + "]";
	}

    
}