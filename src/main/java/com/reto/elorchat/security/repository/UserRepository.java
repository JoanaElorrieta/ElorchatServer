package com.reto.elorchat.security.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.reto.elorchat.security.persistance.User;


public interface UserRepository extends CrudRepository<User, Integer>{
	Optional<User> findByEmail(String email);
	
	@Query("SELECT u FROM User u LEFT JOIN FETCH u.chats WHERE u.id = :id")
    Optional<User> findUserWithChatsById(@Param("id") Integer idUser);
}
