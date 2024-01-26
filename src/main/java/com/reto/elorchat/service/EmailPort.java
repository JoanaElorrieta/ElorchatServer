package com.reto.elorchat.service;


public interface EmailPort {

	boolean sendEmailTool(String textMessage, String email, String subject);
}