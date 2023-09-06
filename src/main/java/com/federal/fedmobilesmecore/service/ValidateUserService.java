package com.federal.fedmobilesmecore.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.persistence.Tuple;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.federal.fedmobilesmecore.config.GlobalProperties;
import com.federal.fedmobilesmecore.config.PasswordHashing;
import com.federal.fedmobilesmecore.config.RecordLog;
import com.federal.fedmobilesmecore.dto.AccountNoIdentifier;
import com.federal.fedmobilesmecore.dto.ApplicationEnterpris;
import com.federal.fedmobilesmecore.dto.ApplicationForm;
import com.federal.fedmobilesmecore.dto.ApplicationUser;
import com.federal.fedmobilesmecore.dto.Branch;
import com.federal.fedmobilesmecore.dto.BranchVerification;
import com.federal.fedmobilesmecore.dto.Enterprises;
import com.federal.fedmobilesmecore.dto.User;
import com.federal.fedmobilesmecore.model.ApplicationFormReqModel;
import com.federal.fedmobilesmecore.model.GetValidateUserSaveFlowModelResp;
import com.federal.fedmobilesmecore.model.RecordIdResp;
import com.federal.fedmobilesmecore.repository.AccountNoIdentifierRepository;
import com.federal.fedmobilesmecore.repository.ApplicationEnterprisRepository;
import com.federal.fedmobilesmecore.repository.ApplicationFormRepository;
import com.federal.fedmobilesmecore.repository.ApplicationUserRepository;
import com.federal.fedmobilesmecore.repository.BranchRepository;
import com.federal.fedmobilesmecore.repository.BranchVerificationRepository;
import com.federal.fedmobilesmecore.repository.EnterprisesRepository;
import com.federal.fedmobilesmecore.repository.UserRepository;

@Service
public class ValidateUserService {
	private static final Logger log4j = LogManager.getLogger(ValidateUserService.class);
	@Autowired
	ApplicationFormRepository applicationFormRepository;
	@Autowired
	ApplicationEnterprisRepository applicationEnterprisRepository;
	@Autowired
	ApplicationUserRepository applicationUserRepository;
	@Autowired
	EnterprisesRepository enterprisesRepository;
	@Autowired
	AccountNoIdentifierRepository accountNoIdentifierRepository;
	@Autowired
	UserRepository userRepository;
	@Autowired
	BranchRepository branchRepository;
	@Autowired
	GetCustDetailsService custDetailsService;
	@Autowired
	GlobalProperties messages;
	@Autowired
	BranchVerificationRepository branchVerificationRepository;
	@Autowired
	PasswordHashing passwordHashing;

//	@Value("${validate.user.longNumber}")
	private String longCode = System.getenv("longcode");
	@Value("${validate.user.retry.count}")
	private String retryCount;

	public GetValidateUserSaveFlowModelResp getAccDetailsIfExistResp(ApplicationFormReqModel formReqModel) {
		Map<String, Object> getAccDetailsIfExist = null;
		GetValidateUserSaveFlowModelResp serviceResp = null;
		RecordIdResp recordIdResp = null;
		Enterprises enterprises = null;
		List<ApplicationEnterpris> applicationEnterpris = null;

		// check if the account no exist
		enterprises = enterprisesRepository.findFirstByAccNoAndActiveOrderByCreatedAtDesc(formReqModel.getAccountNo(),
				true);
//		RecordLog.writeLogFile("enterprises" + enterprises);
		applicationEnterpris = applicationEnterprisRepository.findByAccNo(formReqModel.getAccountNo());
//		RecordLog.writeLogFile("applicationEnterpris" + applicationEnterpris);
//		RecordLog.writeLogFile("condition" + enterprises == null && applicationEnterpris.size() == 0);

		// if (enterprises == null) {
		getAccDetailsIfExist = custDetailsService.checkIfAcctExist(formReqModel.getAccountNo());
		RecordLog.writeLogFile("ValidateUserService account details exist for account:"+formReqModel.getAccountNo()+" checkifaccountexist method resp "+getAccDetailsIfExist);
		if (getAccDetailsIfExist != null) {
			if ((String) getAccDetailsIfExist.get("statusCode") == "00") {
				boolean exist=false;
				try {
				 exist=mobileValidation(getAccDetailsIfExist,enterprises);
				}catch(Exception e) {
					RecordLog.writeLogFile("Exception occured at while mobile validtn:for account "+formReqModel.getAccountNo()+Arrays.toString(e.getStackTrace())+" Exception name: "+e.getMessage());

					recordIdResp = new RecordIdResp();
					recordIdResp.setAppFormId("");
					recordIdResp.setEnterpriseId("");
					recordIdResp.setPrefCorp("");

					serviceResp = new GetValidateUserSaveFlowModelResp();
					serviceResp.setStatus(false);
					serviceResp.setMessage("Exception while validating mobile number.");              
					serviceResp.setDescription("failed");
					serviceResp.setRecordId(recordIdResp);
					return serviceResp;
				}
				//account validation
				boolean accountExist=false;
				try {
					accountExist=accountValidation(formReqModel.getAccountNo());
					}catch(Exception e) {
						RecordLog.writeLogFile("Exception occured at:for account "+formReqModel.getAccountNo()+Arrays.toString(e.getStackTrace())+" Exception name: "+e.getMessage());

						recordIdResp = new RecordIdResp();
						recordIdResp.setAppFormId("");
						recordIdResp.setEnterpriseId("");
						recordIdResp.setPrefCorp("");

						serviceResp = new GetValidateUserSaveFlowModelResp();
						serviceResp.setStatus(false);
						serviceResp.setMessage("Exception while validating account number.");              
						serviceResp.setDescription("failed");
						serviceResp.setRecordId(recordIdResp);
						return serviceResp;
					}
				if(!accountExist) {
				if(!exist) {
				serviceResp = customerDetailsFlow(getAccDetailsIfExist, formReqModel.getMobileNo());
				}else {
					recordIdResp = new RecordIdResp();
					recordIdResp.setAppFormId("");
					recordIdResp.setEnterpriseId("");
					recordIdResp.setPrefCorp("");

					serviceResp = new GetValidateUserSaveFlowModelResp();
					serviceResp.setStatus(false);
					serviceResp.setMessage("Mobile number already exists");              
					serviceResp.setDescription("failed");
					serviceResp.setRecordId(recordIdResp);
				}
				}else {
					recordIdResp = new RecordIdResp();
					recordIdResp.setAppFormId("");
					recordIdResp.setEnterpriseId("");
					recordIdResp.setPrefCorp("");

					serviceResp = new GetValidateUserSaveFlowModelResp();
					serviceResp.setStatus(false);
					serviceResp.setMessage("Application already exists please contact branch");              
					serviceResp.setDescription("failed");
					serviceResp.setRecordId(recordIdResp);
				}
				RecordLog.writeLogFile("ValidateUserService response of customer details flow account:"+formReqModel.getAccountNo()+"  resp "+serviceResp);
			} else if ((String) getAccDetailsIfExist.get("statusCode") == "01"
					&& getAccDetailsIfExist.get("status").equals("Account Number already taken")) {
				serviceResp = customerDetailsFlow_existingAccnt(formReqModel.getAccountNo(),
						formReqModel.getMobileNo());
				RecordLog.writeLogFile("ValidateUserService response of  customerDetailsFlow_existingAccnt flow account:"+formReqModel.getAccountNo()+"  resp "+serviceResp);

			} else {
				recordIdResp = new RecordIdResp();
				recordIdResp.setAppFormId("");
				recordIdResp.setEnterpriseId("");
				recordIdResp.setPrefCorp("");

				serviceResp = new GetValidateUserSaveFlowModelResp();
				serviceResp.setStatus(false);
				serviceResp.setMessage((String) getAccDetailsIfExist.get("status"));
				serviceResp.setDescription("failed");
				serviceResp.setRecordId(recordIdResp);
			}
		} else {
			recordIdResp = new RecordIdResp();
			recordIdResp.setAppFormId("");
			recordIdResp.setEnterpriseId("");
			recordIdResp.setPrefCorp("");

			serviceResp = new GetValidateUserSaveFlowModelResp();
			serviceResp.setStatus(false);
			serviceResp.setMessage("No response from gateway.");
			serviceResp.setDescription("failed");
			serviceResp.setRecordId(recordIdResp);
		}
		/*
		 * } else { serviceResp = customerDetailsFlow_existingAccnt_v1(enterprises,
		 * formReqModel.getMobileNo()); }
		 */

		/*
		 * } else { getAccDetailsIfExist =
		 * custDetailsService.checkIfAcctExist(formReqModel.getAccountNo()); serviceResp
		 * = customerDetailsFlow_existingAccnt_v1(enterprises,
		 * formReqModel.getMobileNo()); } else { List<Tuple> whatdetailscomming =
		 * applicationEnterprisRepository
		 * .getApplEnterpriseDetails(formReqModel.getAccountNo()); if
		 * (whatdetailscomming != null) { RecordLog.writeLogFile("whatdetailscomming size "
		 * + whatdetailscomming.size());
		 * 
		 * if (whatdetailscomming.size() == 0) { getAccDetailsIfExist =
		 * custDetailsService.checkIfAcctExist(formReqModel.getAccountNo()); if
		 * (getAccDetailsIfExist != null) { if ((String)
		 * getAccDetailsIfExist.get("statusCode") == "00") { serviceResp =
		 * customerDetailsFlow(getAccDetailsIfExist, formReqModel.getMobileNo()); } } }
		 * else if (whatdetailscomming.size() > 0) { Tuple tuple =
		 * whatdetailscomming.get(0); String applicationFormId = (String)
		 * tuple.get("application_form_id"); enterprises =
		 * enterprisesRepository.findByApplicationFormId(Long.valueOf(applicationFormId)
		 * ); RecordLog.writeLogFile("enterprises " + enterprises);
		 * 
		 * User user =
		 * userRepository.findByEnterpriseIdAndMobile(String.valueOf(enterprises.getId()
		 * ), String.valueOf(formReqModel.getMobileNo()));
		 * 
		 * RecordLog.writeLogFile("user " + user); recordIdResp = new RecordIdResp();
		 * recordIdResp.setAppFormId(String.valueOf(enterprises.getApplicationFormId()))
		 * ; recordIdResp.setEnterpriseId(String.valueOf(enterprises.getId()));
		 * recordIdResp.setPrefCorp(enterprises.getPrefCorp());
		 * recordIdResp.setPrefNo(user.getPrefNo());
		 * recordIdResp.setLongNumber(longCode); recordIdResp.setRetryCount(retryCount);
		 * 
		 * serviceResp = new GetValidateUserSaveFlowModelResp();
		 * serviceResp.setStatus(true); serviceResp.setMessage("success");
		 * serviceResp.setDescription("user re-validated");
		 * serviceResp.setRecordId(recordIdResp);
		 * 
		 * } else { recordIdResp = new RecordIdResp(); recordIdResp.setAppFormId("");
		 * recordIdResp.setEnterpriseId(""); recordIdResp.setPrefCorp("");
		 * recordIdResp.setPrefNo("");
		 * 
		 * serviceResp = new GetValidateUserSaveFlowModelResp();
		 * serviceResp.setStatus(false); serviceResp.setMessage("");
		 * serviceResp.setDescription("no record found");
		 * serviceResp.setRecordId(recordIdResp); } } else { recordIdResp = new
		 * RecordIdResp(); recordIdResp.setAppFormId("");
		 * recordIdResp.setEnterpriseId(""); recordIdResp.setPrefCorp("");
		 * recordIdResp.setPrefNo("");
		 * 
		 * serviceResp = new GetValidateUserSaveFlowModelResp();
		 * serviceResp.setStatus(false); serviceResp.setMessage("");
		 * serviceResp.setDescription("its null");
		 * serviceResp.setRecordId(recordIdResp); }
		 */

		/*
		 * Long formId = null; if (applicationEnterpris.size() > 0) { for (int i = 0; i
		 * < applicationEnterpris.size(); i++) { formId =
		 * applicationEnterpris.get(i).getApplicationFormId(); Optional<ApplicationForm>
		 * applicationForm = applicationFormRepository.findById(formId);
		 * 
		 * if (applicationForm.get().getStatus().equals("rejected")) {
		 * getAccDetailsIfExist =
		 * custDetailsService.checkIfAcctExist(formReqModel.getAccountNo()); if
		 * (getAccDetailsIfExist != null) { if ((String)
		 * getAccDetailsIfExist.get("statusCode") == "00") { serviceResp =
		 * customerDetailsFlow(getAccDetailsIfExist, formReqModel.getMobileNo()); } else
		 * if ((String) getAccDetailsIfExist.get("statusCode") == "01" &&
		 * getAccDetailsIfExist.get("status").equals("Account Number already taken")) {
		 * serviceResp = customerDetailsFlow_existingAccnt(formReqModel.getAccountNo(),
		 * formReqModel.getMobileNo()); } } } }
		 * 
		 * } else { recordIdResp = new RecordIdResp(); recordIdResp.setAppFormId("");
		 * recordIdResp.setEnterpriseId(""); recordIdResp.setPrefCorp("");
		 * 
		 * serviceResp = new GetValidateUserSaveFlowModelResp();
		 * serviceResp.setStatus(false);
		 * serviceResp.setMessage("some issue occured , please try after sometime");
		 * serviceResp.setDescription("failed"); serviceResp.setRecordId(recordIdResp);
		 * }
		 * 
		 * }
		 */
		RecordLog.writeLogFile("ValidateUserService getAccDetailsIfExistResp method response accnumber "+formReqModel.getAccountNo()+" resp "+serviceResp);
		return serviceResp;
	}

	public GetValidateUserSaveFlowModelResp customerDetailsFlow_existingAccnt_v1(Enterprises enterprises,
			String mobileNo) {
		GetValidateUserSaveFlowModelResp serviceResp = null;
		RecordIdResp recordIdResp = null;

		if (enterprises == null) {
			recordIdResp = new RecordIdResp();
			recordIdResp.setAppFormId("");
			recordIdResp.setEnterpriseId("");
			recordIdResp.setPrefCorp("");
			recordIdResp.setPrefNo("");

			serviceResp = new GetValidateUserSaveFlowModelResp();
			serviceResp.setStatus(false);
			serviceResp.setMessage("");
			serviceResp.setDescription("no record found");
			serviceResp.setRecordId(recordIdResp);
		} else {
			User user = userRepository.findFirstByEnterpriseIdAndMarkAsEnabledAndMobileOrderByCreatedAtDesc(
					String.valueOf(enterprises.getId()), true, mobileNo);

			// RecordLog.writeLogFile(user.getPrefNo());
			if (user == null) {
				serviceResp = new GetValidateUserSaveFlowModelResp();
				serviceResp.setStatus(false);
				serviceResp.setMessage("user not found");
				serviceResp.setDescription("failed");
				serviceResp.setRecordId(recordIdResp);
			} else {
				recordIdResp = new RecordIdResp();
				recordIdResp.setAppFormId(String.valueOf(enterprises.getApplicationFormId()));
				recordIdResp.setEnterpriseId(String.valueOf(enterprises.getId()));
				recordIdResp.setPrefCorp(enterprises.getPrefCorp());
				recordIdResp.setPrefNo(user.getPrefNo());
				recordIdResp.setLongNumber(longCode);
				recordIdResp.setRetryCount(retryCount);

				serviceResp = new GetValidateUserSaveFlowModelResp();
				serviceResp.setStatus(true);
				serviceResp.setMessage("user has been re validated");
				serviceResp.setDescription("success");
				serviceResp.setRecordId(recordIdResp);
			}
		}
		return serviceResp;
	}

	public GetValidateUserSaveFlowModelResp customerDetailsFlow_existingAccnt(String accountNo, String mobileNo) {
		GetValidateUserSaveFlowModelResp serviceResp = null;
		RecordIdResp recordIdResp = null;

		Enterprises enterprises = enterprisesRepository.findByAccNoAndActive(accountNo, true);

		if (enterprises == null) {
			recordIdResp = new RecordIdResp();
			recordIdResp.setAppFormId("");
			recordIdResp.setEnterpriseId("");
			recordIdResp.setPrefCorp("");
			recordIdResp.setPrefNo("");

			serviceResp = new GetValidateUserSaveFlowModelResp();
			serviceResp.setStatus(false);
			serviceResp.setMessage("");
			serviceResp.setDescription("Enterprise record not found");
			serviceResp.setRecordId(recordIdResp);
		} else {
			User user = userRepository.findFirstByEnterpriseIdAndMarkAsEnabledAndMobileOrderByCreatedAtDesc(
					String.valueOf(enterprises.getId()), true, mobileNo);

			if (user == null) {
				serviceResp = new GetValidateUserSaveFlowModelResp();
				serviceResp.setStatus(false);
				serviceResp.setMessage("User record not found, Please input the correct mobile number");
				serviceResp.setDescription("failed");
				serviceResp.setRecordId(recordIdResp);
			}else if(user.getRole()!=null &&  user.getRole().equals("external")) {
				serviceResp = new GetValidateUserSaveFlowModelResp();
				serviceResp.setStatus(false);
				serviceResp.setMessage("Not authorized to login");
				serviceResp.setDescription("failed");
				serviceResp.setRecordId(recordIdResp);
			}  else {
//				RecordLog.writeLogFile(user.getPrefNo());
				
				RecordLog.writeLogFile("updating user last activity at field to todays date with mobile "+user.getMobile());
				user.setLastActivityAt(new Timestamp(new Date(System.currentTimeMillis()).getTime()));
				userRepository.save(user);
				
				recordIdResp = new RecordIdResp();
				recordIdResp.setAppFormId(String.valueOf(enterprises.getApplicationFormId()));
				recordIdResp.setEnterpriseId(String.valueOf(enterprises.getId()));
				recordIdResp.setPrefCorp(enterprises.getPrefCorp());
				recordIdResp.setPrefNo(user.getPrefNo());
				recordIdResp.setLongNumber(longCode);
				recordIdResp.setRetryCount(retryCount);

				serviceResp = new GetValidateUserSaveFlowModelResp();
				serviceResp.setStatus(true);
				serviceResp.setMessage("user has been re validated");
				serviceResp.setDescription("success");
				serviceResp.setRecordId(recordIdResp);
			}

		}
		return serviceResp;
	}

	public Map<String, Object> getCustomerDetails(Map<String, Object> getAccDetailsIfExist, String mobileNumber) {
		JSONObject getResult = null;
		JSONObject getAccountDetails = null;
		JSONObject getDetail = null;
		String constitution = null;
		JSONArray getDetails = null;
		boolean soleProprietorship = false;
		Long getMobileNumber = 0L;
		JSONObject selectedAccount = null;
		Map<String, Object> getCustDetailsResp = null;
//		RecordLog.writeLogFile("custDetails " + getAccDetailsIfExist);

		JSONObject getCustDetails = new JSONObject(getAccDetailsIfExist);
		RecordLog.writeLogFile("ValidateUserService getCustDetails mobile "+mobileNumber+" accountdetails  " + getCustDetails);

		if (getCustDetails.get("status").equals("Success")) {
			if (getCustDetails.has("Result")) {
				getCustDetailsResp = new HashMap<String, Object>();
				getResult = (JSONObject) getCustDetails.get("Result");
				getAccountDetails = getResult.getJSONObject("GetCustomerDetailsResp").getJSONObject("AccountDetails");
				constitution = getAccountDetails.getString("CONSTITUTION");

				if (getAccountDetails.getJSONObject("RELATEDPARTY").get("DETAILS") instanceof JSONObject) {
					getDetail = getAccountDetails.getJSONObject("RELATEDPARTY").getJSONObject("DETAILS");
					getDetails = new JSONArray();
					getDetails.put(getDetail);
				} else if (getAccountDetails.getJSONObject("RELATEDPARTY").get("DETAILS") instanceof JSONArray) {
					getDetails = getAccountDetails.getJSONObject("RELATEDPARTY").getJSONArray("DETAILS");
				}
				RecordLog.writeLogFile("ValidateUserService getDetails array " + getDetails);
				getCustDetailsResp.put("getDetailsLen", getDetails.length());
				if (constitution.toUpperCase().equals("SP") || getDetails.length() == 1) {
					soleProprietorship = true;
					getCustDetailsResp.put("soleProprietorship", soleProprietorship);
				} else {
					soleProprietorship = false;
					getCustDetailsResp.put("soleProprietorship", soleProprietorship);
				}
				for (int i = 0; i < getDetails.length(); i++) {
					getMobileNumber = ((JSONObject) getDetails.get(i)).getLong("CONTACT_NO");

					String mobNoFrmJson = String.valueOf(getMobileNumber);
					String mobileNo = mobileNumber;

					// if (String.valueOf(getMobileNumber).equals(String.valueOf(mobileNumber))) {
					if (mobileNo.equals(mobNoFrmJson)) {
					RecordLog.writeLogFile("ValidateUserService incomming mobilenumber "+mobileNo+" mobile from json "+mobNoFrmJson);
						selectedAccount = (JSONObject) getDetails.get(i);
						getCustDetailsResp.put("selectedAccount", selectedAccount);
						return getCustDetailsResp;
					} else {
					RecordLog.writeLogFile("ValidateUserService mobiles not matching outgoing mobile "+mobileNo+" json mobile "+mobNoFrmJson);
						getCustDetailsResp.put("selectedAccount", new JSONObject().put("ERRORMSG", "MOBNOINVALID"));
					}
				}
			}
		}
		RecordLog.writeLogFile("ValidateUserService getCustDetails mobile "+mobileNumber+" response  " + getCustDetailsResp);
		return getCustDetailsResp;
	}

	public GetValidateUserSaveFlowModelResp customerDetailsFlow(Map<String, Object> getAccDetailsIfExist,
			String mobileNumber) {
		JSONObject getSelectedAccounts = null;
		String genRefNo = null;
		ApplicationForm isGenRefNoExist = null;
		Map<String, Object> getValidateUserSaveFlow = null;
		List<?> getUserRespList = null;
		String prefNo = null;

		GetValidateUserSaveFlowModelResp getValidateUserSaveFlowResp = null;
		RecordIdResp recordIdResp = null;
		Map<String, Object> mapCustDetails = getCustomerDetails(getAccDetailsIfExist, mobileNumber);
		RecordLog.writeLogFile("ValidateUserService mapCustDetails mobile "+mobileNumber+"   " + mapCustDetails);
		if (mapCustDetails.get("soleProprietorship").equals(true)) {
			if (mapCustDetails.get("selectedAccount") instanceof JSONObject) {
				if (((JSONObject) mapCustDetails.get("selectedAccount")).getString("ERRORMSG").equals("Success")) {
					getSelectedAccounts = (JSONObject) mapCustDetails.get("selectedAccount");
				} else {
					recordIdResp = new RecordIdResp();
					recordIdResp.setAppFormId("");
					recordIdResp.setEnterpriseId("");
					recordIdResp.setPrefCorp("");
					recordIdResp.setPrefNo("");
					recordIdResp.setLongNumber(longCode);
					recordIdResp.setRetryCount(retryCount);

					getValidateUserSaveFlowResp = new GetValidateUserSaveFlowModelResp();
					getValidateUserSaveFlowResp.setDescription("failed");
					getValidateUserSaveFlowResp.setMessage("Requested mobno not matched for the given account");
					getValidateUserSaveFlowResp.setStatus(false);
					getValidateUserSaveFlowResp.setRecordId(recordIdResp);
					return getValidateUserSaveFlowResp;
				}
			}
		} else {
			if (mapCustDetails.get("selectedAccount") instanceof JSONObject) {
				if (((JSONObject) mapCustDetails.get("selectedAccount")).getString("ERRORMSG").equals("Success")) {
					getSelectedAccounts = (JSONObject) mapCustDetails.get("selectedAccount");
				} else {
					recordIdResp = new RecordIdResp();
					recordIdResp.setAppFormId("");
					recordIdResp.setEnterpriseId("");
					recordIdResp.setPrefCorp("");
					recordIdResp.setPrefNo("");
					recordIdResp.setLongNumber(longCode);
					recordIdResp.setRetryCount(retryCount);

					getValidateUserSaveFlowResp = new GetValidateUserSaveFlowModelResp();
					getValidateUserSaveFlowResp.setDescription("failed");
					getValidateUserSaveFlowResp.setMessage("Requested mobno not matched for the given account");
					getValidateUserSaveFlowResp.setStatus(false);
					getValidateUserSaveFlowResp.setRecordId(recordIdResp);

					return getValidateUserSaveFlowResp;
				}
			}
		}
		genRefNo = generateRefNumber();
		for (int i = 0; i < 3; i++) {
			isGenRefNoExist = applicationFormRepository.findByRefNo(genRefNo);
			if (isGenRefNoExist == null) {
				break;
			} else {
				genRefNo = generateRefNumber();
				isGenRefNoExist = applicationFormRepository.findByRefNo(genRefNo);
			}
		}
		getValidateUserSaveFlow = validateUserSaveFlow(genRefNo, getAccDetailsIfExist, mobileNumber,
				getSelectedAccounts);
		if (getValidateUserSaveFlow == null) {
			recordIdResp = new RecordIdResp();
			recordIdResp.setAppFormId("");
			recordIdResp.setEnterpriseId("");
			recordIdResp.setPrefNo("");
			recordIdResp.setPrefCorp("");
			recordIdResp.setLongNumber(longCode);
			recordIdResp.setRetryCount(retryCount);

			getValidateUserSaveFlowResp = new GetValidateUserSaveFlowModelResp();
			getValidateUserSaveFlowResp.setDescription("failed");
			getValidateUserSaveFlowResp.setMessage("Error in validate user flow");
			getValidateUserSaveFlowResp.setStatus(false);
			getValidateUserSaveFlowResp.setRecordId(recordIdResp);
		} else {
			if (getValidateUserSaveFlow.get("enterprisesResp") instanceof Enterprises
					&& getValidateUserSaveFlow.get("userRespList") instanceof List<?>) {
				String appFormId = String
						.valueOf(((Enterprises) getValidateUserSaveFlow.get("enterprisesResp")).getApplicationFormId());
				String enterpriseId = String
						.valueOf(((Enterprises) getValidateUserSaveFlow.get("enterprisesResp")).getId());
				String prefCorp = String
						.valueOf(((Enterprises) getValidateUserSaveFlow.get("enterprisesResp")).getPrefCorp());

				getUserRespList = (List<?>) getValidateUserSaveFlow.get("userRespList");
				for (int i = 0; i < getUserRespList.size(); i++) {
					User user = (User) getUserRespList.get(i);
					if (user.getMobile().equals(mobileNumber)) {
						prefNo = user.getPrefNo();
					}
				}

				recordIdResp = new RecordIdResp();
				recordIdResp.setAppFormId(appFormId);
				recordIdResp.setEnterpriseId(enterpriseId);
				recordIdResp.setPrefCorp(prefCorp);
				recordIdResp.setPrefNo(prefNo);
				recordIdResp.setLongNumber(longCode);
				recordIdResp.setRetryCount(retryCount);

				getValidateUserSaveFlowResp = new GetValidateUserSaveFlowModelResp();
				getValidateUserSaveFlowResp.setDescription("success");
				getValidateUserSaveFlowResp.setMessage("User has been validated & created");
				getValidateUserSaveFlowResp.setStatus(true);
				getValidateUserSaveFlowResp.setRecordId(recordIdResp);
			} else {
				recordIdResp = new RecordIdResp();
				recordIdResp.setAppFormId("");
				recordIdResp.setEnterpriseId("");
				recordIdResp.setPrefCorp("");
				recordIdResp.setPrefNo("");
				recordIdResp.setLongNumber(longCode);
				recordIdResp.setRetryCount(retryCount);

				getValidateUserSaveFlowResp = new GetValidateUserSaveFlowModelResp();
				getValidateUserSaveFlowResp.setDescription("failed");
				getValidateUserSaveFlowResp.setMessage(String.valueOf(getValidateUserSaveFlow.get("desc")));
				getValidateUserSaveFlowResp.setStatus(false);
				getValidateUserSaveFlowResp.setRecordId(recordIdResp);
			}
		}

		return getValidateUserSaveFlowResp;
	}

	@Transactional
	public Map<String, Object> validateUserSaveFlow(String genRefNo, Map<String, Object> custDetails,
			String mobileNumber, JSONObject getSelectedAccounts) {
		JSONObject getDetail = null;
		JSONObject getResult = null;
		JSONObject getAccountDetails = null;
		JSONArray getDetails = null;

		ApplicationForm applicationFormResp = null;
		ApplicationEnterpris applicationEnterprisResp = null;
		ApplicationUser applicationUserResp = null;
		List<ApplicationUser> applicationUserRespList = null;
		Enterprises enterprisesResp = null;
		AccountNoIdentifier accountNoIdentifierResp = null;
		List<User> userRespList = null;
		Map<String, Object> sendRespToController = null;

		Long applicationFormRespId = null;
		Long enterpriseIdForUser = null;
		Long applicationEnterprisRespId = null;
		String applicationPrefCorp = null;
		String prefNo = null;
		int detailLength = 0;
		String constitution = null;

		try {
			getResult = new JSONObject(custDetails);
			getAccountDetails = getResult.getJSONObject("Result").getJSONObject("GetCustomerDetailsResp")
					.getJSONObject("AccountDetails");
			constitution = getAccountDetails.getString("CONSTITUTION");
			String.valueOf(getAccountDetails.get("MOBILENUMBER"));
			getAccountDetails.getString("EMAIL");
			if (getAccountDetails.getJSONObject("RELATEDPARTY").get("DETAILS") instanceof JSONObject) {
				getDetail = getAccountDetails.getJSONObject("RELATEDPARTY").getJSONObject("DETAILS");
				getDetails = new JSONArray();
				getDetails.put(getDetail);
			} else if (getAccountDetails.getJSONObject("RELATEDPARTY").get("DETAILS") instanceof JSONArray) {
				getDetails = getAccountDetails.getJSONObject("RELATEDPARTY").getJSONArray("DETAILS");
			}
			RecordLog.writeLogFile("ValidateUserService getDetails array validateUserSaveFlow" + getDetails);
			detailLength = getDetails.length();

			// TODO :- insert into Application_Forms
			applicationFormResp = saveApplicationForm(genRefNo);
			RecordLog.writeLogFile("ValidateUserService added application form refnumber "+genRefNo +" for mobile mobileNumber"+ applicationFormResp);

			applicationFormRespId = applicationFormResp.getId();
//			RecordLog.writeLogFile("applicationFormRespId " + applicationFormRespId);

			// TODO :- insert into Application_Enterprises
			applicationEnterprisResp = saveApplicationEnterpris(getAccountDetails, applicationFormRespId);
			RecordLog.writeLogFile("ValidateUserService added applicationenterprise  applnformid  "+applicationFormRespId +" for mobile mobileNumber "+ applicationEnterprisResp);
			if (applicationEnterprisResp == null) {
				sendRespToController = new HashMap<String, Object>();
				sendRespToController.put("enterprisesResp", "");
				sendRespToController.put("userRespList", "");
				sendRespToController.put("desc", "branch not found in DB");
			} else {
				applicationEnterprisRespId = applicationEnterprisResp.getId();
				applicationPrefCorp = applicationEnterprisResp.getPrefCorp();
				RecordLog.writeLogFile("ValidateUserService applicationEnterprisRespId " + applicationEnterprisRespId +" appln prefcorp "+applicationPrefCorp);

				applicationUserRespList = new ArrayList<ApplicationUser>();
//				RecordLog.writeLogFile("detailLength " + detailLength);
				for (int i = 0; i < detailLength; i++) {
					prefNo = generatePrefNumber();
					RecordLog.writeLogFile("ValidateUserService prefNo " + prefNo);
					for (int j = 0; j < 3; j++) {
						applicationUserResp = applicationUserRepository.findByprefNo(prefNo);
						if (applicationUserResp == null) {
							break;
						} else {
							prefNo = generatePrefCorpNumber();
							applicationUserResp = applicationUserRepository.findByprefNo(prefNo);
						}
					}
					JSONObject getdetail = (JSONObject) getDetails.get(i);

					// TODO :- insert into Application_Users

					applicationUserResp = saveApplicationUsers(getdetail.getString("CUSTOMER_NAME"),
							String.valueOf(applicationEnterprisRespId), prefNo,
							String.valueOf(getdetail.get("CUSTOMER_ID")), String.valueOf(getdetail.get("CONTACT_NO")),
							getdetail.getString("EMAIL"));
					RecordLog.writeLogFile("ValidateUserService adding applicationusers " + applicationUserResp +" with prefnumber "+prefNo +" for applnenterpriseid "+applicationEnterprisRespId);

					applicationUserRespList.add(applicationUserResp);
				}

				// TODO :- insert into Enterprises
//				RecordLog.writeLogFile("applicationUserRespList " + applicationUserRespList);
				enterprisesResp = saveEnterprises(getAccountDetails, applicationFormRespId, applicationPrefCorp);
				enterpriseIdForUser = enterprisesResp.getId();

				// TODO :- insert into AccountNoIdentifier
				RecordLog.writeLogFile("ValidateUserService added enterprises with formid "+applicationFormRespId+" prefcorp "+applicationPrefCorp+" id " + enterprisesResp.getId());
				accountNoIdentifierResp = saveAccountNoIdentifier(getAccountDetails);
				
                BranchVerification branch =new BranchVerification();
                branch.setApplicationFormId(applicationFormRespId);
                if(constitution.equalsIgnoreCase("sp")) {
                	branch.setModeOfOperation("Single");
                	branch.setModeOfOperationConfirmed(Long.valueOf(0));
                	branch.setBoardResolutionConfirmed(Long.valueOf(0));
                	branch.setUserDetailsConfirmed(Long.valueOf(0));
                }else {
                	branch.setModeOfOperation("Joint");
                	branch.setModeOfOperationConfirmed(Long.valueOf(0));
                	branch.setBoardResolutionConfirmed(Long.valueOf(0));
                	branch.setUserDetailsConfirmed(Long.valueOf(0));
                }
                    branchVerificationRepository.save(branch);
				if (accountNoIdentifierResp != null) {

					// TODO :- insert into Users
				RecordLog.writeLogFile("ValidateUserService accountNoIdentifierResp " + accountNoIdentifierResp.getId());
					userRespList = saveUser(constitution, applicationUserRespList, enterpriseIdForUser);
					RecordLog.writeLogFile("ValidateUserService added new users ");

					if (userRespList.size() > 0) {
						sendRespToController = new HashMap<String, Object>();
						sendRespToController.put("enterprisesResp", enterprisesResp);
						sendRespToController.put("userRespList", userRespList);
					} else {
						sendRespToController = new HashMap<String, Object>();
						sendRespToController.put("enterprisesResp", "");
						sendRespToController.put("userRespList", "");
						sendRespToController.put("desc", "unable to save user");
					}
//					RecordLog.writeLogFile("sendRespToController " + sendRespToController);
				} else {
					sendRespToController = new HashMap<String, Object>();
					sendRespToController.put("enterprisesResp", "");
					sendRespToController.put("userRespList", "");
					sendRespToController.put("desc", "unable to save accountNoIdentifier");
				}
			}
		} catch (Exception e) {
			RecordLog.writeLogFile("ValidateUserService Exception occured at: "+Arrays.toString(e.getStackTrace())+" Exception name: "+e.getMessage());
			e.printStackTrace();
			sendRespToController = new HashMap<String, Object>();
			sendRespToController.put("enterprisesResp", "");
			sendRespToController.put("userRespList", "");
			sendRespToController.put("desc", "err occured , please try later");
		}
		return sendRespToController;
	}

	@Transactional
	public ApplicationForm saveApplicationForm(String genRefNo) {
		ApplicationForm applicationForm = null;
		ApplicationForm applicationFormResp = null;
		applicationForm = new ApplicationForm();
		applicationForm.setCreatedAt(new Timestamp(new Date().getTime()));
		applicationForm.setRefNo(genRefNo);
		applicationForm.setStatus("new");
		applicationForm.setStatusDesc("Application submitted to Bank and Waiting for Approval");
		applicationForm.setUpdatedAt(new Timestamp(new Date().getTime()));

		applicationFormResp = applicationFormRepository.save(applicationForm);
//		RecordLog.writeLogFile("applicationFormResp " + applicationFormResp);
		return applicationFormResp;
	}

	@Transactional
	public ApplicationEnterpris saveApplicationEnterpris(JSONObject getAccountDetails, Long applicationFormRespId) {
		JSONArray getAccList = null;

		String address1 = null;
		String address2 = null;
		String fullAddress = null;

		String accName = null;
		String custNo = null;
		String accNo = null;
		long applicationFormId = 0;
		String enterprisMobileNumber = null;
		String email = null;
		String prefCorp = null;
		String constitution = null;
		String branchId = null;
		String branchCode = null;

		Branch branch = null;

		ApplicationEnterpris applicationEnterpris = null;
		ApplicationEnterpris applicationEnterprisResp = null;
		ApplicationEnterpris isPrefCorpExist = null;
		if (getAccountDetails != null) {
			address1 = getAccountDetails.getString("ADDRESS1");
			address2 = getAccountDetails.getString("ADDRESS2");

			fullAddress = address1 + " " + address2;

			getAccList = getAccountDetails.getJSONObject("RELATEDPARTY").getJSONArray("ACC_LIST");
			accName = ((JSONObject) getAccList.get(0)).getString("CUST_NAME");
			custNo = String.valueOf(getAccountDetails.get("CUSTOMERID"));
			accNo = String.valueOf(getAccountDetails.get("FORACID"));
			applicationFormId = applicationFormRespId;
			enterprisMobileNumber = String.valueOf(getAccountDetails.get("MOBILENUMBER"));
			email = getAccountDetails.getString("EMAIL");
			constitution = getAccountDetails.getString("CONSTITUTION");

			if (getAccountDetails.get("SOLID") instanceof Integer) {
				branchId = String.valueOf(((Integer) getAccountDetails.get("SOLID")));
			} else if (getAccountDetails.get("SOLID") instanceof String) {
				branchId = (String) getAccountDetails.get("SOLID");
			}

			branchCode = getAccountDetails.getString("BRANCHCODE");
//			RecordLog.writeLogFile("branchId " + branchId);
			RecordLog.writeLogFile("ValidateUserService branchCode " + branchCode);

			// TODO Branches ID call
			branch = branchRepository.findFirstBySolIdAndBranchCodeOrderByCreatedAtDesc(branchId, branchCode);
			RecordLog.writeLogFile("branch " + branch);

			if (branch == null) {
				applicationFormRepository.deleteById(applicationFormRespId);
//				RecordLog.writeLogFile("applicationEnterprisResp deleted");
				return null;
			} else {
				prefCorp = generatePrefCorpNumber();
				for (int i = 0; i < 3; i++) {
					isPrefCorpExist = applicationEnterprisRepository.findByPrefCorp(prefCorp);
					if (isPrefCorpExist == null) {
						break;
					} else {
						prefCorp = generatePrefCorpNumber();
						isPrefCorpExist = applicationEnterprisRepository.findByPrefCorp(prefCorp);
					}
				}
				applicationEnterpris = new ApplicationEnterpris();
				applicationEnterpris.setAccName(accName);
				applicationEnterpris.setCustNo(custNo);
				applicationEnterpris.setAccNo(accNo);
				applicationEnterpris.setAddress(fullAddress);
				applicationEnterpris.setApplicationFormId(applicationFormId);
				applicationEnterpris.setMobile(enterprisMobileNumber);
				applicationEnterpris.setEmail(email);
				applicationEnterpris.setActive(true);
				applicationEnterpris.setPrefCorp(prefCorp);
				applicationEnterpris.setConstitution(constitution);
				applicationEnterpris.setBranchId(branch.getId());
				applicationEnterpris.setAuthBen("0");
				applicationEnterpris.setAuthExt("0");
				applicationEnterpris.setAuthFund("0");
				applicationEnterpris.setDailyLimit(Long.valueOf(messages.getDaily_transaction()));
				applicationEnterpris.setMonthlyLimit(Long.valueOf(messages.getMonthly_transaction()));
				applicationEnterpris.setCoolingPeriod(0L);
				applicationEnterpris.setCreatedAt(new Timestamp(new Date().getTime()));
				applicationEnterpris.setUpdatedAt(new Timestamp(new Date().getTime()));

				applicationEnterprisResp = applicationEnterprisRepository.save(applicationEnterpris);
		RecordLog.writeLogFile("ValidateUserService applicationEnterprisResp " + applicationEnterprisResp.getId());
			}
		}

		return applicationEnterprisResp;
	}

	@Transactional
	public ApplicationUser saveApplicationUsers(String userName, String applicationEnterpriseId, String prefNo,
			String custNo, String mobile, String email) {
		ApplicationUser applicationUser = null;
		ApplicationUser applicationUserResp = null;

		applicationUser = new ApplicationUser();
		applicationUser.setUserName(userName);
		applicationUser.setApplicationEnterpriseId(applicationEnterpriseId);
		applicationUser.setPrefNo(prefNo);
		applicationUser.setCustNo(custNo);
		applicationUser.setMobile(mobile);
		applicationUser.setEmail(email);
		applicationUser.setActive(true);
		applicationUser.setAuthorizedSignatory(0L);
		applicationUser.setTransLimit(messages.getFedcorp_per_transaction_limit());
		applicationUser.setCreatedAt(new Timestamp(new Date().getTime()));
		applicationUser.setUpdatedAt(new Timestamp(new Date().getTime()));

		applicationUserResp = applicationUserRepository.save(applicationUser);
//		RecordLog.writeLogFile("applicationUserResp " + applicationUserResp);
		return applicationUserResp;
	}

	@Transactional
	public Enterprises saveEnterprises(JSONObject getDetails, long applicationFormId, String applicationPrefCorp) {
		Enterprises enterprises = null;
		Enterprises enterprisesResp = null;

		String address1 = null;
		String address2 = null;
		String fullAddress = null;

		String accName = null;
		String custNo = null;
		String accNo = null;
		String enterprisMobileNumber = null;
		String constitution = null;

		enterprises = new Enterprises();

		if (getDetails != null) {
			address1 = getDetails.getString("ADDRESS1");
			address2 = getDetails.getString("ADDRESS2");

			fullAddress = address1 + " " + address2;

			accName = getDetails.getString("CUSTOMERNAME");
			custNo = String.valueOf(getDetails.get("CUSTOMERID"));
			accNo = String.valueOf(getDetails.get("FORACID"));
			enterprisMobileNumber = String.valueOf(getDetails.get("MOBILENUMBER"));
			constitution = getDetails.getString("CONSTITUTION");

			enterprises.setAccName(accName);
			enterprises.setMobile(enterprisMobileNumber);
			enterprises.setPrefCorp(applicationPrefCorp);
			enterprises.setAccNo(accNo);
			enterprises.setCustNo(custNo);
			enterprises.setAddress(fullAddress);
			enterprises.setConstitution(constitution);
			enterprises.setApplicationFormId(applicationFormId);
			enterprises.setActive(true);
			enterprises.setCoolingPeriod(0L);
			enterprises.setCreatedAt(new Timestamp(new Date().getTime()));
			enterprises.setUpdatedAt(new Timestamp(new Date().getTime()));

			enterprisesResp = enterprisesRepository.save(enterprises);
			RecordLog.writeLogFile("ValidateUserService enterprisesResp " + enterprisesResp);
		}
		return enterprisesResp;
	}

	@Transactional
	public AccountNoIdentifier saveAccountNoIdentifier(JSONObject getAccountDetails) {
		String identifier = null;
		String accNo = null;
		AccountNoIdentifier accountNoIdentifier = null;
		AccountNoIdentifier accountNoIdentifierResp = null;
		JSONArray getAccList = getAccountDetails.getJSONObject("RELATEDPARTY").getJSONArray("ACC_LIST");
		RecordLog.writeLogFile("ValidateUserService getAccList " + getAccList);
		identifier = ((JSONObject) getAccList.get(0)).getString("ACC_IDENTIFIER");
		accNo = String.valueOf(getAccountDetails.get("FORACID"));
		RecordLog.writeLogFile("ValidateUserService identifier " + identifier);
		accountNoIdentifierResp = accountNoIdentifierRepository.findByAccNo(accNo);
		if (accountNoIdentifierResp == null) {
			accountNoIdentifier = new AccountNoIdentifier();
			accountNoIdentifier.setIdentifier(identifier);
			accountNoIdentifier.setAccNo(accNo);
			accountNoIdentifier.setCreatedAt(new Timestamp(new Date().getTime()));
			accountNoIdentifier.setUpdatedAt(new Timestamp(new Date().getTime()));
			accountNoIdentifierResp = accountNoIdentifierRepository.save(accountNoIdentifier);
		} else {
			accountNoIdentifier = new AccountNoIdentifier();
			accountNoIdentifier.setId(accountNoIdentifierResp.getId());
			accountNoIdentifier.setIdentifier(identifier);
			accountNoIdentifier.setAccNo(accNo);
			accountNoIdentifier.setCreatedAt(new Timestamp(new Date().getTime()));
			accountNoIdentifier.setUpdatedAt(new Timestamp(new Date().getTime()));
			accountNoIdentifierResp = accountNoIdentifierRepository.save(accountNoIdentifier);
		}
		return accountNoIdentifierResp;
	}

	@Transactional
	public List<User> saveUser(String constitution, List<ApplicationUser> applicationUserRespList,
			Long enterpriseIdForUser) {
		String encryptedPswd = null;
		User userResp = null;
		List<User> userRespList = null;
		ApplicationUser applicationUser = null;
		String viewPwd = null;
		String viewPwdHash = null;
		int length = applicationUserRespList.size();
		userRespList = new LinkedList<User>();
		for (int i = 0; i < length; i++) {
			viewPwd = String.valueOf(generatePassword(8));
			viewPwdHash = passwordHashing.getPasswordHash(viewPwd);
			applicationUser = applicationUserRespList.get(i);
			encryptedPswd = Base64.getEncoder().encodeToString(viewPwd.getBytes());
			User user = new User();
			user.setUserName(applicationUser.getUserName());
			user.setEnterpriseId(String.valueOf(enterpriseIdForUser));
			user.setPrefNo(applicationUser.getPrefNo());
			user.setCustNo(applicationUser.getCustNo());
			user.setMobile(applicationUser.getMobile());
			//user.setEncryptedPassword(encryptedPswd);
			user.setEmail(applicationUser.getEmail());
			//user.setViewPwd(viewPwd);
			user.setViewPwdHash(viewPwdHash);
			user.setMarkAsEnabled(true);
			user.setSignInCount(0);
			user.setWebSignInCount(0L);
			user.setAuthorizedSignatory(0L);
			user.setTransLimit("0");
			user.setPasswordChangedAt((new Timestamp(new Date().getTime())));
			if (constitution.toUpperCase().equals("SP")) {
				user.setActivationStatus("pending_sole_activation");
			} else {
				user.setActivationStatus("pending_approval");
			}
			user.setCreatedAt(new Timestamp(new Date().getTime()));
			user.setUpdatedAt(new Timestamp(new Date().getTime()));
			user.setTransLimit("0");

			userResp = userRepository.save(user);
			RecordLog.writeLogFile("ValidateUserService added user with id " + userResp.getId()+" prefnum "+userResp.getPrefNo());
			userRespList.add(userResp);
		}
		return userRespList;
	}

	public String generateRefNumber() {
		String zeros = "000000";
		Random rnd = new Random();
		String s = Integer.toString(rnd.nextInt(0X1000000), 16);
		s = zeros.substring(s.length()) + s;
		s = "REF" + s;
		return s.toUpperCase();
	}

	public String generatePrefNumber() {
		String zeros = "00000";
		Random rnd = new Random();
		String s = Integer.toString(rnd.nextInt(0X100000), 16);
		s = zeros.substring(s.length()) + s;
		return s.toLowerCase();
	}

	public String generatePrefCorpNumber() {
		String zeros = "00000000";
		Random rnd = new Random();
		String s = Integer.toString(rnd.nextInt(0X1000000), 16);
		s = zeros.substring(s.length()) + s;
		return s.toLowerCase();
	}

	private static char[] generatePassword(int length) {
		String capitalCaseLetters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		String lowerCaseLetters = "abcdefghijklmnopqrstuvwxyz";
		String specialCharacters = "!@#_";
		String numbers = "1234567890";
		String combinedChars = capitalCaseLetters + lowerCaseLetters + specialCharacters + numbers;
		Random random = new Random();
		char[] password = new char[length];

		password[0] = lowerCaseLetters.charAt(random.nextInt(lowerCaseLetters.length()));
		password[1] = capitalCaseLetters.charAt(random.nextInt(capitalCaseLetters.length()));
		password[2] = specialCharacters.charAt(random.nextInt(specialCharacters.length()));
		password[3] = numbers.charAt(random.nextInt(numbers.length()));

		for (int i = 4; i < length; i++) {
			password[i] = combinedChars.charAt(random.nextInt(combinedChars.length()));
		}
		return password;
	}

	public boolean mobileValidation(Map<String,Object> getcustDetails,Enterprises enterprise) {
		//Map<String,Object> getCustDetails=new HashMap<>();
		RecordLog.writeLogFile("Mobile number validation started here for enterprise ");
		List<String> mobile=new ArrayList<>();
		boolean exist=false;
		JSONObject details=new JSONObject(getcustDetails);
		if(details.getJSONObject("Result").getJSONObject("GetCustomerDetailsResp").get("AccountDetails") instanceof JSONArray) {
			JSONArray accounts=details.getJSONObject("GetCustomerDetailsResp").getJSONArray("AccountDetails");
			for(int i=0;i<accounts.length();i++) {
				
				JSONObject accnt=accounts.getJSONObject(i);
				if(accnt.getJSONObject("RELATEDPARTY").get("DETAILS") instanceof JSONArray) {
					JSONArray actdetails=accnt.getJSONObject("RELATEDPARTY").getJSONArray("DETAILS");
					for (int k=0;k<actdetails.length();k++) {
						mobile.add(String.valueOf( actdetails.getJSONObject(k).get("CONTACT_NO")));
					}
				}else {
					mobile.add(String.valueOf( accnt.getJSONObject("RELATEDPARTY").getJSONObject("DETAILS").get("CONTACT_NO")));
				}
			}
			
		}else {
			JSONObject accountDet=details.getJSONObject("Result").getJSONObject("GetCustomerDetailsResp").getJSONObject("AccountDetails");
			if(accountDet.getJSONObject("RELATEDPARTY").get("DETAILS") instanceof JSONArray) {
				JSONArray actdetails=accountDet.getJSONObject("RELATEDPARTY").getJSONArray("DETAILS");
				for (int k=0;k<actdetails.length();k++) {
					mobile.add(String.valueOf(actdetails.getJSONObject(k).get("CONTACT_NO")));
				}
			}else {
				mobile.add(String.valueOf( accountDet.getJSONObject("RELATEDPARTY").getJSONObject("DETAILS").get("CONTACT_NO")));
			}
		}
		 RecordLog.writeLogFile("mobile from getcustdetails -----------------------------"+mobile);
			List<ApplicationUser> users=userRepository.findByMobileAndEnterprise(mobile);
			RecordLog.writeLogFile("mobile from getcustdetails -----------------------------"+mobile +" total records in db "+users.size());
			exist=users.size()==0?false:true;
		RecordLog.writeLogFile("Mobile number validation completed with value mobile exist "+exist);

		return exist;
	}
	
	public boolean accountValidation(String account) {
		RecordLog.writeLogFile("account number validation started with  account  "+account);
		boolean exist=false;
		List<ApplicationEnterpris> applnEntrp=userRepository.findByAccountInIntermediate(account);
		RecordLog.writeLogFile("with account-----------------------------"+account +" total records in db "+applnEntrp.size());
		exist=applnEntrp.size()==0?false:true;
		RecordLog.writeLogFile("account number validation completed with  account exist "+exist);
		return exist;
	}
	
	
}