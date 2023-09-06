package com.federal.fedmobilesmecore.service;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.federal.fedmobilesmecore.config.GlobalProperties;
import com.federal.fedmobilesmecore.config.RecordLog;
import com.federal.fedmobilesmecore.dto.ApplicationForm;
import com.federal.fedmobilesmecore.dto.Enterprises;
import com.federal.fedmobilesmecore.dto.User;
import com.federal.fedmobilesmecore.model.APIResponse;
import com.federal.fedmobilesmecore.model.DebitcardVerificationPayload;
import com.federal.fedmobilesmecore.model.GetCustDetails;
import com.federal.fedmobilesmecore.model.SoleActivateResp;
import com.federal.fedmobilesmecore.repository.ApplicationFormRepository;
import com.federal.fedmobilesmecore.repository.EnterprisesRepository;
import com.federal.fedmobilesmecore.repository.UserRepository;
import com.federal.fedmobilesmecore.repository.UserRepository_V1;

@Service
public class SolActivateService {

	@Autowired
	UserRepository_V1 userRepository;

	@Autowired
	EnterprisesRepository enterprisesRepository;

	@Autowired
	ApplicationFormRepository applicationFormRepository;

	@Value("${gatewayurl}")
	private String gatewayurl;

	@Autowired
	GlobalProperties globalProperties;

	@Autowired
	GetCustDetails custDetailsService;
	
	@Autowired
	RestTemplate restTemplate;

	public APIResponse debitCardVerfication(DebitcardVerificationPayload debitcardVerification) {
		APIResponse activateResponse = new APIResponse();
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		// headers.setContentType(MediaType.APPLICATION_JSON);
		RecordLog.writeLogFile("debit card verification request" + debitcardVerification);
		String year = debitcardVerification.getExpiryDate().getYear().substring(2, 4);
//		RecordLog.writeLogFile("year" + year);
		String month = debitcardVerification.getExpiryDate().getMonth();
//		RecordLog.writeLogFile(debitcardVerification.getExpiryDate().getMonth());
		String expiryDate1 = year + month;
		RecordLog.writeLogFile("Debit card expiry Date: " + expiryDate1);
		Map<String, String> body = new HashMap<>();
		body.put("cardNo", String.valueOf(debitcardVerification.getCard()));
		body.put("pin", String.valueOf(debitcardVerification.getPin()));
		body.put("expiry", expiryDate1);

		HttpEntity<?> entity = new HttpEntity<>(body, headers);
		RecordLog.writeLogFile("entity request: " + entity);
		ResponseEntity<String> entityresponse = restTemplate.exchange(gatewayurl, HttpMethod.POST, entity,
				String.class);

		RecordLog.writeLogFile("entity response: " + entityresponse);
		String primaryAccountNo = "";
		JSONObject AccountDetails = null;
		JSONArray DetailsArray = null;
		JSONObject details = null;

		try {
			// String primaryAccountNo = entityresponse.getBody().getRecordId();
			JSONObject xmlJSONObj = XML.toJSONObject(entityresponse.getBody());
			String jsonPrettyPrintString = xmlJSONObj.toString(4);
//			RecordLog.writeLogFile(">>>>>>>>>>>>>>>>>>>>>>>>>>" + jsonPrettyPrintString);
			JSONObject env = new JSONObject(jsonPrettyPrintString);
			String env1 = env.get("NS1:Envelope").toString();

			JSONObject bodyJson = new JSONObject(env1);
//			RecordLog.writeLogFile("vjnvjn" + bodyJson);
			String body1 = bodyJson.get("NS1:Body").toString();
			JSONObject response = new JSONObject(body1);
			String reps = response.get("NS2:Response").toString();
			JSONObject respcode = new JSONObject(reps);
			String code = respcode.get("ResponseCode").toString();
			// String desc = respcode.getString("ResponseDescription".toString());
			RecordLog.writeLogFile("responsecode" + code);
			// RecordLog.writeLogFile("ResponseDescription" + desc);
			Map<String, String> responsedetails = new HashMap<String, String>();
//			RecordLog.writeLogFile("code" + code);

			if (code.equals("00")) {
				primaryAccountNo = respcode.get("PrimaryAccountNo").toString();
//				RecordLog.writeLogFile("*************>>>>" + debitcardVerification.getApp_token());
				Optional<User> user = userRepository
						.findByAppTokenAndMarkAsEnabled(debitcardVerification.getApp_token(), true);
				if (user.isPresent()) {
					RecordLog.writeLogFile("User enterprise Id: " + user.get().getEnterpriseId());
					Optional<Enterprises> enterprises = enterprisesRepository
							.findById(Long.valueOf(user.get().getEnterpriseId()));
					ApplicationForm applicationForm = applicationFormRepository
							.findById(enterprises.get().getApplicationFormId())
							.orElseThrow(() -> new RuntimeException(globalProperties.getUsernotfoundmsg()));
//					RecordLog.writeLogFile(">>>>>>>>>>>>>enterprise" + enterprises);
					if (enterprises.isPresent()) {
						String acc_no = enterprises.get().getAccNo();

						String customerdetails = custDetailsService.getData(primaryAccountNo);
//						RecordLog.writeLogFile("custDetails>>>>>>>>>>>>>>>>>>." + customerdetails);
						JSONObject bodyJson1 = XML.toJSONObject(customerdetails);
						RecordLog.writeLogFile("Customer detail: " + bodyJson1);
						JSONObject result = (JSONObject) bodyJson1.get("GetCustomerDetailsResp");
						AccountDetails = result.getJSONObject("AccountDetails");
//						RecordLog.writeLogFile(">>>>>>>>>>." + AccountDetails);
						if (AccountDetails.getJSONObject("RELATEDPARTY").get("DETAILS") instanceof JSONObject) {
							details = AccountDetails.getJSONObject("RELATEDPARTY").getJSONObject("DETAILS");
							DetailsArray = new JSONArray();
							DetailsArray.put(details);
						} else if (AccountDetails.getJSONObject("RELATEDPARTY").get("DETAILS") instanceof JSONArray) {
							DetailsArray = AccountDetails.getJSONObject("RELATEDPARTY").getJSONArray("DETAILS");
						}
						RecordLog.writeLogFile("Customer details: " + DetailsArray);
						User user1 = userRepository.getOne(user.get().getId());
						DetailsArray.forEach(custNO -> {
//							RecordLog.writeLogFile("Hiii");
							String customerId = ((JSONObject) custNO).get("CUSTOMER_ID").toString();
							RecordLog.writeLogFile("customerId " + customerId);
							RecordLog.writeLogFile("getCustNo " + user.get().getCustNo());
							String custNo = user.get().getCustNo();
							if (custNo.equals(customerId)) {
//								RecordLog.writeLogFile("ABCD" + user.get().getCustNo());
								if (applicationForm != null) {
//									RecordLog.writeLogFile("inside applicationForm");
									applicationForm.setStatus("approved");
									applicationForm.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
									applicationFormRepository.save(applicationForm);
									user1.setActivationStatus("activated");
									user1.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
									userRepository.save(user1);
								} else {
									activateResponse.setStatus(false);
									activateResponse.setMessage(globalProperties.getExceptionerrcode());
									activateResponse.setDescription("Application Form Id already exist");
									activateResponse.setRecordId(null);
								}
								if (user1.getAuthToken() == null) {
									user1.setAuthToken("null");
								}

								responsedetails.put("app_form_id", String.valueOf(applicationForm.getId()));
								responsedetails.put("auth_token", user1.getAuthToken());
								responsedetails.put("app_token", user1.getAppToken());
								responsedetails.put("enterpriseId", String.valueOf(enterprises.get().getId()));
								responsedetails.put("prefCorp", enterprises.get().getPrefCorp());
								JSONObject object = new JSONObject(responsedetails);
								activateResponse.setStatus(true);
								activateResponse.setMessage(globalProperties.getSuccessmsg());
								activateResponse.setDescription(globalProperties.getSuccessmsg());
								activateResponse.setRecordId(object.toString());
								RecordLog.writeLogFile("response: " + responsedetails);

							} else {
								activateResponse.setStatus(false);
								activateResponse.setMessage("CUSTOMER_ID does not match with user details");
								activateResponse.setDescription(globalProperties.getExceptionerrcode());
								activateResponse.setRecordId(null);
							}
//							RecordLog.writeLogFile(">>>>>>>>>>>>>>>>>>>>>>>***************" + customerId);
						});
					} else {
						activateResponse.setStatus(false);
						activateResponse.setMessage("Enterprise user not present");
						activateResponse.setDescription(globalProperties.getExceptionerrcode());
						activateResponse.setRecordId(null);
					}
				} else {
					activateResponse.setStatus(false);
					activateResponse.setMessage("Data is not present in the Users table");
					activateResponse.setDescription(globalProperties.getExceptionerrcode());
					activateResponse.setRecordId(null);
				}

			} else {

				activateResponse.setStatus(false);
				activateResponse.setMessage(globalProperties.getSoleActivationError());
				activateResponse.setDescription(respcode.get("ResponseDescription").toString());
				activateResponse.setRecordId(null);
			}

//			RecordLog.writeLogFile(">>>>>>>>>>>>>>>>>>>>>>>>>>>" + primaryAccountNo);
		} catch (JSONException je) {
			RecordLog.writeLogFile("Exception occured at: "+Arrays.toString(je.getStackTrace())+" Exception name: "+je.getMessage());
			activateResponse.setStatus(false);
			activateResponse.setMessage(globalProperties.getExceptionerrmsg());
			activateResponse.setDescription(globalProperties.getExceptionerrcode());
			activateResponse.setRecordId(null);
		}

		return activateResponse;

	}

}
