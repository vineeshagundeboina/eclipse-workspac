package com.federal.fedmobilesmecore.model;

import com.federal.fedmobilesmecore.dto.Beneficiaries;

public class CreateBeneficiaryResponse {
	
	private String status;
	private String ref_No;
	private String ben_acc_n;
	private String ben_name;
	private Beneficiaries beneficiaries;
	
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getRef_No() {
		return ref_No;
	}
	public void setRef_No(String ref_No) {
		this.ref_No = ref_No;
	}
	public String getBen_acc_n() {
		return ben_acc_n;
	}
	public void setBen_acc_n(String ben_acc_n) {
		this.ben_acc_n = ben_acc_n;
	}
	public String getBen_name() {
		return ben_name;
	}
	public void setBen_name(String ben_name) {
		this.ben_name = ben_name;
	}
	public Beneficiaries getBeneficiaries() {
		return beneficiaries;
	}
	public void setBeneficiaries(Beneficiaries beneficiaries) {
		this.beneficiaries = beneficiaries;
	}
	@Override
	public String toString() {
		return "CreateBeneficiaryResponse [status=" + status + ", ref_No=" + ref_No + ", ben_acc_n=" + ben_acc_n
				+ ", ben_name=" + ben_name + ", beneficiaries=" + beneficiaries + "]";
	}
}
