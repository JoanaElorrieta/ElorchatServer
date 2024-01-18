package com.reto.elorchat.service;

import java.util.List;

import com.reto.elorchat.model.persistence.Message;
import com.reto.elorchat.model.service.MessageDTO;

public interface IMessageService {

	List<MessageDTO> findAllMessageByChatId(Integer chatId);

	Iterable<Message> findAll();

	MessageDTO createMessage(MessageDTO message);

}
