package com.reto.elorchat.service;

import java.util.List;

import com.reto.elorchat.model.service.MessageDTO;

public interface IMessageService {

	List<MessageDTO> findAllMessagesByChatId(Integer chatId);

	List<MessageDTO> findAll(Integer id);

	MessageDTO createMessage(MessageDTO messageDTO);
	
	public MessageDTO createBase64FileOnResourceFile(MessageDTO messageDTO);
}
