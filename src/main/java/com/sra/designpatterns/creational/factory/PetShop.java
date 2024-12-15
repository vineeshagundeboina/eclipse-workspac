package com.sra.designpatterns.creational.factory;

public class PetShop {
	public static void main(String args[]) {

		AnimalFactory animalFactory=new AnimalFactory();
		//1st customer need to dog
		System.out.print("buy dog >>>>>> ");
		animalFactory.getAnimal("dog").purchaseAnimal();
		System.out.println();
		
		//2nd customer need cat
		System.out.print("buy cat >>>>>> ");
		animalFactory.getAnimal("cat").purchaseAnimal();
		
	}

}
