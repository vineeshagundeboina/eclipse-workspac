package com.federal.fedmobilesmecore.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.federal.fedmobilesmecore.config.GlobalProperties;
import com.federal.fedmobilesmecore.config.RecordLog;
import com.federal.fedmobilesmecore.dto.ApplicationForm;
import com.federal.fedmobilesmecore.dto.Branch;
import com.federal.fedmobilesmecore.dto.Enterprises;
import com.federal.fedmobilesmecore.dto.ManagedAccount;
import com.federal.fedmobilesmecore.dto.User;
import com.federal.fedmobilesmecore.getcust.pojo.AccountDetail;
import com.federal.fedmobilesmecore.getcust.pojo.GetCustomerDetailsResp;
import com.federal.fedmobilesmecore.model.GetCustDetails;
import com.federal.fedmobilesmecore.repository.ApplicationEnterprisRepository;
import com.federal.fedmobilesmecore.repository.ApplicationFormRepository;
import com.federal.fedmobilesmecore.repository.EnterprisesRepository;
import com.federal.fedmobilesmecore.repository.GetCustomerBranchRepo;
import com.federal.fedmobilesmecore.repository.ManagedAccountRepo;
import com.federal.fedmobilesmecore.repository.UserRepository;

/**
 * @author Syed_Splenta
 *
 */
@Service
public class GetCustDetailsService {
	@Autowired
	EnterprisesRepository entRepository;

	@Autowired
	ApplicationEnterprisRepository enterprisRepository;

	@Autowired
	ApplicationFormRepository applicationFormRepository;

	@Autowired
	GetCustomerBranchRepo GetBranchDetails;

	@Autowired
	GlobalProperties messages;

	@Autowired
	GetCustDetails getCustDetails;

	@Autowired
	UserRepository userrepo;

	@Autowired
	ManagedAccountRepo managedaccrepo;

	public Map<String, Object> checkIfAcctExist(String acctno) {
		RecordLog.writeLogFile("getting custdetails for account:" + acctno);
		Map<String, Object> enterprisRepositoryResp = null;
		Map<String, Object> retMap = new HashMap<String, Object>();
		Object check = null;

		if (acctno == "" || acctno.length() <= 10) {
			enterprisRepositoryResp = new HashMap<String, Object>();
			enterprisRepositoryResp.put("status", messages.getcustinvalid);
			enterprisRepositoryResp.put("statusCode", "01");
			return enterprisRepositoryResp;
		}

		/*
		 * List<ApplicationEnterpris> applicationEnterpris =
		 * enterprisRepository.findByAccNoAndActive(acctno, true);
		 * RecordLog.writeLogFile(applicationEnterpris.size()); ArrayList<Long>
		 * applicationFormId = new ArrayList<>();
		 */
		try {
			Enterprises enterprise = entRepository.findByAccNoAndActive(acctno, true);
			if (enterprise == null) {
				RecordLog.writeLogFile("enterprise is not null for account "+acctno);

				enterprisRepositoryResp = new HashMap<String, Object>();
				enterprisRepositoryResp.put("status", "Success");
				enterprisRepositoryResp.put("statusCode", "00");
				String custDetailResponse = getCustDetails.getData(acctno);
//				RecordLog.writeLogFile("Fedcorp:" + custDetailResponse);
				JSONObject json = XML.toJSONObject(custDetailResponse);
				RecordLog.writeLogFile("getcustdetails response for the account :"+acctno+"  " + json);
				JSONObject GetCustomerDetailsObj = json.getJSONObject("GetCustomerDetailsResp");
				RecordLog.writeLogFile("getcustdetails response for  account"+acctno +"   "+ GetCustomerDetailsObj.toString());
				if (GetCustomerDetailsObj.get("AccountDetails") instanceof JSONArray) {
					
					
					
					JSONArray jsonArrays=GetCustomerDetailsObj.getJSONArray("AccountDetails");
					for(int i=0;i<jsonArrays.length();i++) {
						JSONObject singleAccount=jsonArrays.getJSONObject(i);
						
					
						if(!singleAccount.get("FORACID").toString().equals(acctno)) {
							json.getJSONObject("GetCustomerDetailsResp")
							.getJSONArray("AccountDetails").remove(i);
						}
						
					}

					
					
					
					
					
//					enterprisRepositoryResp = new HashMap<String, Object>();
//					enterprisRepositoryResp.put("status", "Try Different Account");
//					enterprisRepositoryResp.put("statusCode", "04");
//					return enterprisRepositoryResp;
				}
				
				
				JSONObject GetCustomerDetailsResp=null;
				if (GetCustomerDetailsObj.get("AccountDetails") instanceof JSONArray) {
					GetCustomerDetailsResp=json.getJSONObject("GetCustomerDetailsResp")
							.getJSONArray("AccountDetails").getJSONObject(0);
					json.getJSONObject("GetCustomerDetailsResp").put("AccountDetails", json.getJSONObject("GetCustomerDetailsResp")
							.getJSONArray("AccountDetails").getJSONObject(0));
					
				}else {

				 GetCustomerDetailsResp = json.getJSONObject("GetCustomerDetailsResp")
						.getJSONObject("AccountDetails");
				
				
				}
				
				
				
				
				RecordLog.writeLogFile("Fedcorp:" + GetCustomerDetailsResp.toString());
				if (GetCustomerDetailsResp.has("ERRORCODE")) {
					check = GetCustomerDetailsResp.get("ERRORCODE");
				} else {
					check = GetCustomerDetailsResp.get("ERRCODE");
				}
				if (check.equals("00")) {
					JSONObject GetCustomerDetailsadd = json.getJSONObject("GetCustomerDetailsResp")
							.getJSONObject("AccountDetails");
					String mskacct = GetCustomerDetailsadd.get("FORACID").toString();
					String mskemail = GetCustomerDetailsadd.get("EMAIL").toString();
					String mskmob = GetCustomerDetailsadd.get("MOBILENUMBER").toString();
					String custname = GetCustomerDetailsadd.get("CUSTOMERNAME").toString();
					String custacct = GetCustomerDetailsadd.get("FORACID").toString();
					mskacct = StringUtils.overlay(mskacct, StringUtils.repeat("X", mskacct.length() - 4), 0,
							mskacct.length() - 4);
					mskemail = StringUtils.overlay(mskemail, StringUtils.repeat("X", mskacct.length()), 4,
							mskacct.length());
					mskmob = StringUtils.overlay(mskmob, StringUtils.repeat("X", mskmob.length() - 2), 0,
							mskmob.length() - 2);
					json.getJSONObject("GetCustomerDetailsResp").getJSONObject("AccountDetails")
							.put("MASKED_ACCOUNTNUMBER", mskacct);
					json.getJSONObject("GetCustomerDetailsResp").getJSONObject("AccountDetails").put("MASKED_EMAIL",
							mskemail);
					json.getJSONObject("GetCustomerDetailsResp").getJSONObject("AccountDetails")
							.put("MASKED_MOBILENUMBER", mskmob);
					JSONArray array1 = new JSONArray();
					JSONObject getRelatedParty = json.getJSONObject("GetCustomerDetailsResp")
							.getJSONObject("AccountDetails").getJSONObject("RELATEDPARTY");

					if (getRelatedParty.get("DETAILS") instanceof JSONObject) {
//						RecordLog.writeLogFile("JSONOBJECT");
						JSONObject custDetailsadd = json.getJSONObject("GetCustomerDetailsResp")
								.getJSONObject("AccountDetails").getJSONObject("RELATEDPARTY").getJSONObject("DETAILS");
						RecordLog.writeLogFile("custDetailsadd>>" + custDetailsadd);
						JSONArray aactlist = new JSONArray();
						JSONObject aactlistobj = new JSONObject();
						String acctindtfr = RandomStringUtils.randomAlphanumeric(20).toUpperCase();
						aactlistobj.put("CUST_NAME", custname);
						aactlistobj.put("MASKED_ACC_NO", mskacct);
						aactlistobj.put("ACC_IDENTIFIER", acctindtfr);
						aactlist.put(aactlistobj);
						json.getJSONObject("GetCustomerDetailsResp").getJSONObject("AccountDetails")
								.getJSONObject("RELATEDPARTY").put("ACC_LIST", aactlist);
					} else if (getRelatedParty.get("DETAILS") instanceof JSONArray) {
						array1 = json.getJSONObject("GetCustomerDetailsResp").getJSONObject("AccountDetails")
								.getJSONObject("RELATEDPARTY").getJSONArray("DETAILS");
						for (int i = 0; i < array1.length(); i++) {
							JSONObject rec = array1.getJSONObject(i);
							String customername = rec.getString("CUSTOMER_NAME");
							String custacctno = rec.getString("CUSTOMER_NAME");

							JSONArray aactlist = new JSONArray();
							JSONObject aactlistobj = new JSONObject();
							String acctindtfr = RandomStringUtils.randomAlphanumeric(20).toUpperCase();

							aactlistobj.put("CUST_NAME", custname);
							aactlistobj.put("MASKED_ACC_NO", mskacct);
							aactlistobj.put("ACC_IDENTIFIER", acctindtfr);

							aactlist.put(aactlistobj);

							json.getJSONObject("GetCustomerDetailsResp").getJSONObject("AccountDetails")
									.getJSONObject("RELATEDPARTY").put("ACC_LIST", aactlist);
						}
					}
					String getSolId = GetCustomerDetailsadd.get("SOLID").toString();
					Branch getBranch = GetBranchDetails.findFirstBySolId(getSolId);
					String brname = getBranch.getBranchName().toString();
					JSONObject solidlist = new JSONObject();
					solidlist.put("SOL_ID", getSolId);
					solidlist.put("BRANCH", brname);
					json.getJSONObject("GetCustomerDetailsResp").getJSONObject("AccountDetails").put("SOL_ID_BRANCH",
							solidlist);
					if (json != JSONObject.NULL) {
						retMap = toMap(json);
					}
					enterprisRepositoryResp.put("status", "Success");
					enterprisRepositoryResp.put("statusCode", "00");
					enterprisRepositoryResp.put("Result", retMap);
				}

				else {
					enterprisRepositoryResp.put("status", GetCustomerDetailsResp.has("ERRMSG")? GetCustomerDetailsResp.get("ERRMSG"):GetCustomerDetailsResp.get("ERRORMSG"));
					enterprisRepositoryResp.put("statusCode", GetCustomerDetailsResp.has("ERRCODE")?GetCustomerDetailsResp.get("ERRCODE"):GetCustomerDetailsResp.get("ERRORCODE"));
				}

			} else if (enterprise != null) {
				/*
				 * for (int i = 0; i < applicationEnterpris.size(); i++) {
				 * applicationFormId.add(applicationEnterpris.get(i).getApplicationFormId()); }
				 */
				Optional<ApplicationForm> applicationForm = applicationFormRepository
						.getValidApplicationFormById(enterprise.getApplicationFormId());
				/*
				 * Optional<ApplicationForm> applicationForm = applicationFormRepository
				 * .findById(Long.valueOf(enterprise.getApplicationFormId()));
				 */
				RecordLog.writeLogFile("enterprise is not null account "+acctno);
				boolean isEmpty = applicationForm.isPresent();
				if (!isEmpty) {
					enterprisRepositoryResp = new HashMap<String, Object>();
					String custDetailResponse = getCustDetails.getData(acctno);
					RecordLog.writeLogFile("Fedcorp:" + custDetailResponse);
					JSONObject json = XML.toJSONObject(custDetailResponse);
					RecordLog.writeLogFile("Fedcorp: account "+acctno+"   " + json);
					JSONObject GetCustomerDetailsObj = json.getJSONObject("GetCustomerDetailsResp");
					RecordLog.writeLogFile("Fedcorp:" + GetCustomerDetailsObj.toString());
					if (GetCustomerDetailsObj.get("AccountDetails") instanceof JSONArray) {
						enterprisRepositoryResp = new HashMap<String, Object>();
						enterprisRepositoryResp.put("status", "Try With Different Account");
						enterprisRepositoryResp.put("statusCode", "04");
						return enterprisRepositoryResp;
					}

					JSONObject GetCustomerDetailsResp = json.getJSONObject("GetCustomerDetailsResp")
							.getJSONObject("AccountDetails");
					RecordLog.writeLogFile("Fedcorp:" + GetCustomerDetailsResp.toString());
					if (GetCustomerDetailsResp.has("ERRORCODE")) {
						check = GetCustomerDetailsResp.get("ERRORCODE");
					} else {
						check = GetCustomerDetailsResp.get("ERRCODE");
					}
					if (check.equals("00")) {
						JSONObject GetCustomerDetailsadd = json.getJSONObject("GetCustomerDetailsResp")
								.getJSONObject("AccountDetails");
						String mskacct = GetCustomerDetailsadd.get("FORACID").toString();
						String mskemail = GetCustomerDetailsadd.get("EMAIL").toString();
						String mskmob = GetCustomerDetailsadd.get("MOBILENUMBER").toString();
						String custname = GetCustomerDetailsadd.get("CUSTOMERNAME").toString();
						String custacct = GetCustomerDetailsadd.get("FORACID").toString();
						mskacct = StringUtils.overlay(mskacct, StringUtils.repeat("X", mskacct.length() - 4), 0,
								mskacct.length() - 4);
						mskemail = StringUtils.overlay(mskemail, StringUtils.repeat("X", mskacct.length()), 4,
								mskacct.length());
						mskmob = StringUtils.overlay(mskmob, StringUtils.repeat("X", mskmob.length() - 2), 0,
								mskmob.length() - 2);
						json.getJSONObject("GetCustomerDetailsResp").getJSONObject("AccountDetails")
								.put("MASKED_ACCOUNTNUMBER", mskacct);
						json.getJSONObject("GetCustomerDetailsResp").getJSONObject("AccountDetails").put("MASKED_EMAIL",
								mskemail);
						json.getJSONObject("GetCustomerDetailsResp").getJSONObject("AccountDetails")
								.put("MASKED_MOBILENUMBER", mskmob);
						JSONArray array1 = new JSONArray();
						JSONObject getRelatedParty = json.getJSONObject("GetCustomerDetailsResp")
								.getJSONObject("AccountDetails").getJSONObject("RELATEDPARTY");

						if (getRelatedParty.get("DETAILS") instanceof JSONObject) {

//							RecordLog.writeLogFile("JSONOBJECT");
							JSONObject custDetailsadd = json.getJSONObject("GetCustomerDetailsResp")
									.getJSONObject("AccountDetails").getJSONObject("RELATEDPARTY")
									.getJSONObject("DETAILS");

							RecordLog.writeLogFile("custDetailsadd>>" + custDetailsadd);

							JSONArray aactlist = new JSONArray();
							JSONObject aactlistobj = new JSONObject();
							String acctindtfr = RandomStringUtils.randomAlphanumeric(20).toUpperCase();

							aactlistobj.put("CUST_NAME", custname);
							aactlistobj.put("MASKED_ACC_NO", mskacct);
							aactlistobj.put("ACC_IDENTIFIER", acctindtfr);

							aactlist.put(aactlistobj);

							json.getJSONObject("GetCustomerDetailsResp").getJSONObject("AccountDetails")
									.getJSONObject("RELATEDPARTY").put("ACC_LIST", aactlist);

						} else if (getRelatedParty.get("DETAILS") instanceof JSONArray) {
							array1 = json.getJSONObject("GetCustomerDetailsResp").getJSONObject("AccountDetails")
									.getJSONObject("RELATEDPARTY").getJSONArray("DETAILS");
							for (int i = 0; i < array1.length(); i++) {
								JSONObject rec = array1.getJSONObject(i);
								String customername = rec.getString("CUSTOMER_NAME");
								String custacctno = rec.getString("CUSTOMER_NAME");
								JSONArray aactlist = new JSONArray();
								JSONObject aactlistobj = new JSONObject();
								String acctindtfr = RandomStringUtils.randomAlphanumeric(20).toUpperCase();

								aactlistobj.put("CUST_NAME", custname);
								aactlistobj.put("MASKED_ACC_NO", mskacct);
								aactlistobj.put("ACC_IDENTIFIER", acctindtfr);
								aactlist.put(aactlistobj);
								json.getJSONObject("GetCustomerDetailsResp").getJSONObject("AccountDetails")
										.getJSONObject("RELATEDPARTY").put("ACC_LIST", aactlist);
							}
						}
						String getSolId = GetCustomerDetailsadd.get("SOLID").toString();
						Branch getBranch = GetBranchDetails.findFirstBySolId(getSolId);
						String brname = getBranch.getBranchName().toString();
						JSONObject solidlist = new JSONObject();
						solidlist.put("SOL_ID", getSolId);
						solidlist.put("BRANCH", brname);
						json.getJSONObject("GetCustomerDetailsResp").getJSONObject("AccountDetails")
								.put("SOL_ID_BRANCH", solidlist);
						if (json != JSONObject.NULL) {
							retMap = toMap(json);
						}
						enterprisRepositoryResp.put("status", "Success");
						enterprisRepositoryResp.put("statusCode", "00");
						enterprisRepositoryResp.put("Result", retMap);
					} else {
						enterprisRepositoryResp.put("status", GetCustomerDetailsResp.has("ERRMSG")?GetCustomerDetailsResp.get("ERRMSG"):GetCustomerDetailsResp.get("ERRORMSG"));
						enterprisRepositoryResp.put("statusCode", GetCustomerDetailsResp.has("ERRCODE")?GetCustomerDetailsResp.get("ERRCODE"):GetCustomerDetailsResp.get("ERRORCODE"));
					}
				} else {
					enterprisRepositoryResp = new HashMap<String, Object>();
					enterprisRepositoryResp.put("status", messages.getcustdetlserr);
					enterprisRepositoryResp.put("statusCode", "01");
				}
			}
		} catch (IncorrectResultSizeDataAccessException e) {
			RecordLog.writeLogFile("Exception occured at: for account "+acctno+"  "+Arrays.toString(e.getStackTrace())+" Exception name: "+e.getMessage());
			e.printStackTrace();
			enterprisRepositoryResp = new HashMap<String, Object>();
			enterprisRepositoryResp.put("status", "More than one enterprise exists");
			enterprisRepositoryResp.put("statusCode", "02");
		} catch (Exception ex) {
			RecordLog.writeLogFile("Exception occured at: account "+acctno+"  "+Arrays.toString(ex.getStackTrace())+" Exception name: "+ex.getMessage());
			ex.printStackTrace();
			enterprisRepositoryResp = new HashMap<String, Object>();
			enterprisRepositoryResp.put("status", messages.getcustexperr);
			enterprisRepositoryResp.put("statusCode", "02");
		}
		return enterprisRepositoryResp;
	}

	public static Map<String, Object> toMap(JSONObject object) throws JSONException {
		Map<String, Object> map = new HashMap<String, Object>();

		Iterator<String> keysItr = object.keys();
		while (keysItr.hasNext()) {
			String key = keysItr.next();
			Object value = object.get(key);

			if (value instanceof JSONArray) {
				value = toList((JSONArray) value);
			}

			else if (value instanceof JSONObject) {
				value = toMap((JSONObject) value);
			}
			map.put(key, value);
		}
		return map;
	}

	public static List<Object> toList(JSONArray array) throws JSONException {
		List<Object> list = new ArrayList<Object>();
		for (int i = 0; i < array.length(); i++) {
			Object value = array.get(i);
			if (value instanceof JSONArray) {
				value = toList((JSONArray) value);
			}

			else if (value instanceof JSONObject) {
				value = toMap((JSONObject) value);
			}
			list.add(value);
		}
		return list;
	}

	public boolean isJSONArray(String input) {
		try {
			new JSONArray(input);
			return true;
		} catch (JSONException ex) {
			return false;
		}
	}

	/**
	 * @author Vikas </br>
	 *         This method will do the filtering of accounts based on account
	 *         numbers available in ManagedAccount table.If there are no records
	 *         available it will return all the details available in getcustdetails
	 *         API.
	 * @param getcustdetails
	 * @param prefno
	 * @return GetCustomerDetailsResp
	 */
	public GetCustomerDetailsResp filterAccounts(GetCustomerDetailsResp getcustdetails, String prefno,String mobile) {
		GetCustomerDetailsResp tempresp = getcustdetails;
		List<AccountDetail> existing_account_details = tempresp.getGetCustomerDetailsResp().getAccountDetails();
		List<AccountDetail> filtered_account_details = new ArrayList<AccountDetail>();
		List<ManagedAccount> accounts = new ArrayList<ManagedAccount>();
		if (StringUtils.isNotBlank(prefno)) {
			try {
				Optional<User> user = userrepo.findByPrefNoAndMobileAndMarkAsEnabled(prefno,mobile,true);
				if (user.isPresent()) {
					accounts = managedaccrepo.findByUserOrderByUpdatedAtDesc(user.get());
				}
			} catch (Exception e) {
				RecordLog.writeLogFile("Exception occured at: "+Arrays.toString(e.getStackTrace())+" Exception name: "+e.getMessage());
				e.printStackTrace();
			}
		}

		if (!CollectionUtils.isEmpty(accounts)) {
			// Filter based on foraccid and set the object and return it.
			ManagedAccount account = accounts.get(0);
			String[] accountlist = account.getAccounts().replace("[", "").replace("]", "").replaceAll("\"", "")
					.split(",");
			for (String acc : accountlist) {
				for (AccountDetail detail : existing_account_details) {
					if (detail.getForacid().equals(acc)) {
						filtered_account_details.add(detail);
						// break;
					}
				}
			}
			if (filtered_account_details.size() > 0) {
				tempresp.getGetCustomerDetailsResp().setAccountDetails(filtered_account_details);
				return tempresp;
			}
		}

		return getcustdetails;
	}

}
