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
	
}
