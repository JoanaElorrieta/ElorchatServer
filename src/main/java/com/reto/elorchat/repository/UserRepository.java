package com.reto.elorchat.repository;

import org.springframework.data.repository.CrudRepository;

import com.reto.elorchat.model.User;


public interface UserRepository extends CrudRepository<User, Integer>{

}
