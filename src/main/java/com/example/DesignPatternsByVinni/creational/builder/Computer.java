package com.example.DesignPatternsByVinni.creational.builder;

public class Computer {
	
	private String storage;
	
	private String cpu;
	
	private String ram;
	
	public Computer(String storage,String cpu,String ram){
		this.storage=storage;
		this.cpu=cpu;
		this.ram=ram;
		
	}

	@Override
	public String toString() {
		return "Computer [storage=" + storage + ", cpu=" + cpu + ", ram=" + ram + "]";
	}
	
	
	
	
}
