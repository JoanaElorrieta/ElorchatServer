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
import com.reto.elorchat.model.User;
import com.reto.elorchat.model.UserPostRequest;
import com.reto.elorchat.repository.UserRepository;

@RestController
@RequestMapping("api")
public class UserController {
	@Autowired
	private UserRepository userRepository;
	
	@GetMapping("/users")
	public ResponseEntity<Iterable<User>> getUsers(){
		return new ResponseEntity<Iterable<User>>(userRepository.findAll(),HttpStatus.OK);
	}
	
	@PostMapping("/users")
	public ResponseEntity<Chat> createUsers(@RequestBody UserPostRequest userPostRequest){

		User user = new User (
				userPostRequest.getName(),
				userPostRequest.getSurname(),
				userPostRequest.getEmail(),
				userPostRequest.getPhoneNumber()
				);
		userRepository.save(user);
		return new ResponseEntity<Chat>(HttpStatus.CREATED);
	}
	
	@DeleteMapping("/users/{id}")
	public ResponseEntity<Integer> deleteUserById(@PathVariable("id") Integer id){
		try {
			userRepository.deleteById(id);
			return new ResponseEntity<Integer>(HttpStatus.NO_CONTENT);
		}catch(EmptyResultDataAccessException e) {
			throw new ResponseStatusException(HttpStatus.NO_CONTENT, "Usuario no encontrado");
		}
	}
}
