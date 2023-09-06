package com.federal.fedmobilesmecore.model;

public class InitTransactionRespModel {
	public boolean status;
	public String description;
	public String transactionId;

	public boolean getStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	@Override
	public String toString() {
		return "InitTransactionRespModel [status=" + status + ", description=" + description + ", transactionId="
				+ transactionId + "]";
	}

}
