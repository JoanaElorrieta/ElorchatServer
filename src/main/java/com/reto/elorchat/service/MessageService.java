package com.reto.elorchat.service;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.reto.elorchat.exception.message.MessageNotFoundException;
import com.reto.elorchat.model.controller.request.MessagePostRequest;
import com.reto.elorchat.model.controller.request.PendingMessagePostRequest;
import com.reto.elorchat.model.controller.response.MessageGetResponse;
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
	public List<MessageDTO> findAll(Integer id){

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

	private void convertToBase64(MessageDTO actualMessage) throws IOException{

		String filePath = actualMessage.getText(); // Assuming actualMessage.getText() contains the correct full path
		File file = new File(filePath);
		byte[] fileContent = Files.readAllBytes(file.toPath());
		actualMessage.setText(Base64.getEncoder().encodeToString(fileContent));

	}

	@Override
	public List<MessageDTO> findAllMessagesByChatId(Integer chatId){


		Iterable<Message> listMessage = messageRepository.findAllMessagesByChatId(chatId);

		List<MessageDTO> response = new ArrayList<MessageDTO>();

		for(Message message: listMessage) {
			response.add(convertFromMessageDAOToDTO(message));
		}

		return response;	
	}

	@Override
	public MessageDTO createMessage(MessageDTO messageDTO){

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
	
	@Override
	public List<MessageDTO> insertPendingMessages(List<MessageDTO> listMessageDTO) {
		
		List<MessageDTO> response = new ArrayList<MessageDTO>(); 
		
		for(MessageDTO messageDTO : listMessageDTO){
			MessageDTO createdMessage = createMessage(messageDTO);
			response.add(createdMessage);
		}
		
		return response;
	}


	public MessageDTO createBase64FileOnResourceFile(MessageDTO messageDTO) throws NoSuchAlgorithmException, IOException, MessageNotFoundException {

		MessageDTO response = null;

		String imageString = messageDTO.getText();
		String fileExtension = detectMimeType(imageString);
		String fileName = messageDTO.getUserId() + "_" + messageDTO.getChatId() + "_" + generateUniqueFileName() + fileExtension;

		String outputFile = "src/main/resources/public/images/" + fileName;

		// Verificar si la imagen ya existe antes de crear una nueva ruta
		if (imageAlreadyExists(imageString, messageDTO.getChatId()) != null) {
			// La imagen ya existe, puedes tomar decisiones adicionales aquí si es necesario
			System.out.println("La imagen ya existe. No es necesario crear una nueva ruta.");
			String filePath = imageAlreadyExists(imageString, messageDTO.getChatId());
			System.out.println("FILE PATH TO SAVE: " + filePath);
			messageDTO.setText(filePath);
			response = createMessage(messageDTO);
		}else {
			// La imagen es nueva, puedes crear una nueva ruta
			// Move these lines here
			byte[] decodedImg = Base64.getDecoder().decode(imageString.getBytes(StandardCharsets.UTF_8));
			Path destinationFile = Paths.get(outputFile);

			try {
				Files.write(destinationFile, decodedImg);
				messageDTO.setText(destinationFile.toString());
				System.out.println("HE ESCRITO EL MENSAJE COMO FICHERO" + destinationFile.toString());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// Guardar la imagen en la ruta outputFile si es necesario
			// Además, puedes incluir la lógica para crear un nuevo MessageDTO o manejar la situación como sea necesario
			System.out.println("La imagen es nueva. Debes crear una nueva ruta.");

			response = createMessage(messageDTO);
		}
		return response;
	}

	public String imageAlreadyExists(String imageString, Integer chatId) {
		List<String> base64ImagesExistente = obtenerBase64ImagesExistente(chatId);

		// Comparar la cadena Base64 con las imágenes existentes
		for (String base64Existente : base64ImagesExistente) {
			if (base64Existente.equals(imageString)) {
				// Return the file path associated with the matched image
				return obtenerFilePathDelMensaje(base64Existente, chatId);
			}
		}
		return null; // La imagen no existe
	}

	public String obtenerFilePathDelMensaje(String imageString, Integer chatId) {
		Iterable<Message> mensajes = messageRepository.findAllMessagesByChatId(chatId);

		for (Message mensaje : mensajes) {
			MessageDTO messageDTO = convertFromMessageDAOToDTO(mensaje);
			if (messageDTO.getText().equals(imageString)) {
				return mensaje.getText(); // Return the file path
			}
		}
		return null; // File path not found (should not happen if everything is consistent)
	}

	public List<String> obtenerBase64ImagesExistente(Integer chatId) {
		List<String> base64ImagesExistente = new ArrayList<>();

		Iterable<Message> mensajes = messageRepository.findAllMessagesByChatId(chatId);

		for (Message mensaje : mensajes) {
			MessageDTO messageDTO = convertFromMessageDAOToDTO(mensaje);
			//String base64Existente = convertToBase64(mensaje.getText());
			base64ImagesExistente.add(messageDTO.getText());
		}

		return base64ImagesExistente;
	}

	private String generateUniqueFileName() {
		long timestamp = System.currentTimeMillis();
		Date date = new Date (timestamp);
		String uniqueId = UUID.randomUUID().toString().replace("-", "");
		return date + "_" + uniqueId;
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
				messageDTO.getUserId(),
				messageDTO.getTextType());

		response.setUser(user);
		response.setChat(chat);

		return response;
	}

	private MessageDTO convertFromMessageDAOToDTO(Message message){
		// TODO Auto-generated method stub
		MessageDTO response = new MessageDTO(
				message.getId(), 
				message.getText(),
				message.getSent(),
				message.getSaved(),
				message.getChatId(),
				message.getUserId(),
				message.getTextType());

		if(response.getTextType().value.equals("FILE")) {
			try {
				convertToBase64(response);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return response;
	}
	////
}
