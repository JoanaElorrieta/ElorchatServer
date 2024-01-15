package com.reto.elorchat.security.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.reto.elorchat.security.persistance.User;


public interface UserRepository extends CrudRepository<User, Integer>{
	Optional<User> findByEmail(String email);
}
