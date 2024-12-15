package com.sra.designpatterns.structural.decorator.classes;

import com.sra.designpatterns.structural.decorator.interfaces.Pizza;

public class PlainPizza implements Pizza {

	@Override
	public String getDescription() {
		return "Plain Pizza";
	}

	@Override
	public long getAmount() {
		return 5;
	}

}
