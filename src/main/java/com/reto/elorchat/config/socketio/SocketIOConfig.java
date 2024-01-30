package com.reto.elorchat.config.socketio;

import java.sql.Timestamp;
import java.time.Instant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DataListener;
import com.corundumstudio.socketio.listener.DisconnectListener;
import com.reto.elorchat.model.enums.MessageType;
import com.reto.elorchat.model.service.ChatDTO;
import com.reto.elorchat.model.service.MessageDTO;
import com.reto.elorchat.model.service.UserDTO;
import com.reto.elorchat.model.socket.MessageFromClient;
import com.reto.elorchat.model.socket.MessageFromServer;
import com.reto.elorchat.model.socket.Room;
import com.reto.elorchat.security.configuration.JwtTokenUtil;
import com.reto.elorchat.security.service.UserService;
import com.reto.elorchat.service.ChatService;
import com.reto.elorchat.service.MessageService;

import io.netty.handler.codec.http.HttpHeaders;
import jakarta.annotation.PreDestroy;

@Configuration
public class SocketIOConfig {

	@Autowired
	JwtTokenUtil jwtUtil;

	@Autowired
	UserService userService;

	@Autowired
	ChatService chatService;


	@Autowired
	MessageService messageService;

	@Value("${socket-server.host}")
	private String host;

	@Value("${socket-server.port}")
	private Integer socketServerPort;
	@Value("${server.port}")
	private Integer webServerPort;

	private SocketIOServer server;

	public final static String CLIENT_USER_NAME_PARAM = "authorname";
	public final static String CLIENT_USER_ID_PARAM = "authorid";
	public final static String CLIENT_USER_PHOTO_PARAM = "authorPhoto";
	public final static String AUTHORIZATION_HEADER = "Authorization";

	@Bean
	public SocketIOServer socketIOServer() {
		com.corundumstudio.socketio.Configuration config = new com.corundumstudio.socketio.Configuration();
		config.setHostname(host);
		config.setPort(socketServerPort);

		// vamos a permitir a una web que no este en el mismo host y port conectarse. Si no da error de Cross Domain
		config.setAllowHeaders("Authorization");
		config.setOrigin("http://localhost:" + webServerPort);

		server = new SocketIOServer(config);

		server.addConnectListener(new MyConnectListener(server));
		server.addDisconnectListener(new MyDisconnectListener());
		server.addEventListener(SocketEvents.ON_MESSAGE_RECEIVED.value, MessageFromClient.class, onSendMessage());
		server.addEventListener(SocketEvents.ON_ROOM_JOIN.value, Room.class, onRoomJoin());
		server.addEventListener(SocketEvents.ON_ROOM_LEFT.value, Room.class, onRoomLeft());
		server.addEventListener(SocketEvents.ON_CHAT_ADDED.value, Room.class, onChatAdded());
		server.addEventListener(SocketEvents.ON_CHAT_THROW_OUT.value, Room.class, onChatThrowOut());
		server.start();

		return server;
	}

	//SINO QUITO EL STATIC ME DICE QUE JWT ES NULO
	private class MyConnectListener implements ConnectListener {

		private SocketIOServer server;

		MyConnectListener(SocketIOServer server) {
			this.server = server;
		}

		@Override
		public void onConnect(SocketIOClient client) {

			if (!isAllowedToConnect(client)){
				// FUERA
				System.out.println("Nuevo cliente no permitida la conexion: " + client.getSessionId());
				client.disconnect();
			} else {
				HttpHeaders headers = client.getHandshakeData().getHttpHeaders();
				loadClientData(headers, client);
				System.out.printf("Nuevo cliente conectado: %s . Clientes conectados ahora mismo: %d \n", client.getSessionId(), this.server.getAllClients().size());
				// aqui incluso se podria notificar a todos o a salas de que se ha conectado...
				// server.getBroadcastOperations().sendEvent("chat message", messageFromServer);
			}
		}

		//COMPROBAMOS SI EL CLIENTE CUMPLE LOS REQUISITOS PARA CONECTARSE
		private boolean isAllowedToConnect(SocketIOClient client) {

			HttpHeaders headers = client.getHandshakeData().getHttpHeaders();

			String authorization = headers.get(AUTHORIZATION_HEADER);
			String token = authorization.split(" ")[1].trim();

			boolean hasAuthorizationHeader = headers.get(AUTHORIZATION_HEADER) != null;

			boolean isTokenValid = jwtUtil.validateAccessToken(token);

			return hasAuthorizationHeader && isTokenValid;
		}

		private void loadClientData(HttpHeaders headers, SocketIOClient client) {

			try {
				String authorization = headers.get(AUTHORIZATION_HEADER);
				String token = authorization.split(" ")[1].trim();

				Integer userId = jwtUtil.getUserId(token);
				//ASK necesito hacer aqui otro converter? para que no dependa del modelo del service?
				UserDTO userDTO = userService.findById(userId);
				String authorId = userDTO.getId().toString();
				String authorName = userDTO.getName();
				String authorPhoto =  userDTO.getPhoto();

				client.set(CLIENT_USER_ID_PARAM, authorId);
				client.set(CLIENT_USER_NAME_PARAM, authorName);
				//client.set(CLIENT_USER_PHOTO_PARAM, authorPhoto);

				//ASK DEBO COMPROBAR SI YA ESTABA JOINEADO A UNA ROOM? O ESTA BIEN QUE SI EXISTE UNA NUEVA CONEXION CON EL SOCKET ME LO META OTRA VEZ A LA SALA?? SE PODRIA ENTENDER COMO UNA CONEX CON EL SOCKER
				//DESDE WHATSAPP WEB Y MOVIL POR LO TANTO ESTA BIEN?
				System.out.println(userDTO.getChats().size());
				for(ChatDTO chat: userDTO.getChats()) {			
					client.joinRoom(chat.getId().toString());
					System.out.println("Usuario " + authorName + " conectado a " + chat.getName() + " id: " + chat.getId());							
				}			
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private class MyDisconnectListener implements DisconnectListener {
		@Override
		public void onDisconnect(SocketIOClient client) {
			client.getNamespace().getAllClients().stream().forEach(data-> {
				System.out.println("user disconnected "+ data.getSessionId().toString());
				//FIXME
				// notificateDisconnectToUsers(client);
			});
			System.out.printf("Cliente restantes: %s . Clientes conectados ahora mismo: %d \n", client.getSessionId(), server.getAllClients().size());
		}

		// podemos notificar a los demas usuarios que ha salido. Ojo por que el broadcast envia a todos
		private void notificateDisconnectToUsers(SocketIOClient client) {
			Integer room = null;
			String message = "el usuario se ha desconectado salido";
			String authorIdS = client.get(CLIENT_USER_ID_PARAM);
			Integer authorId = Integer.valueOf(authorIdS);
			String authorName = client.get(CLIENT_USER_NAME_PARAM);

			MessageFromServer messageFromServer = new MessageFromServer(
					MessageType.SERVER, 
					room, 
					null,
					null,
					message, 
					authorName, 
					authorId,
					null,
					null
					);
			client.getNamespace().getBroadcastOperations().sendEvent(SocketEvents.ON_SEND_MESSAGE.value, messageFromServer);
		}
	}

	private DataListener<MessageFromClient> onSendMessage() {
		return (senderClient, data, acknowledge) -> {

			String authorIdS = senderClient.get(CLIENT_USER_ID_PARAM);
			Integer authorId = Integer.valueOf(authorIdS);
			String authorName = senderClient.get(CLIENT_USER_NAME_PARAM);

			System.out.printf("Mensaje recibido de (%d) %s. El mensaje es el siguiente: %s \n", authorId, authorName, data.toString());

			// TODO comprobar si el usuario esta en la room a la que quiere enviar...
			boolean isAllowedToSendToRoom = checkIfSendCanSendToRoom(senderClient, data.getRoom().toString());
			if (isAllowedToSendToRoom) {

				if (data.getMessage() == null || data.getMessage().trim().isEmpty()) {
					MessageFromServer errorMessage  = new MessageFromServer(
							MessageType.SERVER, 
							data.getRoom(), 
							null,
							null,
							"No puedes enviar un mensaje vacio", 
							"Server", 
							0,
							null,
							null
							);

					System.out.printf("Mensaje reenviado al usuario" + errorMessage);
					senderClient.sendEvent(SocketEvents.ON_MESSAGE_NOT_SENT.value, errorMessage);
					return;
				}

				MessageFromServer message = new MessageFromServer(
						MessageType.CLIENT,
						data.getRoom(), 
						data.getLocalId(),
						null,
						data.getMessage(), 
						authorName, 
						authorId,
						data.getSent(),	
						null
						);

				//ASK PORQUE ME SUMA 1 HORA MAS¿?¿? LOL
				//				System.out.println("HORA DEL OBJETO DE POSTMAN" + data.getSent());
				//				System.out.println("HORA DEL OBJETO CREADO PARA VOLVER A MANDAR A LOS DEMÁS" + message.getSent());

				ChatDTO chatDTO = chatService.findById(data.getRoom());
				// Get the current timestamp
				Instant currentInstant = Instant.now();
				// Convert Instant to Timestamp para obtener la date con la hora/min/sec
				Timestamp savedDate = Timestamp.from(currentInstant);

				Long sentValue = data.getSent();

				// Convert the long value to a Timestamp
				Timestamp sentTimestamp = Timestamp.from(Instant.ofEpochMilli(sentValue));

				MessageDTO messageDTO = new MessageDTO(null, data.getMessage(), sentTimestamp, savedDate, chatDTO.getId() , authorId);
				MessageDTO createdMessage = messageService.createMessage(messageDTO);

				//message.setMessageId(createdMessage.getId());
				message.setMessageId(data.getLocalId());
				//ASK es mejor estar pillando los que va creando la bbdd? igual si, no?
				message.setSaved(createdMessage.getSaved().getTime());
				System.out.println(createdMessage.getSaved().getTime());
				message.setSaved(createdMessage.getSaved().getTime());

				// enviamos a la room correspondiente:
				System.out.printf("Mensaje enviado a la room" + message);
				System.out.printf("A LAS " + createdMessage.getSent());
				server.getRoomOperations(data.getRoom().toString()).sendEvent(SocketEvents.ON_SEND_MESSAGE.value, message);

				// TODO esto es para mandar a todos los clientes. No para mandar a los de una Room
				// senderClient.getNamespace().getBroadcastOperations().sendEvent("chat message", message);

				// esto puede que veamos mas adelante
				// acknowledge.sendAckData("El mensaje se envio al destinatario satisfactoriamente");
			} else {
				// TODO
				// como minimo no dejar. se podria devolver un mensaje como MessageType.SERVER de que no puede enviar...
				// incluso ampliar la clase messageServer con otro enum de errores
				// o crear un evento nuevo, no "chat message" con otros datos
				//¿?¿?¿?¿?

				MessageFromServer errorMessage  = new MessageFromServer(
						MessageType.SERVER, 
						data.getRoom(), 
						null,
						null,
						"No puedes enviar a este grupo", 
						"Server", 
						0,
						null,
						null
						);
				System.out.printf("Mensaje reenviado al usuario" + errorMessage);
				senderClient.sendEvent(SocketEvents.ON_MESSAGE_NOT_SENT.value, errorMessage);
			}
		};
	}

	private boolean checkIfSendCanSendToRoom(SocketIOClient senderClient, String room) {
		if (senderClient.getAllRooms().contains(room)) {
			System.out.println("SI tiene permiso para enviar mensaje en la room");
			return true;
		} else {
			System.out.println("NO tiene permiso para enviar mensaje en la room");
			return false;
		}
	}

	private DataListener<Room> onRoomJoin() {
		return (senderClient, data, acknowledge) -> {
			System.out.println("ROOM JOIN");
			String authorIdS = senderClient.get(CLIENT_USER_ID_PARAM);
			Integer authorId = Integer.valueOf(authorIdS);
			String room = data.getRoomId().toString();
			String authorName = senderClient.get(CLIENT_USER_NAME_PARAM);

			System.out.println("USUARIOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO " + authorName + " CONECTADOOOOOOOO a " + room);							
			senderClient.joinRoom(room);

		};
	}

	private DataListener<Room> onRoomLeft() {
		return (senderClient, data, acknowledge) -> {
			System.out.println("ROOM LEFT");
			String authorIdS = senderClient.get(CLIENT_USER_ID_PARAM);
			Integer authorId = Integer.valueOf(authorIdS);
			String room = data.getRoomId().toString();
			String authorName = senderClient.get(CLIENT_USER_NAME_PARAM);

			System.out.println("USUARIOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO " + authorName + " se ha DESCONECTADOOOOOOOO de " + room);							
			senderClient.leaveRoom(room);
		};
	}

	private DataListener<Room> onChatThrowOut() {
		return (senderClient, data, acknowledge) -> {
		};
	}

	private DataListener<Room> onChatAdded() {
		return (senderClient, data, acknowledge) -> {
		};
	}


	@PreDestroy
	public void stopSocketIOServer() {
		this.server.stop();
	}

}