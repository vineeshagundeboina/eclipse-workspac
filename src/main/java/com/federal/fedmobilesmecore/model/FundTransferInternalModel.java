package com.federal.fedmobilesmecore.model;

import com.federal.fedmobilesmecore.dto.FundTransfer;
import com.federal.fedmobilesmecore.dto.FundTransferLog;
import com.federal.fedmobilesmecore.dto.ImpsTransfer;

public class FundTransferInternalModel {
	private boolean status;
	private String description;
	private String message;
	private FundTransfer fundTransfer;
	private FundTransferLog fundTransferLog;
	private ImpsTransfer impsTransfer;

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

	public FundTransferLog getFundTransferLog() {
		return fundTransferLog;
	}

	public void setFundTransferLog(FundTransferLog fundTransferLog) {
		this.fundTransferLog = fundTransferLog;
	}

	public ImpsTransfer getImpsTransfer() {
		return impsTransfer;
	}

	public void setImpsTransfer(ImpsTransfer impsTransfer) {
		this.impsTransfer = impsTransfer;
	}

	@Override
	public String toString() {
		return "FundTransferInternalModel [status=" + status + ", description=" + description + ", message=" + message
				+ ", fundTransfer=" + fundTransfer + ", fundTransferLog=" + fundTransferLog + ", impsTransfer="
				+ impsTransfer + "]";
	}

}