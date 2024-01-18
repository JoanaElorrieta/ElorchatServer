package com.reto.elorchat.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.reto.elorchat.model.persistence.Message;
import com.reto.elorchat.model.service.MessageDTO;
import com.reto.elorchat.repository.ChatRepository;
import com.reto.elorchat.repository.MessageRepository;

@Service
public class MessageService implements IMessageService{

	@Autowired
	MessageRepository messageRepository;

	@Autowired
	ChatRepository chatRepository;
	
	@Override
	public Iterable<Message> findAll() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public List<MessageDTO> findAllMessagesByChatId(Integer chatId) {
		
//		Chat chat = chatRepository.findById(chatId).orElseThrow(
//				() -> new ResponseStatusException(HttpStatus.NO_CONTENT, "Chat no encontrado")
//				);

		Iterable<Message> listMessage = messageRepository.findAllMessagesByChatId(chatId);

		List<MessageDTO> response = new ArrayList<MessageDTO>();

		for(Message message: listMessage) {
			response.add(convertFromMessageDAOToDTO(message));
		}

		return response;	
	}
	
	@Override
	public MessageDTO createMessage(MessageDTO message) {
		// TODO Auto-generated method stub
		return null;
	}


	//CONVERTS
	private MessageDTO convertFromMessageDAOToDTO(Message message) {
		// TODO Auto-generated method stub
		MessageDTO response = new MessageDTO(
				message.getId(), 
				message.getText(),
				message.getDate(),
				message.getChatId(),
				message.getUserId());
		return response;
	}
	////

}
