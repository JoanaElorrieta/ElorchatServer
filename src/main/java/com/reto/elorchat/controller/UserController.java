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

import com.reto.elorchat.model.controller.request.UserPostRequest;
import com.reto.elorchat.security.persistance.User;
import com.reto.elorchat.security.repository.UserRepository;

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
	public ResponseEntity<User> createUsers(@RequestBody UserPostRequest userPostRequest){

		User user = new User (
				userPostRequest.getName(),
				userPostRequest.getSurname(),
				userPostRequest.getEmail()
				);
		userRepository.save(user);
		return new ResponseEntity<User>(HttpStatus.CREATED);
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
