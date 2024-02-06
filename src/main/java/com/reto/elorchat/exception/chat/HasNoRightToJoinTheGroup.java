package com.reto.elorchat.exception.chat;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus( code = HttpStatus.MOVED_PERMANENTLY, reason = "Cant join that chat")
public class HasNoRightToJoinTheGroup extends Exception{

	private static final long serialVersionUID = 1L;

	public HasNoRightToJoinTheGroup(String message) {
		super(message);
	}
}
