package com.reto.elorchat.exception.chat;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus( code = HttpStatus.NO_CONTENT, reason = "Chat does not exist")
public class ChatNotFoundException extends Exception{
	private static final long serialVersionUID = 1L;

	public ChatNotFoundException(String message) {
		super(message);
	}
}
