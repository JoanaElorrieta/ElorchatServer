package com.reto.elorchat.service;

import com.reto.elorchat.model.controller.request.EmailRequest;

public interface EmailPort {
	public boolean sendEmail(EmailRequest emailBody);
}