package com.federal.fedmobilesmecore.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class NeftTransferReqModel {
	public String respUrl;
	public String userid;
	public String password;
	public String sendercd;
	public String tranDate;
	public String ReferenceId;
	public String Cust_Ref_No;
	public String debitAccountNumber;
	public String debitSuspenseAccount;
	public RemmiterDetails remmiterDetails;
	public BeneficiaryDetails beneficiaryDetails;
	public String Amount;
	public String Remarks;
	public String Alternative_Payments;
	public String Postpone;
	public String Sender_Data;

	public String getRespUrl() {
		return respUrl;
	}

	public void setRespUrl(String respUrl) {
		this.respUrl = respUrl;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getSendercd() {
		return sendercd;
	}

	public void setSendercd(String sendercd) {
		this.sendercd = sendercd;
	}

	public String getTranDate() {
		return tranDate;
	}

	public void setTranDate(String tranDate) {
		this.tranDate = tranDate;
	}

	public String getReferenceId() {
		return ReferenceId;
	}

	public void setReferenceId(String referenceId) {
		ReferenceId = referenceId;
	}

	public String getCust_Ref_No() {
		return Cust_Ref_No;
	}

	public void setCust_Ref_No(String cust_Ref_No) {
		Cust_Ref_No = cust_Ref_No;
	}

	public String getDebitAccountNumber() {
		return debitAccountNumber;
	}

	public void setDebitAccountNumber(String debitAccountNumber) {
		this.debitAccountNumber = debitAccountNumber;
	}

	public String getDebitSuspenseAccount() {
		return debitSuspenseAccount;
	}

	public void setDebitSuspenseAccount(String debitSuspenseAccount) {
		this.debitSuspenseAccount = debitSuspenseAccount;
	}

	public RemmiterDetails getRemmiterDetails() {
		return remmiterDetails;
	}

	public void setRemmiterDetails(RemmiterDetails remmiterDetails) {
		this.remmiterDetails = remmiterDetails;
	}

	public BeneficiaryDetails getBeneficiaryDetails() {
		return beneficiaryDetails;
	}

	public void setBeneficiaryDetails(BeneficiaryDetails beneficiaryDetails) {
		this.beneficiaryDetails = beneficiaryDetails;
	}

	public String getAmount() {
		return Amount;
	}

	public void setAmount(String amount) {
		Amount = amount;
	}

	public String getRemarks() {
		return Remarks;
	}

	public void setRemarks(String remarks) {
		Remarks = remarks;
	}

	public String getAlternative_Payments() {
		return Alternative_Payments;
	}

	public void setAlternative_Payments(String alternative_Payments) {
		Alternative_Payments = alternative_Payments;
	}

	public String getPostpone() {
		return Postpone;
	}

	public void setPostpone(String postpone) {
		Postpone = postpone;
	}

	public String getSender_Data() {
		return Sender_Data;
	}

	public void setSender_Data(String sender_Data) {
		Sender_Data = sender_Data;
	}

	public class RemmiterDetails {
		public String Name;
		public String AccNumber;
		public String Acctype;
		public String Mobile;
		public String Email;
		public String address;
		public String Notification_Flag;

		public String getName() {
			return Name;
		}

		public void setName(String name) {
			Name = name;
		}

		public String getAccNumber() {
			return AccNumber;
		}

		public void setAccNumber(String accNumber) {
			AccNumber = accNumber;
		}

		public String getAcctype() {
			return Acctype;
		}

		public void setAcctype(String acctype) {
			Acctype = acctype;
		}

		public String getMobile() {
			return Mobile;
		}

		public void setMobile(String mobile) {
			Mobile = mobile;
		}

		public String getEmail() {
			return Email;
		}

		public void setEmail(String email) {
			Email = email;
		}

		public String getAddress() {
			return address;
		}

		public void setAddress(String address) {
			this.address = address;
		}

		public String getNotification_Flag() {
			return Notification_Flag;
		}

		public void setNotification_Flag(String notification_Flag) {
			Notification_Flag = notification_Flag;
		}

		@Override
		public String toString() {
			return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);

		}
	}

	public class BeneficiaryDetails {
		public String Name;
		public String AccNumber;
		public String Acctype;
		public String IFSC;
		public String Mobile;
		public String Email;
		public String address;
		public String Notification_Flag;

		public String getName() {
			return Name;
		}

		public void setName(String name) {
			Name = name;
		}

		public String getAccNumber() {
			return AccNumber;
		}

		public void setAccNumber(String accNumber) {
			AccNumber = accNumber;
		}

		public String getAcctype() {
			return Acctype;
		}

		public void setAcctype(String acctype) {
			Acctype = acctype;
		}

		public String getIFSC() {
			return IFSC;
		}

		public void setIFSC(String iFSC) {
			IFSC = iFSC;
		}

		public String getMobile() {
			return Mobile;
		}

		public void setMobile(String mobile) {
			Mobile = mobile;
		}

		public String getEmail() {
			return Email;
		}

		public void setEmail(String email) {
			Email = email;
		}

		public String getAddress() {
			return address;
		}

		public void setAddress(String address) {
			this.address = address;
		}

		public String getNotification_Flag() {
			return Notification_Flag;
		}

		public void setNotification_Flag(String notification_Flag) {
			Notification_Flag = notification_Flag;
		}

		@Override
		public String toString() {
			return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);

		}
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);

	}
}
