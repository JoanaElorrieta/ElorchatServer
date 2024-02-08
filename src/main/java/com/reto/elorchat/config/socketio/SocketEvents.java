package com.reto.elorchat.config.socketio;

public enum SocketEvents {

	ON_MESSAGE_RECEIVED("send message"),
	ON_SEND_MESSAGE("receive message"),
	ON_MESSAGE_NOT_SENT("not sent message"), 
	
	ON_CHAT_RECEIVED("send chat"),
	ON_SEND_CHAT("receive chat"),
	ON_CHAT_NOT_SENT("not sent chat"),
	
	ON_CHAT_JOIN_RECEIVED("chat join"),
	ON_CHAT_JOIN("receive chat join"),
	ON_CHAT_NOT_JOIN("not chat join"),

	ON_CHAT_LEAVE_RECEIVED("chat leave"),
	ON_CHAT_LEAVE("receive chat leave"),
	ON_CHAT_NOT_LEAVE("not chat leave"),
	
	ON_CHAT_ADD_RECEIVED("add to chat"),
	ON_CHAT_ADD("receive add to chat"),
	ON_CHAT_NOT_ADD("not add to chat"),
	
	ON_CHAT_THROW_OUT_RECEIVED("throw out from chat"),
	ON_CHAT_THROW_OUT("receive throw out from chat"),
	ON_CHAT_NOT_THROW_OUT("not throw out from chat"), 
	
	ON_FILE_RECEIVED ("send file"),
	ON_SEND_FILE("receive file"),
	ON_FILE__NOT_SENT("not send file");;
	


	public final String value;

	private SocketEvents(String value) {
		this.value = value;
	}
}