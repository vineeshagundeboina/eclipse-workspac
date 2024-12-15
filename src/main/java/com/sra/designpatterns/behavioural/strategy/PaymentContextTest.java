package com.sra.designpatterns.behavioural.strategy;

public class PaymentContextTest {

	public static void main(String[] args) {
		PaymentContext paymentContext = new PaymentContext(new PaypalPayment());
		paymentContext.makePayment(100);

		paymentContext = new PaymentContext(new CreditCardPayment());
		paymentContext.makePayment(200);

	}
}
