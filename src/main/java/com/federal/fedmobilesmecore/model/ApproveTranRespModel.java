package com.federal.fedmobilesmecore.model;

import com.federal.fedmobilesmecore.dto.FundTransfer;
import com.federal.fedmobilesmecore.dto.ImpsTransfer;
import com.federal.fedmobilesmecore.dto.ScheduledTransaction;

public class ApproveTranRespModel {
	public boolean status;
	public String description;
	private String message;
	private FundTransfer fundTransfer;
	private ImpsTransfer impsTransfer;
	private ScheduledTransaction scheduledTransaction;

	public boolean isStatus() {
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

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public FundTransfer getFundTransfer() {
		return fundTransfer;
	}

	public void setFundTransfer(FundTransfer fundTransfer) {
		this.fundTransfer = fundTransfer;
	}

	public ImpsTransfer getImpsTransfer() {
		return impsTransfer;
	}

	public void setImpsTransfer(ImpsTransfer impsTransfer) {
		this.impsTransfer = impsTransfer;
	}

	public ScheduledTransaction getScheduledTransaction() {
		return scheduledTransaction;
	}

	public void setScheduledTransaction(ScheduledTransaction scheduledTransaction) {
		this.scheduledTransaction = scheduledTransaction;
	}

	@Override
	public String toString() {
		return "ApproveTranRespModel [status=" + status + ", description=" + description + ", message=" + message
				+ ", fundTransfer=" + fundTransfer + ", impsTransfer=" + impsTransfer + ", scheduledTransaction="
				+ scheduledTransaction + "]";
	}
	
}
