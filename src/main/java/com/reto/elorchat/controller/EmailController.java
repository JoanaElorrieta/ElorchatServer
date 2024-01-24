package com.reto.elorchat.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.reto.elorchat.service.EmailPort;

@RestController
@RequestMapping("api/email")
public class EmailController {
	
	@Autowired
	private EmailPort emailPort;
	
	@PostMapping(value = "/send")
	@ResponseBody
	public boolean sendEmail()  {
		return emailPort.sendEmail();
	}
	
}