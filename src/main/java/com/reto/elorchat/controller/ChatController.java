package com.reto.elorchat.controller;

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
import com.reto.elorchat.exception.chat.IsNotTheGroupAdminException;
import com.reto.elorchat.exception.chat.UserAlreadyExistsOnChat;
import com.reto.elorchat.exception.chat.UserDoesNotExistOnChat;
import com.reto.elorchat.model.controller.request.ChatPostRequest;
import com.reto.elorchat.model.controller.response.ChatGetResponse;
import com.reto.elorchat.model.controller.response.ChatPostResponse;
import com.reto.elorchat.model.controller.response.UserGetResponse;
import com.reto.elorchat.model.service.ChatDTO;
import com.reto.elorchat.model.service.UserDTO;
import com.reto.elorchat.model.socket.Room;
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

	@GetMapping
	public ResponseEntity<List<ChatGetResponse>> getChats(){
		List<ChatDTO> listChatDTO = chatService.findAll();
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
		return new ResponseEntity<ChatPostResponse>(response, HttpStatus.CREATED);
	}


	@DeleteMapping("/{id}")
	public ResponseEntity<Integer> deleteChatById(@PathVariable("id") Integer id) throws ChatNotFoundException{
		chatService.deleteChat(id);
		return new ResponseEntity<>(HttpStatus.OK);
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
		boolean boolValue = chatService.existsByIdAndUsers_Id(idChat, user.getId());

		if (boolValue) {
			response = 1;
		}
		else {
			response = 0;
		}
		return new ResponseEntity<Integer>(response, HttpStatus.OK);
	}

	@PostMapping("/addToPrivateGroup/{idChat}/{idUser}")
	public ResponseEntity<Integer> addUserToPrivateChat(@PathVariable("idChat") Integer idChat, @PathVariable("idUser") Integer idUser, Authentication authentication) throws ChatNotFoundException, UserAlreadyExistsOnChat, IsNotTheGroupAdminException {

		User admin = (User) authentication.getPrincipal();
		chatService.addUserToChat(idChat, idUser, admin.getId()); 


		UserDTO joiningUserDTO = userService.findById(idUser);
		UserGetResponse joiningUserGetResponse = convertFromUserDTOToGetResponse(joiningUserDTO);

		//TENGO QUE HACER ESTO SINO NO ME APARECE EL NUMERO DEL ADMIN
		UserDTO joiningAdminDTO = userService.findById(admin.getId());
		UserGetResponse joiningAdminGetResponse = convertFromUserDTOToGetResponse(joiningAdminDTO);

		Room room = new Room(idChat, joiningUserGetResponse.getId(), joiningAdminGetResponse.getId(), joiningUserGetResponse.getName(), joiningAdminGetResponse.getName());

		socketIoServer.getBroadcastOperations().sendEvent(SocketEvents.ON_CHAT_ADDED.value, room);

		return new ResponseEntity<Integer>(HttpStatus.OK);

	}

	@PostMapping("/addToPublicGroup/{idChat}")
	public ResponseEntity<Integer> addUserToPublicChat(@PathVariable("idChat") Integer idChat, Authentication authentication) throws ChatNotFoundException, UserAlreadyExistsOnChat, IsNotTheGroupAdminException {

		User user = (User) authentication.getPrincipal();
		chatService.addUserToChat(idChat, null, user.getId()); 

		SocketIOClient client = findClientByUserId(user.getId());

		if (client != null) {
			UserDTO joiningUserDTO = userService.findById(user.getId());
			UserGetResponse joiningUserGetResponse = convertFromUserDTOToGetResponse(joiningUserDTO);

			Room room = new Room(idChat, joiningUserGetResponse.getId(), joiningUserGetResponse.getName());

			socketIoServer.getBroadcastOperations().sendEvent(SocketEvents.ON_ROOM_JOIN.value, room);

			client.joinRoom(idChat.toString());				
			return new ResponseEntity<Integer>(HttpStatus.OK);
		}else {
			return new ResponseEntity<Integer>(HttpStatus.NON_AUTHORITATIVE_INFORMATION);
		}
	}

	@DeleteMapping("/leaveChat/{idChat}")
	public ResponseEntity<Integer> leaveChat(@PathVariable Integer idChat, Authentication authentication) throws ChatNotFoundException, CantLeaveChatException, IsNotTheGroupAdminException, UserDoesNotExistOnChat{

		User user = (User) authentication.getPrincipal();
		chatService.leaveChat(idChat, null , user.getId());

		SocketIOClient client = findClientByUserId(user.getId());

		if (client != null) {
			UserDTO joiningUserDTO = userService.findById(user.getId());
			UserGetResponse joiningUserGetResponse = convertFromUserDTOToGetResponse(joiningUserDTO);

			Room room = new Room(idChat, joiningUserGetResponse.getId(), joiningUserGetResponse.getName());
			socketIoServer.getBroadcastOperations().sendEvent(SocketEvents.ON_ROOM_LEFT.value, room);

			client.leaveRoom(idChat.toString());				
			return new ResponseEntity<Integer>(HttpStatus.OK);
		}else {
			return new ResponseEntity<Integer>(HttpStatus.NON_AUTHORITATIVE_INFORMATION);
		}
	}

	@DeleteMapping("/throwFromChat/{idChat}/{idUser}")
	public ResponseEntity<Integer> throwFromChat(@PathVariable("idChat") Integer idChat, @PathVariable("idUser") Integer idUser, Authentication authentication) throws ChatNotFoundException, CantLeaveChatException, IsNotTheGroupAdminException, UserDoesNotExistOnChat{

		User admin = (User) authentication.getPrincipal();
		chatService.leaveChat(idChat, idUser, admin.getId());

		UserDTO joiningUserDTO = userService.findById(idUser);
		UserGetResponse joiningUserGetResponse = convertFromUserDTOToGetResponse(joiningUserDTO);

		//TENGO QUE HACER ESTO SINO NO ME APARECE EL NUMERO DEL ADMIN
		UserDTO joiningAdminDTO = userService.findById(admin.getId());
		UserGetResponse joiningAdminGetResponse = convertFromUserDTOToGetResponse(joiningAdminDTO);

		Room room = new Room(idChat, joiningUserGetResponse.getId(), joiningAdminGetResponse.getId(), joiningUserGetResponse.getName() , joiningAdminGetResponse.getName());
		socketIoServer.getBroadcastOperations().sendEvent(SocketEvents.ON_CHAT_THROW_OUT.value, room);

		return new ResponseEntity<Integer>( HttpStatus.OK);
	}

	//CONVERTS
	//---------------------------------------
	private ChatGetResponse convertFromChatDTOToGetResponse(ChatDTO chatDTO) {

		ChatGetResponse response = new ChatGetResponse(
				chatDTO.getId(),
				chatDTO.getName(),
				chatDTO.getType(),
				chatDTO.getAdminId()
				);

		//		if (chatDTO.getUsers() != null) {
		//			List<UserGetResponse> userList = new ArrayList<UserGetResponse>();
		//			for(UserDTO userDTO : chatDTO.getUsers()) {
		//				userList.add(convertFromUserDTOToGetResponse(userDTO));
		//			}
		//			response.setUsers(userList);
		//		}
		//		if (chatDTO.getMessages() != null) {
		//			List<MessageGetResponse> messageList = new ArrayList<MessageGetResponse>();
		//			for(MessageDTO messageDTO : chatDTO.getMessages()) {
		//				messageList.add(convertFromMessageDTOToGetResponse(messageDTO));
		//			}
		//			response.setMessages(messageList);
		//		}
		return response;
	}

	private ChatPostResponse convertFromChatDTOToPostResponse(ChatDTO chatDTO) {
		ChatPostResponse response = new ChatPostResponse(
				chatDTO.getId(),
				chatDTO.getName(),
				chatDTO.getType(),
				chatDTO.getAdminId()
				);

		//		if (chatDTO.getUsers() != null) {
		//			List<UserGetResponse> userList = new ArrayList<UserGetResponse>();
		//			for(UserDTO userDTO : chatDTO.getUsers()) {
		//				userList.add(convertFromUserDTOToGetResponse(userDTO));
		//			}
		//			response.setUsers(userList);
		//		}
		//		if (chatDTO.getMessages() != null) {
		//			List<MessageGetResponse> messageList = new ArrayList<MessageGetResponse>();
		//			for(MessageDTO messageDTO : chatDTO.getMessages()) {
		//				messageList.add(convertFromMessageDTOToGetResponse(messageDTO));
		//			}
		//			response.setMessages(messageList);
		//		}
		return response;
	}

	private ChatDTO convertFromChatPostRequestToDTO(ChatPostRequest chatPostRequest, Integer adminId) {

		ChatDTO response = new ChatDTO(
				chatPostRequest.getId(),
				chatPostRequest.getName(),
				chatPostRequest.getType(),
				adminId
				);
		return response;
	}

	//	private MessageGetResponse convertFromMessageDTOToGetResponse(MessageDTO messageDTO) {
	//		MessageGetResponse response = new MessageGetResponse(
	//				messageDTO.getId(),
	//				messageDTO.getText(),
	//				messageDTO.getDate()
	//				);
	//		return response;
	//	}
	//
	//
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

