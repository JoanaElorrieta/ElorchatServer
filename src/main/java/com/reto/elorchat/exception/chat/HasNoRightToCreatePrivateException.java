package com.reto.elorchat.exception.chat;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus( code = HttpStatus.NOT_ACCEPTABLE, reason = "Chat does not exist")
public class HasNoRightToCreatePrivateException extends Exception{
	private static final long serialVersionUID = 1L;
	
	public HasNoRightToCreatePrivateException(String message) {
		super(message);
	}
}
