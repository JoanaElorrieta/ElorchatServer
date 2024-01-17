package com.reto.elorchat.exception.chat;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus( code = HttpStatus.NOT_ACCEPTABLE, reason = "User already in Chat")
public class UserAlreadyExistsOnChat extends Exception{
	private static final long serialVersionUID = 1L;
	public UserAlreadyExistsOnChat(String message) {
		super(message);
	}
}
