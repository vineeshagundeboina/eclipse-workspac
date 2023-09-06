package com.federal.fedmobilesmecore.dto;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.*;

@Entity
@Table(name = "ACCOUNT_NO_IDENTIFIERS")
public class AccountNoIdentifier implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(unique = true, nullable = false, precision = 38)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SME_ANI_SEQ")
	@SequenceGenerator(sequenceName = "ACCOUNT_NO_IDENTIFIERS_SEQ", allocationSize = 1, name = "SME_ANI_SEQ")
	private long id;

	@Column(name = "ACC_NO", length = 255)
	private String accNo;

	@Column(name = "CREATED_AT", nullable = false)
	private Timestamp createdAt;

	@Column(length = 255)
	private String identifier;

	@Column(name = "UPDATED_AT", nullable = false)
	private Timestamp updatedAt;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getAccNo() {
		return accNo;
	}

	public void setAccNo(String accNo) {
		this.accNo = accNo;
	}

	public Timestamp getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Timestamp createdAt) {
		this.createdAt = createdAt;
	}

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public Timestamp getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Timestamp updatedAt) {
		this.updatedAt = updatedAt;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}