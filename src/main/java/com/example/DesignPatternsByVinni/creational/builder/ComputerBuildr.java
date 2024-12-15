package com.example.DesignPatternsByVinni.creational.builder;

public class ComputerBuildr {

	private String storage;

	private String cpu;

	private String ram;
	
	public ComputerBuildr setStorage(String storage) {
		this.storage=storage;
		return this;
	}
	
	public ComputerBuildr setCpu(String cpu) {
		this.cpu=cpu;
		return this;
	}
	
	public ComputerBuildr setRam(String ram) {
		this.ram=ram;
		return this;
	}
	
	public Computer buildComputer() {
		return new Computer(storage,cpu,ram);
	}

}
