package com.reto.elorchat.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.reto.elorchat.exception.chat.CantLeaveChatException;
import com.reto.elorchat.exception.chat.ChatNameAlreadyExists;
import com.reto.elorchat.exception.chat.ChatNotFoundException;
import com.reto.elorchat.exception.chat.UserAlreadyExistsOnChat;
import com.reto.elorchat.model.controller.request.ChatPostRequest;
import com.reto.elorchat.model.controller.response.ChatGetResponse;
import com.reto.elorchat.model.controller.response.ChatPostResponse;
import com.reto.elorchat.model.service.ChatDTO;
import com.reto.elorchat.security.persistance.User;
import com.reto.elorchat.service.IChatService;

@RestController
@RequestMapping("api/chats")
public class ChatController {

	@Autowired
	private IChatService chatService;

	@GetMapping
	public ResponseEntity<List<ChatGetResponse>> getChats(){
		List<ChatDTO> listChatDTO = chatService.findAll();
		List<ChatGetResponse> response = new ArrayList<ChatGetResponse>(); 
		//Transform every DTO from the list to GetResponse
		for(ChatDTO chatDTO: listChatDTO) {
			response.add(convertFromChatDTOToGetResponse(chatDTO));
		}
		return new ResponseEntity<List<ChatGetResponse>>(response ,HttpStatus.OK);
	}

	@GetMapping("/{id}")
	public ResponseEntity<ChatGetResponse> getChatById(@PathVariable("id") Integer id) throws ChatNotFoundException{
		ChatDTO chatDTO = chatService.findById(id);
		ChatGetResponse response = convertFromChatDTOToGetResponse(chatDTO);
		return new ResponseEntity<ChatGetResponse>(response, HttpStatus.OK);
	}

	@PostMapping
	public ResponseEntity<ChatPostResponse> createChat(@RequestBody ChatPostRequest chatPostRequest, Authentication authentication) throws ChatNameAlreadyExists{
		User user = (User) authentication.getPrincipal();
		ChatPostResponse response = convertFromChatDTOToPostResponse(chatService.createChat(convertFromChatPostRequestToDTO(chatPostRequest, user.getId())));
		return new ResponseEntity<ChatPostResponse>(response, HttpStatus.CREATED);
	}


	@DeleteMapping("/{id}")
	public ResponseEntity<Integer> deleteChatById(@PathVariable("id") Integer id) throws ChatNotFoundException{
		chatService.deleteChat(id);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@GetMapping("/entryPermission/{idChat}")
	public ResponseEntity<Integer> canEnterUserChat(@PathVariable Integer idChat,
			Authentication authentication) throws ChatNotFoundException{
		User user = (User) authentication.getPrincipal();
		Integer response = chatService.canEnterUserChat(idChat, user.getId());
		return new ResponseEntity<Integer>(response, HttpStatus.OK);
	}


	@GetMapping("/deletePermission/{idChat}")
	public ResponseEntity<Integer> countByIdAndAdminId(@PathVariable Integer idChat,
			Authentication authentication) throws ChatNotFoundException{
		User user = (User) authentication.getPrincipal();
		Integer response = chatService.canDeleteChat(idChat, user.getId());
		return new ResponseEntity<Integer>(response, HttpStatus.OK);
	}


	@GetMapping("/existsOnChat/{idChat}")
	public ResponseEntity<Integer> existsByIdAndUsers_Id(@PathVariable Integer idChat,
			Authentication authentication) throws ChatNotFoundException{
		Integer response; 
		User user = (User) authentication.getPrincipal();
		boolean boolValue = chatService.existsByIdAndUsers_Id(idChat, user.getId());

		if (boolValue) {
			response = 1;
		}
		else {
			response = 0;
		}
		return new ResponseEntity<Integer>(response, HttpStatus.OK);
	}

	@PostMapping("/addToGroup/{idChat}")
	public ResponseEntity<Integer> addUserToChat(@PathVariable Integer idChat,
			Authentication authentication) throws ChatNotFoundException, UserAlreadyExistsOnChat{
		User user = (User) authentication.getPrincipal();
		chatService.addUserToChat(idChat, user.getId());			
		// TODO: handle exception
		return new ResponseEntity<Integer>(HttpStatus.OK);
	}

	@DeleteMapping("/leaveChat/{idChat}")
	public ResponseEntity<Integer> leaveChat(@PathVariable Integer idChat,
			Authentication authentication) throws ChatNotFoundException, CantLeaveChatException{
		Integer response; 
		User user = (User) authentication.getPrincipal();
		boolean boolValue =chatService.leaveChat(idChat, user.getId());
		if (boolValue) {
			response = 1;
		}
		else {
			response = 0;
		}
		return new ResponseEntity<Integer>(response,HttpStatus.OK);
	}

	//CONVERTS
	//---------------------------------------
	private ChatGetResponse convertFromChatDTOToGetResponse(ChatDTO chatDTO) {

		ChatGetResponse response = new ChatGetResponse(
				chatDTO.getId(),
				chatDTO.getName(),
				chatDTO.getType(),
				chatDTO.getAdminId()
				);

//		if (chatDTO.getUsers() != null) {
//			List<UserGetResponse> userList = new ArrayList<UserGetResponse>();
//			for(UserDTO userDTO : chatDTO.getUsers()) {
//				userList.add(convertFromUserDTOToGetResponse(userDTO));
//			}
//			response.setUsers(userList);
//		}
//		if (chatDTO.getMessages() != null) {
//			List<MessageGetResponse> messageList = new ArrayList<MessageGetResponse>();
//			for(MessageDTO messageDTO : chatDTO.getMessages()) {
//				messageList.add(convertFromMessageDTOToGetResponse(messageDTO));
//			}
//			response.setMessages(messageList);
//		}
		return response;
	}

	private ChatPostResponse convertFromChatDTOToPostResponse(ChatDTO chatDTO) {
		ChatPostResponse response = new ChatPostResponse(
				chatDTO.getId(),
				chatDTO.getName(),
				chatDTO.getType(),
				chatDTO.getAdminId()
				);

//		if (chatDTO.getUsers() != null) {
//			List<UserGetResponse> userList = new ArrayList<UserGetResponse>();
//			for(UserDTO userDTO : chatDTO.getUsers()) {
//				userList.add(convertFromUserDTOToGetResponse(userDTO));
//			}
//			response.setUsers(userList);
//		}
//		if (chatDTO.getMessages() != null) {
//			List<MessageGetResponse> messageList = new ArrayList<MessageGetResponse>();
//			for(MessageDTO messageDTO : chatDTO.getMessages()) {
//				messageList.add(convertFromMessageDTOToGetResponse(messageDTO));
//			}
//			response.setMessages(messageList);
//		}
		return response;
	}

	private ChatDTO convertFromChatPostRequestToDTO(ChatPostRequest chatPostRequest, Integer adminId) {

		ChatDTO response = new ChatDTO(
				chatPostRequest.getId(),
				chatPostRequest.getName(),
				chatPostRequest.getType(),
				adminId
				);
		return response;
	}
	
//	private MessageGetResponse convertFromMessageDTOToGetResponse(MessageDTO messageDTO) {
//		MessageGetResponse response = new MessageGetResponse(
//				messageDTO.getId(),
//				messageDTO.getText(),
//				messageDTO.getDate()
//				);
//		return response;
//	}
//
//
//	private UserGetResponse convertFromUserDTOToGetResponse(UserDTO userDTO) {
//		UserGetResponse response = new UserGetResponse(
//				userDTO.getId(),
//				userDTO.getName(),
//				userDTO.getSurname(),
//				userDTO.getEmail(),
//				userDTO.getPhoneNumber1());
//		return response;
//	}

	/////
}

