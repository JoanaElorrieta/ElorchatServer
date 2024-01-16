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

import com.reto.elorchat.exception.chat.ChatNotFoundException;
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
	public ResponseEntity<Iterable<ChatGetResponse>> getChats(){
		List<ChatDTO> listChatDTO = chatService.findAll();
		List<ChatGetResponse> response = new ArrayList<ChatGetResponse>(); 
		//Transform every DTO from the list to GetResponse
		for(ChatDTO chatDTO: listChatDTO) {
			response.add(convertFromChatDTOToGetResponse(chatDTO));
		}
		return new ResponseEntity<Iterable<ChatGetResponse>>(response ,HttpStatus.OK);
	}

	@GetMapping("/{id}")
	public ResponseEntity<ChatGetResponse> getChatById(@PathVariable("id") Integer id) throws ChatNotFoundException{
		ChatDTO chatDTO = chatService.findById(id);
		ChatGetResponse response = convertFromChatDTOToGetResponse(chatDTO);
		return new ResponseEntity<ChatGetResponse>(response, HttpStatus.OK);
	}

	@PostMapping
	public ResponseEntity<ChatPostResponse> createChat(@RequestBody ChatPostRequest chatPostRequest){
		ChatPostResponse response = convertFromChatDTOToPostResponse(chatService.createChat(convertFromChatPostRequestToDTO(chatPostRequest)));
		return new ResponseEntity<ChatPostResponse>(response, HttpStatus.CREATED);
	}


	@DeleteMapping("/{id}")
	public ResponseEntity<Integer> deleteChatById(@PathVariable("id") Integer id) throws ChatNotFoundException{
		chatService.deleteChat(id);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@GetMapping("entryPermission/{idChat}")
	public ResponseEntity<Integer> countByUsers_IdAndId(@PathVariable Integer idChat,
			Authentication authentication) throws ChatNotFoundException{
		User user = (User) authentication.getPrincipal();
		Integer response = chatService.countByUsers_IdAndId(idChat, user.getId());
		//ChatDTO chatDTO = chatService.findById(id);
		//ChatGetResponse response = convertFromChatDTOToGetResponse(chatDTO);
		return new ResponseEntity<Integer>(response, HttpStatus.OK);
	}


	@GetMapping("deletePermission/{idChat}")
	public ResponseEntity<Integer> countByIdAndAdminId(@PathVariable Integer idChat,
			Authentication authentication) throws ChatNotFoundException{
		User user = (User) authentication.getPrincipal();
		Integer response = chatService.countByIdAndAdminId(idChat, user.getId());
		//ChatDTO chatDTO = chatService.findById(id);
		//ChatGetResponse response = convertFromChatDTOToGetResponse(chatDTO);
		return new ResponseEntity<Integer>(response, HttpStatus.OK);
	}


	@GetMapping("exists/{idChat}")
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

		//ChatDTO chatDTO = chatService.findById(id);
		//ChatGetResponse response = convertFromChatDTOToGetResponse(chatDTO);
		return new ResponseEntity<Integer>(response, HttpStatus.OK);
	}

	//ASK ES CORRECTO ESTAR PASANDO EL AUTHENTICATION??
	@DeleteMapping("delete/{idChat}")
	public ResponseEntity<Integer> deleteByUsers_IdAndId(@PathVariable Integer idChat,
			Authentication authentication) throws ChatNotFoundException{
		User user = (User) authentication.getPrincipal();
		chatService.deleteByUsers_IdAndId(idChat, user.getId());
		return new ResponseEntity<>(HttpStatus.OK);
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

