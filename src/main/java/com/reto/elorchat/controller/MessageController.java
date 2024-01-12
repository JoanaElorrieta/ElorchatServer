package com.reto.elorchat.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.reto.elorchat.model.Chat;
import com.reto.elorchat.model.Message;
import com.reto.elorchat.model.MessagePostRequest;
import com.reto.elorchat.repository.MessageRepository;

@RestController
@RequestMapping("api")
public class MessageController {
	@Autowired
	private MessageRepository messageRepository;

	@GetMapping("/messages")
	public ResponseEntity<Iterable<Message>> getMessages(){
		return new ResponseEntity<Iterable<Message>>(messageRepository.findAll(),HttpStatus.OK);
	}

	@PostMapping("/messages")
	public ResponseEntity<Chat> createMessage(@RequestBody MessagePostRequest messagePostRequest){

		Message message = new Message (
				messagePostRequest.getText(),
				messagePostRequest.getDate(),
				messagePostRequest.getGroupId(),
				messagePostRequest.getUseId()
				);
		messageRepository.save(message);
		return new ResponseEntity<Chat>(HttpStatus.CREATED);
	}
}
