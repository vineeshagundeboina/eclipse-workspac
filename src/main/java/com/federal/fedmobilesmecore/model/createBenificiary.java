package com.federal.fedmobilesmecore.model;


public class createBenificiary {
	private String mode;
	private String ben_acc_no;
	private String mobile;
	private String ifsc;
	private String mmid;
	private String acc_no;
	private String nick_name;
	private String name;
	private String pref_corp;
	public String getMode() {
		return mode;
	}
	public void setMode(String mode) {
		this.mode = mode;
	}
	public String getBen_acc_no() {
		return ben_acc_no;
	}
	public void setBen_acc_no(String ben_acc_no) {
		this.ben_acc_no = ben_acc_no;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getIfsc() {
		return ifsc;
	}
	public void setIfsc(String ifsc) {
		this.ifsc = ifsc;
	}
	public String getMmid() {
		return mmid;
	}
	public void setMmid(String mmid) {
		this.mmid = mmid;
	}
	public String getAcc_no() {
		return acc_no;
	}
	public void setAcc_no(String acc_no) {
		this.acc_no = acc_no;
	}
	public String getNick_name() {
		return nick_name;
	}
	public void setNick_name(String nick_name) {
		this.nick_name = nick_name;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPref_corp() {
		return pref_corp;
	}
	public void setPref_corp(String pref_corp) {
		this.pref_corp = pref_corp;
	}
	@Override
	public String toString() {
		return "createBenificiary [mode=" + mode + ", ben_acc_no=" + ben_acc_no + ", mobile=" + mobile + ", ifsc="
				+ ifsc + ", mmid=" + mmid + ", acc_no=" + acc_no + ", nick_name=" + nick_name + ", name=" + name
				+ ", pref_corp=" + pref_corp + "]";
	}
}
