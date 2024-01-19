package com.reto.elorchat.model.enums;

public enum RoleEnum {
	
	STUDENT("Estudiante"),
	PROFESSOR("Profesor");

	public final String value;

	private RoleEnum(String value) {
		this.value = value;
	}
}
