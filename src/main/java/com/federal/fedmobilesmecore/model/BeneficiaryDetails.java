package com.federal.fedmobilesmecore.model;

public class BeneficiaryDetails {
	public String Name;
	public String AccNumber;
	public String Acctype;
	public String Email;
	public String Mobile;
	public String Notification_Flag;
	public String IFSC;
	public String MMID;

	public String getAcctype() {
		return Acctype;
	}

	public void setAcctype(String acctype) {
		Acctype = acctype;
	}

	public String getMMID() {
		return MMID;
	}

	public void setMMID(String mMID) {
		MMID = mMID;
	}

	public String getIFSC() {
		return IFSC;
	}

	public void setIFSC(String iFSC) {
		IFSC = iFSC;
	}

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

	public String getEmail() {
		return Email;
	}

	public void setEmail(String email) {
		Email = email;
	}

	public String getMobile() {
		return Mobile;
	}

	public void setMobile(String mobile) {
		Mobile = mobile;
	}

	public String getNotification_Flag() {
		return Notification_Flag;
	}

	public void setNotification_Flag(String notification_Flag) {
		Notification_Flag = notification_Flag;
	}

	@Override
	public String toString() {
		return "BeneficiaryDetails [Name=" + Name + ", AccNumber=" + AccNumber + ", Email=" + Email + ", Mobile="
				+ Mobile + ", Notification_Flag=" + Notification_Flag + ", IFSC=" + IFSC + ", MMID=" + MMID + "]";
	}

}