package com.federal.fedmobilesmecore.dto;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name="LOGIN_DETAILS")
public class LoginDetails {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "SMELOGIN_DETAIL_SEQ")
	@SequenceGenerator(sequenceName = "LOGIN_DETAILS_SEQUENCE",allocationSize = 1,name="SMELOGIN_DETAIL_SEQ")
	@Column(unique = true, nullable = false)
	private Long id;
	
	@Column(name="APP_VERSION",nullable = true)
	private String appVersion;
	
	@Column(name="OS_VERSION",nullable = true)
	private String osVersion;
	
	@Column(name="OS_TYPE",nullable = true)
	private String osType;
	
	@Column(name="IP_ADDRESS",nullable = true)
	private String ipAddress;
	
	@Column(name="GEO_LOCATION",nullable = true)
	private String geoLocation;
	
	@Column(name="MOBILE_NUMBER",nullable = false)
	private String mobile;
	
	
	@Column(name="ACC_NUMBER")
	private String accountNumber;

	@Column(name="ACC_NAME")
	private String accountName;
	
	
	@Column(name="CREATED_AT",nullable = false)
	private Timestamp createdAt;
	
	@Enumerated(value = EnumType.STRING)
	@Column(name="CHANNEL_FLAG",nullable = false)
	private ChannelFlag channelFlag;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getAppVersion() {
		return appVersion;
	}

	public void setAppVersion(String appVersion) {
		this.appVersion = appVersion;
	}

	public String getOsVersion() {
		return osVersion;
	}

	public void setOsVersion(String osVersion) {
		this.osVersion = osVersion;
	}

	public String getOsType() {
		return osType;
	}

	public void setOsType(String osType) {
		this.osType = osType;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public String getGeoLocation() {
		return geoLocation;
	}

	public void setGeoLocation(String geoLocation) {
		this.geoLocation = geoLocation;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public ChannelFlag getChannelFlag() {
		return channelFlag;
	}

	public void setChannelFlag(ChannelFlag channelFlag) {
		this.channelFlag = channelFlag;
	}

	public Timestamp getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Timestamp createdAt) {
		this.createdAt = createdAt;
	}

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

}
