package com.federal.fedmobilesmecore.dto;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

@Entity
@Table(name = "BENEFICIARIES")
public class Beneficiaries implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SME_BEN_SEQ")
	@SequenceGenerator(sequenceName = "BENEFICIARIES_SEQ", allocationSize = 1, name = "SME_BEN_SEQ")
	@Column(unique = true, nullable = false, precision = 38)
	private long id;

	@Column(name = "[MODE]", length = 255)
	private String mode;

	@Column(name = "NICK_NAME", length = 255)
	private String nickName;

	@Column(name = "NAME", length = 255)
	private String name;

	@Column(name = "IFSC", length = 255)
	private String ifsc;

	@Column(name = "ENTERPRISE_ID", length = 255)
	private String enterpriseId;

	@Column(name = "ACC_NO", length = 255)
	private String accNo;

	@Column(length = 255)
	private String mobile;

	@Column(name = "OPERATION_ID", length = 255)
	private String operationId;

	@Column(name = "MMID", length = 255)
	private String mmid;

	@Column(name = "STATUS", length = 255)
	private String status;

	@Column(name = "BEN_NO", length = 255)
	private String ben_No;

	@Column(name = "NETWORK_NO", length = 255)
	private String newtwork_No;

	@Column(name = "CORP_NO", length = 255)
	private String corp_No;

	@Column(name = "BANK_IDENTIFIER", length = 255)
	private String bank_Identifier;

	@Column(name = "REF_NO", length = 255)
	private String refNo;

	@Column(name = "BEN_ACC_TYPE", length = 255)
	private String ben_acc_type;

	@Column(name = "DEL_FLAG", length = 255)
	private String del_flag;

	@Column(name = "MOB_CUST", length = 255)
	private String mobCust;

	@Column(name = "IMPORTED", length = 255)
	private String imported;

	@Column(name = "LOCK_VERSION", precision = 38)
	private long lockVersion;

	@Column(name = "IS_JOB_ACTIVE", precision = 38)
	private long isJobActive;

	@Column(name = "IS_ACTIVE", precision = 38)
	private boolean isActive;

	@CreatedDate
	@Column(name = "CREATED_AT", nullable = false)
	private Timestamp createdAt;

	@LastModifiedDate
	@Column(name = "UPDATED_AT")
	private Timestamp updatedAt;

	@Column(name = "APPROVED_AT")
	private Timestamp approvedAt;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIfsc() {
		return ifsc;
	}

	public void setIfsc(String ifsc) {
		this.ifsc = ifsc;
	}

	public String getEnterpriseId() {
		return enterpriseId;
	}

	public void setEnterpriseId(String enterpriseId) {
		this.enterpriseId = enterpriseId;
	}

	public String getAccNo() {
		return accNo;
	}

	public void setAccNo(String accNo) {
		this.accNo = accNo;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getOperationId() {
		return operationId;
	}

	public void setOperationId(String operationId) {
		this.operationId = operationId;
	}

	public String getMmid() {
		return mmid;
	}

	public void setMmid(String mmid) {
		this.mmid = mmid;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getBen_No() {
		return ben_No;
	}

	public void setBen_No(String ben_No) {
		this.ben_No = ben_No;
	}

	public String getNewtwork_No() {
		return newtwork_No;
	}

	public void setNewtwork_No(String newtwork_No) {
		this.newtwork_No = newtwork_No;
	}

	public String getCorp_No() {
		return corp_No;
	}

	public void setCorp_No(String corp_No) {
		this.corp_No = corp_No;
	}

	public String getBank_Identifier() {
		return bank_Identifier;
	}

	public void setBank_Identifier(String bank_Identifier) {
		this.bank_Identifier = bank_Identifier;
	}

	public String getRefNo() {
		return refNo;
	}

	public void setRefNo(String refNo) {
		this.refNo = refNo;
	}

	public String getBen_acc_type() {
		return ben_acc_type;
	}

	public void setBen_acc_type(String ben_acc_type) {
		this.ben_acc_type = ben_acc_type;
	}

	public String getDel_flag() {
		return del_flag;
	}

	public void setDel_flag(String del_flag) {
		this.del_flag = del_flag;
	}

	public String getMobCust() {
		return mobCust;
	}

	public void setMobCust(String mobCust) {
		this.mobCust = mobCust;
	}

	public String getImported() {
		return imported;
	}

	public void setImported(String imported) {
		this.imported = imported;
	}

	public long getLockVersion() {
		return lockVersion;
	}

	public void setLockVersion(long lock_Version) {
		this.lockVersion = lock_Version;
	}

	public long getIsJobActive() {
		return isJobActive;
	}

	public void setIsJobActive(long isJobActive) {
		this.isJobActive = isJobActive;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public Timestamp getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Timestamp createdAt) {
		this.createdAt = createdAt;
	}

	public Timestamp getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Timestamp updatedAt) {
		this.updatedAt = updatedAt;
	}

	public Timestamp getApprovedAt() {
		return approvedAt;
	}

	public void setApprovedAt(Timestamp approvedAt) {
		this.approvedAt = approvedAt;
	}

}
