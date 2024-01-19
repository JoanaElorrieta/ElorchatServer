package com.reto.elorchat.exception.chat;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus( code = HttpStatus.NOT_ACCEPTABLE, reason = "Chat name already taken")
public class ChatNameAlreadyExists extends Exception{
	private static final long serialVersionUID = 1L;
	
	public ChatNameAlreadyExists(String message) {
		super(message);
	}
}
