package com.sra.designpatterns.creational.factory.classes;

import com.sra.designpatterns.creational.factory.interfaces.Animal;

public class Cat implements Animal {

	@Override
	public void purchaseAnimal() {
		
		System.out.println("Iam cat");
		
	}

}
