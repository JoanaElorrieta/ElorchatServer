package com.reto.elorchat.security.service;

import java.util.List;

import com.reto.elorchat.model.service.UserDTO;

public interface IUserService {
	List<UserDTO> findAll(Integer id);
	UserDTO findById(Integer id);
	List<UserDTO> findAllUsersByChatId(Integer chatId);
	Integer findUserByEmail(String email);
	Integer resetPassword(String email);

}
