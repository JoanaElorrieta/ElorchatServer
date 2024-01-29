package com.reto.elorchat.security.persistance;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.reto.elorchat.model.persistence.Chat;
import com.reto.elorchat.model.persistence.Message;
import com.reto.elorchat.model.persistence.Role;

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
public class User implements UserDetails {
	private static final long serialVersionUID = 1L;

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
	@Column(name="phone_number1")
	private Long phoneNumber1;
	@Column
	private String photo;

	@ManyToMany(cascade = {
			CascadeType.PERSIST,
			CascadeType.MERGE
	})
	//NECESITO ESTO PARA QUE CUANDO LLAME AL USER TENGA LAS ROOMS DONDE METERLE
	//fetch = FetchType.EAGER
	@JoinTable(name = "user_chat",
	joinColumns = @JoinColumn(name = "user_id"),
	inverseJoinColumns = @JoinColumn(name = "chat_id"))
	private List<Chat> chats;


	@OneToMany(mappedBy = "user", orphanRemoval = true, fetch = FetchType.LAZY)
	@JsonBackReference
	@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
	private List<Message> messages;

	@ManyToMany(cascade = {
			CascadeType.PERSIST,
			CascadeType.MERGE
	})
	@JoinTable(name = "user_role",
	joinColumns = @JoinColumn(name = "user_id"),
	inverseJoinColumns = @JoinColumn(name = "role_id"))
	private List<Role> roles;

	public User() {}

	public User(String name, String surname, String email) {
		super();
		this.name = name;
		this.surname = surname;
		this.email = email;
	}

	public User(Integer id, String name, String surname, String email, Long phoneNumber1, String photo) {
		super();
		this.id = id;
		this.name = name;
		this.surname = surname;
		this.email = email;
		this.phoneNumber1 = phoneNumber1;
		this.photo = photo;
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

	public Long getPhoneNumber1() {
		return phoneNumber1;
	}

	public void setPhoneNumber1(Long phoneNumber1) {
		this.phoneNumber1 = phoneNumber1;
	}

	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public List<Chat> getChats() {
		//PARA NO TENERLO A EAGER PERO NO ME DEJA
		//Hibernate.initialize(chats);
		return chats;
	}

	public void setChats(List<Chat> chats) {
		this.chats = chats;
	}

	public List<Message> getMessages() {
		return messages;
	}

	public void setMessages(List<Message> messages) {
		this.messages = messages;
	}

	public List<Role> getRoles() {
		return roles;
	}

	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return password;
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return email;
	}

	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", name=" + name + ", surname=" + surname + ", email=" + email + ", password="
				+ password + ", phoneNumber1=" + phoneNumber1 + ", photo=" + photo + ", chats=" + chats + ", messages="
				+ messages + ", roles=" + roles + "]";
	}

}
