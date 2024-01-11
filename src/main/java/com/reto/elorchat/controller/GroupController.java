package com.reto.elorchat.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.reto.elorchat.model.Group;
import com.reto.elorchat.model.GroupPostRequest;
import com.reto.elorchat.repository.GroupRepository;

@RestController
@RequestMapping("api")
public class GroupController {
	@Autowired
	private GroupRepository groupRepository;
	
	@GetMapping("/groups")
	public ResponseEntity<Iterable<Group>> getGroups(){
		return new ResponseEntity<Iterable<Group>>(groupRepository.findAll(),HttpStatus.OK);
	}
	
	@PostMapping("/groups")
	public ResponseEntity<Group> createGroup(@RequestBody GroupPostRequest groupPostRequest){

		Group group = new Group (
				groupPostRequest.getName(),
				groupPostRequest.getType()
				);
		groupRepository.save(group);
		return new ResponseEntity<Group>(HttpStatus.CREATED);
	}
	@DeleteMapping("/groups/{id}")
	public ResponseEntity<Integer> deleteGroupById(@PathVariable("id") Integer id){
		try {
			groupRepository.deleteById(id);
			return new ResponseEntity<Integer>(HttpStatus.NO_CONTENT);
		}catch(EmptyResultDataAccessException e) {
			throw new ResponseStatusException(HttpStatus.NO_CONTENT, "Grupo no encontrado");
		}
	}
}
