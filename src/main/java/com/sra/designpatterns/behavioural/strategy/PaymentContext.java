package com.sra.designpatterns.behavioural.strategy;

public class PaymentContext {

	private Payment payment;

	public PaymentContext(Payment payment) {
		this.payment = payment;
	}

	public void makePayment(int amount) {
		this.payment.pay(amount);
	}
}
