package com.federal.fedmobilesmecore.model;

import java.util.List;

import com.federal.fedmobilesmecore.dto.BankIfsc;

public class IfscRespModel {
	public boolean status;
	public String description;
	public List<String> result;
	public BankIfsc bankIfsc;

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

	public List<String> getResult() {
		return result;
	}

	public void setResult(List<String> result) {
		this.result = result;
	}

	public BankIfsc getBankIfsc() {
		return bankIfsc;
	}

	public void setBankIfsc(BankIfsc bankIfsc) {
		this.bankIfsc = bankIfsc;
	}

	@Override
	public String toString() {
		return "IfscRespModel [status=" + status + ", description=" + description + ", result=" + result + ", bankIfsc="
				+ bankIfsc + "]";
	}

}
