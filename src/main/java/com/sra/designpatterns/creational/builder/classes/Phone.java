package com.sra.designpatterns.creational.builder.classes;

import lombok.Data;

public class Phone {

	private String name;
	private String model;
	private String os;
	private String ram;
	private String battery;
	private String storage;
	
	public Phone(String name, String model, String os, String ram, String battery, String storage) {
		super();
		this.name = name;
		this.model = model;
		this.os = os;
		this.ram = ram;
		this.battery = battery;
		this.storage = storage;
	}

	@Override
	public String toString() {
		return "Phone [name=" + name + ", model=" + model + ", os=" + os + ", ram=" + ram + ", battery=" + battery
				+ ", storage=" + storage + "]";
	}
	
	
}
