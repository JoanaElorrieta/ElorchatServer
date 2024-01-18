package com.reto.elorchat.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.reto.elorchat.model.controller.request.MessagePostRequest;
import com.reto.elorchat.model.controller.response.MessageGetResponse;
import com.reto.elorchat.model.persistence.Message;
import com.reto.elorchat.model.service.MessageDTO;
import com.reto.elorchat.service.IMessageService;

@RestController
@RequestMapping("api/messages")
public class MessageController {
	@Autowired
	private IMessageService messageService;

	@GetMapping
	public ResponseEntity<Iterable<Message>> getMessages(){
		return new ResponseEntity<Iterable<Message>>(messageService.findAll(),HttpStatus.OK);
	}

	@GetMapping("/{chatId}")
	public ResponseEntity<List<MessageGetResponse>> getMessageByChatId(@PathVariable("chatId") Integer chatId){
		List <MessageDTO> listMessageDTO = messageService.findAllMessageByChatId(chatId);
		List<MessageGetResponse> response = new ArrayList<MessageGetResponse>(); 

		//Transform every DTO from the list to GetResponse
		for(MessageDTO messageDTO: listMessageDTO) {
			response.add(convertFromMessageDTOToGetResponse(messageDTO));
		}
		return new ResponseEntity<List<MessageGetResponse>>(response ,HttpStatus.OK);
	}

	@PostMapping
	public ResponseEntity<Message> createMessage(@RequestBody MessagePostRequest messagePostRequest){

		//messageService.createMessage(message);
		return new ResponseEntity<Message>(HttpStatus.CREATED);
	}
	//CONVERTS
	private MessageGetResponse convertFromMessageDTOToGetResponse(MessageDTO messageDTO) {
		MessageGetResponse response = new MessageGetResponse(
				messageDTO.getId(), 
				messageDTO.getText(),
				messageDTO.getDate(),
				messageDTO.getChatId(),
				messageDTO.getUserId());
		return response;
	}
	//////
}
