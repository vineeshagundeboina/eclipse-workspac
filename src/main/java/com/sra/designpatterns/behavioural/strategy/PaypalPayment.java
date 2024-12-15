package com.sra.designpatterns.behavioural.strategy;

public class PaypalPayment implements Payment{

	@Override
	public void pay(int  amount) {
		System.out.println("Payment done for the amount-> "+amount+" using Paypal ");
	}

}
