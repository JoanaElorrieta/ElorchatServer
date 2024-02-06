package com.reto.elorchat.model.controller.request;

import java.util.List;

public class PendingUserChatPostRequest {

	private List<UserChatInfoPostRequest> pendingUserChat;

	public PendingUserChatPostRequest() {
		super();
	}

	public PendingUserChatPostRequest(List<UserChatInfoPostRequest> pendingUserChatInfo) {
		super();
		this.pendingUserChat = pendingUserChatInfo;
	}

	public List<UserChatInfoPostRequest> getPendingUserChat() {
		return pendingUserChat;
	}

	public void setPendingUserChat(List<UserChatInfoPostRequest> pendingUserChat) {
		this.pendingUserChat = pendingUserChat;
	}
}
