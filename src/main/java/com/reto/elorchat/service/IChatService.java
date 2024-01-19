package com.reto.elorchat.service;

import java.util.List;

import com.reto.elorchat.exception.chat.CantLeaveChatException;
import com.reto.elorchat.exception.chat.ChatNameAlreadyExists;
import com.reto.elorchat.exception.chat.ChatNotFoundException;
import com.reto.elorchat.exception.chat.UserAlreadyExistsOnChat;
import com.reto.elorchat.model.service.ChatDTO;

public interface IChatService {

	List<ChatDTO> findAll();

	ChatDTO findById(Integer id);

	ChatDTO findByName(String name);

	ChatDTO createChat(ChatDTO chatDTO) throws ChatNameAlreadyExists ;

	Integer canEnterUserChat(Integer idChat, Integer idUser);

	Integer canDeleteChat(Integer idChat, Integer idUser);

	boolean existsByIdAndUsers_Id(Integer idChat, Integer idUser);
	
	void addUserToChat(Integer idChat, Integer idUser) throws UserAlreadyExistsOnChat;
	
	void leaveChat(Integer idChat, Integer idUser) throws CantLeaveChatException;

	void deleteChat(Integer id) throws ChatNotFoundException;
}
