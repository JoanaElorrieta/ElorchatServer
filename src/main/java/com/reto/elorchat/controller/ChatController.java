package com.reto.elorchat.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.reto.elorchat.model.Chat;
import com.reto.elorchat.model.ChatPostRequest;
import com.reto.elorchat.repository.ChatRepository;

@RestController
@RequestMapping("api")
public class ChatController {
	@Autowired
	private ChatRepository chatRepository;
	
	@GetMapping("/chats")
	public ResponseEntity<Iterable<Chat>> getChats(){
		return new ResponseEntity<Iterable<Chat>>(chatRepository.findAll(),HttpStatus.OK);
	}
	
	@PostMapping("/chats")
	public ResponseEntity<Chat> createChat(@RequestBody ChatPostRequest chatPostRequest){

		Chat chat = new Chat (
				chatPostRequest.getName(),
				chatPostRequest.getType(),
				chatPostRequest.getUserId()
				);
		
		chatRepository.save(chat);
		return new ResponseEntity<Chat>(HttpStatus.CREATED);
	}
	@DeleteMapping("/chats/{id}")
	public ResponseEntity<Integer> deleteChatById(@PathVariable("id") Integer id){
		try {
			chatRepository.deleteById(id);
			return new ResponseEntity<Integer>(HttpStatus.NO_CONTENT);
		}catch(EmptyResultDataAccessException e) {
			throw new ResponseStatusException(HttpStatus.NO_CONTENT, "Grupo no encontrado");
		}
	}
}

