package com.federal.fedmobilesmecore.dto;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "USER_MOBILE_CHANGES")
public class UserMobileChanges {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "USER_MOBILE_CHANGES_SEQ")
	@SequenceGenerator(sequenceName = "USER_MOBILE_CHANGES_SEQ", allocationSize = 1, name = "USER_MOBILE_CHANGES_SEQ")
	@Column(unique = true, nullable = false)
	private Long id;
	
	@Column(name="CUSTOMER_NO")
	private String customerNo;
	
	@Column(name="OLD_MOBILE_NO")
	private String oldMobile;
	
	@Column(name="NEW_MOBILE_NO")
	private String newMobile;
	
	@Column(name="UPDATED_AT")
	private Timestamp updatedAt;
	
	@Column(name="CREATED_AT")
	private Timestamp createdAt;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCustomerNo() {
		return customerNo;
	}

	public void setCustomerNo(String customerNo) {
		this.customerNo = customerNo;
	}

	public String getOldMobile() {
		return oldMobile;
	}

	public void setOldMobile(String oldMobile) {
		this.oldMobile = oldMobile;
	}

	public String getNewMobile() {
		return newMobile;
	}

	public void setNewMobile(String newMobile) {
		this.newMobile = newMobile;
	}

	public Timestamp getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Timestamp updatedAt) {
		this.updatedAt = updatedAt;
	}
	
	public Timestamp getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Timestamp createdAt) {
		this.createdAt = createdAt;
	}

	@Override
	public String toString() {
		return "UserMobileChanges [id=" + id + ", customerNo=" + customerNo + ", oldMobile=" + oldMobile
				+ ", newMobile=" + newMobile + ", updatedAt=" + updatedAt + ", createdAt=" + createdAt + "]";
	}
}
