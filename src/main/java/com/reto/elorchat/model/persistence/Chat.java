package com.reto.elorchat.model.persistence;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.reto.elorchat.model.enums.ChatTypeEnum;
import com.reto.elorchat.security.persistance.User;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
@Entity
@Table(name = "chats")
public class Chat{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(length=60)
	private String name;

	@Column
	private ChatTypeEnum type;

	@ManyToMany(mappedBy = "chats")
	private List<User> users= new ArrayList<>();

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "Fk_user_id" ))
	@JsonManagedReference
	@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
	private User admin;

	@Column(name = "user_id", insertable = false, updatable = false)
	private Integer adminId;

	@OneToMany(mappedBy = "chat", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	@JsonBackReference
	@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
	private List<Message> messages;

	@Column
	private Date created;

	@Column
	private Date deleted;


	public Chat() {}

	public Chat(String name, ChatTypeEnum type, Integer adminId, Date created, Date deleted) {
		super();
		this.name = name;
		this.type = type;
		this.adminId = adminId;
		this.created = created;
		this.deleted = deleted;
	}

	public Chat(Integer id, String name, ChatTypeEnum type, Integer adminId, Date created, Date deleted) {
		super();
		this.id = id;
		this.name = name;
		this.type = type;
		this.adminId = adminId;
		this.created = created;
		this.deleted = deleted;
	}

	public Chat(Integer id, String name, ChatTypeEnum type, List<User> users, User admin, Integer adminId,
			List<Message> messages,  Date created, Date deleted) {
		super();
		this.id = id;
		this.name = name;
		this.type = type;
		this.users = users;
		this.admin = admin;
		this.adminId = adminId;
		this.messages = messages;
		this.created = created;
		this.deleted = deleted;
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

	public ChatTypeEnum getType() {
		return type;
	}

	public void setType(ChatTypeEnum type) {
		this.type = type;
	}

	public List<User> getUsers() {
		return users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}

	public User getAdmin() {
		return admin;
	}

	public void setAdmin(User admin) {
		this.admin = admin;
	}

	public Integer getAdminId() {
		return adminId;
	}

	public void setAdminId(Integer adminId) {
		this.adminId = adminId;
	}

	public List<Message> getMessages() {
		return messages;
	}

	public void setMessages(List<Message> messages) {
		this.messages = messages;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public Date getDeleted() {
		return deleted;
	}

	public void setDeleted(Date deleted) {
		this.deleted = deleted;
	}
	
	public boolean isChatPrivate(Chat chat) {
		if (chat.getType() == ChatTypeEnum.PRIVATE) {
			System.out.println("Es privado");
			return true;
		}
		return false;
	}

}
