package com.reto.elorchat.exception.chat;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus( code = HttpStatus.NOT_ACCEPTABLE, reason = "Cant Leave Chat")
public class CantLeaveChatException extends Exception{
	private static final long serialVersionUID = 1L;

	public CantLeaveChatException(String message) {
		super(message);
	}
}
