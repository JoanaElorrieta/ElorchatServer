package com.reto.elorchat.model.controller.request;

import java.util.List;

public class PendingMessagePostRequest {

	private List<MessagePostRequest> pendingMessages;

	public PendingMessagePostRequest() {
		super();
	}
	
	public PendingMessagePostRequest(List<MessagePostRequest> pendingMessages) {
		super();
		this.pendingMessages = pendingMessages;
	}

	public List<MessagePostRequest> getPendingMessages() {
		return pendingMessages;
	}

	public void setPendingMessages(List<MessagePostRequest> pendingMessages) {
		this.pendingMessages = pendingMessages;
	}
	
	
}
