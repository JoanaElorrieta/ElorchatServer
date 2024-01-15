package com.reto.elorchat.repository;

import org.springframework.data.repository.CrudRepository;

import com.reto.elorchat.model.persistence.Message;

public interface MessageRepository extends CrudRepository<Message,Integer>{

}
