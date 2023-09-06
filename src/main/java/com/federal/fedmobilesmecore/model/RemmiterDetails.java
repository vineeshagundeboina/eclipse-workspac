package com.federal.fedmobilesmecore.model;

public class RemmiterDetails {
	public String Name;
	public String AccNumber;
	public String Acctype;
	public String Mobile;
	public String Email;
	public String MMID;
	public String Notification_Flag;

	public String getMMID() {
		return MMID;
	}

	public void setMMID(String mMID) {
		MMID = mMID;
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

	public String getNotification_Flag() {
		return Notification_Flag;
	}

	public void setNotification_Flag(String notification_Flag) {
		Notification_Flag = notification_Flag;
	}

	@Override
	public String toString() {
		return "RemmiterDetails [Name=" + Name + ", AccNumber=" + AccNumber + ", Acctype=" + Acctype + ", Mobile="
				+ Mobile + ", Email=" + Email + ", MMID=" + MMID + ", Notification_Flag=" + Notification_Flag + "]";
	}

}