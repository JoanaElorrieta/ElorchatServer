package com.reto.elorchat.security.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.reto.elorchat.model.controller.request.EmailRequest;
import com.reto.elorchat.model.enums.RoleEnum;
import com.reto.elorchat.model.persistence.Chat;
import com.reto.elorchat.model.persistence.Role;
import com.reto.elorchat.model.service.ChatDTO;
import com.reto.elorchat.model.service.UserChatInfoDTO;
import com.reto.elorchat.model.service.UserDTO;
import com.reto.elorchat.repository.ChatRepository;
import com.reto.elorchat.security.persistance.User;
import com.reto.elorchat.security.repository.UserRepository;
import com.reto.elorchat.service.EmailService;

import jakarta.transaction.Transactional;

@Transactional
@Service
public class UserService implements IUserService, UserDetailsService {

	@Autowired
	UserRepository userRepository;

	@Autowired
	ChatRepository chatRepository;

	@Autowired
	EmailService emailService;

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
	public List<UserDTO> findAll(Integer id) {

		List<UserDTO> response = new ArrayList<UserDTO>();


		if(id == 0) {
			Iterable<User> listUser = userRepository.findAll();
			for(User actualUser: listUser) {
				response.add(convertFromUserDAOToDTOWithRolesAndChatInfo(actualUser));
			}
		}else {
			Iterable<User> listUser = userRepository.findAllUsersCreatedAfterId(id);
			for(User actualUser: listUser) {
				response.add(convertFromUserDAOToDTOWithRolesAndChatInfo(actualUser));
			}
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
		Integer found=userRepository.findUserByEmail(email);
		return found;
	}


	@Transactional
	@Override
	public Integer resetPassword(String email) {
		System.out.println("Email "+email);
		EmailRequest emailBody = new EmailRequest();
		String password=emailBody.getPassword();
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		String encodePassword = passwordEncoder.encode(password);
		try {
			Integer reset =userRepository.resetPassword(email, encodePassword);
			String emailText=emailBody.getContent()+""+password;
			if ( reset == 1) {
				emailService.sendEmailTool(emailText, emailBody.getEmail(), emailBody.getSubject());
			} 
			return reset;
		} catch (Exception e) {
			System.out.println("Error reseteando la password");
			throw new RuntimeException(e);
		}

	}

	//ASK
	//AQUI TAMBIEN HAGO? PREGUNTAR PQ ENTONCES EN AUTH CONTROLLER LO CASTEAMOS AL MODELO DE HIBERNATE Y NO AL DEL CONTROLADOR
	//POR EL USERDETAILS? PERO EN DEPENDIENDO PARA QUE NO NECESITO QUE LO IMPLEMENTE, VERDAD?
	//CONVERTS
	private UserDTO convertFromUserDAOToDTOWithRolesAndChatInfo(User user) {

		UserDTO response = new UserDTO(
				user.getId(),
				user.getName(),
				user.getSurname(),
				user.getEmail(),
				user.getPhoneNumber1(),
				user.getPhoto());

		if (user.getChats() != null) {
			List<ChatDTO> chatList = new ArrayList<ChatDTO>();
			List<UserChatInfoDTO> userChatInfoList = new ArrayList<UserChatInfoDTO>();
			for(Chat chat: user.getChats()) {
				chatList.add(convertFromChatDAOToDTO(chat));
				UserChatInfoDTO userChatInfo = getUserChatInfoFromUser(chat.getId(), user.getId());
				userChatInfoList.add(userChatInfo);
			}
			response.setChats(chatList);
			response.setUserChatInfo(userChatInfoList);
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

	private UserChatInfoDTO getUserChatInfoFromUser(Integer idChat, Integer idUser) {
		return userRepository.findUsersJoinedAndDeletedFromChat(idChat, idUser)
				.orElseThrow(() -> new UsernameNotFoundException("User " + idUser + " not found"));

	}

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
				chat.getAdminId(),
				chat.getCreated(),
				chat.getDeleted()
				);

		return response;
	}
	/////
}
