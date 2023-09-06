package com.federal.fedmobilesmecore.model;

import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class InitTransactionReqModel {
	public String mode;
	public String userName;
	public String password;
	public String msgType;
	public String channel;
	public String sender;
	public String senderRef;
	public String msgDate;
	public String currCode;
	public String remCustId;
	public String remMmid;
	@NotBlank
	@NotNull
	@NotEmpty
	public BigDecimal amount;
	public String remAccNo;
	public String remName;
	public String benAccNo;
	public String benName;
	public String benNickname;
	public String benAccType;
	public String benIfsc;
	public String benAadhar;
	public String senderToReciever;
	public String prefCorp;
	public String remarks;
	public boolean isQuickPay;
	public String remMobNo;
	public String benMmid;
	public String benMobNo;
	public String mobileNo;
	public String authToken;
	public Timestamp timestamp;

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getMsgType() {
		return msgType;
	}

	public void setMsgType(String msgType) {
		this.msgType = msgType;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public String getSenderRef() {
		return senderRef;
	}

	public void setSenderRef(String senderRef) {
		this.senderRef = senderRef;
	}

	public String getMsgDate() {
		return msgDate;
	}

	public void setMsgDate(String msgDate) {
		this.msgDate = msgDate;
	}

	public String getCurrCode() {
		return currCode;
	}

	public void setCurrCode(String currCode) {
		this.currCode = currCode;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public String getRemAccNo() {
		return remAccNo;
	}

	public void setRemAccNo(String remAccNo) {
		this.remAccNo = remAccNo;
	}

	public String getRemName() {
		return remName;
	}

	public void setRemName(String remName) {
		this.remName = remName;
	}

	public String getBenAccNo() {
		return benAccNo;
	}

	public void setBenAccNo(String benAccNo) {
		this.benAccNo = benAccNo;
	}

	public String getBenName() {
		return benName;
	}

	public void setBenName(String benName) {
		this.benName = benName;
	}

	public String getBenNickname() {
		return benNickname;
	}

	public void setBenNickname(String benNickname) {
		this.benNickname = benNickname;
	}

	public String getBenAccType() {
		return benAccType;
	}

	public void setBenAccType(String benAccType) {
		this.benAccType = benAccType;
	}

	public String getBenIfsc() {
		return benIfsc;
	}

	public void setBenIfsc(String benIfsc) {
		this.benIfsc = benIfsc;
	}

	public String getSenderToReciever() {
		return senderToReciever;
	}

	public void setSenderToReciever(String senderToReciever) {
		this.senderToReciever = senderToReciever;
	}

	public String getPrefCorp() {
		return prefCorp;
	}

	public void setPrefCorp(String prefCorp) {
		this.prefCorp = prefCorp;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public boolean isQuickPay() {
		return isQuickPay;
	}

	public void setQuickPay(boolean isQuickPay) {
		this.isQuickPay = isQuickPay;
	}

	public String getRemMobNo() {
		return remMobNo;
	}

	public void setRemMobNo(String remMobNo) {
		this.remMobNo = remMobNo;
	}

	public String getBenMmid() {
		return benMmid;
	}

	public void setBenMmid(String benMmid) {
		this.benMmid = benMmid;
	}

	public String getBenMobNo() {
		return benMobNo;
	}

	public void setBenMobNo(String benMobNo) {
		this.benMobNo = benMobNo;
	}

	public String getMobileNo() {
		return mobileNo;
	}

	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}

	public String getAuthToken() {
		return authToken;
	}

	public void setAuthToken(String authToken) {
		this.authToken = authToken;
	}

	public Timestamp getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Timestamp timestamp) {
		this.timestamp = timestamp;
	}

	public String getRemCustId() {
		return remCustId;
	}

	public void setRemCustId(String remCustId) {
		this.remCustId = remCustId;
	}

	public String getRemMmid() {
		return remMmid;
	}

	public void setRemMmid(String remMmid) {
		this.remMmid = remMmid;
	}

	public String getBenAadhar() {
		return benAadhar;
	}

	public void setBenAadhar(String benAadhar) {
		this.benAadhar = benAadhar;
	}

	@Override
	public String toString() {
		return "InitTransactionReqModel [mode=" + mode + ", userName=" + userName + ", password=" + password
				+ ", msgType=" + msgType + ", channel=" + channel + ", sender=" + sender + ", senderRef=" + senderRef
				+ ", msgDate=" + msgDate + ", currCode=" + currCode + ", remCustId=" + remCustId + ", remMmid="
				+ remMmid + ", amount=" + amount + ", remAccNo=" + remAccNo + ", remName=" + remName + ", benAccNo="
				+ benAccNo + ", benName=" + benName + ", benAccType=" + benAccType + ", benIfsc=" + benIfsc
				+ ", benAadhar=" + benAadhar + ", senderToReciever=" + senderToReciever + ", prefCorp=" + prefCorp
				+ ", remarks=" + remarks + ", isQuickPay=" + isQuickPay + ", remMobNo=" + remMobNo + ", benMmid="
				+ benMmid + ", benMobNo=" + benMobNo + ", mobileNo=" + mobileNo + ", authToken=" + authToken
				+ ", timestamp=" + timestamp + "]";
	}

}