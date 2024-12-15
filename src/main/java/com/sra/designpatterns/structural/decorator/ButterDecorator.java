package com.sra.designpatterns.structural.decorator;

import com.sra.designpatterns.structural.decorator.interfaces.Pizza;

public class ButterDecorator implements Pizza {

	private Pizza pizza;

	public ButterDecorator(Pizza pizza) {
		this.pizza = pizza;
	}

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return this.pizza.getDescription() + " Butter";
	}

	@Override
	public long getAmount() {
		return this.pizza.getAmount() + 40;
	}

}
