package com.reto.elorchat.model.controller.request;

public class EmailRequest {
	private String email;
	private String content;
	private String subject;
	
	
	public EmailRequest() {
	}
	
	public String getEmail() {
		return "elorchat.noreply@gmail.com";
	}
	
	public String getContent() {
		return "Tu nueva contraseña es "+getPassword();
	}
	
	public String getSubject() {
		return "Elorchat-noreply";
	}
	
	public String getPassword() {
		PasswordGenerator passwordGenerator = new PasswordGenerator();
        return passwordGenerator.getPassword();
	}
	
	public void setSubject(String subject) {
		this.subject = subject;
	}
	
	@Override
	public String toString() {
		return "EmailRequest [email=" + email + ", content=" + content + ", subject=" + subject +"]";
	}

}