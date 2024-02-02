package com.reto.elorchat.service;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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
import com.reto.elorchat.model.enums.TextTypeEnum;
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
	public List<MessageDTO> findAll(Integer id) throws IOException {

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

	private void convertToBase64(MessageDTO actualMessage) throws IOException {
		String fileName = actualMessage.getUserId() + "_" + actualMessage.getChatId() + "_" + actualMessage.getId() + getExtensionFromFileName(actualMessage.getText());
		String filePath = "src/main/resources/public/images/" + fileName;

		File file = new File(filePath);
		byte[] fileContent = Files.readAllBytes(file.toPath());
		actualMessage.setText(Base64.getEncoder().encodeToString(fileContent));

	}

	private String getExtensionFromFileName(String fileName) {
		int lastDotIndex = fileName.lastIndexOf('.');
		if (lastDotIndex >= 0) {
			return fileName.substring(lastDotIndex);
		}
		// Handle the case where the file name doesn't contain a dot (.)
		return "";
	}

	@Override
	public List<MessageDTO> findAllMessagesByChatId(Integer chatId) throws IOException {


		Iterable<Message> listMessage = messageRepository.findAllMessagesByChatId(chatId);

		List<MessageDTO> response = new ArrayList<MessageDTO>();

		for(Message message: listMessage) {
			response.add(convertFromMessageDAOToDTO(message));
		}

		return response;	
	}

	@Override
	public MessageDTO createMessage(MessageDTO messageDTO) throws IOException {

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


	public MessageDTO createBase64FileOnResourceFile(MessageDTO messageDTO) throws NoSuchAlgorithmException, IOException, MessageNotFoundException {

		MessageDTO response = null;

		String imageString = messageDTO.getText();
		String fileExtension = detectMimeType(imageString);
		String temporaryId = generateTemporaryId();  // Implement this method to generate a temporary ID
		String fileName = messageDTO.getUserId() + "_" + messageDTO.getChatId() + "_" + temporaryId + fileExtension;

		String outputFile = "src/main/resources/public/images/" + fileName;

		// Verificar si la imagen ya existe antes de crear una nueva ruta
		if (imageAlreadyExists(imageString)) {
			// La imagen ya existe, puedes tomar decisiones adicionales aquí si es necesario
			System.out.println("La imagen ya existe. No es necesario crear una nueva ruta.");
			response = createMessage(messageDTO);
		}else {
			// La imagen es nueva, puedes crear una nueva ruta
			// Move these lines here
			byte[] decodedImg = Base64.getDecoder().decode(imageString.getBytes(StandardCharsets.UTF_8));
			Path destinationFile = Paths.get(outputFile);

			try {
				Files.write(destinationFile, decodedImg);
				messageDTO.setText(destinationFile.toString());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// Guardar la imagen en la ruta outputFile si es necesario
			// Además, puedes incluir la lógica para crear un nuevo MessageDTO o manejar la situación como sea necesario
			System.out.println("La imagen es nueva. Debes crear una nueva ruta.");

			response = createMessage(messageDTO);

			// After the database creation, replace the temporary ID in the file name with the actual ID
			Integer actualId = response.getId();
			String actualFileName = messageDTO.getUserId() + "_" + messageDTO.getChatId() + "_" + actualId + fileExtension;
			Path actualDestinationFile = Paths.get("src/main/resources/public/images/" + actualFileName);

			try {
				// Assuming the file has been moved and actualDestinationFile points to the new location
				Files.move(destinationFile, actualDestinationFile, StandardCopyOption.REPLACE_EXISTING);

				// After moving the file, write its content to the same path
				Files.write(actualDestinationFile, decodedImg);
			} catch (IOException e) {
				// Handle the exception appropriately (e.g., log it)
				e.printStackTrace();
			}

			response = updateMessage(messageDTO);
		}
		return response;
	}

	@Override
	public MessageDTO updateMessage(MessageDTO messageDTO) throws MessageNotFoundException, IOException{

		Message message = messageRepository.findById(messageDTO.getId()).orElseThrow(
				() -> new MessageNotFoundException("No ese existe empleado")
				);

		if(messageDTO.getText() != null) {			
			message.setText(messageDTO.getText());
		}

		//TODO CHANGE
		Message messageResponse = messageRepository.updateTextForMessage(message.getText(), message.getId()).orElseThrow(
				() -> new MessageNotFoundException("No ese existe empleado")
				);;

		MessageDTO response = convertFromMessageDAOToDTO(messageResponse);			

		return response;
	}

	private boolean imageAlreadyExists(String imageString) throws NoSuchAlgorithmException, IOException{

		String hashActual = calcularHashDeBase64(imageString);

		// Supongamos que hashesExistente es una lista de hashes de imágenes ya almacenadas
		List<String> hashesExistente = obtenerHashesExistente();

		// Supongamos que hashesExistente es una lista de hashes de imágenes ya almacenadas
		for (String hashExistente : hashesExistente) {
			if (hashActual.equals(hashExistente)) {
				return true; // La imagen ya existe
			}
		}
		return false; // La imagen es nueva
	}

	public List<String> obtenerHashesExistente() {

		List<String> hashesExistente = new ArrayList<>();

		// Supongamos que tienes un método en tu MessageRepository para obtener mensajes con type "FILE"
		// Ajusta esto según la estructura de tu base de datos.
		Iterable<Message> mensajes = messageRepository.findAllMessagesByType(TextTypeEnum.FILE);

		for (Message mensaje : mensajes) {
			// Aquí asumo que cada mensaje tiene un método getRuta(). Ajusta según tus necesidades.
			String rutaCompleta = mensaje.getText();

			// Extracción del hash de la ruta (asumo un formato "userId_chatId_hash.extension")
			String[] partesRuta = rutaCompleta.split("_");
			if (partesRuta.length >= 3) {
				//ANNADO
				String hash = partesRuta[2];
				hashesExistente.add(hash);
			}
		}

		return hashesExistente;
	}

	private String calcularHashDeBase64(String base64Content) throws NoSuchAlgorithmException, IOException {
		MessageDigest digest = MessageDigest.getInstance("SHA-256");
		byte[] decodedImg = Base64.getDecoder().decode(base64Content.getBytes(StandardCharsets.UTF_8));
		digest.update(decodedImg);
		byte[] hashBytes = digest.digest();

		// Convertir el hash a una representación hexadecimal
		StringBuilder hexString = new StringBuilder();
		for (byte hashByte : hashBytes) {
			String hex = Integer.toHexString(0xff & hashByte);
			if (hex.length() == 1) hexString.append('0');
			hexString.append(hex);
		}
		return hexString.toString();
	}

	private String generateTemporaryId() {
		// Using UUID to generate a random and unique temporary ID
		return UUID.randomUUID().toString();
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

	private MessageDTO convertFromMessageDAOToDTO(Message message) throws IOException {
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
			convertToBase64(response);
		}

		return response;
	}
	////
}
