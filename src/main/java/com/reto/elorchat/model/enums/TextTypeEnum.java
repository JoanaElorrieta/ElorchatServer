package com.reto.elorchat.model.enums;

public enum TextTypeEnum {
	TEXT("TEXT"),
	FILE("FILE");
	
	public final String value;

	private TextTypeEnum(String value) {
		this.value = value;
	}
}
