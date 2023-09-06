package com.federal.fedmobilesmecore.model;

import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.federal.fedmobilesmecore.dto.ScheduledPayment;
import com.federal.fedmobilesmecore.dto.ScheduledTransaction;

public class TransactionMessage extends SMEMessage {

	List<ScheduledTransaction> transactions;

	List<ScheduledPayment> payments;

	public List<ScheduledTransaction> getTransactions() {
		return transactions;
	}

	public void setTransactions(List<ScheduledTransaction> transactions) {
		this.transactions = transactions;
	}

	public List<ScheduledPayment> getPayments() {
		return payments;
	}

	public void setPayments(List<ScheduledPayment> payments) {
		this.payments = payments;
	}
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);

	}
}
