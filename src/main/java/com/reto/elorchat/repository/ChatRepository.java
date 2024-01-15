package com.reto.elorchat.repository;

import org.springframework.data.repository.CrudRepository;

import com.reto.elorchat.model.persistence.Chat;

public interface ChatRepository extends CrudRepository<Chat, Integer>{

}
