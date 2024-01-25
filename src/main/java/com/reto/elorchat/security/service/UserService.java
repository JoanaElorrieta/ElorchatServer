package com.reto.elorchat.security.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.reto.elorchat.model.enums.RoleEnum;
import com.reto.elorchat.model.persistence.Chat;
import com.reto.elorchat.model.persistence.Role;
import com.reto.elorchat.model.service.ChatDTO;
import com.reto.elorchat.model.service.UserDTO;
import com.reto.elorchat.repository.ChatRepository;
import com.reto.elorchat.security.persistance.User;
import com.reto.elorchat.security.repository.UserRepository;


@Service
public class UserService implements IUserService, UserDetailsService {

	@Autowired
	UserRepository userRepository;

	@Autowired
	ChatRepository chatRepository;

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
	public List<UserDTO> findAll() {

		Iterable<User> listUser = userRepository.findAll();

		List<UserDTO> response = new ArrayList<UserDTO>();

		for(User user: listUser) {
			response.add(convertFromUserDAOToDTO(user));
		}
		return response;	
	}


	@Override
	public UserDTO findById(Integer id) {

		User user = userRepository.findUserWithChatsById(id)
				.orElseThrow(() -> new UsernameNotFoundException("User " + id + " not found"));

		UserDTO response = convertFromUserDAOToDTO(user);
		return response;

	}

	@Override
	public List<UserDTO> findAllUsersByChatId(Integer chatId) {

		//		Chat chat = chatRepository.findById(chatId).orElseThrow(
		//				() -> new ResponseStatusException(HttpStatus.NO_CONTENT, "Chat no encontrado")
		//				);

		Iterable<User> listUser= userRepository.findAllUsersByChatId(chatId);

		List<UserDTO> response = new ArrayList<UserDTO>();

		for(User user: listUser) {
			response.add(convertFromUserDAOToDTO(user));
		}

		return response;	
	}

	@Override
	public Integer findUserByEmail(String email) {
		return userRepository.findUserByEmail(email);
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

		if (user.getRoles() != null) {
			for(Role role: user.getRoles()) {
				if(role.getName().equalsIgnoreCase(RoleEnum.PROFESSOR.value)) {
					response.setRoleId(role.getId());
					return response;
				}else if(role.getName().equalsIgnoreCase(RoleEnum.STUDENT.value)) {
					response.setRoleId(role.getId());
					return response;
				}
			}
		}
		return null;
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
