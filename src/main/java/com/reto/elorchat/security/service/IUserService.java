package com.reto.elorchat.security.service;

import java.util.List;

import com.reto.elorchat.model.service.UserDTO;
import com.reto.elorchat.security.persistance.User;

public interface IUserService {
	Iterable<User> findAll();
	UserDTO findById(Integer id);
	List<UserDTO> findAllUsersByChatId(Integer chatId);

}
