package com.sra.designpatterns.structural.decorator;

import com.sra.designpatterns.structural.decorator.classes.PlainPizza;
import com.sra.designpatterns.structural.decorator.interfaces.Pizza;

public class Restaurant {

	public static void main(String[] args) {
		Pizza pizza = new PlainPizza();

		// 1st customer need cheese pizza
		Pizza cheesePizza = new CheeseDecorator(pizza);

		System.out.println("cheesePizza total>>>> " + cheesePizza.getAmount());

		// 2nd customer need butter pizza
		Pizza butterPizza = new ButterDecorator(pizza);
		System.out.println("butterPizza total>>>> " + butterPizza.getAmount());

	}

}
