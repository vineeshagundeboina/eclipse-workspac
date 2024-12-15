package com.sra.designpatterns.structural.decorator;

import com.sra.designpatterns.structural.decorator.interfaces.Pizza;

public class CheeseDecorator implements Pizza {

	private Pizza pizza;

	public CheeseDecorator(Pizza pizza) {
		this.pizza = pizza;
	}

	@Override
	public String getDescription() {
		return this.pizza.getDescription() + " Cheese";
	}

	@Override
	public long getAmount() {
		return this.pizza.getAmount() + 2;
	}

}
