package com.reto.elorchat.exception.chat;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus( code = HttpStatus.FAILED_DEPENDENCY, reason = "User does not exist in Chat")
public class UserDoesNotExistOnChat extends Exception{
	private static final long serialVersionUID = 1L;

	public UserDoesNotExistOnChat(String message) {
		super(message);
	}
}

