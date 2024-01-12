package com.reto.elorchat.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.reto.elorchat.model.Chat;
import com.reto.elorchat.model.Message;
import com.reto.elorchat.model.MessagePostRequest;
import com.reto.elorchat.model.User;
import com.reto.elorchat.repository.ChatRepository;
import com.reto.elorchat.repository.MessageRepository;
import com.reto.elorchat.repository.UserRepository;

@RestController
@RequestMapping("api")
public class MessageController {
	@Autowired
	private MessageRepository messageRepository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private ChatRepository chatRepository;

	@GetMapping("/messages")
	public ResponseEntity<Iterable<Message>> getMessages(){
		return new ResponseEntity<Iterable<Message>>(messageRepository.findAll(),HttpStatus.OK);
	}

	@PostMapping("/messages")
	public ResponseEntity<Message> createMessage(@RequestBody MessagePostRequest messagePostRequest){

		Chat chat= chatRepository.findById(messagePostRequest.getChatId()).orElseThrow(
				() -> new ResponseStatusException(HttpStatus.NO_CONTENT, "Jefe no encontrado")
				);
		User user = userRepository.findById(messagePostRequest.getUserId()).orElseThrow(
				() -> new ResponseStatusException(HttpStatus.NO_CONTENT, "Departamento no encontrado")
				);
		
		Message message = new Message (
				messagePostRequest.getText(),
				messagePostRequest.getDate(),
				chat,
				user
				);
		
		messageRepository.save(message);
		return new ResponseEntity<Message>(HttpStatus.CREATED);
	}
}
