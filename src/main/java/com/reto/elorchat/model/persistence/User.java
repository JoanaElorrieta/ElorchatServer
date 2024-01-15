package com.reto.elorchat.model.persistence;

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
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
@Entity
@Table(name = "users")
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	@Column(length=60)
	private String name;
	@Column(length=150)
	private String surname;
	@Column(length=60)
	private String email;
	@Column
	private String password;
	@Column(length=15)
	private int phone_Number1;
	@Column(length=15)
	private int phone_Number2;
	@Column
	private String address;
	@Column
	private String photo;
	@Column
	private Boolean FCTDUAL;


	@ManyToMany(cascade = {
			CascadeType.PERSIST,
			CascadeType.MERGE
	})
	@JoinTable(name = "user_chat",
	joinColumns = @JoinColumn(name = "user_id"),
	inverseJoinColumns = @JoinColumn(name = "chat_id")
			)
	private List<Chat> chats;


	@OneToMany(mappedBy = "user", orphanRemoval = true, fetch = FetchType.LAZY)
	@JsonBackReference
	@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
	private List<Message> messages;

	public User() {}

	public User(String name, String surname, String email, int phoneNumber1) {
		super();
		this.name = name;
		this.surname = surname;
		this.email = email;
		this.phone_Number1 = phoneNumber1;
	}

	public User(Integer id, String name, String surname, String email, int phoneNumber1) {
		super();
		this.id = id;
		this.name = name;
		this.surname = surname;
		this.email = email;
		this.phone_Number1 = phoneNumber1;
	}


	public User(Integer id, String name, String surname, String email, int phoneNumber1, List<Chat> chats,
			List<Message> messages) {
		super();
		this.id = id;
		this.name = name;
		this.surname = surname;
		this.email = email;
		this.phone_Number1 = phoneNumber1;
		this.chats = chats;
		this.messages = messages;
	}

	public List<Message> getMessages() {
		return messages;
	}

	public void setMessages(List<Message> messages) {
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

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public int getPhoneNumber1() {
		return phone_Number1;
	}

	public void setPhoneNumber1(int phoneNumber1) {
		this.phone_Number1 = phoneNumber1;
	}

	public List<Chat> getChats() {
		return chats;
	}

	public void setChats(List<Chat> chats) {
		this.chats = chats;
	}



}
