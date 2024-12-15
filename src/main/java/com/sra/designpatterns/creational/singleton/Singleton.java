package com.sra.designpatterns.creational.singleton;

public class Singleton {

	/*
	 * 1.create private static instance of the class
	 * 
	 *  2.make the constructor as private
	 * 
	 *  3.write static method as getInstance() 
	 *  
	 *  4. call from main method
	 * 
	 * 
	 * 
	 */

	private static Singleton singleton;

	private Singleton() {

	}

	private static Singleton getInstance() {
		if (singleton == null) {
			singleton=new Singleton();
			return singleton;
		}
		return singleton;
	}

	public static void main(String args[]) {
		Singleton singleton1 = new Singleton().getInstance();
		Singleton singleton2 = new Singleton().getInstance();

		System.out.println("is both objects are equal ??>>>>>"+(singleton1 == singleton2));
	}

}
