package com.sra.designpatterns.creational.factory;

import com.sra.designpatterns.creational.factory.classes.Cat;
import com.sra.designpatterns.creational.factory.classes.Dog;
import com.sra.designpatterns.creational.factory.interfaces.Animal;

public class AnimalFactory {

	public Animal getAnimal(String type) {
		if (type.equalsIgnoreCase("dog")) {
			return new Dog();
		} else if (type.equalsIgnoreCase("cat")) {
			return new Cat();
		} else {
			return null;
		}
		//// when we need to more animals we will create here and returns

	}

}
