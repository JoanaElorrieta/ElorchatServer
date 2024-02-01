package com.reto.elorchat.service;

import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.reto.elorchat.model.persistence.Chat;
import com.reto.elorchat.model.persistence.Message;
import com.reto.elorchat.model.service.MessageDTO;
import com.reto.elorchat.repository.ChatRepository;
import com.reto.elorchat.repository.MessageRepository;
import com.reto.elorchat.security.persistance.User;
import com.reto.elorchat.security.repository.UserRepository;

@Service
public class MessageService implements IMessageService{

	@Autowired
	MessageRepository messageRepository;

	@Autowired
	ChatRepository chatRepository;


	@Autowired
	UserRepository userRepository;

	@Override
	public List<MessageDTO> findAll(Integer id) {

		List<MessageDTO> response = new ArrayList<MessageDTO>();		

		if(id == 0) {
			Iterable<Message> listMessage = messageRepository.findAll();
			for(Message actualMessage: listMessage) {
				response.add(convertFromMessageDAOToDTO(actualMessage));
			}
		} else {
			Iterable<Message> listMessage = messageRepository.findAllMessagesCreatedAfterId(id);
			for(Message actualMessage: listMessage) {
				response.add(convertFromMessageDAOToDTO(actualMessage));
			}
		}
		return response;
	}

	@Override
	public List<MessageDTO> findAllMessagesByChatId(Integer chatId) {

		//		Chat chat = chatRepository.findById(chatId).orElseThrow(
		//				() -> new ResponseStatusException(HttpStatus.NO_CONTENT, "Chat no encontrado")
		//				);

		Iterable<Message> listMessage = messageRepository.findAllMessagesByChatId(chatId);

		List<MessageDTO> response = new ArrayList<MessageDTO>();

		for(Message message: listMessage) {
			response.add(convertFromMessageDAOToDTO(message));
		}

		return response;	
	}

	@Override
	public MessageDTO createMessage(MessageDTO messageDTO) {

		User user = userRepository.findById(messageDTO.getUserId()).orElseThrow(
				() -> new ResponseStatusException(HttpStatus.NO_CONTENT, "Creador no encontrado")
				);

		Chat chat = chatRepository.findById(messageDTO.getChatId()).orElseThrow(
				() -> new ResponseStatusException(HttpStatus.NO_CONTENT, "Chat no encontrado")
				);

		Message message = messageRepository.save(convertFromMessageDTOToDAO(messageDTO, user, chat));
		MessageDTO response = convertFromMessageDAOToDTO(message);

		return response;
	}


	public MessageDTO createBase64FileOnResourceFile(MessageDTO messageDTO) {

		String imageString = messageDTO.getText();
		String fileExtension = detectMimeType(imageString);
		String fileName = messageDTO.getId() + fileExtension;
		String outputFile = "src/main/resources/static/images/" + fileName; 

		byte[] decodedImg = Base64.getDecoder().decode(imageString.getBytes(StandardCharsets.UTF_8));
		//Path destinationFile = Paths.
		MessageDTO response = createMessage(messageDTO);

		return response;
	}

	private String detectMimeType(String base64Content) {

		HashMap<String, String> signatures = new HashMap<String, String>();
		signatures.put("JVBERi0", ".pdf");
		signatures.put("R0lGODdh", ".gif");
		signatures.put("iVBORw0KGgo", ".png");
		signatures.put("/9j/", ".jpg");
		String response = "";

		for(Map.Entry<String, String> entry : signatures.entrySet()) {
			String key = entry.getKey();
			if(base64Content.indexOf(0) == 0) {
				response = entry.getValue();
			}
		}

		return response;
	}

	//CONVERTS
	private Message convertFromMessageDTOToDAO(MessageDTO messageDTO, User user, Chat chat) {

		Message response = new Message(
				messageDTO.getId(), 
				messageDTO.getText(),
				messageDTO.getSent(),
				messageDTO.getSaved(),
				messageDTO.getChatId(),
				messageDTO.getUserId());

		response.setUser(user);
		response.setChat(chat);

		return response;
	}

	private MessageDTO convertFromMessageDAOToDTO(Message message) {
		// TODO Auto-generated method stub
		MessageDTO response = new MessageDTO(
				message.getId(), 
				message.getText(),
				message.getSent(),
				message.getSaved(),
				message.getChatId(),
				message.getUserId());
		return response;
	}
	////
}
