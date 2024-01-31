package com.reto.elorchat.controller;

import java.util.Collection;

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
import com.reto.elorchat.exception.chat.ChatNotFoundException;
import com.reto.elorchat.exception.chat.IsNotTheGroupAdminException;
import com.reto.elorchat.exception.chat.UserAlreadyExistsOnChat;
import com.reto.elorchat.exception.chat.UserDoesNotExistOnChat;
import com.reto.elorchat.model.controller.response.UserGetResponse;
import com.reto.elorchat.model.enums.MessageType;
import com.reto.elorchat.model.service.UserDTO;
import com.reto.elorchat.model.socket.MessageFromServer;
import com.reto.elorchat.model.socket.ChatUserFromServer;
import com.reto.elorchat.security.persistance.User;
import com.reto.elorchat.security.service.IUserService;
import com.reto.elorchat.service.IChatService;

@RestController
@RequestMapping("/api/sockets")
public class SocketController {

	private final SocketIOServer socketIoServer;

	@Autowired
	private IChatService chatService;

	@Autowired
	private IUserService userService;

	@Autowired
	public SocketController(SocketIOServer socketIoServer) {
		this.socketIoServer = socketIoServer;
	}

	@GetMapping("/send-message")
	public String sendMessage() {
		// Envia un mensaje a todos los clientes conectados


		MessageFromServer message = new MessageFromServer(
				MessageType.SERVER, 
				null, 
				null,
				null,
				"Hola, clientes de Socket.IO!", 
				"Server", 
				0,
				null, 
				null
				);
		//PARA ENVIAR SOLO A LA ROOM
		//socketIoServer.getRoomOperations(idChat.toString()).sendEvent(SocketEvents.ON_CHAT_ADDED.value, room);
		//socketIoServer.getBroadcastOperations().sendEvent(SocketEvents.ON_SEND_MESSAGE.value, message);

		return "Mensaje enviado";
	}

	//	// deberia ser un POST y con body, pero para probar desde el navegador...
	//	@PostMapping("/join-room")
	//	public String joinRoom(@RequestBody Room room) {
	//
	//		SocketIOClient client = findClientByUserId(room.getUserId());
	//		if (client != null) {
	//			client.joinRoom(room.getRoomId().toString());
	//
	//			System.out.println(client.getNamespace().getName() + "se unio a" + room);
	//			// se podria notificar a aquellos que estan en la 
	//
	//			//socketIoServer.getBroadcastOperations().sendEvent(SocketEvents.ON_CHAT_JOIN.value);
	//
	//			socketIoServer.getRoomOperations(room.getRoomId().toString()).sendEvent(SocketEvents.ON_SEND_MESSAGE.value, "el usuario XXXXXX se ha unido a la sala " + room);
	//			//aunque lo interesante y lo que habra que hacer es notificarle a dicho cliente que ha accedido a la room
	//
	//			return "Usuario unido a la sala";
	//		} else {
	//			return "Ese usuario no esta conectado";
	//		}
	//
	//	}
	//
	//
	//	// deberia ser un POST y con body, pero para probar desde el navegador...
	//	@PostMapping("/leave-room")
	//	public String leaveRoom(@RequestBody Room room) {
	//
	//		SocketIOClient client = findClientByUserId(room.getUserId());
	//		if (client != null) {
	//			System.out.println(client.getAllRooms().size());
	//			client.leaveRoom(room.getRoomId().toString());
	//			System.out.println(client.getAllRooms().size());
	//			// se podria notificar a aquellos que estan en la room
	//			//socketIoServer.getBroadcastOperations().sendEvent(SocketEvents.ON_CHAT_LEFT.value);
	//			socketIoServer.getRoomOperations(room.getRoomId().toString()).sendEvent("chat message", "el usuario XXXXXX se ha ido de la sala " + room);
	//			// podriamos registrar distintos eventos, no "chat message" para estos casos
	//
	//			// lo interesante y lo que habra que hacer es notificarle a dicho cliente que ha sido eliminado de la room
	//			return "Usuario expulsado de la sala";
	//		} else {
	//			return "Ese usuario no estaba conectado";
	//		}
	//	}

	@PostMapping("/createChat")
	public String createChat(@RequestBody ChatUserFromServer room) {

		SocketIOClient client = findClientByUserId(room.getUserId());
		if (client != null) {
			System.out.println(client.getAllRooms().size());
			client.leaveRoom(room.getRoomId().toString());
			System.out.println(client.getAllRooms().size());
			// se podria notificar a aquellos que estan en la room
			//socketIoServer.getBroadcastOperations().sendEvent(SocketEvents.ON_CHAT_LEFT.value);
			socketIoServer.getRoomOperations(room.getRoomId().toString()).sendEvent("chat message", "el usuario XXXXXX se ha ido de la sala " + room);
			// podriamos registrar distintos eventos, no "chat message" para estos casos

			// lo interesante y lo que habra que hacer es notificarle a dicho cliente que ha sido eliminado de la room
			return "Usuario expulsado de la sala";
		} else {
			return "Ese usuario no estaba conectado";
		}
	}

	@PostMapping("/addUserToChat/{idChat}/{idUser}")
	public ResponseEntity<Integer> addUserToChat(@PathVariable("idChat") Integer idChat, @PathVariable("idUser") Integer idUser, Authentication authentication) throws ChatNotFoundException, UserAlreadyExistsOnChat, IsNotTheGroupAdminException {

		User admin = (User) authentication.getPrincipal();
		chatService.addUserToChat(idChat, idUser, admin.getId()); 


		UserDTO joiningUserDTO = userService.findById(idUser);
		UserGetResponse joiningUserGetResponse = convertFromUserDTOToGetResponse(joiningUserDTO);

		//TENGO QUE HACER ESTO SINO NO ME APARECE EL NUMERO DEL ADMIN
		UserDTO joiningAdminDTO = userService.findById(admin.getId());
		UserGetResponse joiningAdminGetResponse = convertFromUserDTOToGetResponse(joiningAdminDTO);

		ChatUserFromServer room = new ChatUserFromServer(idChat, joiningUserGetResponse.getId(), joiningAdminGetResponse.getId(), joiningUserGetResponse.getName(), joiningAdminGetResponse.getName());

		//PARA ENVIAR SOLO A LA ROOM
		socketIoServer.getRoomOperations(idChat.toString()).sendEvent(SocketEvents.ON_CHAT_ADD.value, room);
		//socketIoServer.getBroadcastOperations().sendEvent(SocketEvents.ON_CHAT_ADDED.value, room);

		return new ResponseEntity<Integer>(HttpStatus.OK);

	}

	@DeleteMapping("/throwFromChat/{idChat}/{idUser}")
	public ResponseEntity<Integer> throwFromChat(@PathVariable("idChat") Integer idChat, @PathVariable("idUser") Integer idUser, Authentication authentication) throws ChatNotFoundException, CantLeaveChatException, IsNotTheGroupAdminException, UserDoesNotExistOnChat{

		User admin = (User) authentication.getPrincipal();
		chatService.leaveChat(idChat, idUser, admin.getId());

		UserDTO joiningUserDTO = userService.findById(idUser);
		UserGetResponse joiningUserGetResponse = convertFromUserDTOToGetResponse(joiningUserDTO);

		UserDTO joiningAdminDTO = userService.findById(admin.getId());
		UserGetResponse joiningAdminGetResponse = convertFromUserDTOToGetResponse(joiningAdminDTO);

		ChatUserFromServer room = new ChatUserFromServer(idChat, joiningUserGetResponse.getId(), joiningAdminGetResponse.getId(), joiningUserGetResponse.getName() , joiningAdminGetResponse.getName());
		//PARA ENVIAR SOLO A LA ROOM
		socketIoServer.getRoomOperations(idChat.toString()).sendEvent(SocketEvents.ON_CHAT_ADD.value, room);
		//socketIoServer.getBroadcastOperations().sendEvent(SocketEvents.ON_CHAT_JOIN.value, room);
		return new ResponseEntity<Integer>( HttpStatus.OK);
	}

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

	private UserGetResponse convertFromUserDTOToGetResponse(UserDTO userDTO) {
		UserGetResponse response = new UserGetResponse(
				userDTO.getId(),
				userDTO.getName(),
				userDTO.getSurname(),
				userDTO.getEmail(),
				userDTO.getPhoneNumber1());
		return response;
	}
}
