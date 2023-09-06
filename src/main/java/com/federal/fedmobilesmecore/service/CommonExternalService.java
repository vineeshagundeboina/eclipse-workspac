package com.federal.fedmobilesmecore.service;

import java.util.Arrays;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.federal.fedmobilesmecore.config.RecordLog;
import com.federal.fedmobilesmecore.dto.ApplicationEnterpris;
import com.federal.fedmobilesmecore.dto.Enterprises;
import com.federal.fedmobilesmecore.dto.User;
import com.federal.fedmobilesmecore.repository.ApplicationEnterprisRepository;
import com.federal.fedmobilesmecore.repository.EnterprisesRepository;
import com.federal.fedmobilesmecore.repository.UserRepository;

@Service
public class CommonExternalService {
	@Autowired
	UserRepository userRepository;

	@Autowired
	EnterprisesRepository enterprisesRepository;
	
	@Autowired
	ApplicationEnterprisRepository applicationEnterprisRepository;

	public boolean enterprise_sole_proprietorship(Optional<User> getUserDetails) {
		boolean enterpriseSoleProprietorshipResp = false;
		String enterpriseId = null;
		Optional<Enterprises> getEnterprise = null;
		Enterprises enterprises = null;
		String role = getUserDetails.get().getRole() != null ? getUserDetails.get().getRole() : "";
		enterpriseId = getUserDetails.get().getEnterpriseId();
		RecordLog.writeLogFile("enterprise_sole_proprietorship enterpriseId: " + enterpriseId);
		getEnterprise = enterprisesRepository.findById(Long.valueOf(enterpriseId));
//		RecordLog.writeLogFile("Enterprise details: " + getEnterprise.toString());
		if (getEnterprise.isPresent() == true) {
			enterprises = getEnterprise.get();
			if (enterprises.getConstitution().equals("SP") && !role.equals("external")) {
				enterpriseSoleProprietorshipResp = true;
			} else {
				enterpriseSoleProprietorshipResp = false;
			}
		} else {
			enterpriseSoleProprietorshipResp = false;
		}
//		RecordLog.writeLogFile("enterprise_sole_proprietorship: " + enterpriseSoleProprietorshipResp);
		return enterpriseSoleProprietorshipResp;
	}

	public boolean enterprise_zero_external_user_authorize(Optional<User> getUserDetails) {
		boolean enterpriseZeroExternalUserAuthorize = false;

		String enterpriseId = null;
		Optional<Enterprises> getEnterprise = null;
		Enterprises enterprises = null;
//		RecordLog.writeLogFile("getUserDetails isPresent " + getUserDetails.isPresent());
		String role = getUserDetails.get().getRole() != null ? getUserDetails.get().getRole() : "";

		enterpriseId = getUserDetails.get().getEnterpriseId();
		getEnterprise = enterprisesRepository.findById(Long.valueOf(enterpriseId));
//		RecordLog.writeLogFile("getEnterprise " + getEnterprise.toString());
		try {
			if (getEnterprise.isPresent() == true) {
				enterprises = getEnterprise.get();
				ApplicationEnterpris applicationEnterpris=applicationEnterprisRepository.findByApplicationFormIdAndPrefCorpIgnoreCase(enterprises.getApplicationFormId(),enterprises.getPrefCorp());
				String authFund=applicationEnterpris.getAuthFund()==null?"0":applicationEnterpris.getAuthFund();
				if (authFund != null) {
					if (authFund.equals("0") && !role.equals("external")) {
						enterpriseZeroExternalUserAuthorize = true;
					} else {
						enterpriseZeroExternalUserAuthorize = false;
					}
				} else {
					enterpriseZeroExternalUserAuthorize = false;
				}
			} else {
				enterpriseZeroExternalUserAuthorize = false;
			}
		} catch (Exception e) {
			RecordLog.writeLogFile("Exception occured at: "+Arrays.toString(e.getStackTrace())+" Exception name: "+e.getMessage());
			e.printStackTrace();
			enterpriseZeroExternalUserAuthorize = false;
		}
		RecordLog.writeLogFile("enterprise_zero_external_user_authorize: " + enterpriseZeroExternalUserAuthorize);
		return enterpriseZeroExternalUserAuthorize;
	}

}
