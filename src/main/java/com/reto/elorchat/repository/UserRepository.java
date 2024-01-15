package com.reto.elorchat.repository;

import org.springframework.data.repository.CrudRepository;

import com.reto.elorchat.model.persistence.User;


public interface UserRepository extends CrudRepository<User, Integer>{

}
