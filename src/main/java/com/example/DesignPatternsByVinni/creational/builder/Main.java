package com.example.DesignPatternsByVinni.creational.builder;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		ComputerBuildr computerBuildr = new ComputerBuildr();
		Computer iphone12 = computerBuildr.setCpu("i5").setRam("50kb").buildComputer();
		System.out.println("iphone12 has made as per customer request : "+iphone12.toString());
		
		Computer iphone14=computerBuildr.setCpu("i3").setRam("1mb").buildComputer();
		System.out.println("iphone14 has made as per customer request : "+iphone14.toString());

	}

}
