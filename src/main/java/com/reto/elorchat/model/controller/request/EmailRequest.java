package com.reto.elorchat.model.controller.request;

public class EmailRequest {
	private String email;
	private String content;
	private String subject;
	private String password;
	
	
	public EmailRequest() {
	}

	public String getEmail() {
		return "elorchat.noreply@gmail.com";
	}
	
	public String getContent() {
		return "Tu nueva contraseña es ";
	}
	
	public String getSubject() {
		return "Elorchat-noreply";
	}
	
	public String getPassword() {
		PasswordGenerator passwordGenerator = new PasswordGenerator();
		String password = passwordGenerator.getPassword();
		System.out.println("Este es el getter "+password);
        return password;
	}

	@Override
	public String toString() {
		return "EmailRequest [email=" + email + ", content=" + content + ", subject=" + subject + ", password="
				+ password + "]";
	}
	

}