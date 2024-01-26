package com.reto.elorchat.exception.chat;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus( code = HttpStatus.EXPECTATION_FAILED, reason = "Is not the admin")
public class IsNotTheGroupAdminException extends Exception{
	private static final long serialVersionUID = 1L;
	
	public IsNotTheGroupAdminException(String message) {
		super(message);
	}

}
