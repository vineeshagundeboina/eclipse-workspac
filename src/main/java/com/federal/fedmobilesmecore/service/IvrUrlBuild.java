package com.federal.fedmobilesmecore.service;

import java.math.BigDecimal;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import javax.net.ssl.SSLContext;
import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.ssl.SSLContexts;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.federal.fedmobilesmecore.config.GlobalProperties;
import com.federal.fedmobilesmecore.config.RecordLog;
import com.federal.fedmobilesmecore.dto.ApplicationForm;
import com.federal.fedmobilesmecore.dto.Enterprises;
import com.federal.fedmobilesmecore.dto.Ivr;
import com.federal.fedmobilesmecore.dto.User;
import com.federal.fedmobilesmecore.dto.UserToken;
import com.federal.fedmobilesmecore.model.IVRInput;
import com.federal.fedmobilesmecore.model.SMEMessage;
import com.federal.fedmobilesmecore.model.SendSmsReqModel;
import com.federal.fedmobilesmecore.repository.ApplicationFormRepository;
import com.federal.fedmobilesmecore.repository.EnterprisesRepository;
import com.federal.fedmobilesmecore.repository.IVRRepo;
import com.federal.fedmobilesmecore.repository.UserRepository;
import com.federal.fedmobilesmecore.repository.UserTokenRepo;

@Service
@Transactional
public class IvrUrlBuild {

	private static final Logger log4j = LogManager.getLogger(IvrUrlBuild.class);

	@Autowired
	IVRRepo ivrrepo;

	@Autowired
	GlobalProperties properties;

	@Value("${external.url}")
	private String extenalurl;
	
//	@Value("${app.isUat}")
//	private String isUat;

	@Value("${gateway.url}")
	private String gatewayurl;

	@Autowired
	ApplicationFormRepository apprepo;

	@Autowired
	EnterprisesRepository entrepo;

	@Autowired
	UserRepository userrepo;

	@Autowired
	UserTokenRepo tokenrepo;
	
	@Autowired
	RestTemplate restTemplate;

	public SMEMessage buildUrl(String mobileno)
			throws NoSuchAlgorithmException, JsonProcessingException, KeyManagementException, KeyStoreException {

		SMEMessage smemessage = new SMEMessage();
		HttpHeaders headers = new HttpHeaders();

		IVRInput input = new IVRInput();
		input.setMobile(mobileno);
//		if(isUat.equals("true")) {
//			User user = userrepo.findByMobile(mobileno);
//			SMEMessage msg = generateRegiToken();
//			if (msg.isStatus()) {
//				UserToken usertoken = generateUserToken(user, msg.getRecordid());
//				RecordLog.writeLogFile("usertoken============>"+usertoken);
//				tokenrepo.save(usertoken);
//				
//			}
//		}
		
		
		SSLContext sslcontext = SSLContexts.custom().loadTrustMaterial(null, (chain, authType) -> true).build();
		// FIXME In future issue might come to enhance it to TLSv1.2
		SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslcontext, new String[] { "TLSv1" }, null,
				new NoopHostnameVerifier());
		HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory(
				HttpClientBuilder.create().setSSLSocketFactory(sslsf).build());
		requestFactory.setReadTimeout(60000);
		requestFactory.setConnectTimeout(60000);
		restTemplate.setRequestFactory(requestFactory);
		HttpEntity<IVRInput> entity = new HttpEntity<IVRInput>(input, headers);
		ResponseEntity<SMEMessage> response = restTemplate.exchange(extenalurl + "/ext/createivr", HttpMethod.POST,
				entity, SMEMessage.class);
		if (response.getStatusCode() == HttpStatus.OK) {
			smemessage = response.getBody();
		} else {
			smemessage.setStatus(false);
			smemessage.setMessage("Unable to fetch IVR Details :" + response.getStatusCode());
		}

		return smemessage;
	}

//	public SMEMessage buildUrlFallback(String mobileno) {
//		SMEMessage smemessage = new SMEMessage();
//		smemessage.setStatus(false);
//		smemessage.setMessage(properties.getIvrna());
//		return smemessage;
//	}

	public SMEMessage ivrVerify(IVRInput input) throws NoSuchAlgorithmException {
		SMEMessage smemessage = new SMEMessage();
		if (StringUtils.isNotEmpty(input.getAppHash()) && StringUtils.isNotEmpty(input.getMobile())
				&& StringUtils.isNotEmpty(input.getOtp()) && StringUtils.isNotEmpty(input.getApp_form_id())) {

			List<Ivr> ivrlist = ivrrepo.findByMobileAndOtpAndAppHash(input.getMobile(), input.getOtp(),
					input.getAppHash());
			if (!CollectionUtils.isEmpty(ivrlist)) {
				RecordLog.writeLogFile("Number of IVR's that are available " + ivrlist.size());
				SMEMessage statusmessage = checkApplicationForm(input);
				RecordLog.writeLogFile(statusmessage.isStatus() + ":" + statusmessage.getMessage());
				if (statusmessage.isStatus()) {
					smemessage = statusmessage;
					Ivr ivrs = ivrlist.get(0);
					ivrs.setStatus(IVRStatus.approved.toString());
					ivrrepo.save(ivrs);
				} else {
					smemessage = statusmessage;
				}
			} else {
				smemessage.setStatus(false);
				smemessage.setMessage(properties.getReqinputsinvalidcred());
				RecordLog.writeLogFile("No IVR Available with the given inputs:" + smemessage.getMessage());
			}
		} else {
			smemessage.setStatus(false);
			smemessage.setMessage(properties.getReqinputsna());
			RecordLog.writeLogFile("Required fields are not available:" + smemessage.getMessage());

		}
		return smemessage;
	}
	
	
	//new apiv2_4
	
	public SMEMessage ivrVerifyv24(IVRInput input) throws NoSuchAlgorithmException {
		SMEMessage smemessage = new SMEMessage();
		if (StringUtils.isNotEmpty(input.getAppHash()) && StringUtils.isNotEmpty(input.getMobile())
				&& StringUtils.isNotEmpty(input.getOtp()) && StringUtils.isNotEmpty(input.getApp_form_id())) {

			List<Ivr> ivrlist = ivrrepo.findByMobileAndOtpAndAppHash(input.getMobile(), input.getOtp(),
					input.getAppHash());
			if (!CollectionUtils.isEmpty(ivrlist)) {
				RecordLog.writeLogFile("Number of IVR's that are available " + ivrlist.size());
				SMEMessage statusmessage = checkApplicationFormv24(input);
				RecordLog.writeLogFile(statusmessage.isStatus() + ":" + statusmessage.getMessage());
				if (statusmessage.isStatus()) {
					smemessage = statusmessage;
					Ivr ivrs = ivrlist.get(0);
					ivrs.setStatus(IVRStatus.approved.toString());
					ivrrepo.save(ivrs);
				} else {
					smemessage = statusmessage;
				}
			} else {
				smemessage.setStatus(false);
				smemessage.setMessage(properties.getReqinputsinvalidcred());
				RecordLog.writeLogFile("No IVR Available with the given inputs:" + smemessage.getMessage());
			}
		} else {
			smemessage.setStatus(false);
			smemessage.setMessage(properties.getReqinputsna());
			RecordLog.writeLogFile("Required fields are not available:" + smemessage.getMessage());

		}
		return smemessage;
	}
	
	//new api v24 ended
	
	

	public SMEMessage checkApplicationForm(IVRInput input) {
		SMEMessage message = new SMEMessage();
		String token = "";
		Long appformid = 0L;
		try {
			appformid = Long.parseLong(input.getApp_form_id());
		} catch (Exception e) {
			RecordLog.writeLogFile("Exception occured at: "+Arrays.toString(e.getStackTrace())+" Exception name: "+e.getMessage());
			e.printStackTrace();
			message.setStatus(false);
			message.setMessage("Invalid Application Form Number");
			return message;
		}

		RecordLog.writeLogFile("Form Id:" + appformid);
		Optional<ApplicationForm> application = apprepo.findById(appformid.longValue());
		if (application.isPresent()) {
			List<Enterprises> enterpriseslist = entrepo.findByActiveAndApplicationFormId(true, appformid);
			if (!CollectionUtils.isEmpty(enterpriseslist)) {
				if (enterpriseslist.size() == 1) {
					RecordLog.writeLogFile("No of enterprise:" + enterpriseslist.size() + ":" + enterpriseslist.get(0).getId());

					List<User> userlist = userrepo.findByEnterpriseIdAndMarkAsEnabledAndMobile(
							enterpriseslist.get(0).getId() + "", true, input.getMobile());

					if (!CollectionUtils.isEmpty(userlist)) {
						RecordLog.writeLogFile("No of users:" + userlist.size());
						if (userlist.size() == 1) {
							if (StringUtils.isNotEmpty(application.get().getStatus())) {
								if (application.get().getStatus().equals("approved")
										|| application.get().getStatus().equals("new")
										|| application.get().getStatus().equals("forward")) {
									User user = userlist.get(0);
									if (StringUtils.isNotEmpty(user.getActivationStatus())) {
										if (user.getActivationStatus().equals("activated") || user.getActivationStatus().equals("exceeded")) {
											user.setActivationStatus("pending_activation");
											message = generateRegiToken();
											if (message.isStatus()) {
												UserToken usertoken = generateUserToken(user, message.getRecordid());
												tokenrepo.save(usertoken);
												//TODO Comment it out while go-live.
												message.setDescription(usertoken.getToken());
												token = usertoken.getToken();
											}
										}
										SMEMessage msgapptoken = generateAppToken();
										UserToken usertoken1 = generateUserToken(user, msgapptoken.getRecordid());
										tokenrepo.save(usertoken1);
										user.setWrongActivationTokenCount("0");
										user.setAppToken(msgapptoken.getRecordid());
										userrepo.save(user);

										message.setStatus(true);
										message.setMessage("Success");
										message.setRecordid(msgapptoken.getRecordid());
											
										String messageString = properties.getUserregistrationtokenmessage();
										RecordLog.writeLogFile("messageString:" + messageString);
										RecordLog.writeLogFile("Token:" + msgapptoken.getRecordid());

										if(messageString!=null) {
											if(messageString.length()!=0) {
												
												messageString = messageString.replace("@token@", msgapptoken.getRecordid());
												SMEMessage smsapptoken = sendSMS(messageString, input.getMobile());
												if (smsapptoken.isStatus()) {
													RecordLog.writeLogFile(smsapptoken.isStatus() + ":" + smsapptoken.getMessage());
												}
											}
										}

									} else {
										message.setStatus(false);
										message.setMessage("Activation status is not available");
									}
								} else {
									message.setStatus(false);
									message.setMessage("Enterprise is not yet approved / Rejected.Please contact support team.");
								}

							} else {
								message.setStatus(false);
								message.setMessage("Status is not available for current Enterprise");
							}

						} else {
							message.setStatus(false);
							message.setMessage("More than one user exist with the same mobile numeber and enterprise");
						}
					} else {
						message.setStatus(false);
						message.setMessage("No Users are available");
					}
				} else {
					message.setStatus(false);
					message.setMessage("More than one enterprise exists with same application details");
				}

			} else {
				message.setStatus(false);
				message.setMessage("Your enterprise is not active");
			}

		} else {
			message.setStatus(false);
			message.setMessage("Invalid Application Number");
		}
		return message;
	}
	
	
	//new api v24 started
	
	public SMEMessage checkApplicationFormv24(IVRInput input) {
		SMEMessage message = new SMEMessage();
		String token = "";
		Long appformid = 0L;
		try {
			appformid = Long.parseLong(input.getApp_form_id());
		} catch (Exception e) {
			RecordLog.writeLogFile("Exception occured at: "+Arrays.toString(e.getStackTrace())+" Exception name: "+e.getMessage());
			e.printStackTrace();
			message.setStatus(false);
			message.setMessage("Invalid Application Form Number");
			return message;
		}

		RecordLog.writeLogFile("Form Id:" + appformid);
		Optional<ApplicationForm> application = apprepo.findById(appformid.longValue());
		if (application.isPresent()) {
			List<Enterprises> enterpriseslist = entrepo.findByActiveAndApplicationFormId(true, appformid);
			if (!CollectionUtils.isEmpty(enterpriseslist)) {
				if (enterpriseslist.size() == 1) {
					RecordLog.writeLogFile("No of enterprise:" + enterpriseslist.size() + ":" + enterpriseslist.get(0).getId());

					List<User> userlist = userrepo.findByEnterpriseIdAndMarkAsEnabledAndMobile(
							enterpriseslist.get(0).getId() + "", true, input.getMobile());

					if (!CollectionUtils.isEmpty(userlist)) {
						RecordLog.writeLogFile("No of users:" + userlist.size());
						if (userlist.size() == 1) {
							if (StringUtils.isNotEmpty(application.get().getStatus())) {
								if (application.get().getStatus().equals("approved")
										|| application.get().getStatus().equals("new")
										|| application.get().getStatus().equals("forward")) {
									User user = userlist.get(0);
									if (StringUtils.isNotEmpty(user.getActivationStatus())) {
										if (user.getActivationStatus().equals("activated") || user.getActivationStatus().equals("exceeded")) {
											user.setActivationStatus("pending_activation");
//											message = generateRegiToken();
//											if (message.isStatus()) {
//												UserToken usertoken = generateUserToken(user, message.getRecordid());
//												tokenrepo.save(usertoken);
//												//TODO Comment it out while go-live.
//												message.setDescription(usertoken.getToken());
//												token = usertoken.getToken();
//											}
										}
										SMEMessage msgapptoken = generateAppToken();
//										UserToken usertoken1 = generateUserToken(user, msgapptoken.getRecordid());
//										tokenrepo.save(usertoken1);
										user.setWrongActivationTokenCount("0");
										user.setAppToken(msgapptoken.getRecordid());
										userrepo.save(user);

										message.setStatus(true);
										message.setMessage("Success");
										message.setRecordid(msgapptoken.getRecordid());
											
										String messageString = properties.getUserregistrationtokenmessage();
										RecordLog.writeLogFile("messageString:" + messageString);
										RecordLog.writeLogFile("Token:" + msgapptoken.getRecordid());

//										if(messageString!=null) {
//											if(messageString.length()!=0) {
//												
//												messageString = messageString.replace("@token@", msgapptoken.getRecordid());
//												SMEMessage smsapptoken = sendSMS(messageString, input.getMobile());
//												if (smsapptoken.isStatus()) {
//													RecordLog.writeLogFile(smsapptoken.isStatus() + ":" + smsapptoken.getMessage());
//												}
//											}
//										}

									} else {
										message.setStatus(false);
										message.setMessage("Activation status is not available");
									}
								} else {
									message.setStatus(false);
									message.setMessage("Enterprise is not yet approved / Rejected.Please contact support team.");
								}

							} else {
								message.setStatus(false);
								message.setMessage("Status is not available for current Enterprise");
							}

						} else {
							message.setStatus(false);
							message.setMessage("More than one user exist with the same mobile numeber and enterprise");
						}
					} else {
						message.setStatus(false);
						message.setMessage("No Users are available");
					}
				} else {
					message.setStatus(false);
					message.setMessage("More than one enterprise exists with same application details");
				}

			} else {
				message.setStatus(false);
				message.setMessage("Your enterprise is not active");
			}

		} else {
			message.setStatus(false);
			message.setMessage("Invalid Application Number");
		}
		return message;
	}
	
	//new api v24 ended

	public SMEMessage sendSMS(String message, String mobileno) {
		SMEMessage messageobj = new SMEMessage();
//		SendSmsReqModel sendSmsReqModel = new SendSmsReqModel();
//		sendSmsReqModel.setMessageText(message);
//		sendSmsReqModel.setMobileNo(mobileno);
//		sendSmsReqModel.setShortCode(String.valueOf(new Random().nextInt(9000000) + 1000000));
		JSONObject req = new JSONObject();
        req.put("mobileno",mobileno);
        req.put("messageText",message );
        req.put("shortcode", String.valueOf(new Random().nextInt(9000000) + 1000000));
		
        HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
//headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		HttpEntity<String> entity = new HttpEntity<String>(req.toString(),
				headers);
		try {
			SSLContext sslcontext = SSLContexts.custom().loadTrustMaterial(null, (chain, authType) -> true).build();
			// FIXME In future issue might come to enhance it to TLSv1.2
			SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslcontext, new String[] { "TLSv1" },
					null, new NoopHostnameVerifier());
			HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory(
					HttpClientBuilder.create().setSSLSocketFactory(sslsf).build());
			requestFactory.setReadTimeout(60000);
			requestFactory.setConnectTimeout(60000);

			restTemplate.setRequestFactory(requestFactory);
			System.out.println("send sms request"+req.toString());
			ResponseEntity<?> smsResponse = restTemplate.postForEntity(gatewayurl + "/fblgateway_v1/sendmsgtouser",
					entity, String.class);
			RecordLog.writeLogFile("smsResponse" + smsResponse);
			if (smsResponse.getStatusCode() == HttpStatus.OK) {
				String resp = (String) smsResponse.getBody();
				if (resp.equals("Failure")) {
					messageobj.setStatus(false);
					messageobj.setMessage("Unable to send Message");
				} else {
					messageobj.setStatus(true);
					messageobj.setMessage("SMS Sent succesfully");
				}
			} else {
				messageobj.setStatus(false);
				messageobj.setMessage("Unable to send Message");

			}
		} catch (Exception e) {
			RecordLog.writeLogFile("Exception occured at: "+Arrays.toString(e.getStackTrace())+" Exception name: "+e.getMessage());
			e.printStackTrace();
			messageobj.setStatus(true);
			messageobj.setMessage("Error Occured while sending SMS.");
		}

		return messageobj;
	}

	public SMEMessage generateRegiToken() {

		SMEMessage message = new SMEMessage();
		String random = getRandomHexString(8);
		message.setStatus(true);
		message.setRecordid(random);
		return message;
	}

	public UserToken generateUserToken(User user, String tokentxt) {

		Date date = new Date();
		Timestamp timestamp = new Timestamp(date.getTime());
		ZoneId defaultZoneId = ZoneId.systemDefault();
		LocalDate localDate = LocalDate.now().plusDays(1);
		Date enddate = Date.from(localDate.atStartOfDay(defaultZoneId).toInstant());

		UserToken token = new UserToken();
		token.setActive(BigDecimal.ONE);
		token.setUser(user);
		token.setCreatedAt(timestamp);
		token.setUpdatedAt(timestamp);
		token.setExpiredAt(new Timestamp(enddate.getTime()));
		token.setToken(tokentxt);
		token.setType("RegistrationToken");
		return token;
	}

	public SMEMessage generateAppToken() {

		SMEMessage message = new SMEMessage();
		String random = getRandomHexString(6);
		message.setStatus(true);
		message.setRecordid(random);
		return message;
	}

	private String getRandomHexString(int numchars) {
		Random r = new Random();
		StringBuffer sb = new StringBuffer();
		while (sb.length() < numchars) {
			sb.append(Integer.toHexString(r.nextInt()));
		}

		return sb.toString().substring(0, numchars);
	}

//	public SMEMessage ivrVerifyFallback(IVRInput input) {
//		SMEMessage smemessage = new SMEMessage();
//		smemessage.setStatus(false);
//		// FIXME Create a new message
//		smemessage.setMessage(properties.getIvrna());
//		return smemessage;
//	}

	/**
	 * Created by vikasramireddy
	 * 
	 * @param input
	 * @param otp
	 * @return
	 */
	public boolean constructIVR(IVRInput input, String otp) {
		try {
			Ivr out = new Ivr();
			out.setAppHash(input.getAppHash());
			out.setCreatedAt(new Timestamp(System.currentTimeMillis()));
			out.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
			out.setMobile(input.getMobile());
			out.setOtp(otp);
			out.setStatus(IVRStatus.pending.toString());
			createIVR(out);
			return true;
		} catch (Exception e) {
			RecordLog.writeLogFile("Exception occured at: "+Arrays.toString(e.getStackTrace())+" Exception name: "+e.getMessage());
			e.printStackTrace();
		}

		return false;
	}

	/**
	 * Created by vikas ramireddy
	 * 
	 * @param input
	 * @return
	 */
	public Ivr createIVR(Ivr input) {

		ivrrepo.save(input);
		return input;
	}

	/**
	 * @author Vikas
	 *
	 */
	public enum IVRStatus {
		pending, pending_activation, approved
	}

}
