package com.reto.elorchat.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;

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
@Table(name = "groups")
public class Group {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	@Column(length=60)
	private String name;
	@Column
	private GroupTypeEnum type;
	
	@ManyToMany(mappedBy = "groups")
	private List<User> users= new ArrayList<>();
    
    @ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "Fk_user_id" ))
	@JsonManagedReference
	@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
	private User admin;
	
	@Column(name = "user_id", insertable = false, updatable = false)
	private Integer adminId;
	
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

	public Group(Integer id, String name, GroupTypeEnum type, List<User> users, User admin, Integer adminId,
			List<Message> messages) {
		super();
		this.id = id;
		this.name = name;
		this.type = type;
		this.users = users;
		this.admin = admin;
		this.adminId = adminId;
		this.messages = messages;
	}
		
}
