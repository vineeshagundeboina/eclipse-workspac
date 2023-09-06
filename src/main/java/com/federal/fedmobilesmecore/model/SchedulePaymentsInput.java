package com.federal.fedmobilesmecore.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class SchedulePaymentsInput {

	private String mode;
	private Double amount;
	private String rem_acc_no;
	private String rem_name;
	private String ben_acc_no;
	private String ben_nick_name;
	private String ben_name;
	private String ben_acc_type;
	private String ben_ifsc;
	private String sender_to_receiver;
	private String pref_corp;
	private String remarks;
	private String rem_mob_num;
	private String ben_mmid;
	private String ben_mob_num;
	private String mobile;
	private String start_date;
	private String frequency;
	private Long count;
	private String authtoken;
	private String rem_mmid;
	private String rem_cust_id;
	private String ben_aadhar;

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public String getRem_acc_no() {
		return rem_acc_no;
	}

	public void setRem_acc_no(String rem_acc_no) {
		this.rem_acc_no = rem_acc_no;
	}

	public String getRem_name() {
		return rem_name;
	}

	public void setRem_name(String rem_name) {
		this.rem_name = rem_name;
	}

	public String getBen_acc_no() {
		return ben_acc_no;
	}

	public void setBen_acc_no(String ben_acc_no) {
		this.ben_acc_no = ben_acc_no;
	}

	public String getBen_nick_name() {
		return ben_nick_name;
	}

	public void setBen_nick_name(String ben_nick_name) {
		this.ben_nick_name = ben_nick_name;
	}

	public String getBen_name() {
		return ben_name;
	}

	public void setBen_name(String ben_name) {
		this.ben_name = ben_name;
	}

	public String getBen_acc_type() {
		return ben_acc_type;
	}

	public void setBen_acc_type(String ben_acc_type) {
		this.ben_acc_type = ben_acc_type;
	}

	public String getBen_ifsc() {
		return ben_ifsc;
	}

	public void setBen_ifsc(String ben_ifsc) {
		this.ben_ifsc = ben_ifsc;
	}

	public String getSender_to_receiver() {
		return sender_to_receiver;
	}

	public void setSender_to_receiver(String sender_to_receiver) {
		this.sender_to_receiver = sender_to_receiver;
	}

	public String getPref_corp() {
		return pref_corp;
	}

	public void setPref_corp(String pref_corp) {
		this.pref_corp = pref_corp;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getRem_mob_num() {
		return rem_mob_num;
	}

	public void setRem_mob_num(String rem_mob_num) {
		this.rem_mob_num = rem_mob_num;
	}

	public String getBen_mmid() {
		return ben_mmid;
	}

	public void setBen_mmid(String ben_mmid) {
		this.ben_mmid = ben_mmid;
	}

	public String getBen_mob_num() {
		return ben_mob_num;
	}

	public void setBen_mob_num(String ben_mob_num) {
		this.ben_mob_num = ben_mob_num;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getStart_date() {
		return start_date;
	}

	public void setStart_date(String start_date) {
		this.start_date = start_date;
	}

	public String getFrequency() {
		return frequency;
	}

	public void setFrequency(String frequency) {
		this.frequency = frequency;
	}

	public Long getCount() {
		return count;
	}

	public void setCount(Long count) {
		this.count = count;
	}

	public String getAuthtoken() {
		return authtoken;
	}

	public void setAuthtoken(String authtoken) {
		this.authtoken = authtoken;
	}

	public String getRem_mmid() {
		return rem_mmid;
	}

	public void setRem_mmid(String rem_mmid) {
		this.rem_mmid = rem_mmid;
	}

	public String getRem_cust_id() {
		return rem_cust_id;
	}

	public void setRem_cust_id(String rem_cust_id) {
		this.rem_cust_id = rem_cust_id;
	}

	public String getBen_aadhar() {
		return ben_aadhar;
	}

	public void setBen_aadhar(String ben_aadhar) {
		this.ben_aadhar = ben_aadhar;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);

	}
}
