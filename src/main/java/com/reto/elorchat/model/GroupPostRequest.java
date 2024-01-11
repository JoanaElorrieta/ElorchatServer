package com.reto.elorchat.model;

public class GroupPostRequest {
	
	private String name;
	private GroupTypeEnum type;
	
	public GroupPostRequest() {}

	public GroupPostRequest(String name, GroupTypeEnum type) {
		super();
		this.name = name;
		this.type = type;
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
	
	
}
