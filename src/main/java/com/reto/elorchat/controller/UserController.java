package com.reto.elorchat.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.reto.elorchat.model.controller.response.UserGetResponse;
import com.reto.elorchat.model.service.ChatDTO;
import com.reto.elorchat.model.service.UserDTO;
import com.reto.elorchat.security.service.UserService;

@RestController
@RequestMapping("api/users")
public class UserController {
	@Autowired
	private UserService userService;

	//ASK PREGUNTA SI HAY PROBLEMA EN EL LOG DEL SERVER
	@GetMapping
	public ResponseEntity<List<UserGetResponse>> findAllUsers(){
		List <UserDTO> listUserDTO = userService.findAll();
		List<UserGetResponse> response = new ArrayList<UserGetResponse>(); 

		//Transform every DTO from the list to GetResponse
		for(UserDTO userDTO: listUserDTO) {
			if(userDTO != null){
				response.add(convertFromUserDTOToGetResponseWithChatIds(userDTO));
			}
		}
		return new ResponseEntity<List<UserGetResponse>>(response ,HttpStatus.OK);
	}

	@GetMapping("/chat/{chatId}")
	public ResponseEntity<List<UserGetResponse>> getUserByChatId(@PathVariable("chatId") Integer chatId){
		List <UserDTO> listUserDTO = userService.findAllUsersByChatId(chatId);
		List<UserGetResponse> response = new ArrayList<UserGetResponse>(); 

		//Transform every DTO from the list to GetResponse
		for(UserDTO userDTO: listUserDTO) {
			response.add(convertFromUserDTOToGetResponse(userDTO));
		}
		return new ResponseEntity<List<UserGetResponse>>(response ,HttpStatus.OK);
	}
	@GetMapping("/find/{email}")
	public ResponseEntity<Integer> getUserByEmail(@PathVariable("email") String email){
		Integer response = userService.findUserByEmail(email);
		return new ResponseEntity<Integer>(response, HttpStatus.OK);
	}
	//
	//	@GetMapping("/users")
	//	public ResponseEntity<Iterable<User>> getUsers(){
	//		return new ResponseEntity<Iterable<User>>(userRepository.findAll(),HttpStatus.OK);
	//	}

	//	@PostMapping("/users")
	//	public ResponseEntity<User> createUsers(@RequestBody UserPostRequest userPostRequest){
	//
	//		User user = new User (
	//				userPostRequest.getName(),
	//				userPostRequest.getSurname(),
	//				userPostRequest.getEmail()
	//				);
	//		userRepository.save(user);
	//		return new ResponseEntity<User>(HttpStatus.CREATED);
	//	}
	//
	//	@DeleteMapping("/users/{id}")
	//	public ResponseEntity<Integer> deleteUserById(@PathVariable("id") Integer id){
	//		try {
	//			userRepository.deleteById(id);
	//			return new ResponseEntity<Integer>(HttpStatus.NO_CONTENT);
	//		}catch(EmptyResultDataAccessException e) {
	//			throw new ResponseStatusException(HttpStatus.NO_CONTENT, "Usuario no encontrado");
	//		}
	//	}

	//CONVERTS
	private UserGetResponse convertFromUserDTOToGetResponse(UserDTO userDTO) {
		UserGetResponse response = new UserGetResponse(
				userDTO.getId(),
				userDTO.getName(),
				userDTO.getSurname(),
				userDTO.getEmail(),
				userDTO.getPhoneNumber1());

		return response;
	}

	private UserGetResponse convertFromUserDTOToGetResponseWithChatIds(UserDTO userDTO) {
		
		UserGetResponse response = new UserGetResponse(
				userDTO.getId(),
				userDTO.getName(),
				userDTO.getSurname(),
				userDTO.getEmail(),
				userDTO.getPhoneNumber1());

		if(userDTO.getChats() != null) {
			List<Integer> listChatId = new ArrayList<Integer>();
			for(ChatDTO chatDTO : userDTO.getChats()) {
				listChatId.add(chatDTO.getId());
			}
			response.setChatId(listChatId);
		}
		if (userDTO.getRoleId() != null) {
			response.setRoleId(userDTO.getRoleId());
		}
		return response;
	}

	//////
}
