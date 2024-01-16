package com.reto.elorchat.service;

import java.util.List;

import com.reto.elorchat.exception.chat.ChatNotFoundException;
import com.reto.elorchat.model.service.ChatDTO;

public interface IChatService {
	
	List<ChatDTO> findAll();

	ChatDTO findById(Integer id);

	ChatDTO createChat(ChatDTO chatDTO);

	Integer countByUsers_IdAndId(Integer idChat, Integer IdUser);

	Integer countByIdAndAdminId(Integer idChat, Integer IdUser);

	void deleteByUsers_IdAndId(Integer idChat, Integer IdUser);

	boolean existsByIdAndUsers_Id(Integer idChat, Integer IdUser);
	
	void deleteChat(Integer id) throws ChatNotFoundException;
}
