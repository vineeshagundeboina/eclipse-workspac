package com.federal.fedmobilesmecore.dto;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "MANAGED_ACCOUNTS")
public class ManagedAccount implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(unique = true, nullable = false, precision = 38)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SME_MA_SEQ")
	@SequenceGenerator(sequenceName = "MANAGED_ACCOUNTS_SEQ", allocationSize = 1, name = "SME_MA_SEQ")
	private long id;

	@Column(length = 255)
	private String accounts;

	@Column(name = "CREATED_AT", nullable = false)
	private Timestamp createdAt;

	@Column(name = "UPDATED_AT", nullable = false)
	private Timestamp updatedAt;

	// bi-directional many-to-one association to User
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "USER_ID")
	private User user;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getAccounts() {
		return accounts;
	}

	public void setAccounts(String accounts) {
		this.accounts = accounts;
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

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}