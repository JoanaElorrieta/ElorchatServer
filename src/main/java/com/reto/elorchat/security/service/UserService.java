package com.reto.elorchat.security.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.reto.elorchat.model.persistence.Chat;
import com.reto.elorchat.model.service.ChatDTO;
import com.reto.elorchat.model.service.UserDTO;
import com.reto.elorchat.security.persistance.User;
import com.reto.elorchat.security.repository.UserRepository;
import com.reto.elorchat.service.ChatService;


@Service
public class UserService implements IUserService, UserDetailsService {

	@Autowired
	UserRepository userRepository;

	@Autowired
	ChatService chatService;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// esta es la funcion que busca al usuario por email. 
		// ya que en este caso el campo de login es el email
		// si fuese otro, realizar otra funcion
		return userRepository.findByEmail(username)
				.orElseThrow(
						() -> new UsernameNotFoundException("User " + username + " not found")
						);
	}

	@Override
	public Iterable<User> findAll() {
		return userRepository.findAll();
	}

	@Override
	public UserDTO findById(Integer id) {

		User user = userRepository.findUserWithChatsById(id)
				.orElseThrow(() -> new UsernameNotFoundException("User " + id + " not found"));

		UserDTO response = convertFromUserDAOToDTO(user);
		System.out.println(response.toString());
		return response;

	}

	//ASK
	//AQUI TAMBIEN HAGO? PREGUNTAR PQ ENTONCES EN AUTH CONTROLLER LO CASTEAMOS AL MODELO DE HIBERNATE Y NO AL DEL CONTROLADOR
	//POR EL USERDETAILS? PERO EN DEPENDIENDO PARA QUE NO NECESITO QUE LO IMPLEMENTE, VERDAD?
	//CONVERTS
	private UserDTO convertFromUserDAOToDTO(User user) {
		UserDTO response = new UserDTO(
				user.getId(),
				user.getName(),
				user.getSurname(),
				user.getEmail(),
				user.getPhoneNumber1(),
				user.getPhoto());

		if (user.getChats() != null) {
			List<ChatDTO> chatList = new ArrayList<ChatDTO>();
			for(Chat chat: user.getChats()) {
				chatList.add(convertFromChatDAOToDTO(chat));
			}
			response.setChats(chatList);
		}
		return response;
	}

	private ChatDTO convertFromChatDAOToDTO(Chat chat) {
		ChatDTO response = new ChatDTO(
				chat.getId(),
				chat.getName(),
				chat.getType(),
				chat.getAdminId()
				);

		return response;
	}
	/////
}
