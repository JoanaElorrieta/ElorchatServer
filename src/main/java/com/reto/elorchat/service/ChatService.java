package com.reto.elorchat.service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.reto.elorchat.exception.chat.CantLeaveChatException;
import com.reto.elorchat.exception.chat.ChatNameAlreadyExists;
import com.reto.elorchat.exception.chat.ChatNotFoundException;
import com.reto.elorchat.exception.chat.HasNoRightToCreatePrivateException;
import com.reto.elorchat.exception.chat.HasNoRightToJoinTheGroup;
import com.reto.elorchat.exception.chat.IsNotTheGroupAdminException;
import com.reto.elorchat.exception.chat.UserAlreadyExistsOnChat;
import com.reto.elorchat.exception.chat.UserDoesNotExistOnChat;
import com.reto.elorchat.model.persistence.Chat;
import com.reto.elorchat.model.persistence.Message;
import com.reto.elorchat.model.service.ChatDTO;
import com.reto.elorchat.model.service.MessageDTO;
import com.reto.elorchat.model.service.UserChatInfoDTO;
import com.reto.elorchat.model.service.UserDTO;
import com.reto.elorchat.repository.ChatRepository;
import com.reto.elorchat.security.persistance.User;
import com.reto.elorchat.security.repository.UserRepository;

//FIXME LLAMAR A LOS SERVICIOS EN VEZ DE REPOS
@Service
public class ChatService implements IChatService{

	@Autowired
	private ChatRepository chatRepository;

	//ESTA BIEN LLAMAR AQUI AL REPO O DEBERIA LLAMAR AL SERVICIO
	@Autowired
	private UserRepository userRepository;

	@Override
	public List<ChatDTO> findAll(Integer id, Integer idUser) {

		List<ChatDTO> response = new ArrayList<ChatDTO>();

		User user = userRepository.findById(idUser).orElseThrow(
				() -> new ResponseStatusException(HttpStatus.NO_CONTENT, "Usuario no encontrado")
				);

		UserDTO userDTO = convertFromUserDAOToDTO(user);

		List <Integer> userChatIds = new ArrayList<Integer>();

		for(ChatDTO chatDTO : userDTO.getChats()) {
			userChatIds.add(chatDTO.getId());
		}	

		if(id == 0) {
			Iterable<Chat> listChat = chatRepository.findAllChats();
			for(Chat actualChat: listChat) {
				response.add(convertFromChatDAOToDTO(actualChat));
			}
		}else {
			//OBTENER TODOS LOS CHATS PUBLICOS Y LOS PRIVADOS DE ESE USUARIO DESPUES DE LA ID DADA
			Iterable<Chat> listChat = chatRepository.findAllUserChatsCreatedAfterId(id);
			for(Chat actualChat: listChat) {
				response.add(convertFromChatDAOToDTO(actualChat));
			}
		}
		return response;
	}


	@Override
	public List<UserChatInfoDTO> insertPendingUserChatInfo(List<UserChatInfoDTO> listUserChatInfoDTO) throws CantLeaveChatException, IsNotTheGroupAdminException, UserDoesNotExistOnChat, ChatNotFoundException {
		List<UserChatInfoDTO> response = new ArrayList<UserChatInfoDTO>(); 

		for(UserChatInfoDTO userChatInfoDTO : listUserChatInfoDTO){
			UserChatInfoDTO userChatInfo = leaveChat(userChatInfoDTO.getChatId(), userChatInfoDTO.getUserId());
			response.add(userChatInfo);
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
	public ChatDTO findByName(String name) {

		Chat chat = chatRepository.findByName(name).orElseThrow(
				() -> new ResponseStatusException(HttpStatus.NO_CONTENT, "Chat no encontrado")
				);


		ChatDTO response = convertFromChatDAOToDTO(chat);
		return response;
	}

	//PREGUNTAR PQ EL getUsers() no me carga la lista que he metido en addUserToChat
	@Override
	//@Transactional
	public ChatDTO createChat(ChatDTO chatDTO) throws ChatNameAlreadyExists, HasNoRightToCreatePrivateException {

		User admin = userRepository.findByIdWithRoles(chatDTO.getAdminId()).orElseThrow(
				() -> new ResponseStatusException(HttpStatus.NO_CONTENT, "Administrador no encontrado")
				);

		// Get the current timestamp
		Instant currentInstant = Instant.now();
		// Convert Instant to Timestamp para obtener la date con la hora/min/sec
		Timestamp joinDate = Timestamp.from(currentInstant);

		// Convert Instant to Timestamp para obtener la date con la hora/min/sec
		Timestamp createdDate = Timestamp.from(currentInstant);

		//Timestamp deletedDate = Timestamp.from(Instant.ofEpochMilli(chatPostRequest.getDeleted()));

		if(chatRepository.existsByName(chatDTO.getName())) {			
			throw new ChatNameAlreadyExists("El chat con ese nombre ya existe");
		}else {
			//boolean isPrivate = checkIfGroupIsPrivate(chatDTO);
			if(chatDTO.isChatPrivate(chatDTO)){
				//boolean isProfessor = checkIfIsProfessor(admin);
				if(admin.isUserProffesor(admin)) {
					chatDTO.setCreated(createdDate);
					Chat chat = chatRepository.save(convertFromChatDTOToDAO(chatDTO, admin));
					if(chat != null){
						chatRepository.addUserToChat(chat.getId(), admin.getId(), joinDate);
					}
					ChatDTO response = convertFromChatDAOToDTO(chat);
					return response;	
				}else {
					throw new HasNoRightToCreatePrivateException("Has no right to create a private");
				}
			}else{
				chatDTO.setCreated(createdDate);
				Chat chat = chatRepository.save(convertFromChatDTOToDAO(chatDTO, admin));
				if(chat != null){
					chatRepository.addUserToChat(chat.getId(), admin.getId(),joinDate);
				}
				ChatDTO response = convertFromChatDAOToDTO(chat);
				return response;
			}
		}
	}

	@Override
	public ChatDTO deleteChat(Integer idChat,  Integer userId) throws ChatNotFoundException, IsNotTheGroupAdminException{
		Chat chat = chatRepository.findChatWithUsersById(idChat).orElseThrow(
				() -> new ResponseStatusException(HttpStatus.NO_CONTENT, "Chat no encontrado")
				);

		if(!chatRepository.isChatDeleted(idChat)) {
			// Get the current timestamp
			Instant currentInstant = Instant.now();
			// Convert Instant to Timestamp para obtener la date con la hora/min/sec
			Timestamp deleteDate = Timestamp.from(currentInstant);
			if(chat.getAdminId() != userId) {
				throw new IsNotTheGroupAdminException("Is not the chat Admin");
			}
			//Sofdeleteamos el chat
			Integer updated = chatRepository.updateDeleteById(idChat, deleteDate);
			//Despues de sotfdeletear el chat sofdeleteamos las relaciones
			chatRepository.deleteUserChatRelations(idChat, deleteDate);
			if(updated == 1) {
				//Obtenemos el chatsoftdeleteado para devolver la fecha de borrado
				Chat updatedChat = chatRepository.findByIdUpdatedChat(idChat).orElseThrow(
						() -> new ResponseStatusException(HttpStatus.NO_CONTENT, "Chat no encontrado")
						);
				System.out.println(updatedChat.getDeleted());
				ChatDTO response = convertFromChatDAOToDTO(updatedChat);
				//response.setDeleted(deleteDate);
				System.out.println(response.getDeleted());
				return response;	
			} else {
				System.out.println("NO SE ACTUALIZA Y DEVOLVEMOS EL CHAT");
				return convertFromChatDAOToDTO(chat);
			}
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
	public boolean existsOnChat(Integer idChat, Integer idUser) {
		// TODO Auto-generated method stub
		if(chatRepository.existsOnChat(idChat, idUser) > 0 ) {
			return true;
		}
		return false;
	}

	@Override
	public UserChatInfoDTO insertUserToChat(Integer idChat, Integer idUser) throws ChatNotFoundException {
		// Get the current timestamp
		Instant currentInstant = Instant.now();
		// Convert Instant to Timestamp para obtener la date con la hora/min/sec
		Timestamp joinDate = Timestamp.from(currentInstant);
		if(!chatRepository.isChatDeleted(idChat)){
			//VERIFICA SI EL USUARIO YA EXISTIA EN LA TABLA DE LA RELACION
			if(chatRepository.existsUserChatRelation(idChat, idUser) > 0) {
				//Simplemente borramos seteamos el deleted a null
				chatRepository.updateJoinDateInUserChat(idChat, idUser, joinDate);
			}else {				
				chatRepository.addUserToChat(idChat, idUser, joinDate);
			}
			UserChatInfoDTO userChatInfo = getUserChatInfoFromUser(idChat, idUser);
			return userChatInfo;
		}else {
			throw new ChatNotFoundException("El chat no existe");
		}
	}

	@Override
	public UserChatInfoDTO deleteUserFromChat(Integer idChat, Integer idUser) throws ChatNotFoundException {
		// Get the current timestamp
		Instant currentInstant = Instant.now();
		// Convert Instant to Timestamp para obtener la date con la hora/min/sec
		Timestamp deleteDate = Timestamp.from(currentInstant);

		if(!chatRepository.isChatDeleted(idChat)){		
			chatRepository.leaveChat(idChat, idUser, deleteDate);
			UserChatInfoDTO userChatInfo = getUserChatInfoFromUser(idChat, idUser);
			return userChatInfo;
		}else {
			throw new ChatNotFoundException("El chat no existe");
		}

	}

	@Override
	public UserChatInfoDTO addUserToChat(Integer idChat, Integer idUser, Integer idAdmin) throws UserAlreadyExistsOnChat, IsNotTheGroupAdminException, ChatNotFoundException, HasNoRightToJoinTheGroup{
		// TODO Auto-generated method stub

		Chat chat = chatRepository.findChatWithUsersById(idChat).orElseThrow(
				() -> new ResponseStatusException(HttpStatus.NO_CONTENT, "Chat no encontrado")
				);		

		//VERIFICA SI EL USUARIO YA EXISTE EN EL CHAT
		userExistsOnChat(idChat, idUser);
		//VERIFICA SI EL USUARIO ES EL ADMIN DEL CHAT
		if(chat.getAdminId() != idAdmin) {
			throw new IsNotTheGroupAdminException("Is not the chat Admin");
		}

		UserChatInfoDTO userChatInfo = insertUserToChat(idChat, idUser);
		return userChatInfo;

	}

	@Override
	public UserChatInfoDTO joinToChat(Integer idChat, Integer idUser) throws UserAlreadyExistsOnChat, HasNoRightToJoinTheGroup, ChatNotFoundException {

		Chat chat = chatRepository.findChatWithUsersById(idChat).orElseThrow(
				() -> new ResponseStatusException(HttpStatus.NO_CONTENT, "Chat no encontrado")
				);

		//VERIFICA SI EL USUARIO YA EXISTE EN EL CHAT
		userExistsOnChat(idChat, idUser);
		//boolean isPrivate = checkIfGroupIsPrivate(convertFromChatDAOToDTO(chat));
		if (chat.isChatPrivate(chat)) {
			throw new HasNoRightToJoinTheGroup("Cant Join a Private Group");
		}

		UserChatInfoDTO userChatInfo = insertUserToChat(idChat, idUser);
		return userChatInfo;
	}

	@Override
	public UserChatInfoDTO throwFromChat(Integer idChat, Integer idUser, Integer idAdmin) throws IsNotTheGroupAdminException, UserDoesNotExistOnChat, CantLeaveChatException, ChatNotFoundException {

		Chat chat = chatRepository.findChatWithUsersById(idChat).orElseThrow(
				() -> new ResponseStatusException(HttpStatus.NO_CONTENT, "Chat no encontrado")
				);

		if(chat.getAdminId() != idAdmin) {
			throw new IsNotTheGroupAdminException("Is not the chat Admin");
		}
		userDoesNotExistOnChat(idChat, idUser);
		if(idUser == chat.getAdminId()) {
			throw new CantLeaveChatException("You cant throw out the admin");
		}	

		UserChatInfoDTO userChatInfo = deleteUserFromChat(idChat, idUser);
		return userChatInfo;
	}

	@Override
	public UserChatInfoDTO leaveChat(Integer idChat, Integer idUser) throws CantLeaveChatException, UserDoesNotExistOnChat, ChatNotFoundException{

		Chat chat = chatRepository.findChatWithUsersById(idChat).orElseThrow(
				() -> new ResponseStatusException(HttpStatus.NO_CONTENT, "Chat no encontrado")
				);

		userDoesNotExistOnChat(idChat, idUser);

		//boolean isPrivate = checkIfGroupIsPrivate(convertFromChatDAOToDTO(chat));

		if(chat.isChatPrivate(chat)) {
			throw new CantLeaveChatException("Cant Leave a Private Group");
		}
		if(idUser == chat.getAdminId()) {
			throw new CantLeaveChatException("Admin Cant Leave the Group");
		}	
		UserChatInfoDTO userChatInfo = deleteUserFromChat(idChat, idUser);
		return userChatInfo;
	}

	//CONVERTS
	//---------------------------------------
	private UserDTO convertFromUserDAOToDTO(User user) {
		UserDTO response = new UserDTO(
				user.getId(),
				user.getName(),
				user.getSurname(),
				user.getEmail(),
				user.getPhoneNumber1(),
				user.getPhoto());

		if (user.getChats() != null) {
			List<ChatDTO> chatList = new ArrayList<ChatDTO>();
			for(Chat chat: user.getChats()) {
				chatList.add(convertFromChatDAOToDTO(chat));
			}
			response.setChats(chatList);
		}

		return response;
	}

	private ChatDTO convertFromChatDAOToDTO(Chat chat) {

		ChatDTO response = new ChatDTO(
				chat.getId(),
				chat.getName(),
				chat.getType(),
				chat.getAdminId(),
				chat.getCreated(),
				chat.getDeleted()
				);
		return response;
	}


	//	private MessageDTO convertFromMessageDAOToDTO(Message message) {
	//
	//		MessageDTO response = new MessageDTO(
	//				message.getId(),
	//				message.getText(),
	//				message.getDate()
	//				);
	//		return response;
	//	}
	//
	//	private UserDTO convertFromUserDAOToDTO(User user) {
	//		UserDTO response = new UserDTO(
	//				user.getId(),
	//				user.getName(),
	//				user.getSurname(),
	//				user.getEmail(),
	//				user.getPhoneNumber1(),
	//				user.getPhoto());
	//
	//		return response;
	//	}

	private Chat convertFromChatDTOToDAO(ChatDTO chatDTO, User admin) {

		Chat response = new Chat(
				chatDTO.getId(),
				chatDTO.getName(),
				chatDTO.getType(),
				chatDTO.getAdminId(),
				chatDTO.getCreated(),
				chatDTO.getDeleted()
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
				messageDTO.getSent(),
				messageDTO.getSaved(),
				messageDTO.getTextType()
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

	private void userExistsOnChat(Integer idChat, Integer idUser) throws UserAlreadyExistsOnChat {
		if(chatRepository.isUserAlreadyOnChat(idChat, idUser) > 0) {	
			throw new UserAlreadyExistsOnChat("User already exists on Chat");
		}
	}

	private void userDoesNotExistOnChat(Integer idChat, Integer idUser) throws UserDoesNotExistOnChat {
		if(chatRepository.isDeletedUserChat(idChat, idUser) > 0) {		
			throw new UserDoesNotExistOnChat("User does not exist on Chat");
		}
	}

	private UserChatInfoDTO getUserChatInfoFromUser(Integer idChat, Integer idUser) {
		return userRepository.findUsersJoinedAndDeletedFromChat(idChat, idUser)
				.orElseThrow(() -> new UsernameNotFoundException("User " + idUser + " not found"));

	}
}
