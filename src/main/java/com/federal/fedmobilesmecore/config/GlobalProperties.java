package com.federal.fedmobilesmecore.config;

import java.math.BigDecimal;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource("classpath:messages.properties")
@ConfigurationProperties
public class GlobalProperties {

	private boolean success;
	private boolean failed;

	private String successmsg;
	private String successcode;
	private String errormsg;
	private String exceptionerrcode;
	private String mandatoryNotPassed;
	private String bodyIsEmpty;

	// Beneficiary
	private String alreadyApproved;
	private String approved;
	private String invalidRefNo;
	private String reject;
	private String beneRejectedstatus;
	private String currentUser;
	private String userNotFound;

	// MakerCheker
	private String failedMsg;

	// Enterprise
	private String invalidEnterprise;

	// imps_limit
//	private String imps_limit;
//	private String quickpaylimit;

	// tran_limit
	private String daily_transaction;
	private String monthly_transaction;
	private String fedcorp_per_transaction_limit;

	// Nick_Name
	private String nickNameExists;

	// mobile
	private String mobileExist;

	// acc_no
	private String accNoExist;

	// Session
	private String invalidPrefNoOrPass;
	private String invalidPrefNoOrMpin;
	private String authTokenActive;
	private String invalidPassOrMpin;
	private String invalidApptoken;
	private String appTokenExpired;

	// Activate user
	private String invalidUser;
	private String invalidRegistrationToken;
	private String activationLimitExceeded;
	private String activationExceeded;
	private String activationError;
	private String soleActivationError;

	private String msgsimbindingsuccess;
	private String getMsgsimbindingsuccess01;
	private String simbinderr01;

	private String simbinderr02;
	private String simbinderr03;
	private String simbinderr04;
	private String simbinderr05;
	private String simbinderr06;
	private String simbinderr07;
	private String simbinderr08;
	private String simbinderr09;
	private String simbinderr10;
	private String simbinderr01code;
	private String msgsimbindinginvalidlength;
	private String msgsimbindingerrmsg;
	private String msgsimbindingerrmsg02;

	private String usernotfoundmsg;
	private String userenterpriseidnotfoundmsg;

	private String exceptionerrmsg;

	public String noalreadyregmsg;
	public String userIdalreadyregmsg;

	public String getcustdetlserr;
	public String getcustexperr;
	public String getcustinvalid;
	public String getmobinvalid;
	public String getmpincreateerr;
	public String getmpincreatehasherr;
	public String getmpincreateinvaliderr;
	public String mpin_blocked;
    public String fundTransferLimitMSG;
    public String quickPayTransferLimitMSG;
    
    
    public String re_onboard_message;
	public String web_blocked_message;
	public String web_blocked_message_count;
	public String mpin_blocked_message_for_single_wrong_attempts;
	public String web_blocked_message_for_single_wrong_attempts;
	public String web_blocked_message_for_second_wrong_attempts;
	public String mpin_blocked_message_for_second_wrong_attempts;
	
	public String appversion_message;
	public String os_version_message;
	private String fund_transfer_transaction_msg;
	private String mpin_blocked_message;
	private String beneficiary_approved_msg;
	public String failuremsg;
	public String updateFailure;
	public String updateSuccess;
	public String tcJsonException;
	public String tcFlatFileNotFound;
	public String tcNotPresnt;
	public String invalid;
	
	public String web_six_month_gap;
	public String enterpriseUserUnblockSms;
    public String web_six_month_gap_extuser;
    public String web_wrongpassword;
    
    public String registraction_success_sms;
    
    public String fed2fedstatuscode;
    public String impsstatuscode;
    public String schedulecode;
    
    

	public String getQuickPayTransferLimitMSG() {
		return quickPayTransferLimitMSG;
	}

	public void setQuickPayTransferLimitMSG(String quickPayTransferLimitMSG) {
		this.quickPayTransferLimitMSG = quickPayTransferLimitMSG;
	}

	public String getFundTransferLimitMSG() {
		return fundTransferLimitMSG;
	}

	public void setFundTransferLimitMSG(String fundTransferLimitMSG) {
		this.fundTransferLimitMSG = fundTransferLimitMSG;
	}

	public String getMpin_blocked() {
		return mpin_blocked;
	}

	public void setMpin_blocked(String mpin_blocked) {
		this.mpin_blocked = mpin_blocked;
	}

	// api gateway sms
	public String enterpriseUserWelcomeMessage;
	public String enterpriseUserWelcomeMessagePending;
	public String enterpriseUserWelcomeMessageApprove;

	// IVR Related messages. Added by vikasramireddy
	private String reqinputsinvalidcred;
	private String reqinputsna;
	private String ivrfailobjsave;
	private String ivrfailcheck;
	private String userregistrationtokenmessage;
	private String impsLimitMessage;
	public String web_auth_sign_msg;
	public String getImpsLimitMessage() {
		return impsLimitMessage;
	}

	public void setImpsLimitMessage(String impsLimitMessage) {
		this.impsLimitMessage = impsLimitMessage;
	}

	private String schpaymentsinvalidcorp;

	private String schpaymentsinactivecorp;

	private String invalident;

	private String gatewaysuccess = System.getenv("gateway_succ_codes");
	
	private String simBindingReceipient_mobileNo=System.getenv("longcode");
	
	public String getSimBindingReceipient_mobileNo() {
		return simBindingReceipient_mobileNo;
	}

	public void setSimBindingReceipient_mobileNo(String simBindingReceipient_mobileNo) {
		this.simBindingReceipient_mobileNo = simBindingReceipient_mobileNo;
	}

	private BigDecimal imps_per_transaction_limit;

	private BigDecimal quickPay_per_transactionlimit;
	
	
	private BigDecimal mmid_per_transaction_limit;

	public BigDecimal getMmid_per_transaction_limit() {
		return mmid_per_transaction_limit;
	}

	public void setMmid_per_transaction_limit(BigDecimal mmid_per_transaction_limit) {
		this.mmid_per_transaction_limit = mmid_per_transaction_limit;
	}

	public BigDecimal getQuickPay_per_transactionlimit() {
		return quickPay_per_transactionlimit;
	}

	public void setQuickPay_per_transactionlimit(BigDecimal quickPay_per_transactionlimit) {
		this.quickPay_per_transactionlimit = quickPay_per_transactionlimit;
	}

    public BigDecimal getImps_per_transaction_limit() {
		return imps_per_transaction_limit;
	}

	public void setImps_per_transaction_limit(BigDecimal imps_per_transaction_limit) {
		this.imps_per_transaction_limit = imps_per_transaction_limit;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public boolean isFailed() {
		return failed;
	}

	public void setFailed(boolean failed) {
		this.failed = failed;
	}

	public String getSuccessmsg() {
		return successmsg;
	}

	public void setSuccessmsg(String successmsg) {
		this.successmsg = successmsg;
	}

	public String getSuccesscode() {
		return successcode;
	}

	public void setSuccesscode(String successcode) {
		this.successcode = successcode;
	}

	public String getErrormsg() {
		return errormsg;
	}

	public void setErrormsg(String errormsg) {
		this.errormsg = errormsg;
	}

	public String getExceptionerrcode() {
		return exceptionerrcode;
	}

	public void setExceptionerrcode(String exceptionerrcode) {
		this.exceptionerrcode = exceptionerrcode;
	}

	public String getMandatoryNotPassed() {
		return mandatoryNotPassed;
	}

	public void setMandatoryNotPassed(String mandatoryNotPassed) {
		this.mandatoryNotPassed = mandatoryNotPassed;
	}

	public String getBodyIsEmpty() {
		return bodyIsEmpty;
	}

	public void setBodyIsEmpty(String bodyIsEmpty) {
		this.bodyIsEmpty = bodyIsEmpty;
	}

	public String getAlreadyApproved() {
		return alreadyApproved;
	}

	public void setAlreadyApproved(String alreadyApproved) {
		this.alreadyApproved = alreadyApproved;
	}

	public String getApproved() {
		return approved;
	}

	public void setApproved(String approved) {
		this.approved = approved;
	}

	public String getInvalidRefNo() {
		return invalidRefNo;
	}

	public void setInvalidRefNo(String invalidRefNo) {
		this.invalidRefNo = invalidRefNo;
	}

	public String getReject() {
		return reject;
	}

	public void setReject(String reject) {
		this.reject = reject;
	}

	public String getBeneRejectedstatus() {
		return beneRejectedstatus;
	}

	public void setBeneRejectedstatus(String beneRejectedstatus) {
		this.beneRejectedstatus = beneRejectedstatus;
	}

	public String getFailedMsg() {
		return failedMsg;
	}

	public void setFailedMsg(String failedMsg) {
		this.failedMsg = failedMsg;
	}

	public String getInvalidEnterprise() {
		return invalidEnterprise;
	}

	public void setInvalidEnterprise(String invalidEnterprise) {
		this.invalidEnterprise = invalidEnterprise;
	}

//	public String getImps_limit() {
//		return imps_limit;
//	}
//
//	public void setImps_limit(String imps_limit) {
//		this.imps_limit = imps_limit;
//	}

//	public String getQuickpaylimit() {
//		return quickpaylimit;
//  }
//
//	public void setQuickpaylimit(String quickpaylimit) {
//		this.quickpaylimit = quickpaylimit;
//	}

	public String getDaily_transaction() {
		return daily_transaction;
	}

	public void setDaily_transaction(String daily_transaction) {
		this.daily_transaction = daily_transaction;
	}

	public String getMonthly_transaction() {
		return monthly_transaction;
	}

	public void setMonthly_transaction(String monthly_transaction) {
		this.monthly_transaction = monthly_transaction;
	}

	public String getFedcorp_per_transaction_limit() {
		return fedcorp_per_transaction_limit;
	}

	public void setFedcorp_per_transaction_limit(String fedcorp_per_transaction_limit) {
		this.fedcorp_per_transaction_limit = fedcorp_per_transaction_limit;
	}

	public String getNickNameExists() {
		return nickNameExists;
	}

	public void setNickNameExists(String nickNameExists) {
		this.nickNameExists = nickNameExists;
	}

	public String getMobileExist() {
		return mobileExist;
	}

	public void setMobileExist(String mobileExist) {
		this.mobileExist = mobileExist;
	}

	public String getAccNoExist() {
		return accNoExist;
	}

	public void setAccNoExist(String accNoExist) {
		this.accNoExist = accNoExist;
	}

	public String getInvalidPrefNoOrPass() {
		return invalidPrefNoOrPass;
	}

	public void setInvalidPrefNoOrPass(String invalidPrefNoOrPass) {
		this.invalidPrefNoOrPass = invalidPrefNoOrPass;
	}

	public String getInvalidPrefNoOrMpin() {
		return invalidPrefNoOrMpin;
	}

	public void setInvalidPrefNoOrMpin(String invalidPrefNoOrMpin) {
		this.invalidPrefNoOrMpin = invalidPrefNoOrMpin;
	}

	public String getAuthTokenActive() {
		return authTokenActive;
	}

	public void setAuthTokenActive(String authTokenActive) {
		this.authTokenActive = authTokenActive;
	}

	public String getInvalidPassOrMpin() {
		return invalidPassOrMpin;
	}

	public void setInvalidPassOrMpin(String invalidPassOrMpin) {
		this.invalidPassOrMpin = invalidPassOrMpin;
	}

	public String getInvalidApptoken() {
		return invalidApptoken;
	}

	public void setInvalidApptoken(String invalidApptoken) {
		this.invalidApptoken = invalidApptoken;
	}

	public String getAppTokenExpired() {
		return appTokenExpired;
	}

	public void setAppTokenExpired(String appTokenExpired) {
		this.appTokenExpired = appTokenExpired;
	}

	public String getInvalidUser() {
		return invalidUser;
	}

	public void setInvalidUser(String invalidUser) {
		this.invalidUser = invalidUser;
	}

	public String getInvalidRegistrationToken() {
		return invalidRegistrationToken;
	}

	public void setInvalidRegistrationToken(String invalidRegistrationToken) {
		this.invalidRegistrationToken = invalidRegistrationToken;
	}

	public String getActivationLimitExceeded() {
		return activationLimitExceeded;
	}

	public void setActivationLimitExceeded(String activationLimitExceeded) {
		this.activationLimitExceeded = activationLimitExceeded;
	}

	public String getActivationExceeded() {
		return activationExceeded;
	}

	public void setActivationExceeded(String activationExceeded) {
		this.activationExceeded = activationExceeded;
	}

	public String getActivationError() {
		return activationError;
	}

	public void setActivationError(String activationError) {
		this.activationError = activationError;
	}

	public String getSoleActivationError() {
		return soleActivationError;
	}

	public void setSoleActivationError(String soleActivationError) {
		this.soleActivationError = soleActivationError;
	}

	public String getMsgsimbindingsuccess() {
		return msgsimbindingsuccess;
	}

	public void setMsgsimbindingsuccess(String msgsimbindingsuccess) {
		this.msgsimbindingsuccess = msgsimbindingsuccess;
	}

	public String getGetMsgsimbindingsuccess01() {
		return getMsgsimbindingsuccess01;
	}

	public void setGetMsgsimbindingsuccess01(String getMsgsimbindingsuccess01) {
		this.getMsgsimbindingsuccess01 = getMsgsimbindingsuccess01;
	}

	public String getSimbinderr01() {
		return simbinderr01;
	}

	public void setSimbinderr01(String simbinderr01) {
		this.simbinderr01 = simbinderr01;
	}

	public String getSimbinderr02() {
		return simbinderr02;
	}

	public void setSimbinderr02(String simbinderr02) {
		this.simbinderr02 = simbinderr02;
	}

	public String getSimbinderr03() {
		return simbinderr03;
	}

	public void setSimbinderr03(String simbinderr03) {
		this.simbinderr03 = simbinderr03;
	}

	public String getSimbinderr04() {
		return simbinderr04;
	}

	public void setSimbinderr04(String simbinderr04) {
		this.simbinderr04 = simbinderr04;
	}

	public String getSimbinderr05() {
		return simbinderr05;
	}

	public void setSimbinderr05(String simbinderr05) {
		this.simbinderr05 = simbinderr05;
	}

	public String getSimbinderr06() {
		return simbinderr06;
	}

	public void setSimbinderr06(String simbinderr06) {
		this.simbinderr06 = simbinderr06;
	}

	public String getSimbinderr07() {
		return simbinderr07;
	}

	public void setSimbinderr07(String simbinderr07) {
		this.simbinderr07 = simbinderr07;
	}

	public String getSimbinderr08() {
		return simbinderr08;
	}

	public void setSimbinderr08(String simbinderr08) {
		this.simbinderr08 = simbinderr08;
	}

	public String getSimbinderr09() {
		return simbinderr09;
	}

	public void setSimbinderr09(String simbinderr09) {
		this.simbinderr09 = simbinderr09;
	}

	public String getSimbinderr10() {
		return simbinderr10;
	}

	public void setSimbinderr10(String simbinderr10) {
		this.simbinderr10 = simbinderr10;
	}

	public String getSimbinderr01code() {
		return simbinderr01code;
	}

	public void setSimbinderr01code(String simbinderr01code) {
		this.simbinderr01code = simbinderr01code;
	}

	public String getMsgsimbindinginvalidlength() {
		return msgsimbindinginvalidlength;
	}

	public void setMsgsimbindinginvalidlength(String msgsimbindinginvalidlength) {
		this.msgsimbindinginvalidlength = msgsimbindinginvalidlength;
	}

	public String getMsgsimbindingerrmsg() {
		return msgsimbindingerrmsg;
	}

	public void setMsgsimbindingerrmsg(String msgsimbindingerrmsg) {
		this.msgsimbindingerrmsg = msgsimbindingerrmsg;
	}

	public String getUsernotfoundmsg() {
		return usernotfoundmsg;
	}

	public void setUsernotfoundmsg(String usernotfoundmsg) {
		this.usernotfoundmsg = usernotfoundmsg;
	}

	public String getUserenterpriseidnotfoundmsg() {
		return userenterpriseidnotfoundmsg;
	}

	public void setUserenterpriseidnotfoundmsg(String userenterpriseidnotfoundmsg) {
		this.userenterpriseidnotfoundmsg = userenterpriseidnotfoundmsg;
	}

	public String getExceptionerrmsg() {
		return exceptionerrmsg;
	}

	public void setExceptionerrmsg(String exceptionerrmsg) {
		this.exceptionerrmsg = exceptionerrmsg;
	}

	public String getNoalreadyregmsg() {
		return noalreadyregmsg;
	}

	public void setNoalreadyregmsg(String noalreadyregmsg) {
		this.noalreadyregmsg = noalreadyregmsg;
	}

	public String getUserIdalreadyregmsg() {
		return userIdalreadyregmsg;
	}

	public void setUserIdalreadyregmsg(String userIdalreadyregmsg) {
		this.userIdalreadyregmsg = userIdalreadyregmsg;
	}

	public String getGetcustdetlserr() {
		return getcustdetlserr;
	}

	public void setGetcustdetlserr(String getcustdetlserr) {
		this.getcustdetlserr = getcustdetlserr;
	}

	public String getGetcustexperr() {
		return getcustexperr;
	}

	public void setGetcustexperr(String getcustexperr) {
		this.getcustexperr = getcustexperr;
	}

	public String getGetcustinvalid() {
		return getcustinvalid;
	}

	public void setGetcustinvalid(String getcustinvalid) {
		this.getcustinvalid = getcustinvalid;
	}

	public String getGetmobinvalid() {
		return getmobinvalid;
	}

	public void setGetmobinvalid(String getmobinvalid) {
		this.getmobinvalid = getmobinvalid;
	}

	public String getGetmpincreateerr() {
		return getmpincreateerr;
	}

	public void setGetmpincreateerr(String getmpincreateerr) {
		this.getmpincreateerr = getmpincreateerr;
	}

	public String getGetmpincreatehasherr() {
		return getmpincreatehasherr;
	}

	public void setGetmpincreatehasherr(String getmpincreatehasherr) {
		this.getmpincreatehasherr = getmpincreatehasherr;
	}

	public String getGetmpincreateinvaliderr() {
		return getmpincreateinvaliderr;
	}

	public void setGetmpincreateinvaliderr(String getmpincreateinvaliderr) {
		this.getmpincreateinvaliderr = getmpincreateinvaliderr;
	}

	public String getEnterpriseUserWelcomeMessage() {
		return enterpriseUserWelcomeMessage;
	}

	public void setEnterpriseUserWelcomeMessage(String enterpriseUserWelcomeMessage) {
		this.enterpriseUserWelcomeMessage = enterpriseUserWelcomeMessage;
	}

	public String getReqinputsinvalidcred() {
		return reqinputsinvalidcred;
	}

	public void setReqinputsinvalidcred(String reqinputsinvalidcred) {
		this.reqinputsinvalidcred = reqinputsinvalidcred;
	}

	public String getReqinputsna() {
		return reqinputsna;
	}

	public void setReqinputsna(String reqinputsna) {
		this.reqinputsna = reqinputsna;
	}

	public String getIvrfailobjsave() {
		return ivrfailobjsave;
	}

	public void setIvrfailobjsave(String ivrfailobjsave) {
		this.ivrfailobjsave = ivrfailobjsave;
	}

	public String getIvrfailcheck() {
		return ivrfailcheck;
	}

	public void setIvrfailcheck(String ivrfailcheck) {
		this.ivrfailcheck = ivrfailcheck;
	}

	public String getUserregistrationtokenmessage() {
		return userregistrationtokenmessage;
	}

	public void setUserregistrationtokenmessage(String userregistrationtokenmessage) {
		this.userregistrationtokenmessage = userregistrationtokenmessage;
	}

	public String getSchpaymentsinvalidcorp() {
		return schpaymentsinvalidcorp;
	}

	public void setSchpaymentsinvalidcorp(String schpaymentsinvalidcorp) {
		this.schpaymentsinvalidcorp = schpaymentsinvalidcorp;
	}

	public String getSchpaymentsinactivecorp() {
		return schpaymentsinactivecorp;
	}

	public void setSchpaymentsinactivecorp(String schpaymentsinactivecorp) {
		this.schpaymentsinactivecorp = schpaymentsinactivecorp;
	}

	public String getCurrentUser() {
		return currentUser;
	}

	public void setCurrentUser(String currentUser) {
		this.currentUser = currentUser;
	}

	public String getUserNotFound() {
		return userNotFound;
	}

	public void setUserNotFound(String userNotFound) {
		this.userNotFound = userNotFound;
	}

	public String getMsgsimbindingerrmsg02() {
		return msgsimbindingerrmsg02;
	}

	public void setMsgsimbindingerrmsg02(String msgsimbindingerrmsg02) {
		this.msgsimbindingerrmsg02 = msgsimbindingerrmsg02;
	}

	public String getInvalident() {
		return invalident;
	}

	public void setInvalident(String invalident) {
		this.invalident = invalident;
	}

	public String getGatewaysuccess() {
		return gatewaysuccess;
	}

	public void setGatewaysuccess(String gatewaysuccess) {
		this.gatewaysuccess = gatewaysuccess;
	}

	public String getRe_onboard_message() {
		return re_onboard_message;
	}

	public void setRe_onboard_message(String re_onboard_message) {
		this.re_onboard_message = re_onboard_message;
	}

	public String getWeb_blocked_message() {
		return web_blocked_message;
	}

	public void setWeb_blocked_message(String web_blocked_message) {
		this.web_blocked_message = web_blocked_message;
	}

	public String getWeb_blocked_message_count() {
		return web_blocked_message_count;
	}

	public void setWeb_blocked_message_count(String web_blocked_message_count) {
		this.web_blocked_message_count = web_blocked_message_count;
	}

	public String getAppversion_message() {
		return appversion_message;
	}

	public void setAppversion_message(String appversion_message) {
		this.appversion_message = appversion_message;
	}

	public String getOs_version_message() {
		return os_version_message;
	}

	public void setOs_version_message(String os_version_message) {
		this.os_version_message = os_version_message;
	}

	public String getFund_transfer_transaction_msg() {
		return fund_transfer_transaction_msg;
	}

	public void setFund_transfer_transaction_msg(String fund_transfer_transaction_msg) {
		this.fund_transfer_transaction_msg = fund_transfer_transaction_msg;
	}

	public String getMpin_blocked_message() {
		return mpin_blocked_message;
	}

	public void setMpin_blocked_message(String mpin_blocked_message) {
		this.mpin_blocked_message = mpin_blocked_message;
	}

	public String getBeneficiary_approved_msg() {
		return beneficiary_approved_msg;
	}

	public void setBeneficiary_approved_msg(String beneficiary_approved_msg) {
		this.beneficiary_approved_msg = beneficiary_approved_msg;
	}

	public String getFailuremsg() {
		return failuremsg;
	}

	public void setFailuremsg(String failuremsg) {
		this.failuremsg = failuremsg;
	}

	public String getUpdateFailure() {
		return updateFailure;
	}

	public void setUpdateFailure(String updateFailure) {
		this.updateFailure = updateFailure;
	}

	public String getUpdateSuccess() {
		return updateSuccess;
	}

	public void setUpdateSuccess(String updateSuccess) {
		this.updateSuccess = updateSuccess;
	}

	public String getTcJsonException() {
		return tcJsonException;
	}

	public void setTcJsonException(String tcJsonException) {
		this.tcJsonException = tcJsonException;
	}

	public String getTcFlatFileNotFound() {
		return tcFlatFileNotFound;
	}

	public void setTcFlatFileNotFound(String tcFlatFileNotFound) {
		this.tcFlatFileNotFound = tcFlatFileNotFound;
	}

	public String getTcNotPresnt() {
		return tcNotPresnt;
	}

	public void setTcNotPresnt(String tcNotPresnt) {
		this.tcNotPresnt = tcNotPresnt;
	}

	public String getInvalid() {
		return invalid;
	}

	public void setInvalid(String invalid) {
		this.invalid = invalid;
	}

	public String getWeb_six_month_gap() {
		return web_six_month_gap;
	}

	public void setWeb_six_month_gap(String web_six_month_gap) {
		this.web_six_month_gap = web_six_month_gap;
	}

	public String getEnterpriseUserUnblockSms() {
		return enterpriseUserUnblockSms;
	}

	public void setEnterpriseUserUnblockSms(String enterpriseUserUnblockSms) {
		this.enterpriseUserUnblockSms = enterpriseUserUnblockSms;
	}

	public String getWeb_six_month_gap_extuser() {
		return web_six_month_gap_extuser;
	}

	public void setWeb_six_month_gap_extuser(String web_six_month_gap_extuser) {
		this.web_six_month_gap_extuser = web_six_month_gap_extuser;
	}

	public String getWeb_wrongpassword() {
		return web_wrongpassword;
	}

	public void setWeb_wrongpassword(String web_wrongpassword) {
		this.web_wrongpassword = web_wrongpassword;
	}

	public String getRegistraction_success_sms() {
		return registraction_success_sms;
	}

	public void setRegistraction_success_sms(String registraction_success_sms) {
		this.registraction_success_sms = registraction_success_sms;
	}

	public String getWeb_auth_sign_msg() {
		return web_auth_sign_msg;
	}

	public void setWeb_auth_sign_msg(String web_auth_sign_msg) {
		this.web_auth_sign_msg = web_auth_sign_msg;
	}

	public String getFed2fedstatuscode() {
		return fed2fedstatuscode;
	}

	public void setFed2fedstatuscode(String fed2fedstatuscode) {
		this.fed2fedstatuscode = fed2fedstatuscode;
	}

	public String getImpsstatuscode() {
		return impsstatuscode;
	}

	public void setImpsstatuscode(String impsstatuscode) {
		this.impsstatuscode = impsstatuscode;
	}

	public String getSchedulecode() {
		return schedulecode;
	}

	public void setSchedulecode(String schedulecode) {
		this.schedulecode = schedulecode;
	}

	public String getMpin_blocked_message_for_single_wrong_attempts() {
		return mpin_blocked_message_for_single_wrong_attempts;
	}

	public void setMpin_blocked_message_for_single_wrong_attempts(String mpin_blocked_message_for_single_wrong_attempts) {
		this.mpin_blocked_message_for_single_wrong_attempts = mpin_blocked_message_for_single_wrong_attempts;
	}

	public String getWeb_blocked_message_for_single_wrong_attempts() {
		return web_blocked_message_for_single_wrong_attempts;
	}

	public void setWeb_blocked_message_for_single_wrong_attempts(String web_blocked_message_for_single_wrong_attempts) {
		this.web_blocked_message_for_single_wrong_attempts = web_blocked_message_for_single_wrong_attempts;
	}

	public String getWeb_blocked_message_for_second_wrong_attempts() {
		return web_blocked_message_for_second_wrong_attempts;
	}

	public void setWeb_blocked_message_for_second_wrong_attempts(String web_blocked_message_for_second_wrong_attempts) {
		this.web_blocked_message_for_second_wrong_attempts = web_blocked_message_for_second_wrong_attempts;
	}

	public String getMpin_blocked_message_for_second_wrong_attempts() {
		return mpin_blocked_message_for_second_wrong_attempts;
	}

	public void setMpin_blocked_message_for_second_wrong_attempts(String mpin_blocked_message_for_second_wrong_attempts) {
		this.mpin_blocked_message_for_second_wrong_attempts = mpin_blocked_message_for_second_wrong_attempts;
	}

	public String getEnterpriseUserWelcomeMessagePending() {
		return enterpriseUserWelcomeMessagePending;
	}

	public void setEnterpriseUserWelcomeMessagePending(String enterpriseUserWelcomeMessagePending) {
		this.enterpriseUserWelcomeMessagePending = enterpriseUserWelcomeMessagePending;
	}

	public String getEnterpriseUserWelcomeMessageApprove() {
		return enterpriseUserWelcomeMessageApprove;
	}

	public void setEnterpriseUserWelcomeMessageApprove(String enterpriseUserWelcomeMessageApprove) {
		this.enterpriseUserWelcomeMessageApprove = enterpriseUserWelcomeMessageApprove;
	}
	
	

}
