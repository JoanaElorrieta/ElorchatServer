package com.reto.elorchat.controller;

import java.io.IOException;
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

	@GetMapping("findAll/{id}")
	public ResponseEntity<List<MessageGetResponse>> getMessages(@PathVariable("id") Integer id) throws IOException{
		
		List <MessageDTO> listMessageDTO = messageService.findAll(id);
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

	@PostMapping
	public ResponseEntity<Message> createMessage(@RequestBody MessagePostRequest messagePostRequest){

		//messageService.createMessage(message);
		return new ResponseEntity<Message>(HttpStatus.CREATED);
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
	//////
}
