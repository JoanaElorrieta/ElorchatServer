package com.reto.elorchat.security.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.reto.elorchat.security.persistance.User;
import com.reto.elorchat.security.repository.UserRepository;


@Service
public class UserService implements IUserService, UserDetailsService {

	@Autowired
	UserRepository userRepository;

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

}
