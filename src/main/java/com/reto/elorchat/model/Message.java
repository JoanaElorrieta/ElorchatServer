package com.reto.elorchat.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
@Entity
@Table(name = "messages")
public class Message {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	@Column
	private String text;
	@Column
	private Date date;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "group_id", foreignKey = @ForeignKey(name = "Fk_group_id" ))
	@JsonManagedReference
	@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
	private Group group;
	
	@Column(name = "group_id", insertable = false, updatable = false)
	private Integer groupId;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "Fk_user_id" ))
	@JsonManagedReference
	@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
	private User user;
	
	@Column(name = "user_id", insertable = false, updatable = false)
	private Integer userId;
	
	public Message () {}

	public Message(String text, Date date) {
		super();
		this.text = text;
		this.date = date;
	}

	public Message(String text, Date date, Group group, Integer groupId) {
		super();
		this.text = text;
		this.date = date;
		this.group = group;
		this.groupId= groupId;
	}
	public Message(Integer id, String text, Date date, Group group, Integer groupId) {
		super();
		this.id = id;
		this.text = text;
		this.date = date;
		this.group = group;
		this.groupId= groupId;
	}

	public Message(Integer id, String text, Date date, Group group, Integer groupId, User user, Integer userId) {
		super();
		this.id = id;
		this.text = text;
		this.date = date;
		this.group = group;
		this.groupId = groupId;
		this.user = user;
		this.userId = userId;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Group getGroup() {
		return group;
	}

	public void setGroup(Group group) {
		this.group = group;
	}

	public Integer getGroupId() {
		return groupId;
	}

	public void setGroupId(Integer groupId) {
		this.groupId = groupId;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	
	
}
