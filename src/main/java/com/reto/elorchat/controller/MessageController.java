package com.reto.elorchat.controller;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.corundumstudio.socketio.SocketIOServer;
import com.reto.elorchat.config.socketio.SocketEvents;
import com.reto.elorchat.exception.message.MessageNotFoundException;
import com.reto.elorchat.model.controller.request.MessagePostRequest;
import com.reto.elorchat.model.controller.response.MessageGetResponse;
import com.reto.elorchat.model.enums.MessageType;
import com.reto.elorchat.model.service.ChatDTO;
import com.reto.elorchat.model.service.MessageDTO;
import com.reto.elorchat.model.service.UserDTO;
import com.reto.elorchat.model.socket.MessageFromServer;
import com.reto.elorchat.security.persistance.User;
import com.reto.elorchat.security.service.IUserService;
import com.reto.elorchat.service.IChatService;
import com.reto.elorchat.service.IMessageService;

@RestController
@RequestMapping("api/messages")
public class MessageController {
	@Autowired
	private IMessageService messageService;

	@Autowired
	private IChatService chatService;

	@Autowired
	private IUserService userService;

	@Autowired
	private SocketIOServer socketIoServer;

	@GetMapping("findAll/{id}")
	public ResponseEntity<List<MessageGetResponse>> getMessages(@PathVariable("id") Integer id, Authentication authentication) throws IOException{

		User user = (User) authentication.getPrincipal();

		List <MessageDTO> listMessageDTO = messageService.findAll(id, user.getId());
		List<MessageGetResponse> response = new ArrayList<MessageGetResponse>(); 

		//Transform every DTO from the list to GetResponse
		for(MessageDTO messageDTO: listMessageDTO) {
			response.add(convertFromMessageDTOToGetResponse(messageDTO));
		}
		return new ResponseEntity<List<MessageGetResponse>>(response,HttpStatus.OK);
	}

	@GetMapping("/chat/{chatId}")
	public ResponseEntity<List<MessageGetResponse>> getMessagesByChatId(@PathVariable("chatId") Integer chatId) throws IOException{
		List <MessageDTO> listMessageDTO = messageService.findAllMessagesByChatId(chatId);
		List<MessageGetResponse> response = new ArrayList<MessageGetResponse>(); 

		//Transform every DTO from the list to GetResponse
		for(MessageDTO messageDTO: listMessageDTO) {
			response.add(convertFromMessageDTOToGetResponse(messageDTO));
		}
		return new ResponseEntity<List<MessageGetResponse>>(response ,HttpStatus.OK);
	}

	@PostMapping("/pendingMessages")
	public ResponseEntity<List<MessageGetResponse>> pendingMessages(@RequestBody List <MessagePostRequest> pendingMessagePostRequest) throws IOException, NoSuchAlgorithmException, MessageNotFoundException{

		List<MessageGetResponse> response = new ArrayList<MessageGetResponse>(); 

		for (MessagePostRequest messagePostRequest : pendingMessagePostRequest) {
			MessageDTO createdMessage;

			if (messagePostRequest.isTypeText(messagePostRequest)) {
				createdMessage = messageService.createMessage(convertFromMessagePostRequestToDTO(messagePostRequest));
			} else {
				createdMessage = messageService.createBase64FileOnResourceFile(convertFromMessagePostRequestToDTO(messagePostRequest));
			}

			UserDTO userDTO = userService.findById(messagePostRequest.getUserId());
			
			sendEvent(createdMessage, messagePostRequest, userDTO);

			response.add(convertFromMessageDTOToGetResponse(createdMessage));
		}
		return new ResponseEntity<List<MessageGetResponse>>(response ,HttpStatus.OK);
	}


	//CONVERTS
	private MessageGetResponse convertFromMessageDTOToGetResponse(MessageDTO messageDTO) {
		Long sentInMillis = messageDTO.getSent().getTime();
		Long savedInMillis2 = messageDTO.getSaved().getTime();

		MessageGetResponse response = new MessageGetResponse(
				messageDTO.getId(), 
				messageDTO.getText(),
				sentInMillis,
				savedInMillis2,
				messageDTO.getChatId(),
				messageDTO.getUserId(),
				messageDTO.getTextType());
		return response;
	}

	private MessageDTO convertFromMessagePostRequestToDTO(MessagePostRequest messagePostRequest) {

		// Get the current timestamp
		Instant currentInstant = Instant.now();
		// Convert Instant to Timestamp para obtener la date con la hora/min/sec
		Timestamp savedDate = Timestamp.from(currentInstant);

		Long sentValue = messagePostRequest.getSent();

		ChatDTO chatDTO = chatService.findById(messagePostRequest.getRoom());

		// Convert the long value to a Timestamp
		Timestamp sentDate = Timestamp.from(Instant.ofEpochMilli(sentValue));

		MessageDTO response = new MessageDTO(
				null, 
				messagePostRequest.getMessage(),
				sentDate,
				savedDate,
				chatDTO.getId(),
				messagePostRequest.getUserId(),
				messagePostRequest.getType());

		return response;	
	}
	//////
	private void sendEvent(MessageDTO createdMessage, MessagePostRequest messagePostRequest, UserDTO userDTO) {
		MessageFromServer message = new MessageFromServer(
				MessageType.CLIENT,
				messagePostRequest.getRoom(), 
				null,
				messagePostRequest.getLocalId(),
				messagePostRequest.getMessage(), 
				userDTO.getName(), 
				userDTO.getId(),
				messagePostRequest.getSent(),   
				null,
				messagePostRequest.getType()
				);

		if (createdMessage != null) {
			message.setMessageServerId(createdMessage.getId());
			message.setSaved(createdMessage.getSaved().getTime());
			message.setSaved(createdMessage.getSaved().getTime());
		}
		socketIoServer.getRoomOperations(createdMessage.getChatId().toString()).sendEvent(SocketEvents.ON_SEND_MESSAGE.value, message);	
	}
}
