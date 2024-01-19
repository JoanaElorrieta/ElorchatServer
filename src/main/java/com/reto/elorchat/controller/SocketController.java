package com.reto.elorchat.controller;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.reto.elorchat.config.socketio.SocketEvents;
import com.reto.elorchat.config.socketio.SocketIOConfig;
import com.reto.elorchat.model.enums.MessageType;
import com.reto.elorchat.model.socket.MessageFromServer;
import com.reto.elorchat.model.socket.Room;

@RestController
@RequestMapping("/api/sockets")
public class SocketController {

	private final SocketIOServer socketIoServer;

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
				"Hola, clientes de Socket.IO!", 
				"Server", 
				0
				);

		socketIoServer.getBroadcastOperations().sendEvent(SocketEvents.ON_SEND_MESSAGE.value, message);

		return "Mensaje enviado";
	}

	// deberia ser un POST y con body, pero para probar desde el navegador...
	@PostMapping("/join-room")
	public String joinRoom(@RequestBody Room room) {

		SocketIOClient client = findClientByUserId(room.getUserId());
		if (client != null) {
			client.joinRoom(room.getName());

			System.out.println(client.getNamespace().getName() + "se unio a" + room);
			// se podria notificar a aquellos que estan en la 

			socketIoServer.getBroadcastOperations().sendEvent(SocketEvents.ON_ROOM_JOIN.value);

			socketIoServer.getRoomOperations(room.getName()).sendEvent(SocketEvents.ON_SEND_MESSAGE.value, "el usuario XXXXXX se ha unido a la sala " + room);
			//aunque lo interesante y lo que habra que hacer es notificarle a dicho cliente que ha accedido a la room

			return "Usuario unido a la sala";
		} else {
			return "Ese usuario no esta conectado";
		}

	}


	// deberia ser un POST y con body, pero para probar desde el navegador...
	@PostMapping("/leave-room")
			public String leaveRoom(@RequestBody Room room) {

				SocketIOClient client = findClientByUserId(room.getUserId());
				if (client != null) {
					System.out.println(client.getAllRooms().size());
					client.leaveRoom(room.getName());
					System.out.println(client.getAllRooms().size());
					// se podria notificar a aquellos que estan en la room
					socketIoServer.getBroadcastOperations().sendEvent(SocketEvents.ON_ROOM_LEFT.value);
					socketIoServer.getRoomOperations(room.getName()).sendEvent("chat message", "el usuario XXXXXX se ha ido de la sala " + room);
					// podriamos registrar distintos eventos, no "chat message" para estos casos

					// lo interesante y lo que habra que hacer es notificarle a dicho cliente que ha sido eliminado de la room
					return "Usuario expulsado de la sala";
				} else {
					return "Ese usuario no estaba conectado";
				}
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
}
