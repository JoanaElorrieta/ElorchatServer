package com.reto.elorchat.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.reto.elorchat.exception.chat.CantLeaveChatException;
import com.reto.elorchat.exception.chat.ChatNotFoundException;
import com.reto.elorchat.exception.chat.UserAlreadyExistsOnChat;
import com.reto.elorchat.model.persistence.Chat;
import com.reto.elorchat.model.persistence.Message;
import com.reto.elorchat.model.service.ChatDTO;
import com.reto.elorchat.model.service.MessageDTO;
import com.reto.elorchat.model.service.UserDTO;
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
		if(chat != null){
			chatRepository.addUserToChat(chat.getId(), admin.getId());
		}
		System.out.println(chat.getId());
		Chat chat2 = chatRepository.findById(chat.getId()).orElseThrow(
				() -> new ResponseStatusException(HttpStatus.NO_CONTENT, "Chat no encontrado")
				);
		System.out.println("Usuarios asociados después de la creación del chat: " + chat2.getUsers());
		ChatDTO response = convertFromChatDAOToDTO(chat);
		return response;
	}

	@Override
	public void deleteChat(Integer id) throws ChatNotFoundException{
		if(chatRepository.existsById(id)) {
			chatRepository.deleteById(id);
		}else {
			throw new ChatNotFoundException("El chat no existe");
		}
	}

	@Override
	public Integer canEnterUserChat(Integer idChat, Integer idUser) {
		// TODO Auto-generated method stub
		return chatRepository.canEnterUserChat(idChat, idUser);
	}

	@Override
	public Integer canDeleteChat(Integer idChat, Integer idUser) {
		// TODO Auto-generated method stub
		return chatRepository.canDeleteChat(idChat, idUser);
	}

	@Override
	public boolean existsByIdAndUsers_Id(Integer idChat, Integer idUser) {
		// TODO Auto-generated method stub
		return chatRepository.existsByIdAndUsers_Id(idChat, idUser);
	}

	@Override
	public void addUserToChat(Integer idChat, Integer idUser) throws UserAlreadyExistsOnChat{
		// TODO Auto-generated method stub

		Chat chat = chatRepository.findById(idChat).orElseThrow(
				() -> new ResponseStatusException(HttpStatus.NO_CONTENT, "Chat no encontrado")
				);
		for(User user : chat.getUsers()){
			if(user.getId() == idUser) {
				throw new UserAlreadyExistsOnChat("User already exists on Chat");
			}
		}
		chatRepository.addUserToChat(idChat, idUser);
	}

	@Override
	public void leaveChat(Integer idChat, Integer idUser) throws CantLeaveChatException{

		Chat chat = chatRepository.findById(idChat).orElseThrow(
				() -> new ResponseStatusException(HttpStatus.NO_CONTENT, "Chat no encontrado")
				);
		if(idUser == chat.getAdminId()) {
			throw new CantLeaveChatException("Admin Cant Leave the Group");
		}			
		chatRepository.leaveChat(idChat, idUser);
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

		if (chat.getUsers() != null) {
			List<UserDTO> userList = new ArrayList<UserDTO>();
			for(User user : chat.getUsers()) {
				userList.add(convertFromUserDAOToDTO(user));
			}
			response.setUsers(userList);
		}
		if (chat.getMessages() != null) {
			List<MessageDTO> messageList = new ArrayList<MessageDTO>();
			for(Message message : chat.getMessages()) {
				messageList.add(convertFromMessageDAOToDTO(message));
			}
			response.setMessages(messageList);
		}
		return response;
	}


	private MessageDTO convertFromMessageDAOToDTO(Message message) {

		MessageDTO response = new MessageDTO(
				message.getId(),
				message.getText(),
				message.getDate()
				);
		return response;
	}

	private UserDTO convertFromUserDAOToDTO(User user) {
		UserDTO response = new UserDTO(
				user.getId(),
				user.getName(),
				user.getSurname(),
				user.getEmail(),
				user.getPhoneNumber1(),
				user.getPhoto());

		return response;
	}

	private Chat convertFromChatDTOToDAO(ChatDTO chatDTO, User admin) {

		Chat response = new Chat(
				chatDTO.getId(),
				chatDTO.getName(),
				chatDTO.getType(),
				chatDTO.getAdminId()
				);
		if (chatDTO.getUsers() != null) {
			List<User> userList = new ArrayList<User>();
			for(UserDTO userDTO : chatDTO.getUsers()) {
				userList.add(convertFromUserDTOToDAO(userDTO));
			}
			response.setUsers(userList);
		}
		if (chatDTO.getMessages() != null) {
			List<Message> messageList = new ArrayList<Message>();
			for(MessageDTO messageDTO : chatDTO.getMessages()) {
				messageList.add(convertFromMessageDTOToDAO(messageDTO));
			}
			response.setMessages(messageList);
		}
		response.setAdmin(admin);
		return response;
	}
	private Message convertFromMessageDTOToDAO(MessageDTO messageDTO) {
		Message response = new Message(
				messageDTO.getId(),
				messageDTO.getText(),
				messageDTO.getDate()
				);
		return response;
	}


	private User convertFromUserDTOToDAO(UserDTO userDTO) {
		User response = new User(
				userDTO.getId(),
				userDTO.getName(),
				userDTO.getSurname(),
				userDTO.getEmail(),
				userDTO.getPhoneNumber1(),
				userDTO.getPhoto());
		return response;
	}
	//---------------------------------------

}
