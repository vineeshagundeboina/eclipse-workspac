package com.federal.fedmobilesmecore.service;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.federal.fedmobilesmecore.config.GlobalProperties;
import com.federal.fedmobilesmecore.config.RecordLog;
import com.federal.fedmobilesmecore.dto.User;
import com.federal.fedmobilesmecore.dto.UserMobileChanges;
import com.federal.fedmobilesmecore.model.GetCustDetails;
import com.federal.fedmobilesmecore.model.MpinCreateReqModel;
import com.federal.fedmobilesmecore.model.MpinReqModel;
import com.federal.fedmobilesmecore.model.RecordId_MpinCheck;
import com.federal.fedmobilesmecore.model.SMEMessage;
import com.federal.fedmobilesmecore.repository.ApplicationUserRepository;
import com.federal.fedmobilesmecore.repository.EnterprisesRepository;
import com.federal.fedmobilesmecore.repository.UserMobileChangesRepo;
import com.federal.fedmobilesmecore.repository.UserRepository;
import com.federal.fedmobilesmecore.dto.ApplicationUser;
import com.federal.fedmobilesmecore.dto.Enterprises;

@Repository
public class MpinCheckService {

	@Autowired
	GetCustDetails getCustDetails;
	@Autowired
	GlobalProperties messages;
	@Autowired
	UserRepository userRepository;
	@Autowired
	EnterprisesRepository Enterprises;
	@Autowired
	GetCustDetails getcustdetls;

	@Autowired
	FblLandingPageService landingservice;
	
	@Autowired
	UserMobileChangesRepo userMobileChangesRepo;
	
	@Autowired
	ApplicationUserRepository applicationUserRepository;
	
	@Autowired
	private SimBindingService simBindingService;
	
//	@Value("${appversion_android}")
	private String appVersionAndroid=System.getenv("appversion_android");
	
//	@Value("${appversion_ios}")
	private String appVersionIos=System.getenv("appversion_ios");

	
//	@Value("${osversion}")
	private String osVersion=System.getenv("osversion");

	public Map<String, Object> getUserDetails(String app_token, String pefcrpno, String mpinhash) {

		Map<String, Object> MpinCheckServiceResp = null;
		UserMobileChanges userMobileChanges=null;
		try {
		// ArrayList<String> applicationpasswd = new ArrayList<>();
		List<User> userEnterpris = userRepository.findByAppTokenAndMarkAsEnabled(app_token, true);

		if (userEnterpris.size() > 0) {
			if(userEnterpris.get(0).getMpinCheckStatus()==null || !(userEnterpris.get(0).getMpinCheckStatus().equals("blocked"))){

			String enterprs_id = userEnterpris.get(0).getEnterpriseId();

			String usermobno = userEnterpris.get(0).getMobile();

			String tmpinhash = userEnterpris.get(0).getMpin();

			long long_enterprs_id = Long.parseLong(enterprs_id);

			Enterprises Enterpris = Enterprises.findByIdAndPrefCorp(long_enterprs_id, pefcrpno);

			if (Enterpris == null) {
				MpinCheckServiceResp = new HashMap<String, Object>();
				MpinCheckServiceResp.put("status", false);
				MpinCheckServiceResp.put("description", messages.getmpincreateerr);
				MpinCheckServiceResp.put("message", messages.getmpincreateerr);
			} else {
				String getacctno = Enterpris.getAccNo();
				String custDetailResponse = getcustdetls.getData(getacctno);
//				RecordLog.writeLogFile("Fedcorp:" + custDetailResponse);
				JSONObject json = XML.toJSONObject(custDetailResponse);
				RecordLog.writeLogFile("Fedcorp customer detail:" + json);
				String requestData = json.toString(4);
//				RecordLog.writeLogFile(requestData);
				JSONObject jsonrequestData = new JSONObject(requestData);
				JSONObject transactionResult = jsonrequestData.getJSONObject("GetCustomerDetailsResp");
				if (transactionResult.get("AccountDetails") instanceof JSONObject) {
					JSONObject header = transactionResult.getJSONObject("AccountDetails").getJSONObject("RELATEDPARTY");

					if (header.get("DETAILS") instanceof JSONObject) {

						JSONObject ResponseHeader = header.getJSONObject("DETAILS");

						String CONTACT_NO = ResponseHeader.get("CONTACT_NO").toString();

						String customerId=ResponseHeader.get("CUSTOMER_ID").toString();	
						String custId = userEnterpris.get(0).getCustNo();
						String prefNo = userEnterpris.get(0).getPrefNo();
						
						if (customerId.equals(custId)) {
							RecordLog.writeLogFile("both customer id are equal:"+customerId  +" "+custId);
							RecordLog.writeLogFile("both customer are getcust:"+CONTACT_NO  +" user: "+usermobno);
							if (!CONTACT_NO.equals(usermobno)) {
								RecordLog.writeLogFile("updating users details 111");
								//creating record in usermobile changes table
								userMobileChanges=new UserMobileChanges();
								userMobileChanges.setCustomerNo(customerId);
								userMobileChanges.setOldMobile(usermobno);
								userMobileChanges.setNewMobile(CONTACT_NO);
								userMobileChanges.setUpdatedAt(new Timestamp(new Date(System.currentTimeMillis()).getTime()));
								userMobileChanges.setCreatedAt(new Timestamp(new Date(System.currentTimeMillis()).getTime()));

							    userMobileChangesRepo.save(userMobileChanges);
								//update users
								userEnterpris.get(0).setMobile(CONTACT_NO);
							    userRepository.save(userEnterpris.get(0));
								usermobno = CONTACT_NO;
								
								// update application_users
								ApplicationUser appUser=applicationUserRepository.findByCustNoAndPrefNo(custId,prefNo);
								if(appUser!=null) {
								appUser.setMobile(CONTACT_NO);
								applicationUserRepository.save(appUser);
								}
								RecordLog.writeLogFile("updating users details completed");
							}
						}
						
						if (CONTACT_NO.equals(usermobno)) {

							if (mpinhash.equals(tmpinhash)) {

								RecordId_MpinCheck recordId = new RecordId_MpinCheck();

								recordId.setUser(userEnterpris);
								recordId.setAuthtoken(userEnterpris.get(0).getAuthToken());
								recordId.setPrefcorp(pefcrpno);
								recordId.setState(userEnterpris.get(0).getActivationStatus());
								recordId.setFavouriteaccount(userEnterpris.get(0).getFavouriteAccount());
								
								User user=userEnterpris.get(0);
								user.setWrongMpinCount(Integer.toString(0));
								userRepository.save(user);
								
								MpinCheckServiceResp = new HashMap<String, Object>();
								MpinCheckServiceResp.put("status", true);
								MpinCheckServiceResp.put("description", "Success");
								MpinCheckServiceResp.put("message", "Success");
								MpinCheckServiceResp.put("recordId", recordId);
								MpinCheckServiceResp.put("activationtoken",
										landingservice.getactiveRegToken(userEnterpris.get(0)));

							}

							else {
								User user=userEnterpris.get(0);
								Integer count=user.getWrongMpinCount()==null? 1:Integer.parseInt(user.getWrongMpinCount())+1;
								
								
								
								if(count<3) {
								MpinCheckServiceResp = new HashMap<String, Object>();
								MpinCheckServiceResp.put("status", false);
								MpinCheckServiceResp.put("description", messages.getmpincreateinvaliderr);
								MpinCheckServiceResp.put("message", "Invalid MPIN. "+(3-count)+" attempts remaining");
								user.setWrongMpinCount(Integer.toString(count));
								userRepository.save(user);
								}
								else {
									MpinCheckServiceResp = new HashMap<String, Object>();
									MpinCheckServiceResp.put("status", false);
									MpinCheckServiceResp.put("description", messages.getmpincreateinvaliderr);
									MpinCheckServiceResp.put("message", messages.getMpin_blocked());
									user.setMpinCheckStatus("blocked");
									userRepository.save(user);
								}
							}

						} else {
							MpinCheckServiceResp = new HashMap<String, Object>();
							MpinCheckServiceResp.put("status", false);
							MpinCheckServiceResp.put("description", messages.getmobinvalid);
							MpinCheckServiceResp.put("message", "");

						}

					} else if (header.get("DETAILS") instanceof JSONArray) {

						boolean mobstatus = false;

						JSONArray ResponseHeader = header.getJSONArray("DETAILS");

						for (int i = 0; i < ResponseHeader.length(); i++) {

							JSONObject rec = ResponseHeader.getJSONObject(i);

							String CONTACT_NO = rec.get("CONTACT_NO").toString();
							
							
							String customerId=rec.get("CUSTOMER_ID").toString();
							String prefNo = userEnterpris.get(0).getPrefNo();
							String custId = userEnterpris.get(0).getCustNo();
							if (customerId.equals(custId)) {
								RecordLog.writeLogFile("both customer id are equal:"+customerId  +" "+custId);
								RecordLog.writeLogFile("both customer are getcust:"+CONTACT_NO  +" user: "+usermobno);
								if (!CONTACT_NO.equals(usermobno)) {
									RecordLog.writeLogFile("updating users details 210");
									//creating record in usermobile changes
									userMobileChanges=new UserMobileChanges();
									userMobileChanges.setCustomerNo(customerId);
									userMobileChanges.setOldMobile(usermobno);
									userMobileChanges.setNewMobile(CONTACT_NO);
									userMobileChanges.setUpdatedAt(new Timestamp(new Date(System.currentTimeMillis()).getTime()));
									userMobileChanges.setCreatedAt(new Timestamp(new Date(System.currentTimeMillis()).getTime()));

								    userMobileChangesRepo.save(userMobileChanges);
									//update users
									userEnterpris.get(0).setMobile(CONTACT_NO);
									userRepository.save(userEnterpris.get(0));
									usermobno = CONTACT_NO;
									
									// update application_users
									ApplicationUser appUser=applicationUserRepository.findByCustNoAndPrefNo(custId,prefNo);
									if(appUser!=null) {
									appUser.setMobile(CONTACT_NO);
									applicationUserRepository.save(appUser);
									}
								}
							}


							if (CONTACT_NO.equals(usermobno)) {
								mobstatus = true;
							}
						}

						if (mobstatus == true) {

							if (mpinhash.equals(tmpinhash)) {

								RecordId_MpinCheck recordId = new RecordId_MpinCheck();

								recordId.setUser(userEnterpris);
								recordId.setAuthtoken(userEnterpris.get(0).getAuthToken());
								recordId.setPrefcorp(pefcrpno);
								recordId.setState(userEnterpris.get(0).getActivationStatus());
								recordId.setFavouriteaccount(userEnterpris.get(0).getFavouriteAccount());
								User user=userEnterpris.get(0);
								user.setWrongMpinCount(Integer.toString(0));
								userRepository.save(user);
								MpinCheckServiceResp = new HashMap<String, Object>();
								MpinCheckServiceResp.put("status", true);
								MpinCheckServiceResp.put("description", "Success");
								MpinCheckServiceResp.put("message", "Success");
								MpinCheckServiceResp.put("recordId", recordId);

							}

							else {
								User user=userEnterpris.get(0);
								Integer count=user.getWrongMpinCount()==null? 1:Integer.parseInt(user.getWrongMpinCount())+1;
								MpinCheckServiceResp = new HashMap<String, Object>();
								if(count<3) {
								MpinCheckServiceResp.put("status", false);
								MpinCheckServiceResp.put("description", messages.getmpincreateinvaliderr);
								MpinCheckServiceResp.put("message", "Invalid MPIN. "+(3-count)+" attempts remaining");
								user.setWrongMpinCount(Integer.toString(count));
								userRepository.save(user);
								}else {
									MpinCheckServiceResp.put("status", false);
									MpinCheckServiceResp.put("description", messages.getmpincreateinvaliderr);
									MpinCheckServiceResp.put("message", messages.getMpin_blocked());
									user.setMpinCheckStatus("blocked");
									userRepository.save(user);
								}
							}

						} else {
							MpinCheckServiceResp = new HashMap<String, Object>();
							MpinCheckServiceResp.put("status", false);
							MpinCheckServiceResp.put("description", messages.getmobinvalid);
							MpinCheckServiceResp.put("message", "");

						}

					}
				} else {
					JSONArray headerarr = transactionResult.getJSONArray("AccountDetails");
					JSONObject headerobj = null;
					for (int i = 0; i < headerarr.length(); i++) {
						JSONObject objects = headerarr.getJSONObject(i);
						Object foracid = objects.get("FORACID");
						String acctNumber = foracid.toString();
						// if (objects.getString("FORACID").equals(getacctno)) {
						if (acctNumber.equals(getacctno)) {
							headerobj = objects;
						}
					}

					JSONObject header = headerobj.getJSONObject("RELATEDPARTY");

					if (header.get("DETAILS") instanceof JSONObject) {

						JSONObject ResponseHeader = header.getJSONObject("DETAILS");

						String CONTACT_NO = ResponseHeader.get("CONTACT_NO").toString();


						String customerId=ResponseHeader.get("CUSTOMER_ID").toString();	
						String custId = userEnterpris.get(0).getCustNo();
						String prefNo = userEnterpris.get(0).getPrefNo();

						if (customerId.equals(custId)) {
							RecordLog.writeLogFile("both customer id are equal:"+customerId  +" "+custId);
							RecordLog.writeLogFile("both customer are getcust:"+CONTACT_NO  +" user: "+usermobno);
							if (!CONTACT_NO.equals(usermobno)) {
								RecordLog.writeLogFile("updating users details 320");
								//creating record in usermobile changes
								userMobileChanges=new UserMobileChanges();
								userMobileChanges.setCustomerNo(customerId);
								userMobileChanges.setOldMobile(usermobno);
								userMobileChanges.setNewMobile(CONTACT_NO);
								userMobileChanges.setCreatedAt(new Timestamp(new Date(System.currentTimeMillis()).getTime()));
								userMobileChanges.setUpdatedAt(new Timestamp(new Date(System.currentTimeMillis()).getTime()));

							    userMobileChangesRepo.save(userMobileChanges);							
								//update users
								userEnterpris.get(0).setMobile(CONTACT_NO);
								userRepository.save(userEnterpris.get(0));
								usermobno = CONTACT_NO;
								
								// update application_users
								ApplicationUser appUser=applicationUserRepository.findByCustNoAndPrefNo(custId,prefNo);
								if(appUser!=null) {
								appUser.setMobile(CONTACT_NO);
								applicationUserRepository.save(appUser);
								}
							}
						}

						
						
						if (CONTACT_NO.equals(usermobno)) {

							if (mpinhash.equals(tmpinhash)) {

								RecordId_MpinCheck recordId = new RecordId_MpinCheck();

								recordId.setUser(userEnterpris);
								recordId.setAuthtoken(userEnterpris.get(0).getAuthToken());
								recordId.setPrefcorp(pefcrpno);
								recordId.setState(userEnterpris.get(0).getActivationStatus());
								recordId.setFavouriteaccount(userEnterpris.get(0).getFavouriteAccount());

								MpinCheckServiceResp = new HashMap<String, Object>();
								MpinCheckServiceResp.put("status", true);
								MpinCheckServiceResp.put("description", "Success");
								MpinCheckServiceResp.put("message", "Success");
								MpinCheckServiceResp.put("recordId", recordId);
								MpinCheckServiceResp.put("activationtoken",
										landingservice.getactiveRegToken(userEnterpris.get(0)));

							}

							else {
								MpinCheckServiceResp = new HashMap<String, Object>();
								MpinCheckServiceResp.put("status", false);
								MpinCheckServiceResp.put("description", messages.getmpincreateinvaliderr);
								MpinCheckServiceResp.put("message", messages.getmpincreateinvaliderr);
							}

						} else {
							MpinCheckServiceResp = new HashMap<String, Object>();
							MpinCheckServiceResp.put("status", false);
							MpinCheckServiceResp.put("description", messages.getmobinvalid);
							MpinCheckServiceResp.put("message", "");

						}

					} else if (header.get("DETAILS") instanceof JSONArray) {

						boolean mobstatus = false;

						JSONArray ResponseHeader = header.getJSONArray("DETAILS");

						for (int i = 0; i < ResponseHeader.length(); i++) {

							JSONObject rec = ResponseHeader.getJSONObject(i);

							String CONTACT_NO = rec.get("CONTACT_NO").toString();

					
							String customerId=rec.get("CUSTOMER_ID").toString();	
							String custId = userEnterpris.get(0).getCustNo();
							String prefNo = userEnterpris.get(0).getPrefNo();

							if (customerId.equals(custId)) {
								RecordLog.writeLogFile("both customer id are equal:"+customerId  +" "+custId);
								RecordLog.writeLogFile("both customer are getcust:"+CONTACT_NO  +" user: "+usermobno);
								if (!CONTACT_NO.equals(usermobno)) {
									RecordLog.writeLogFile("updating users details 404");
								//creating record in usermobile changes
									userMobileChanges=new UserMobileChanges();
									userMobileChanges.setCustomerNo(customerId);
									userMobileChanges.setOldMobile(usermobno);
									userMobileChanges.setNewMobile(CONTACT_NO);
									userMobileChanges.setUpdatedAt(new Timestamp(new Date(System.currentTimeMillis()).getTime()));
									userMobileChanges.setCreatedAt(new Timestamp(new Date(System.currentTimeMillis()).getTime()));

								    userMobileChangesRepo.save(userMobileChanges);
									//update users
									userEnterpris.get(0).setMobile(CONTACT_NO);
									userRepository.save(userEnterpris.get(0));
									usermobno = CONTACT_NO;
									
									// update application_users
									ApplicationUser appUser=applicationUserRepository.findByCustNoAndPrefNo(custId,prefNo);
									if(appUser!=null) {
									appUser.setMobile(CONTACT_NO);
									applicationUserRepository.save(appUser);
									}
								}
							}

							
							
							if (CONTACT_NO.equals(usermobno)) {
								mobstatus = true;
							}
						}

						if (mobstatus == true) {

							if (mpinhash.equals(tmpinhash)) {

								RecordId_MpinCheck recordId = new RecordId_MpinCheck();

								recordId.setUser(userEnterpris);
								recordId.setAuthtoken(userEnterpris.get(0).getAuthToken());
								recordId.setPrefcorp(pefcrpno);
								recordId.setState(userEnterpris.get(0).getActivationStatus());
								recordId.setFavouriteaccount(userEnterpris.get(0).getFavouriteAccount());

								MpinCheckServiceResp = new HashMap<String, Object>();
								MpinCheckServiceResp.put("status", true);
								MpinCheckServiceResp.put("description", "Success");
								MpinCheckServiceResp.put("message", "Success");
								MpinCheckServiceResp.put("recordId", recordId);

							}

							else {
								MpinCheckServiceResp = new HashMap<String, Object>();
								MpinCheckServiceResp.put("status", false);
								MpinCheckServiceResp.put("description", messages.getmpincreateinvaliderr);
								MpinCheckServiceResp.put("message", messages.getmpincreateinvaliderr);
							}

						} else {
							MpinCheckServiceResp = new HashMap<String, Object>();
							MpinCheckServiceResp.put("status", false);
							MpinCheckServiceResp.put("description", messages.getmobinvalid);
							MpinCheckServiceResp.put("message", "");

						}

					}

				}

			}
		}
			else {
				MpinCheckServiceResp = new HashMap<String, Object>();
				MpinCheckServiceResp.put("status", false);
				MpinCheckServiceResp.put("description", messages.getmpincreateerr);
				MpinCheckServiceResp.put("message", messages.getMpin_blocked());
			}

		}

		else {
			MpinCheckServiceResp = new HashMap<String, Object>();
			MpinCheckServiceResp.put("status", false);
			MpinCheckServiceResp.put("description", messages.getmpincreateerr);
			MpinCheckServiceResp.put("message", messages.getmpincreateerr);
		}

		return MpinCheckServiceResp;
		
	}catch(Exception e) {
		RecordLog.writeLogFile("Test11: "+Arrays.toString(e.getStackTrace())+" Test1100: "+e.getMessage());
		e.printStackTrace();
		

		return MpinCheckServiceResp;
	}
		
	}
		//new version v2.4  started getUserDetailsV24
		
		public Map<String, Object> getUserDetailsV24(String app_token, String pefcrpno, String mpinhash,String osVersion,String appVersion,String osType) {

			Map<String, Object> MpinCheckServiceResp = null;
			UserMobileChanges userMobileChanges=null;
			boolean isApproved = false;
			try {
			// ArrayList<String> applicationpasswd = new ArrayList<>();
			List<User> userEnterpris = userRepository.findByAppTokenAndMarkAsEnabled(app_token, true);
			
           if(userEnterpris.get(0).getAppVersion() != null && Float.valueOf(appVersion)>Float.valueOf(userEnterpris.get(0).getAppVersion())) {
				
				isApproved = true;
			}
			
			if (userEnterpris.size() > 0) {
				
				userEnterpris.get(0).setOsType(osType);
				userEnterpris.get(0).setAppVersion(appVersion);
				userEnterpris.get(0).setOsVersion(osVersion);
				
			String userOSType=userEnterpris.get(0).getOsType();
			
			RecordLog.writeLogFile("env app version for mobile "+userEnterpris.get(0).getMobile() 
					+" android "+this.appVersionAndroid+" ios "+this.appVersionIos);
			RecordLog.writeLogFile("env os version "+userEnterpris.get(0).getMobile() +"  "+this.osVersion);
			
			
			List<String> appvrsnAndroid=Arrays.asList(this.appVersionAndroid.split(","));
			List<String> appvrsnIos=Arrays.asList(this.appVersionIos.split(","));

			
//			String[] androidApp=appversion[0].split("_");
//			String[] iosApp=appversion[1].split("_");
			
			
			String[] osversion=this.osVersion.split(",");
			String[] androidOsVersion=osversion[0].split("_");
			String[] iosOsVersion=osversion[1].split("_");

			
			List<String> envAppVersion;
			String userAppVersion="";
			
			String envOSVersion="";
			String userOSVersion="";
				if ( userOSType.equalsIgnoreCase("Android")) {
					envAppVersion = appvrsnAndroid;
					userAppVersion = userEnterpris.get(0).getAppVersion();
				
					envOSVersion=androidOsVersion[1];
					userOSVersion=userEnterpris.get(0).getOsVersion() == null ? envOSVersion
							: userEnterpris.get(0).getOsVersion();
					RecordLog.writeLogFile("user app version "+userAppVersion +" user os version "+userOSVersion +" users os type "+userOSType);
					RecordLog.writeLogFile("env app version "+envAppVersion +" env os version "+envOSVersion +" users os type "+userOSType);

				} else {
					envAppVersion=appvrsnIos;
					userAppVersion = userEnterpris.get(0).getAppVersion();
					envOSVersion=iosOsVersion[1];
					userOSVersion=userEnterpris.get(0).getOsVersion() == null ? envOSVersion
							: userEnterpris.get(0).getOsVersion();
					RecordLog.writeLogFile("user app version "+userAppVersion +" user os version "+userOSVersion +" users os type "+userOSType);
					RecordLog.writeLogFile("env app version "+envAppVersion +" env os version "+envOSVersion +" users os type "+userOSType);

				}
			String[] userOsSplit=userOSVersion.split("[.]",0);
			
			String userOs="";
			if(userOsSplit.length>1) {
			 userOs=userOs.concat( userOsSplit[0])+userOs.concat(".")+userOs.concat(userOsSplit[1]);
			}else {
				userOs=userOsSplit[0];
			}
	        if(!envAppVersion.contains(userAppVersion)) {
	        	
	     			MpinCheckServiceResp = new HashMap<String, Object>();
	     			MpinCheckServiceResp.put("status", false);
	     			MpinCheckServiceResp.put("appUpdate", true);
	     			MpinCheckServiceResp.put("description", messages.getAppversion_message());
	     			MpinCheckServiceResp.put("message", messages.getAppversion_message());

	        }else if(Float.valueOf(envOSVersion)>Float.valueOf(userOs)) {
	        	
	     			MpinCheckServiceResp = new HashMap<String, Object>();
	     			MpinCheckServiceResp.put("status", false);
	     			MpinCheckServiceResp.put("description", messages.getOs_version_message());
	     			MpinCheckServiceResp.put("message", messages.getOs_version_message());
	        }
	        
	        
	        
	        
	        else {

			
				long days=0l;
				 long yeardiff=0l;
				if(userEnterpris.get(0).getLastActivityAt()!=null) {
				Date now=new Date(System.currentTimeMillis());
				Date old=Date.from(userEnterpris.get(0).getLastActivityAt().toInstant());
				long timediff=now.getTime()-old.getTime();
				days=(timediff/(1000*60*60*24))%365;
				 yeardiff=(timediff/(1000l*60*60*24*365));
				}
				
				RecordLog.writeLogFile(" days difference mobile "+userEnterpris.get(0).getMobile()+"  "+days);
				if(days<=180l && yeardiff<=0l) {
				
				if(userEnterpris.get(0).getMpinCheckStatus()==null || !(userEnterpris.get(0).getMpinCheckStatus().equals("blocked"))){

				String enterprs_id = userEnterpris.get(0).getEnterpriseId();

				String usermobno = userEnterpris.get(0).getMobile();

				String tmpinhash = userEnterpris.get(0).getMpin();

				long long_enterprs_id = Long.parseLong(enterprs_id);

				Enterprises Enterpris = Enterprises.findByIdAndPrefCorp(long_enterprs_id, pefcrpno);

				if (Enterpris == null) {
					MpinCheckServiceResp = new HashMap<String, Object>();
					MpinCheckServiceResp.put("status", false);
					MpinCheckServiceResp.put("description", messages.getmpincreateerr);
					MpinCheckServiceResp.put("message", messages.getmpincreateerr);
				} else {
					String getacctno = Enterpris.getAccNo();
					String custDetailResponse = getcustdetls.getData(getacctno);
//					RecordLog.writeLogFile("Fedcorp:" + custDetailResponse);
					JSONObject json = XML.toJSONObject(custDetailResponse);
					RecordLog.writeLogFile("Fedcorp customer detail:" + json);
					String requestData = json.toString(4);
//					RecordLog.writeLogFile(requestData);
					JSONObject jsonrequestData = new JSONObject(requestData);
					JSONObject transactionResult = jsonrequestData.getJSONObject("GetCustomerDetailsResp");
					if (transactionResult.get("AccountDetails") instanceof JSONObject) {
						JSONObject header = transactionResult.getJSONObject("AccountDetails").getJSONObject("RELATEDPARTY");

						if (header.get("DETAILS") instanceof JSONObject) {

							JSONObject ResponseHeader = header.getJSONObject("DETAILS");

							String CONTACT_NO = ResponseHeader.get("CONTACT_NO").toString();

							String customerId=ResponseHeader.get("CUSTOMER_ID").toString();	
							String custId = userEnterpris.get(0).getCustNo();
							String prefNo = userEnterpris.get(0).getPrefNo();
							
							if (customerId.equals(custId)) {
								RecordLog.writeLogFile("both customer id are equal:"+customerId  +" "+custId);
								RecordLog.writeLogFile("both customer are getcust:"+CONTACT_NO  +" user: "+usermobno);
								if (!CONTACT_NO.equals(usermobno)) {
									RecordLog.writeLogFile("updating users details 111");
									//creating record in usermobile changes table
									userMobileChanges=new UserMobileChanges();
									userMobileChanges.setCustomerNo(customerId);
									userMobileChanges.setOldMobile(usermobno);
									userMobileChanges.setNewMobile(CONTACT_NO);
									userMobileChanges.setUpdatedAt(new Timestamp(new Date(System.currentTimeMillis()).getTime()));
									userMobileChanges.setCreatedAt(new Timestamp(new Date(System.currentTimeMillis()).getTime()));

								    userMobileChangesRepo.save(userMobileChanges);
									//update users
									userEnterpris.get(0).setMobile(CONTACT_NO);					
								    userRepository.save(userEnterpris.get(0));
									usermobno = CONTACT_NO;
									
									// update application_users
									ApplicationUser appUser=applicationUserRepository.findByCustNoAndPrefNo(custId,prefNo);
									if(appUser!=null) {
									appUser.setMobile(CONTACT_NO);
									applicationUserRepository.save(appUser);
									}
									RecordLog.writeLogFile("updating users details completed");
								}
							}
							
							if (CONTACT_NO.equals(usermobno)) {

								if (mpinhash.equals(tmpinhash)) {

									RecordId_MpinCheck recordId = new RecordId_MpinCheck();

									recordId.setUser(userEnterpris);
									recordId.setAuthtoken(userEnterpris.get(0).getAuthToken());
									recordId.setPrefcorp(pefcrpno);
									recordId.setState(userEnterpris.get(0).getActivationStatus());
									recordId.setFavouriteaccount(userEnterpris.get(0).getFavouriteAccount());
									
									User user=userEnterpris.get(0);
									user.setWrongMpinCount(Integer.toString(0));
									if(isApproved) {
										user.setIsAccepted("N");
									}
									userRepository.save(user);
									
									MpinCheckServiceResp = new HashMap<String, Object>();
									MpinCheckServiceResp.put("status", true);
									MpinCheckServiceResp.put("description", "Success");
									MpinCheckServiceResp.put("message", "Success");
									MpinCheckServiceResp.put("recordId", recordId);
									MpinCheckServiceResp.put("isAccepted", user.getIsAccepted());
									MpinCheckServiceResp.put("activationtoken",
											landingservice.getactiveRegToken(userEnterpris.get(0)));

								}

								else {
									User user=userEnterpris.get(0);
									Integer count=user.getWrongMpinCount()==null? 1:Integer.parseInt(user.getWrongMpinCount())+1;
									if(count == 1) {
										MpinCheckServiceResp = new HashMap<String, Object>();
										MpinCheckServiceResp.put("status", false);
										MpinCheckServiceResp.put("description", messages.getmpincreateinvaliderr);
										MpinCheckServiceResp.put("message", "Invalid MPIN. "+(3-count)+" attempts remaining");
										user.setWrongMpinCount(Integer.toString(count));
										userRepository.save(user);
										SMEMessage smsapptoken = simBindingService.sendSMS(messages.getMpin_blocked_message_for_single_wrong_attempts(),userEnterpris.get(0).getMobile());
										RecordLog.writeLogFile("Every Single Wrong Mpin Attempt for mobile: "+userEnterpris.get(0).getMobile()+" Response is : "+smsapptoken.isStatus() +":"+smsapptoken.getMessage());
										}
																		
										else if(count == 2) {
										MpinCheckServiceResp = new HashMap<String, Object>();
										MpinCheckServiceResp.put("status", false);
										MpinCheckServiceResp.put("description", messages.getmpincreateinvaliderr);
										MpinCheckServiceResp.put("message", "Invalid MPIN. "+(3-count)+" attempts remaining");
										user.setWrongMpinCount(Integer.toString(count));
										userRepository.save(user);
										SMEMessage smsapptoken = simBindingService.sendSMS(messages.getMpin_blocked_message_for_second_wrong_attempts(),userEnterpris.get(0).getMobile());
										RecordLog.writeLogFile("Every Single Wrong Mpin Attempt for mobile: "+userEnterpris.get(0).getMobile()+" Response is : "+smsapptoken.isStatus() +":"+smsapptoken.getMessage());
										}else {
										MpinCheckServiceResp = new HashMap<String, Object>();
										MpinCheckServiceResp.put("status", false);
										MpinCheckServiceResp.put("description", messages.getmpincreateinvaliderr);
										MpinCheckServiceResp.put("message", messages.getMpin_blocked());
										user.setMpinCheckStatus("blocked");
										userRepository.save(user);
										
										
										
										simBindingService.sendSMS(messages.getMpin_blocked_message(),userEnterpris.get(0).getMobile());
										
									}
								}

							} else {
								MpinCheckServiceResp = new HashMap<String, Object>();
								MpinCheckServiceResp.put("status", false);
								MpinCheckServiceResp.put("description", messages.getmobinvalid);
								MpinCheckServiceResp.put("message", messages.getmobinvalid);

							}

						} else if (header.get("DETAILS") instanceof JSONArray) {

							boolean mobstatus = false;

							JSONArray ResponseHeader = header.getJSONArray("DETAILS");

							for (int i = 0; i < ResponseHeader.length(); i++) {

								JSONObject rec = ResponseHeader.getJSONObject(i);

								String CONTACT_NO = rec.get("CONTACT_NO").toString();
								
								
								String customerId=rec.get("CUSTOMER_ID").toString();
								String prefNo = userEnterpris.get(0).getPrefNo();
								String custId = userEnterpris.get(0).getCustNo();
								if (customerId.equals(custId)) {
									RecordLog.writeLogFile("both customer id are equal:"+customerId  +" "+custId);
									RecordLog.writeLogFile("both customer are getcust:"+CONTACT_NO  +" user: "+usermobno);
									if (!CONTACT_NO.equals(usermobno)) {
										RecordLog.writeLogFile("updating users details 210");
										//creating record in usermobile changes
										userMobileChanges=new UserMobileChanges();
										userMobileChanges.setCustomerNo(customerId);
										userMobileChanges.setOldMobile(usermobno);
										userMobileChanges.setNewMobile(CONTACT_NO);
										userMobileChanges.setUpdatedAt(new Timestamp(new Date(System.currentTimeMillis()).getTime()));
										userMobileChanges.setCreatedAt(new Timestamp(new Date(System.currentTimeMillis()).getTime()));

									    userMobileChangesRepo.save(userMobileChanges);
										//update users
										userEnterpris.get(0).setMobile(CONTACT_NO);
										userRepository.save(userEnterpris.get(0));
										usermobno = CONTACT_NO;
										
										// update application_users
										ApplicationUser appUser=applicationUserRepository.findByCustNoAndPrefNo(custId,prefNo);
										if(appUser!=null) {
										appUser.setMobile(CONTACT_NO);
										applicationUserRepository.save(appUser);
										}
									}
								}


								if (CONTACT_NO.equals(usermobno)) {
									mobstatus = true;
								}
							}

							if (mobstatus == true) {

								if (mpinhash.equals(tmpinhash)) {

									RecordId_MpinCheck recordId = new RecordId_MpinCheck();

									recordId.setUser(userEnterpris);
									recordId.setAuthtoken(userEnterpris.get(0).getAuthToken());
									recordId.setPrefcorp(pefcrpno);
									recordId.setState(userEnterpris.get(0).getActivationStatus());
									recordId.setFavouriteaccount(userEnterpris.get(0).getFavouriteAccount());
									User user=userEnterpris.get(0);
									user.setWrongMpinCount(Integer.toString(0));
									if(isApproved) {
										user.setIsAccepted("N");
									}
									userRepository.save(user);
									MpinCheckServiceResp = new HashMap<String, Object>();
									MpinCheckServiceResp.put("status", true);
									MpinCheckServiceResp.put("isAccepted", user.getIsAccepted());
									MpinCheckServiceResp.put("description", "Success");
									MpinCheckServiceResp.put("message", "Success");
									MpinCheckServiceResp.put("recordId", recordId);

								}

								else {
									User user=userEnterpris.get(0);
									Integer count=user.getWrongMpinCount()==null? 1:Integer.parseInt(user.getWrongMpinCount())+1;
									
									if(count == 1) {
										MpinCheckServiceResp = new HashMap<String, Object>();
										MpinCheckServiceResp.put("status", false);
										MpinCheckServiceResp.put("description", messages.getmpincreateinvaliderr);
										MpinCheckServiceResp.put("message", "Invalid MPIN. "+(3-count)+" attempts remaining");
										user.setWrongMpinCount(Integer.toString(count));
										userRepository.save(user);
										SMEMessage smsapptoken = simBindingService.sendSMS(messages.getMpin_blocked_message_for_single_wrong_attempts(),userEnterpris.get(0).getMobile());
										RecordLog.writeLogFile("Every Single Wrong Mpin Attempt for mobile: "+userEnterpris.get(0).getMobile()+" Response is : "+smsapptoken.isStatus() +":"+smsapptoken.getMessage());
									}
									else if(count == 2) {
										MpinCheckServiceResp = new HashMap<String, Object>();
										MpinCheckServiceResp.put("status", false);
										MpinCheckServiceResp.put("description", messages.getmpincreateinvaliderr);
										MpinCheckServiceResp.put("message", "Invalid MPIN. "+(3-count)+" attempts remaining");
										user.setWrongMpinCount(Integer.toString(count));
										userRepository.save(user);
										SMEMessage smsapptoken = simBindingService.sendSMS(messages.getMpin_blocked_message_for_second_wrong_attempts(),userEnterpris.get(0).getMobile());
										RecordLog.writeLogFile("Every Single Wrong Mpin Attempt for mobile: "+userEnterpris.get(0).getMobile()+" Response is : "+smsapptoken.isStatus() +":"+smsapptoken.getMessage());
										}
//									if(count<3) {
//									MpinCheckServiceResp.put("status", false);
//									MpinCheckServiceResp.put("description", messages.getmpincreateinvaliderr);
//									MpinCheckServiceResp.put("message", "Invalid MPIN. "+(3-count)+" attempts remaining");
//									user.setWrongMpinCount(Integer.toString(count));
//									userRepository.save(user);
//									}
									else {
										MpinCheckServiceResp = new HashMap<String, Object>();
										MpinCheckServiceResp.put("status", false);
										MpinCheckServiceResp.put("description", messages.getmpincreateinvaliderr);
										MpinCheckServiceResp.put("message", messages.getMpin_blocked());
										user.setMpinCheckStatus("blocked");
										userRepository.save(user);
										simBindingService.sendSMS(messages.getMpin_blocked_message(),userEnterpris.get(0).getMobile());

									}
								}

							} else {
								MpinCheckServiceResp = new HashMap<String, Object>();
								MpinCheckServiceResp.put("status", false);
								MpinCheckServiceResp.put("description", messages.getmobinvalid);
								MpinCheckServiceResp.put("message", "");

							}

						}
					} else {
						JSONArray headerarr = transactionResult.getJSONArray("AccountDetails");
						JSONObject headerobj = null;
						for (int i = 0; i < headerarr.length(); i++) {
							JSONObject objects = headerarr.getJSONObject(i);
							Object foracid = objects.get("FORACID");
							String acctNumber = foracid.toString();
							// if (objects.getString("FORACID").equals(getacctno)) {
							if (acctNumber.equals(getacctno)) {
								headerobj = objects;
							}
						}

						JSONObject header = headerobj.getJSONObject("RELATEDPARTY");

						if (header.get("DETAILS") instanceof JSONObject) {

							JSONObject ResponseHeader = header.getJSONObject("DETAILS");

							String CONTACT_NO = ResponseHeader.get("CONTACT_NO").toString();


							String customerId=ResponseHeader.get("CUSTOMER_ID").toString();	
							String custId = userEnterpris.get(0).getCustNo();
							String prefNo = userEnterpris.get(0).getPrefNo();

							if (customerId.equals(custId)) {
								RecordLog.writeLogFile("both customer id are equal:"+customerId  +" "+custId);
								RecordLog.writeLogFile("both customer are getcust:"+CONTACT_NO  +" user: "+usermobno);
								if (!CONTACT_NO.equals(usermobno)) {
									RecordLog.writeLogFile("updating users details 320");
									//creating record in usermobile changes
									userMobileChanges=new UserMobileChanges();
									userMobileChanges.setCustomerNo(customerId);
									userMobileChanges.setOldMobile(usermobno);
									userMobileChanges.setNewMobile(CONTACT_NO);
									userMobileChanges.setCreatedAt(new Timestamp(new Date(System.currentTimeMillis()).getTime()));
									userMobileChanges.setUpdatedAt(new Timestamp(new Date(System.currentTimeMillis()).getTime()));

								    userMobileChangesRepo.save(userMobileChanges);							
									//update users
									userEnterpris.get(0).setMobile(CONTACT_NO);
									userRepository.save(userEnterpris.get(0));
									usermobno = CONTACT_NO;
									
									// update application_users
									ApplicationUser appUser=applicationUserRepository.findByCustNoAndPrefNo(custId,prefNo);
									if(appUser!=null) {
									appUser.setMobile(CONTACT_NO);
									applicationUserRepository.save(appUser);
									}
								}
							}

							
							
							if (CONTACT_NO.equals(usermobno)) {

								if (mpinhash.equals(tmpinhash)) {

									RecordId_MpinCheck recordId = new RecordId_MpinCheck();

									recordId.setUser(userEnterpris);
									recordId.setAuthtoken(userEnterpris.get(0).getAuthToken());
									recordId.setPrefcorp(pefcrpno);
									recordId.setState(userEnterpris.get(0).getActivationStatus());
									recordId.setFavouriteaccount(userEnterpris.get(0).getFavouriteAccount());
									User user=userEnterpris.get(0);
									user.setWrongMpinCount(Integer.toString(0));
									if(isApproved) {
										user.setIsAccepted("N");
									}
									userRepository.save(user);
									MpinCheckServiceResp = new HashMap<String, Object>();
									MpinCheckServiceResp.put("status", true);
									MpinCheckServiceResp.put("description", "Success");
									MpinCheckServiceResp.put("isAccepted", user.getIsAccepted());

									MpinCheckServiceResp.put("message", "Success");
									MpinCheckServiceResp.put("recordId", recordId);
									MpinCheckServiceResp.put("activationtoken",
											landingservice.getactiveRegToken(userEnterpris.get(0)));

								}

								else {

									
									
									User user=userEnterpris.get(0);
									Integer count=user.getWrongMpinCount()==null? 1:Integer.parseInt(user.getWrongMpinCount())+1;
									
									
									if(count == 1) {
										MpinCheckServiceResp = new HashMap<String, Object>();
										MpinCheckServiceResp.put("status", false);
										MpinCheckServiceResp.put("description", messages.getmpincreateinvaliderr);
										MpinCheckServiceResp.put("message", "Invalid MPIN. "+(3-count)+" attempts remaining");
										user.setWrongMpinCount(Integer.toString(count));
										userRepository.save(user);
										SMEMessage smsapptoken = simBindingService.sendSMS(messages.getMpin_blocked_message_for_single_wrong_attempts(),userEnterpris.get(0).getMobile());
										RecordLog.writeLogFile("Every Single Wrong Mpin Attempt for mobile: "+userEnterpris.get(0).getMobile()+" Response is : "+smsapptoken.isStatus() +":"+smsapptoken.getMessage());
									}
									else if(count == 2) {
										MpinCheckServiceResp = new HashMap<String, Object>();
										MpinCheckServiceResp.put("status", false);
										MpinCheckServiceResp.put("description", messages.getmpincreateinvaliderr);
										MpinCheckServiceResp.put("message", "Invalid MPIN. "+(3-count)+" attempts remaining");
										user.setWrongMpinCount(Integer.toString(count));
										userRepository.save(user);
										SMEMessage smsapptoken = simBindingService.sendSMS(messages.getMpin_blocked_message_for_second_wrong_attempts(),userEnterpris.get(0).getMobile());
										RecordLog.writeLogFile("Every Single Wrong Mpin Attempt for mobile: "+userEnterpris.get(0).getMobile()+" Response is : "+smsapptoken.isStatus() +":"+smsapptoken.getMessage());
										}
									
//									if(count<3) {
//									MpinCheckServiceResp = new HashMap<String, Object>();
//									MpinCheckServiceResp.put("status", false);
//									MpinCheckServiceResp.put("description", messages.getmpincreateinvaliderr);
//									MpinCheckServiceResp.put("message", "Invalid MPIN. "+(3-count)+" attempts remaining");
//									user.setWrongMpinCount(Integer.toString(count));
//									userRepository.save(user);
//									}
									else {
										MpinCheckServiceResp = new HashMap<String, Object>();
										MpinCheckServiceResp.put("status", false);
										MpinCheckServiceResp.put("description", messages.getmpincreateinvaliderr);
										MpinCheckServiceResp.put("message", messages.getMpin_blocked());
										user.setMpinCheckStatus("blocked");
										userRepository.save(user);
										
										
										
										simBindingService.sendSMS(messages.getMpin_blocked_message(),userEnterpris.get(0).getMobile());
										
									}
									
									
									
									
								}

							} else {
								MpinCheckServiceResp = new HashMap<String, Object>();
								MpinCheckServiceResp.put("status", false);
								MpinCheckServiceResp.put("description", messages.getmobinvalid);
								MpinCheckServiceResp.put("message", "");

							}

						} else if (header.get("DETAILS") instanceof JSONArray) {

							boolean mobstatus = false;

							JSONArray ResponseHeader = header.getJSONArray("DETAILS");

							for (int i = 0; i < ResponseHeader.length(); i++) {

								JSONObject rec = ResponseHeader.getJSONObject(i);

								String CONTACT_NO = rec.get("CONTACT_NO").toString();

						
								String customerId=rec.get("CUSTOMER_ID").toString();	
								String custId = userEnterpris.get(0).getCustNo();
								String prefNo = userEnterpris.get(0).getPrefNo();

								if (customerId.equals(custId)) {
									RecordLog.writeLogFile("both customer id are equal:"+customerId  +" "+custId);
									RecordLog.writeLogFile("both customer are getcust:"+CONTACT_NO  +" user: "+usermobno);
									if (!CONTACT_NO.equals(usermobno)) {
										RecordLog.writeLogFile("updating users details 404");
									//creating record in usermobile changes
										userMobileChanges=new UserMobileChanges();
										userMobileChanges.setCustomerNo(customerId);
										userMobileChanges.setOldMobile(usermobno);
										userMobileChanges.setNewMobile(CONTACT_NO);
										userMobileChanges.setUpdatedAt(new Timestamp(new Date(System.currentTimeMillis()).getTime()));
										userMobileChanges.setCreatedAt(new Timestamp(new Date(System.currentTimeMillis()).getTime()));

									    userMobileChangesRepo.save(userMobileChanges);
										//update users
										userEnterpris.get(0).setMobile(CONTACT_NO);
										userRepository.save(userEnterpris.get(0));
										usermobno = CONTACT_NO;
										
										// update application_users
										ApplicationUser appUser=applicationUserRepository.findByCustNoAndPrefNo(custId,prefNo);
										if(appUser!=null) {
										appUser.setMobile(CONTACT_NO);
										applicationUserRepository.save(appUser);
										}
									}
								}

								
								
								if (CONTACT_NO.equals(usermobno)) {
									mobstatus = true;
								}
							}

							if (mobstatus == true) {

								if (mpinhash.equals(tmpinhash)) {

									RecordId_MpinCheck recordId = new RecordId_MpinCheck();

									recordId.setUser(userEnterpris);
									recordId.setAuthtoken(userEnterpris.get(0).getAuthToken());
									recordId.setPrefcorp(pefcrpno);
									recordId.setState(userEnterpris.get(0).getActivationStatus());
									recordId.setFavouriteaccount(userEnterpris.get(0).getFavouriteAccount());
									User user=userEnterpris.get(0);
									user.setWrongMpinCount(Integer.toString(0));
									if(isApproved) {
										user.setIsAccepted("N");
									}
									userRepository.save(user);
									MpinCheckServiceResp = new HashMap<String, Object>();
									MpinCheckServiceResp.put("status", true);
									MpinCheckServiceResp.put("description", "Success");
									MpinCheckServiceResp.put("isAccepted", user.getIsAccepted());
									MpinCheckServiceResp.put("message", "Success");
									MpinCheckServiceResp.put("recordId", recordId);

								}

								else {
									User user=userEnterpris.get(0);
									Integer count=user.getWrongMpinCount()==null? 1:Integer.parseInt(user.getWrongMpinCount())+1;
									
									if(count == 1) {
										MpinCheckServiceResp = new HashMap<String, Object>();
										MpinCheckServiceResp.put("status", false);
										MpinCheckServiceResp.put("description", messages.getmpincreateinvaliderr);
										MpinCheckServiceResp.put("message", "Invalid MPIN. "+(3-count)+" attempts remaining");
										user.setWrongMpinCount(Integer.toString(count));
										userRepository.save(user);
										SMEMessage smsapptoken = simBindingService.sendSMS(messages.getMpin_blocked_message_for_single_wrong_attempts(),userEnterpris.get(0).getMobile());
										RecordLog.writeLogFile("Every Single Wrong Mpin Attempt for mobile: "+userEnterpris.get(0).getMobile()+" Response is : "+smsapptoken.isStatus() +":"+smsapptoken.getMessage());
									}
									else if(count == 2) {
										MpinCheckServiceResp = new HashMap<String, Object>();
										MpinCheckServiceResp.put("status", false);
										MpinCheckServiceResp.put("description", messages.getmpincreateinvaliderr);
										MpinCheckServiceResp.put("message", "Invalid MPIN. "+(3-count)+" attempts remaining");
										user.setWrongMpinCount(Integer.toString(count));
										userRepository.save(user);
										SMEMessage smsapptoken = simBindingService.sendSMS(messages.getMpin_blocked_message_for_second_wrong_attempts(),userEnterpris.get(0).getMobile());
										RecordLog.writeLogFile("Every Single Wrong Mpin Attempt for mobile: "+userEnterpris.get(0).getMobile()+" Response is : "+smsapptoken.isStatus() +":"+smsapptoken.getMessage());
										}
									
//									if(count<3) {
//									MpinCheckServiceResp = new HashMap<String, Object>();
//									MpinCheckServiceResp.put("status", false);
//									MpinCheckServiceResp.put("description", messages.getmpincreateinvaliderr);
//									MpinCheckServiceResp.put("message", "Invalid MPIN. "+(3-count)+" attempts remaining");
//									user.setWrongMpinCount(Integer.toString(count));
//									userRepository.save(user);
//									}
									else {
										MpinCheckServiceResp = new HashMap<String, Object>();
										MpinCheckServiceResp.put("status", false);
										MpinCheckServiceResp.put("description", messages.getmpincreateinvaliderr);
										MpinCheckServiceResp.put("message", messages.getMpin_blocked());
										user.setMpinCheckStatus("blocked");
										userRepository.save(user);
										
										
										
										simBindingService.sendSMS(messages.getMpin_blocked_message(),userEnterpris.get(0).getMobile());
										
									}
								}

							} else {
								MpinCheckServiceResp = new HashMap<String, Object>();
								MpinCheckServiceResp.put("status", false);
								MpinCheckServiceResp.put("description", messages.getmobinvalid);
								MpinCheckServiceResp.put("message", "");

							}

						}

					}

				}
			}
				
				else {
					MpinCheckServiceResp = new HashMap<String, Object>();
					MpinCheckServiceResp.put("status", false);
					MpinCheckServiceResp.put("description", messages.getmpincreateerr);
					MpinCheckServiceResp.put("message", messages.getMpin_blocked());
				}
				


			} else {
					MpinCheckServiceResp = new HashMap<String, Object>();
					MpinCheckServiceResp.put("status", false);
					MpinCheckServiceResp.put("description",  messages.getRe_onboard_message());
					MpinCheckServiceResp.put("message", messages.getRe_onboard_message());
					MpinCheckServiceResp.put("forceUpdate",true);

				}
				

			}
			}
			else {
				MpinCheckServiceResp = new HashMap<String, Object>();
				MpinCheckServiceResp.put("status", false);
				MpinCheckServiceResp.put("description", messages.getmpincreateerr);
				MpinCheckServiceResp.put("message", messages.getmpincreateerr);
			}
			
		//	}

			return MpinCheckServiceResp;
			
		}catch(Exception e) {
			RecordLog.writeLogFile("Test11: "+Arrays.toString(e.getStackTrace())+" Test1100: "+e.getMessage());
			e.printStackTrace();
			MpinCheckServiceResp = new HashMap<String, Object>();
		    MpinCheckServiceResp.put("status", false);
			MpinCheckServiceResp.put("description", "Exception occurred in mpin check");
			MpinCheckServiceResp.put("message", "Exception occurred in mpin check");
			

			return MpinCheckServiceResp;
		}
		}
		
		
		//new version v2.4 ended getUserDetailsV24
	
}