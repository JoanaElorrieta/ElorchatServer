package com.reto.elorchat.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
@Entity
@Table(name = "groups")
public class Group {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	@Column(length=60)
	private String name;
	@Column
	private GroupTypeEnum type;
	
	@OneToMany(mappedBy = "group", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	@JsonBackReference
	@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
	private List<User> users;
	
	@OneToMany(mappedBy = "group", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	@JsonBackReference
	@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
	private List<Message> messages;
	
	public Group() {}

	public Group(String name, GroupTypeEnum type) {
		super();
		this.name = name;
		this.type = type;
	}

	public Group(Integer id, String name, GroupTypeEnum type) {
		super();
		this.id = id;
		this.name = name;
		this.type = type;
	}

	public Group(Integer id, String name, GroupTypeEnum type, List<User> users, List<Message> messages) {
		super();
		this.id = id;
		this.name = name;
		this.type = type;
		this.users = users;
		this.messages = messages;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public GroupTypeEnum getType() {
		return type;
	}

	public void setType(GroupTypeEnum type) {
		this.type = type;
	}

	public List<User> getUsers() {
		return users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}

	public List<Message> getMessages() {
		return messages;
	}

	public void setMessages(List<Message> messages) {
		this.messages = messages;
	}
		
}
