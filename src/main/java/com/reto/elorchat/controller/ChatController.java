package com.reto.elorchat.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.reto.elorchat.config.socketio.SocketEvents;
import com.reto.elorchat.config.socketio.SocketIOConfig;
import com.reto.elorchat.exception.chat.CantLeaveChatException;
import com.reto.elorchat.exception.chat.ChatNameAlreadyExists;
import com.reto.elorchat.exception.chat.ChatNotFoundException;
import com.reto.elorchat.exception.chat.HasNoRightToCreatePrivateException;
import com.reto.elorchat.exception.chat.HasNoRightToJoinTheGroup;
import com.reto.elorchat.exception.chat.IsNotTheGroupAdminException;
import com.reto.elorchat.exception.chat.UserAlreadyExistsOnChat;
import com.reto.elorchat.exception.chat.UserDoesNotExistOnChat;
import com.reto.elorchat.model.controller.request.ChatPostRequest;
import com.reto.elorchat.model.controller.request.UserChatInfoPostRequest;
import com.reto.elorchat.model.controller.response.ChatGetResponse;
import com.reto.elorchat.model.controller.response.ChatPostResponse;
import com.reto.elorchat.model.controller.response.UserChatInfoGetResponse;
import com.reto.elorchat.model.controller.response.UserGetResponse;
import com.reto.elorchat.model.service.ChatDTO;
import com.reto.elorchat.model.service.UserChatInfoDTO;
import com.reto.elorchat.model.service.UserDTO;
import com.reto.elorchat.model.socket.ChatFromServer;
import com.reto.elorchat.model.socket.ChatUserFromServer;
import com.reto.elorchat.security.persistance.User;
import com.reto.elorchat.security.service.IUserService;
import com.reto.elorchat.service.IChatService;

@RestController
@RequestMapping("api/chats")
public class ChatController {

	@Autowired
	private IChatService chatService;

	@Autowired
	private IUserService userService;

	@Autowired
	private SocketIOServer socketIoServer;

	//	@Autowired
	//	public ChatController(SocketIOServer socketIoServer) {
	//		this.socketIoServer = socketIoServer;
	//	}

	@GetMapping("findAll/{id}")
	public ResponseEntity<List<ChatGetResponse>> getChats(@PathVariable("id") Integer id, Authentication authentication){
		User user = (User) authentication.getPrincipal();
		List<ChatDTO> listChatDTO = chatService.findAll(id, user.getId());
		List<ChatGetResponse> response = new ArrayList<ChatGetResponse>(); 
		//Transform every DTO from the list to GetResponse
		for(ChatDTO chatDTO: listChatDTO) {
			response.add(convertFromChatDTOToGetResponse(chatDTO));
		}
		return new ResponseEntity<List<ChatGetResponse>>(response ,HttpStatus.OK);
	}

	@GetMapping("/{id}")
	public ResponseEntity<ChatGetResponse> getChatById(@PathVariable("id") Integer id) throws ChatNotFoundException{
		ChatDTO chatDTO = chatService.findById(id);
		ChatGetResponse response = convertFromChatDTOToGetResponse(chatDTO);
		return new ResponseEntity<ChatGetResponse>(response, HttpStatus.OK);
	}

	@PostMapping
	public ResponseEntity<ChatPostResponse> createChat(@RequestBody ChatPostRequest chatPostRequest, Authentication authentication) throws ChatNameAlreadyExists, HasNoRightToCreatePrivateException{
		User user = (User) authentication.getPrincipal();
		ChatPostResponse response = convertFromChatDTOToPostResponse(chatService.createChat(convertFromChatPostRequestToDTO(chatPostRequest, user.getId())));
		ChatFromServer chatFromServer = new ChatFromServer(
				chatPostRequest.getId(),
				chatPostRequest.getName(), 
				chatPostRequest.getType(),
				response.getCreated(),
				response.getDeleted(),
				chatPostRequest.getAdminId()
				);
		SocketIOClient client = findClientByUserId(user.getId());
		if (client != null) {			
			client.joinRoom(response.getId().toString());
			client.getNamespace().getBroadcastOperations().sendEvent(SocketEvents.ON_SEND_CHAT.value, chatFromServer);
		}

		return new ResponseEntity<ChatPostResponse>(response, HttpStatus.CREATED);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<ChatGetResponse> deleteChatById(@PathVariable("id") Integer id) throws ChatNotFoundException{
		ChatGetResponse response = new ChatGetResponse();
		ChatDTO chatDTO = chatService.deleteChat(id);
		response = convertFromChatDTOToGetResponse(chatDTO);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@GetMapping("/entryPermission/{idChat}")
	public ResponseEntity<Integer> canEnterUserChat(@PathVariable Integer idChat,
			Authentication authentication) throws ChatNotFoundException{
		User user = (User) authentication.getPrincipal();
		Integer response = chatService.canEnterUserChat(idChat, user.getId());
		return new ResponseEntity<Integer>(response, HttpStatus.OK);
	}


	@GetMapping("/deletePermission/{idChat}")
	public ResponseEntity<Integer> countByIdAndAdminId(@PathVariable Integer idChat,
			Authentication authentication) throws ChatNotFoundException{
		User user = (User) authentication.getPrincipal();
		Integer response = chatService.canDeleteChat(idChat, user.getId());
		return new ResponseEntity<Integer>(response, HttpStatus.OK);
	}


	@GetMapping("/existsOnChat/{idChat}")
	public ResponseEntity<Integer> existsByIdAndUsers_Id(@PathVariable Integer idChat,
			Authentication authentication) throws ChatNotFoundException{
		Integer response; 
		User user = (User) authentication.getPrincipal();
		boolean boolValue = chatService.existsOnChat(idChat, user.getId());

		if (boolValue) {
			response = 1;
		}
		else {
			response = 0;
		}
		return new ResponseEntity<Integer>(response, HttpStatus.OK);
	}

	@PostMapping("/addUserToChat/{idChat}/{idUser}")
	public ResponseEntity<UserChatInfoGetResponse> addUserToChat(@PathVariable("idChat") Integer idChat, @PathVariable("idUser") Integer idUser, Authentication authentication) throws UserAlreadyExistsOnChat, IsNotTheGroupAdminException, ChatNotFoundException, HasNoRightToJoinTheGroup {

		User admin = (User) authentication.getPrincipal();

		UserChatInfoDTO userChatInfoDTO= chatService.addUserToChat(idChat, idUser, admin.getId()); 
		UserChatInfoGetResponse response = convertFromUserChatInfoDTOToGetResponse(userChatInfoDTO);

		UserDTO joiningUserDTO = userService.findById(idUser);
		UserGetResponse joiningUserGetResponse = convertFromUserDTOToGetResponse(joiningUserDTO);

		//TENGO QUE HACER ESTO SINO NO ME APARECE EL NUMERO DEL ADMIN
		UserDTO joiningAdminDTO = userService.findById(admin.getId());
		UserGetResponse joiningAdminGetResponse = convertFromUserDTOToGetResponse(joiningAdminDTO);

		ChatUserFromServer chatUserFromServer = new ChatUserFromServer(idChat, joiningUserGetResponse.getId(), joiningAdminGetResponse.getId(), joiningUserGetResponse.getName(), joiningAdminGetResponse.getName(), response.getJoined(), response.getDeleted());

		//PARA ENVIAR SOLO A LA ROOM
		socketIoServer.getRoomOperations(idChat.toString()).sendEvent(SocketEvents.ON_CHAT_ADD.value, chatUserFromServer);

		SocketIOClient client = findClientByUserId(idUser);
		if (client != null) {			
			client.joinRoom(idChat.toString());
			client.sendEvent(SocketEvents.ON_CHAT_ADD.value, chatUserFromServer);
		}
		return new ResponseEntity<UserChatInfoGetResponse>(response, HttpStatus.OK);

	}

	@PostMapping("/joinToChat/{idChat}")
	public ResponseEntity<UserChatInfoGetResponse> joinToChat(@PathVariable("idChat") Integer idChat, Authentication authentication) throws UserAlreadyExistsOnChat, IsNotTheGroupAdminException, ChatNotFoundException, HasNoRightToJoinTheGroup {

		User user = (User) authentication.getPrincipal();
		UserChatInfoDTO userChatInfoDTO= chatService.joinToChat(idChat, user.getId()); 
		UserChatInfoGetResponse response = convertFromUserChatInfoDTOToGetResponse(userChatInfoDTO);
		SocketIOClient client = findClientByUserId(user.getId());

		UserDTO joiningUserDTO = userService.findById(user.getId());
		UserGetResponse joiningUserGetResponse = convertFromUserDTOToGetResponse(joiningUserDTO);

		ChatUserFromServer chatUserFromServer = new ChatUserFromServer(idChat, joiningUserGetResponse.getId(), joiningUserGetResponse.getName(), response.getJoined(), response.getDeleted());
		socketIoServer.getRoomOperations(idChat.toString()).sendEvent(SocketEvents.ON_CHAT_JOIN.value, chatUserFromServer);

		if (client != null) {
			client.joinRoom(idChat.toString());		
			client.sendEvent(SocketEvents.ON_CHAT_JOIN.value, chatUserFromServer);
		}

		return new ResponseEntity<UserChatInfoGetResponse>(response, HttpStatus.OK);
	}

	private UserChatInfoGetResponse convertFromUserChatInfoDTOToGetResponse(UserChatInfoDTO userChatInfoDTO) {
		Long joinedInMillis = userChatInfoDTO.getJoined().getTime();

		UserChatInfoGetResponse response = new UserChatInfoGetResponse(
				userChatInfoDTO.getUserId(),
				userChatInfoDTO.getChatId(),
				joinedInMillis,
				null
				);		

		if(userChatInfoDTO.getDeleted() != null) {			
			Long deletedInMillis = userChatInfoDTO.getDeleted().getTime();
			response.setDeleted(deletedInMillis);
		}

		return response;
	}

	@DeleteMapping("/leaveChat/{idChat}")
	public ResponseEntity<UserChatInfoGetResponse> leaveChat(@PathVariable Integer idChat, Authentication authentication) throws CantLeaveChatException, IsNotTheGroupAdminException, UserDoesNotExistOnChat, ChatNotFoundException{

		User user = (User) authentication.getPrincipal();
		UserChatInfoDTO userChatInfoDTO = chatService.leaveChat(idChat, user.getId());
		UserChatInfoGetResponse response = convertFromUserChatInfoDTOToGetResponse(userChatInfoDTO);

		SocketIOClient client = findClientByUserId(user.getId());
		UserDTO joiningUserDTO = userService.findById(user.getId());
		UserGetResponse joiningUserGetResponse = convertFromUserDTOToGetResponse(joiningUserDTO);

		ChatUserFromServer chatUserFromServer = new ChatUserFromServer(idChat, joiningUserGetResponse.getId(), joiningUserGetResponse.getName(), response.getJoined(), response.getDeleted());

		//AQUI PODRIA PRIMERO MANDAR EL MENSAJE A TODA LA ROOM Y DESPUES SACAR AL CLIENTE PARA HACERLE LLEGAR EL EVENTO
		//PERO PREFIERO ASEGURARME DE SACARLO PRIMERO, AVISARLE, Y DESPUES AL RESTO
		if (client != null) {
			client.leaveRoom(idChat.toString());	
			client.sendEvent(SocketEvents.ON_CHAT_THROW_OUT.value, chatUserFromServer);
		}

		socketIoServer.getRoomOperations(idChat.toString()).sendEvent(SocketEvents.ON_CHAT_LEAVE.value, chatUserFromServer);
		return new ResponseEntity<UserChatInfoGetResponse>(response, HttpStatus.OK);
	}

	@DeleteMapping("/throwFromChat/{idChat}/{idUser}")
	public ResponseEntity<UserChatInfoGetResponse> throwFromChat(@PathVariable("idChat") Integer idChat, @PathVariable("idUser") Integer idUser, Authentication authentication) throws CantLeaveChatException, IsNotTheGroupAdminException, UserDoesNotExistOnChat, ChatNotFoundException{

		User admin = (User) authentication.getPrincipal();
		UserChatInfoDTO userChatInfoDTO = chatService.throwFromChat(idChat, idUser, admin.getId());
		UserChatInfoGetResponse response = convertFromUserChatInfoDTOToGetResponse(userChatInfoDTO);

		UserDTO joiningUserDTO = userService.findById(idUser);
		UserGetResponse joiningUserGetResponse = convertFromUserDTOToGetResponse(joiningUserDTO);

		//TENGO QUE HACER ESTO SINO NO ME APARECE EL NUMERO DEL ADMIN
		UserDTO joiningAdminDTO = userService.findById(admin.getId());
		UserGetResponse joiningAdminGetResponse = convertFromUserDTOToGetResponse(joiningAdminDTO);

		ChatUserFromServer chatUserFromServer = new ChatUserFromServer(idChat, joiningUserGetResponse.getId(), joiningAdminGetResponse.getId(), joiningUserGetResponse.getName() , joiningAdminGetResponse.getName(), response.getJoined(), response.getDeleted());

		//TODO QUITAR DE LA ROOM SI ESTA CONECTADO
		SocketIOClient client = findClientByUserId(idUser);
		if (client != null) {			
			client.leaveRoom(idChat.toString());
			client.sendEvent(SocketEvents.ON_CHAT_THROW_OUT.value, chatUserFromServer);
		}

		socketIoServer.getRoomOperations(idChat.toString()).sendEvent(SocketEvents.ON_CHAT_THROW_OUT.value, chatUserFromServer);
		return new ResponseEntity<UserChatInfoGetResponse>(response, HttpStatus.OK);
	}

	@PostMapping("/pendingUserChat")
	public ResponseEntity<List<UserChatInfoGetResponse>> pendingUserChat(@RequestBody List<UserChatInfoPostRequest> pendingUserChatPostRequest, Authentication authentication) throws IOException, CantLeaveChatException, IsNotTheGroupAdminException, UserDoesNotExistOnChat, ChatNotFoundException, UserAlreadyExistsOnChat, HasNoRightToJoinTheGroup{

		List<UserChatInfoGetResponse> response = new ArrayList<UserChatInfoGetResponse>(); 

		//Transform every DTO from the list to GetResponse
		for(UserChatInfoPostRequest userChatInfoPostRequest: pendingUserChatPostRequest) {
			UserChatInfoGetResponse deletedRelation = leaveChat(userChatInfoPostRequest.getChatId(), authentication).getBody();
			response.add(deletedRelation);
		}
		return new ResponseEntity<List<UserChatInfoGetResponse>>(response ,HttpStatus.OK);
	}

	//CONVERTS
	//---------------------------------------
	private ChatGetResponse convertFromChatDTOToGetResponse(ChatDTO chatDTO) {
		Long createdToMillis = chatDTO.getCreated().getTime();

		ChatGetResponse response = new ChatGetResponse(
				chatDTO.getId(),
				chatDTO.getName(),
				chatDTO.getType(),
				chatDTO.getAdminId(),
				createdToMillis
				);

		if(chatDTO.getDeleted() != null) {			
			Long deletedToMillis = chatDTO.getDeleted().getTime();
			response.setDeleted(deletedToMillis);
		}
		return response;
	}

	private ChatPostResponse convertFromChatDTOToPostResponse(ChatDTO chatDTO) {
		Long createdToMillis = chatDTO.getCreated().getTime();

		ChatPostResponse response = new ChatPostResponse(
				chatDTO.getId(),
				chatDTO.getName(),
				chatDTO.getType(),
				chatDTO.getAdminId(),
				createdToMillis
				);

		if(chatDTO.getDeleted() != null) {			
			Long deletedToMillis = chatDTO.getDeleted().getTime();
			response.setDeleted(deletedToMillis);
		}
		return response;
	}

	private ChatDTO convertFromChatPostRequestToDTO(ChatPostRequest chatPostRequest, Integer adminId) {

		ChatDTO response = new ChatDTO(
				chatPostRequest.getId(),
				chatPostRequest.getName(),
				chatPostRequest.getType(),
				adminId,
				null,
				null
				);

		return response;
	}

	private UserGetResponse convertFromUserDTOToGetResponse(UserDTO userDTO) {
		UserGetResponse response = new UserGetResponse(
				userDTO.getId(),
				userDTO.getName(),
				userDTO.getSurname(),
				userDTO.getEmail(),
				userDTO.getPhoneNumber1());
		return response;
	}

	/////

	private SocketIOClient findClientByUserId(Integer idUser) {
		SocketIOClient response = null;

		Collection<SocketIOClient> clients = socketIoServer.getAllClients();
		for (SocketIOClient client: clients) {
			Integer currentClientId = Integer.valueOf(client.get(SocketIOConfig.CLIENT_USER_ID_PARAM));
			if (currentClientId == idUser) {
				response = client;
				break;
			}
		}
		return response;
	}
}

