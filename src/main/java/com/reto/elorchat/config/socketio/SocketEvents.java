package com.reto.elorchat.config.socketio;

public enum SocketEvents {

	ON_MESSAGE_RECEIVED("send message"),
	ON_SEND_MESSAGE("receive message"),
	ON_ROOM_LEFT("room left"),
	ON_ROOM_JOIN("room join"),
	ON_CHAT_ADDED("added to chat"),
	ON_CHAT_THROW_OUT("throw out from chat"),
	ON_MESSAGE_NOT_SENT("not sent message");

	public final String value;

	private SocketEvents(String value) {
		this.value = value;
	}
}