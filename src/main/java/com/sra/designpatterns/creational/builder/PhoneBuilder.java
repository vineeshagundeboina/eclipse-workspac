package com.sra.designpatterns.creational.builder;

import com.sra.designpatterns.creational.builder.classes.Phone;

public class PhoneBuilder {

	private String name;
	private String model;
	private String os;
	private String ram;
	private String battery;
	private String storage;

	public PhoneBuilder setName(String name) {
		this.name = name;
		return this;

	}

	public PhoneBuilder setModel(String model) {
		this.model = model;
		return this;
	}

	public PhoneBuilder setOs(String os) {
		this.os = os;
		return this;
	}

	public PhoneBuilder setRam(String ram) {
		this.ram = ram;
		return this;
	}

	public PhoneBuilder setBattery(String battery) {
		this.battery = battery;
		return this;
	}

	public PhoneBuilder setStorage(String storage) {
		this.storage = storage;
		return this;
	}

	public Phone buildPhone() {
		return new Phone(name, model, os, ram, battery, storage);
	}

}
