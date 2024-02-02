package com.reto.elorchat.service;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import com.reto.elorchat.exception.message.MessageNotFoundException;
import com.reto.elorchat.model.service.MessageDTO;

public interface IMessageService {

	List<MessageDTO> findAllMessagesByChatId(Integer chatId) throws IOException;

	List<MessageDTO> findAll(Integer id) throws IOException;

	MessageDTO createMessage(MessageDTO messageDTO) throws IOException;
	
	public MessageDTO createBase64FileOnResourceFile(MessageDTO messageDTO) throws NoSuchAlgorithmException, IOException, MessageNotFoundException;

	MessageDTO updateMessage(MessageDTO messageDTO) throws MessageNotFoundException, IOException;
}
