package com.federal.fedmobilesmecore.dto;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.*;

@Entity
@Table(name = "ENTERPRISE_USERS")
//@NamedQuery(name="EnterpriseUser.findAll", query="SELECT e FROM EnterpriseUser e")
public class EnterpriseUser implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(unique = true, nullable = false, precision = 38)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SME_EU_SEQ")
	@SequenceGenerator(sequenceName = "ENTERPRISE_USERS_SEQ", allocationSize = 1, name = "SME_EU_SEQ")
	private long id;

	@Column(name = "CREATED_AT", nullable = false)
	private Timestamp createdAt;

	@Column(name = "CUST_NO", length = 255)
	private String custNo;

	@Column(length = 255)
	private String email;

	@Column(name = "ENTERPRISE_ID", length = 255)
	private String enterpriseId;

	@Column(length = 255)
	private String mobile;

	@Column(name = "OPERATION_ID", length = 255)
	private String operationId;

	@Column(length = 255)
	private String password;

	@Column(name = "PREF_NO", length = 255)
	private String prefNo;

	@Column(name = "REF_NO", length = 255)
	private String refNo;

	@Column(name = "ROLE", length = 255)
	private String role;

	@Column(length = 255)
	private String status;

	@Column(name = "UPDATED_AT", nullable = false)
	private Timestamp updatedAt;

	@Column(name = "USER_NAME", length = 255)
	private String userName;

	@Column(name = "VIEW_PWD", length = 255)
	private String viewPwd;
	
	@Column(name = "VIEW_PWD_HASH", length=200)
	private String viewPwdHash;
	
	@Column(name = "WEB_BLOCK_STATUS")
	private String webBlockStatus;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Timestamp getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Timestamp createdAt) {
		this.createdAt = createdAt;
	}

	public String getCustNo() {
		return custNo;
	}

	public void setCustNo(String custNo) {
		this.custNo = custNo;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getEnterpriseId() {
		return enterpriseId;
	}

	public void setEnterpriseId(String enterpriseId) {
		this.enterpriseId = enterpriseId;
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

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPrefNo() {
		return prefNo;
	}

	public void setPrefNo(String prefNo) {
		this.prefNo = prefNo;
	}

	public String getRefNo() {
		return refNo;
	}

	public void setRefNo(String refNo) {
		this.refNo = refNo;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Timestamp getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Timestamp updatedAt) {
		this.updatedAt = updatedAt;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getViewPwd() {
		return viewPwd;
	}

	public void setViewPwd(String viewPwd) {
		this.viewPwd = viewPwd;
	}
	
	

	public String getViewPwdHash() {
		return viewPwdHash;
	}

	public void setViewPwdHash(String viewPwdHash) {
		this.viewPwdHash = viewPwdHash;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getWebBlockStatus() {
		return webBlockStatus;
	}

	public void setWebBlockStatus(String webBlockStatus) {
		this.webBlockStatus = webBlockStatus;
	}

}