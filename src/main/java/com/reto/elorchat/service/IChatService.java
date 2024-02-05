package com.reto.elorchat.service;

import java.util.List;

import com.reto.elorchat.exception.chat.CantLeaveChatException;
import com.reto.elorchat.exception.chat.ChatNameAlreadyExists;
import com.reto.elorchat.exception.chat.ChatNotFoundException;
import com.reto.elorchat.exception.chat.HasNoRightToCreatePrivateException;
import com.reto.elorchat.exception.chat.IsNotTheGroupAdminException;
import com.reto.elorchat.exception.chat.UserAlreadyExistsOnChat;
import com.reto.elorchat.exception.chat.UserDoesNotExistOnChat;
import com.reto.elorchat.model.service.ChatDTO;
import com.reto.elorchat.model.service.UserChatInfoDTO;

public interface IChatService {

	List<ChatDTO> findAll(Integer id, Integer idUser);
	
	//List<ChatDTO> getAllChatsOfUser(Integer userId);

	ChatDTO findById(Integer id);

	ChatDTO findByName(String name);

	ChatDTO createChat(ChatDTO chatDTO) throws ChatNameAlreadyExists, HasNoRightToCreatePrivateException;

	Integer canEnterUserChat(Integer idChat, Integer idUser);

	Integer canDeleteChat(Integer idChat, Integer idUser);

	boolean existsOnChat(Integer idChat, Integer idUser);

	UserChatInfoDTO addUserToChat(Integer idChat, Integer idUser, Integer idAdmin) throws UserAlreadyExistsOnChat, IsNotTheGroupAdminException;

	UserChatInfoDTO leaveChat(Integer idChat, Integer idUser, Integer idAdmin) throws CantLeaveChatException, IsNotTheGroupAdminException, UserDoesNotExistOnChat;

	void deleteChat(Integer id) throws ChatNotFoundException;
}
