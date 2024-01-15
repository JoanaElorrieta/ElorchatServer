package com.reto.elorchat.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.reto.elorchat.exception.chat.ChatNotFoundException;
import com.reto.elorchat.model.controller.request.ChatPostRequest;
import com.reto.elorchat.model.controller.response.ChatGetResponse;
import com.reto.elorchat.model.controller.response.ChatPostResponse;
import com.reto.elorchat.model.controller.response.UserGetResponse;
import com.reto.elorchat.model.service.ChatDTO;
import com.reto.elorchat.model.service.UserDTO;
import com.reto.elorchat.service.IChatService;

@RestController
@RequestMapping("api")
public class ChatController {

	@Autowired
	private IChatService chatService;

	@GetMapping("/chats")
	public ResponseEntity<Iterable<ChatGetResponse>> getChats(){
		List<ChatDTO> listChatDTO = chatService.findAll();
		List<ChatGetResponse> response = new ArrayList<ChatGetResponse>(); 
		//Transform every DTO from the list to GetResponse
		for(ChatDTO chatDTO: listChatDTO) {
			response.add(convertFromChatDTOToDepartmentGetResponse(chatDTO));
		}
		return new ResponseEntity<Iterable<ChatGetResponse>>(response ,HttpStatus.OK);
	}

	@PostMapping("/chats")
	public ResponseEntity<ChatPostResponse> createChat(@RequestBody ChatPostRequest chatPostRequest){
		ChatPostResponse response = convertFromChatDTOToPostResponse(chatService.createChat(convertFromChatPostRequestToDTO(chatPostRequest)));
		return new ResponseEntity<ChatPostResponse>(response, HttpStatus.CREATED);
	}


	@DeleteMapping("/chats/{id}")
	public ResponseEntity<Integer> deleteChatById(@PathVariable("id") Integer id) throws ChatNotFoundException{
		chatService.deleteChat(id);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	//CONVERTS
	//---------------------------------------
	private ChatGetResponse convertFromChatDTOToDepartmentGetResponse(ChatDTO chatDTO) {

		ChatGetResponse response = new ChatGetResponse(
				chatDTO.getId(),
				chatDTO.getName(),
				chatDTO.getType(),
				chatDTO.getAdminId()
				);

		if (chatDTO.getUsers() != null) {
			List<UserGetResponse> usersList = new ArrayList<UserGetResponse>();
			for(UserDTO userDTO: chatDTO.getUsers()) {
				usersList.add(convertFromUserDTOToGetResponse(userDTO));
			}
			response.setUsers(usersList);
		}

		return response;
	}
	private UserGetResponse convertFromUserDTOToGetResponse(UserDTO userDTO) {
		UserGetResponse response = new UserGetResponse(
				userDTO.getId(),
				userDTO.getName(),
				userDTO.getSurname(),
				userDTO.getEmail(),
				userDTO.getPassword(),
				userDTO.getPhone_Number1(),
				userDTO.getPhone_Number2(),
				userDTO.getAddress(),
				userDTO.getPhoto(),
				userDTO.getFCTDUAL()
				);
		return response;
	}

	private ChatPostResponse convertFromChatDTOToPostResponse(ChatDTO chatDTO) {
		ChatPostResponse response = new ChatPostResponse(
				chatDTO.getId(),
				chatDTO.getName(),
				chatDTO.getType(),
				chatDTO.getAdminId()
				);
		return response;
	}

	private ChatDTO convertFromChatPostRequestToDTO(ChatPostRequest chatPostRequest) {

		ChatDTO response = new ChatDTO(
				chatPostRequest.getId(),
				chatPostRequest.getName(),
				chatPostRequest.getType(),
				chatPostRequest.getAdminId()
				);
		return response;
	}
	/////
}

