package com.reto.elorchat.service;

import java.util.List;

import com.reto.elorchat.exception.chat.ChatNotFoundException;
import com.reto.elorchat.model.service.ChatDTO;

public interface IChatService {
	
	List<ChatDTO> findAll();

	ChatDTO findById(Integer id, List<ChatDTO> expand);

	ChatDTO createChat(ChatDTO chatDTO);

	void deleteChat(Integer id) throws ChatNotFoundException;
}
