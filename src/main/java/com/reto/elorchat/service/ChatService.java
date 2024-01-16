package com.reto.elorchat.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.reto.elorchat.exception.chat.ChatNotFoundException;
import com.reto.elorchat.model.persistence.Chat;
import com.reto.elorchat.model.service.ChatDTO;
import com.reto.elorchat.repository.ChatRepository;
import com.reto.elorchat.security.persistance.User;
import com.reto.elorchat.security.repository.UserRepository;

@Service
public class ChatService implements IChatService{

	@Autowired
	private ChatRepository chatRepository;

	@Autowired
	private UserRepository userRepository;

	@Override
	public List<ChatDTO> findAll() {
		Iterable<Chat> listChat = chatRepository.findAll();

		List<ChatDTO> response = new ArrayList<ChatDTO>();

		for(Chat chat: listChat) {
			response.add(convertFromChatDAOToDTO(chat));
		}

		return response;
	}

	@Override
	public ChatDTO findById(Integer id) {

		Chat chat = chatRepository.findById(id).orElseThrow(
				() -> new ResponseStatusException(HttpStatus.NO_CONTENT, "Chat no encontrado")
				);


		ChatDTO response = convertFromChatDAOToDTO(chat);
		return response;
	}

	@Override
	public ChatDTO createChat(ChatDTO chatDTO) {

		User admin = userRepository.findById(chatDTO.getAdminId()).orElseThrow(
				() -> new ResponseStatusException(HttpStatus.NO_CONTENT, "Administrador no encontrado")
				);

		Chat chat = chatRepository.save(convertFromChatDTOToDAO(chatDTO, admin));
		ChatDTO response = convertFromChatDAOToDTO(chat);
		return response;
	}

	@Override
	public void deleteChat(Integer id) throws ChatNotFoundException{
		if(chatRepository.existsById(id)) {
			chatRepository.deleteById(id);
		}else {
			throw new ChatNotFoundException("El departamento no existe");
		}
	}

	@Override
	public Integer countByUsers_IdAndId(Integer idChat, Integer IdUser) {
		// TODO Auto-generated method stub
		return chatRepository.countByUsers_IdAndId(idChat, IdUser);
	}

	@Override
	public Integer countByIdAndAdminId(Integer idChat, Integer IdUser) {
		// TODO Auto-generated method stub
		return chatRepository.countByIdAndAdminId(idChat, IdUser);
	}

	@Override
	public void deleteByUsers_IdAndId(Integer idChat, Integer IdUser) {
		// TODO VERIFICAR SI SE HA BORRADO
		chatRepository.deleteByUsers_IdAndId(idChat, IdUser);
	}

	@Override
	public boolean existsByIdAndUsers_Id(Integer idChat, Integer IdUser) {
		// TODO Auto-generated method stub
		return chatRepository.existsByIdAndUsers_Id(idChat, IdUser);
	}
	//CONVERTS
	//---------------------------------------
	private ChatDTO convertFromChatDAOToDTO(Chat chat) {

		ChatDTO response = new ChatDTO(
				chat.getId(),
				chat.getName(),
				chat.getType(),
				chat.getAdminId()
				);
		return response;
	}


	private Chat convertFromChatDTOToDAO(ChatDTO chatDTO, User admin) {

		Chat response = new Chat(
				chatDTO.getId(),
				chatDTO.getName(),
				chatDTO.getType(),
				chatDTO.getAdminId()
				);
		response.setAdmin(admin);
		return response;
	}
	//---------------------------------------
}
