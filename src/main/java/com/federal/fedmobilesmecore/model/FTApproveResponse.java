package com.federal.fedmobilesmecore.model;

public class FTApproveResponse extends SMEMessage {

	private String method;
	private String senderrefid;
	private Double transferamount;
	private String responsecode;
	private String reason;
	private String accountbalance;
	private String craccountbalance;
	private String transactionid;
	private String transactioncode;
	private String transactionmessage;

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getSenderrefid() {
		return senderrefid;
	}

	public void setSenderrefid(String senderrefid) {
		this.senderrefid = senderrefid;
	}

	public Double getTransferamount() {
		return transferamount;
	}

	public void setTransferamount(Double transferamount) {
		this.transferamount = transferamount;
	}

	public String getResponsecode() {
		return responsecode;
	}

	public void setResponsecode(String responsecode) {
		this.responsecode = responsecode;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getAccountbalance() {
		return accountbalance;
	}

	public void setAccountbalance(String accountbalance) {
		this.accountbalance = accountbalance;
	}

	public String getCraccountbalance() {
		return craccountbalance;
	}

	public void setCraccountbalance(String craccountbalance) {
		this.craccountbalance = craccountbalance;
	}

	public String getTransactionid() {
		return transactionid;
	}

	public void setTransactionid(String transactionid) {
		this.transactionid = transactionid;
	}

	public String getTransactioncode() {
		return transactioncode;
	}

	public void setTransactioncode(String transactioncode) {
		this.transactioncode = transactioncode;
	}

	public String getTransactionmessage() {
		return transactionmessage;
	}

	public void setTransactionmessage(String transactionmessage) {
		this.transactionmessage = transactionmessage;
	}

	@Override
	public String toString() {
		return "FTApproveResponse [method=" + method + ", senderrefid=" + senderrefid + ", transferamount="
				+ transferamount + ", responsecode=" + responsecode + ", reason=" + reason + ", accountbalance="
				+ accountbalance + ", craccountbalance=" + craccountbalance + ", transactionid=" + transactionid
				+ ", transactioncode=" + transactioncode + ", transactionmessage=" + transactionmessage + "]";
	}

}
