package com.reto.elorchat.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.reto.elorchat.model.persistence.Message;

public interface MessageRepository extends CrudRepository<Message,Integer>{
	
	@Query("SELECT m FROM Message m WHERE m.chatId = :chatId")
    Iterable<Message> findAllMessagesByChatId(@Param("chatId") Integer chatId);

}
