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

@Entity
@Table(name="USER_AUTH_TOKENS")
public class UsersAuthTokens implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "USER_AUTH_TOKEN_ID_SEQ")
	@SequenceGenerator(sequenceName = "USER_AUTH_TOKEN_ID_SEQ", allocationSize = 1, name = "USER_AUTH_TOKEN_ID_SEQ")
	@Column(unique = true, nullable = false, precision = 38)
	private long id;

	@Column(name = "PREF_NO", length = 255)
	private String prefNo;

	@Column(name = "AUTH_TOKEN", length = 255)
	private String authToken;

	@Column(name = "CREATED_AT")
	private Timestamp createdAt;

//	public long getId() {
//		return id;
//	}
//
//	public void setId(long id) {
//		this.id = id;
//	}

	public String getPrefNo() {
		return prefNo;
	}

	public void setPrefNo(String prefNo) {
		this.prefNo = prefNo;
	}

	public String getAuthToken() {
		return authToken;
	}

	public void setAuthToken(String authToken) {
		this.authToken = authToken;
	}

	public Timestamp getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Timestamp createdAt) {
		this.createdAt = createdAt;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	
	
	

}
