package com.federal.fedmobilesmecore.model;

import java.util.List;

public class SchedulePaymentMessage extends SMEMessage {

	List<MakerCheckerListGeneric> transactions;

	public List<MakerCheckerListGeneric> getTransactions() {
		return transactions;
	}

	public void setTransactions(List<MakerCheckerListGeneric> transactions) {
		this.transactions = transactions;
	}

	@Override
	public String toString() {
		return "SchedulePaymentMessage [transactions=" + transactions + "]";
	}
}
