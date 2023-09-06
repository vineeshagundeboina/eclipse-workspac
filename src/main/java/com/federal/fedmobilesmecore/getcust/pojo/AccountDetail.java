
package com.federal.fedmobilesmecore.getcust.pojo;

import java.io.Serializable;
import java.util.List;

import javax.annotation.Generated;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "RELATEDPARTY", "FORACID", "ERRORMSG", "REMARKS", "SCHEMETYPE", "ACCTSTATUS", "MOBILENUMBER",
		"EMAIL", "CONSTITUTION", "CUSTOMERID", "ERRORCODE", "BRANCHCODE", "ADDRESS1", "SOLID", "CUSTOMERNAME",
		"SCHEMECODE", "ADDRESS2" })
public class AccountDetail implements Serializable {

	@JsonProperty("RELATEDPARTY")
	private List<Relatedparty> relatedparty;

	@JsonProperty("FORACID")
	private String foracid;
	@JsonProperty("ERRORMSG")
	private String errormsg;
	@JsonProperty("REMARKS")
	private String remarks;
	@JsonProperty("SCHEMETYPE")
	private String schemetype;
	@JsonProperty("ACCTSTATUS")
	private String acctstatus;
	@JsonProperty("MOBILENUMBER")
	private Long mobilenumber;
	@JsonProperty("EMAIL")
	private String email;
	@JsonProperty("CONSTITUTION")
	private String constitution;
	@JsonProperty("CUSTOMERID")
	private Long customerid;
	@JsonProperty("ERRORCODE")
	private String errorcode;
	@JsonProperty("BRANCHCODE")
	private String branchcode;
	@JsonProperty("ADDRESS1")
	private String address1;
	@JsonProperty("SOLID")
	private Long solid;
	@JsonProperty("CUSTOMERNAME")
	private String customername;
	@JsonProperty("SCHEMECODE")
	private Long schemecode;
	@JsonProperty("ADDRESS2")
	private String address2;
	private final static long serialVersionUID = -5987957034529823487L;

	/**
	 * No args constructor for use in serialization
	 * 
	 */
	public AccountDetail() {
	}

	/**
	 * 
	 * @param relatedparty
	 * @param branchcode
	 * @param mobilenumber
	 * @param constitution
	 * @param solid
	 * @param address2
	 * @param address1
	 * @param errormsg
	 * @param schemecode
	 * @param schemetype
	 * @param customerid
	 * @param acctstatus
	 * @param customername
	 * @param remarks
	 * @param email
	 * @param errorcode
	 * @param foracid
	 */
	public AccountDetail(List<Relatedparty> relatedparty, String foracid, String errormsg, String remarks,
			String schemetype, String acctstatus, Long mobilenumber, String email, String constitution, Long customerid,
			String errorcode, String branchcode, String address1, Long solid, String customername, Long schemecode,
			String address2) {
		super();
		this.relatedparty = relatedparty;
		this.foracid = foracid;
		this.errormsg = errormsg;
		this.remarks = remarks;
		this.schemetype = schemetype;
		this.acctstatus = acctstatus;
		this.mobilenumber = mobilenumber;
		this.email = email;
		this.constitution = constitution;
		this.customerid = customerid;
		this.errorcode = errorcode;
		this.branchcode = branchcode;
		this.address1 = address1;
		this.solid = solid;
		this.customername = customername;
		this.schemecode = schemecode;
		this.address2 = address2;
	}

	public List<Relatedparty> getRelatedparty() {
		return relatedparty;
	}

	public void setRelatedparty(List<Relatedparty> relatedparty) {
		this.relatedparty = relatedparty;
	}

	@JsonProperty("FORACID")
	public String getForacid() {
		return foracid;
	}

	@JsonProperty("FORACID")
	public void setForacid(String foracid) {
		this.foracid = foracid;
	}

	@JsonProperty("ERRORMSG")
	public String getErrormsg() {
		return errormsg;
	}

	@JsonProperty("ERRORMSG")
	public void setErrormsg(String errormsg) {
		this.errormsg = errormsg;
	}

	@JsonProperty("REMARKS")
	public String getRemarks() {
		return remarks;
	}

	@JsonProperty("REMARKS")
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	@JsonProperty("SCHEMETYPE")
	public String getSchemetype() {
		return schemetype;
	}

	@JsonProperty("SCHEMETYPE")
	public void setSchemetype(String schemetype) {
		this.schemetype = schemetype;
	}

	@JsonProperty("ACCTSTATUS")
	public String getAcctstatus() {
		return acctstatus;
	}

	@JsonProperty("ACCTSTATUS")
	public void setAcctstatus(String acctstatus) {
		this.acctstatus = acctstatus;
	}

	@JsonProperty("MOBILENUMBER")
	public Long getMobilenumber() {
		return mobilenumber;
	}

	@JsonProperty("MOBILENUMBER")
	public void setMobilenumber(Long mobilenumber) {
		this.mobilenumber = mobilenumber;
	}

	@JsonProperty("EMAIL")
	public String getEmail() {
		return email;
	}

	@JsonProperty("EMAIL")
	public void setEmail(String email) {
		this.email = email;
	}

	@JsonProperty("CONSTITUTION")
	public String getConstitution() {
		return constitution;
	}

	@JsonProperty("CONSTITUTION")
	public void setConstitution(String constitution) {
		this.constitution = constitution;
	}

	@JsonProperty("CUSTOMERID")
	public Long getCustomerid() {
		return customerid;
	}

	@JsonProperty("CUSTOMERID")
	public void setCustomerid(Long customerid) {
		this.customerid = customerid;
	}

	@JsonProperty("ERRORCODE")
	public String getErrorcode() {
		return errorcode;
	}

	@JsonProperty("ERRORCODE")
	public void setErrorcode(String errorcode) {
		this.errorcode = errorcode;
	}

	@JsonProperty("BRANCHCODE")
	public String getBranchcode() {
		return branchcode;
	}

	@JsonProperty("BRANCHCODE")
	public void setBranchcode(String branchcode) {
		this.branchcode = branchcode;
	}

	@JsonProperty("ADDRESS1")
	public String getAddress1() {
		return address1;
	}

	@JsonProperty("ADDRESS1")
	public void setAddress1(String address1) {
		this.address1 = address1;
	}

	@JsonProperty("SOLID")
	public Long getSolid() {
		return solid;
	}

	@JsonProperty("SOLID")
	public void setSolid(Long solid) {
		this.solid = solid;
	}

	@JsonProperty("CUSTOMERNAME")
	public String getCustomername() {
		return customername;
	}

	@JsonProperty("CUSTOMERNAME")
	public void setCustomername(String customername) {
		this.customername = customername;
	}

	@JsonProperty("SCHEMECODE")
	public Long getSchemecode() {
		return schemecode;
	}

	@JsonProperty("SCHEMECODE")
	public void setSchemecode(Long schemecode) {
		this.schemecode = schemecode;
	}

	@JsonProperty("ADDRESS2")
	public String getAddress2() {
		return address2;
	}

	@JsonProperty("ADDRESS2")
	public void setAddress2(String address2) {
		this.address2 = address2;
	}

}
