package com.federal.fedmobilesmecore.service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.federal.fedmobilesmecore.config.GlobalProperties;
import com.federal.fedmobilesmecore.config.PasswordHashing;
import com.federal.fedmobilesmecore.config.RecordLog;
import com.federal.fedmobilesmecore.dto.EnterpriseUser;
import com.federal.fedmobilesmecore.dto.Enterprises;
import com.federal.fedmobilesmecore.dto.User;
import com.federal.fedmobilesmecore.dto.UserPin;
import com.federal.fedmobilesmecore.dto.UserToken;
import com.federal.fedmobilesmecore.model.CaptchaReq;
import com.federal.fedmobilesmecore.model.CaptchaResp;
import com.federal.fedmobilesmecore.model.MessageResp;
import com.federal.fedmobilesmecore.model.SMEMessage;
import com.federal.fedmobilesmecore.model.SendSmsReqModel;
import com.federal.fedmobilesmecore.model.TokenGenerator;
import com.federal.fedmobilesmecore.model.ValidateOtpRespModel;
import com.federal.fedmobilesmecore.model.WebUserSessionApiResp;
import com.federal.fedmobilesmecore.model.WebUserSessionModel;
import com.federal.fedmobilesmecore.model.WebUserSessionResponse;
import com.federal.fedmobilesmecore.repository.EnterpriseUserRepository;
import com.federal.fedmobilesmecore.repository.EnterprisesRepository;
import com.federal.fedmobilesmecore.repository.UserPinRepo;
import com.federal.fedmobilesmecore.repository.UserRepository;
import com.federal.fedmobilesmecore.repository.UserTokenRepo;

@Component
public class ResetOtpWebUsersInternalService {
	@Autowired
	UserPinRepo userPinRepo;
	@Autowired
	UserRepository userRepository;
	@Autowired
	UserTokenRepo userTokenRepo;
	@Autowired
	CustomUserDetailsService_V1 customUserDetailsService_V1;
	@Autowired
	EnterprisesRepository enterprisesRepository;
	@Autowired
	private EnterpriseUserRepository enterpriseUserRepository;

	@Autowired
	RestTemplate restTemplate;
	
	@Autowired
	GlobalProperties globalProperties;
	
	@Autowired
	PasswordHashing passwordHashing;

	@Value("${fblgateway.url.http}")
	private String fblgatewayurl;

	@Value("${external.url}")
	private String fblgatewayurlExt;
	
	@Autowired
	private ResetOtpWebUsersService restOtpWebUsersService;

	public boolean validateWebActivationTokenAndEnable(Optional<User> user, String token) {
		User userResp = null;
		boolean isValidated = false;
		try {
			if (validWebActivationToken(token)) {
				user.get().setActivationStatus("activated");
				// user.get().setWebSignInCount(1L);
				userResp = userRepository.save(user.get());
				if (userResp != null) {
					isValidated = true;
				} else {
					isValidated = false;
				}
			} else {
				isValidated = false;
			}
		} catch (Exception e) {
			RecordLog.writeLogFile("Exception occured at: "+Arrays.toString(e.getStackTrace())+" Exception name: "+e.getMessage());
			isValidated = false;
			e.printStackTrace();
		}
		return isValidated;
	}

	public boolean validWebActivationToken(String token) {
		boolean isActive = false;
		List<String> type = null;
		Optional<UserToken> getUserToken = null;
		UserToken getUserTokenResp = null;

		type = new ArrayList<String>();
		type.add("WebActivationToken");

		try {
			getUserToken = userTokenRepo.findByTypeInAndActiveAndTokenAndExpiredAtGreaterThan(type, BigDecimal.ONE,
					token, new Timestamp(new Date().getTime()));

			if (getUserToken.isPresent()) {
				getUserToken.get().setActive(BigDecimal.ZERO);
				getUserToken.get().setUpdatedAt(new Timestamp(new Date().getTime()));

				getUserTokenResp = userTokenRepo.save(getUserToken.get());
				if (getUserTokenResp != null) {
					isActive = true;
				} else {
					isActive = true;
				}
			} else {
				isActive = false;
			}
		} catch (Exception e) {
			RecordLog.writeLogFile("Exception occured at: "+Arrays.toString(e.getStackTrace())+" Exception name: "+e.getMessage());
			isActive = false;
			e.printStackTrace();
		}

		return isActive;
	}

	public boolean validateWebPinAndEnable(Optional<User> user, String token) {
		User userResp = null;
		boolean isValidated = false;
		try {
			if (validPin(token, user.get().getPrefNo(),user.get().getMobile())) {
				user.get().setActivationStatus("activated");
				userResp = userRepository.save(user.get());
				if (userResp != null) {
					isValidated = true;
				} else {
					isValidated = false;
				}
			} else {
				isValidated = false;
			}
		} catch (Exception e) {
			RecordLog.writeLogFile("Exception occured at: "+Arrays.toString(e.getStackTrace())+" Exception name: "+e.getMessage());
			isValidated = false;
			e.printStackTrace();
		}
		return isValidated;
	}

	public boolean validPin(String forgotPin, String prefNo,String mobile) {
//		RecordLog.writeLogFile(forgotPin + " " + prefNo);
		Optional<UserPin> userPin = null;
		boolean isValidForgotPin = false;
		Optional<User> user = null;
		List<String> values = null;
		try {

			values = new ArrayList<>();
			values.add("WebPin");
			userPin = userPinRepo.findByActiveAndPinAndTypeInAndExpiredAtGreaterThan(BigDecimal.ONE, forgotPin, values,
					new Timestamp(new Date().getTime()));
			if (userPin.isPresent()) {
				user = userRepository.findByPrefNoAndMobileAndMarkAsEnabled(prefNo,mobile,true);
				if (user.isPresent()) {
					userPin.get().setActive(BigDecimal.ZERO);
					userPinRepo.save(userPin.get());
					user.get().setWrongMpinCount("0");
					user.get().setMpinCheckStatus("active");
					userRepository.save(user.get());
					isValidForgotPin = true;
				} else {
					isValidForgotPin = false;
				}
			} else {
				isValidForgotPin = false;
			}
		} catch (Exception e) {
			RecordLog.writeLogFile("Exception occured at: "+Arrays.toString(e.getStackTrace())+" Exception name: "+e.getMessage());
			isValidForgotPin = false;
			e.printStackTrace();
		}
		return isValidForgotPin;
	}

	public boolean validForgotPin(String forgotPin, String prefNo,String mobile) {
		Optional<UserPin> userPin = null;
		boolean isValidForgotPin = false;
		Optional<User> user = null;
		List<String> values = null;
		try {
			values = new ArrayList<>();
			values.add("ForgotPin");
			
			
			
			
			
			userPin = userPinRepo.findByActiveAndPinAndTypeInAndExpiredAtGreaterThan(BigDecimal.ONE, forgotPin, values,
					new Timestamp(new Date().getTime()));
			RecordLog.writeLogFile("ajay0502"+new Timestamp(new Date().getTime()));
			RecordLog.writeLogFile("ajay0502"+new Date());
			RecordLog.writeLogFile("ajay0502"+userPin.get().getExpiredAt());
			if (userPin.isPresent()) {
				user = userRepository.findByPrefNoAndMobileAndMarkAsEnabled(prefNo,mobile,true);
				if (user.isPresent()) {
					userPin.get().setActive(BigDecimal.ZERO);
					userPinRepo.save(userPin.get());
					user.get().setWrongMpinCount("0");
					user.get().setLastSignInAt(new Timestamp(new Date(System.currentTimeMillis()).getTime()));
					user.get().setMpinCheckStatus("active");
					userRepository.save(user.get());
					isValidForgotPin = true;
				} else {
					isValidForgotPin = false;
				}
			} else {
				isValidForgotPin = false;
			}
		} catch (Exception e) {
			RecordLog.writeLogFile("Exception occured at: "+Arrays.toString(e.getStackTrace())+" Exception name: "+e.getMessage());
			isValidForgotPin = false;
			e.printStackTrace();
		}
		return isValidForgotPin;
	}

	public WebUserSessionApiResp generateAuthenticationToken(String prefNo, String password) {
		WebUserSessionApiResp resp = null;
		WebUserSessionModel userSessionModel = null;

		userSessionModel = new WebUserSessionModel();
		userSessionModel.setPrefNo(prefNo);
		userSessionModel.setPassword(password);

		resp = customUserDetailsService_V1.generateWebUserAuthToken(userSessionModel);
		return resp;
	}

	public boolean isValidOtp(String userOtp, String inputOtp, Timestamp otpExpiredAt) {
		boolean isValidOtp = false;
       
		if (userOtp.equals(inputOtp) && isOtpExpired(otpExpiredAt)) {
			isValidOtp = true;
		}
		return isValidOtp;
	}

	public boolean isOtpExpired(Timestamp otpExpiredAt) {
		boolean isOtpExpired = false;
		int timeStampCompare = 0;
		if (otpExpiredAt == null) {
			return isOtpExpired = true;
		}
		timeStampCompare = otpExpiredAt.compareTo(new Timestamp(new Date().getTime()));
		if (timeStampCompare > 0) {
			isOtpExpired = true;
		} else {
			isOtpExpired = false;
		}
		return isOtpExpired;
	}

	public ValidateOtpRespModel generateTwoFactorAuth(Optional<User> user) {
		String otp = null;
		String textMessage = null;

		ValidateOtpRespModel validateOtpRespModel = null;
		MessageResp messageResp = null;

		try {
			if (user.get().getRole() != null) {
				if (user.get().getRole().equals("external")) {
					otp = new DecimalFormat("000000").format(new Random().nextInt(999999));

					user.get().setOtp(otp);
					user.get().setUpdatedAt(new Timestamp(new Date().getTime()));
					user.get().setOtpExpiredAt(new Timestamp(
							new Date(System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(10)).getTime()));
					userRepository.save(user.get());

					textMessage = otp
							+ " is the OTP for completing Corporate FedMobile Registration Process. Never share this OTP with anyone including Bank officials. Federal Bank";
					RecordLog.writeLogFile("textMessage " + textMessage);
//					SendSmsReqModel sendSmsReqModel = new SendSmsReqModel();
//					sendSmsReqModel.setMessageText(textMessage);
//					sendSmsReqModel.setMobileNo(user.get().getMobile());
//					sendSmsReqModel.setShortCode(String.valueOf(new Random().nextInt(9000000) + 1000000));
					
					
					
					
					JSONObject req = new JSONObject();

			        req.put("mobileno", user.get().getMobile());
			        req.put("messageText",textMessage );
			        req.put("shortcode", String.valueOf(new Random().nextInt(9000000) + 1000000));
					
			        HttpHeaders headers = new HttpHeaders();
					headers.setContentType(MediaType.APPLICATION_JSON);
			//headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
					HttpEntity<String> entity = new HttpEntity<String>(req.toString(),
							headers);
					

					ResponseEntity<?> smsResponse = restTemplate.postForEntity(
							fblgatewayurl + "/fblgateway_v1/sendmsgtouser", entity, String.class);
					if (smsResponse.getStatusCodeValue() == 200) {

						messageResp = new MessageResp();
						messageResp.setCustomerName(user.get().getCustomerName());
						messageResp.setFavoriteAcc(user.get().getFavouriteAccount());
						messageResp.setFirstTimeLogin(null);
						messageResp.setMobile(user.get().getMobile());
						messageResp.setPrefNo(user.get().getPrefNo());
						messageResp.setRole(user.get().getRole());
						messageResp.setUserName(user.get().getUserName());

						validateOtpRespModel = new ValidateOtpRespModel();
						validateOtpRespModel.setStatus(true);
						validateOtpRespModel.setDescription("success");
						validateOtpRespModel.setMessage("OTP sent");
						validateOtpRespModel.setMessageResp(messageResp);
					} else {
						validateOtpRespModel = new ValidateOtpRespModel();
						validateOtpRespModel.setStatus(false);
						validateOtpRespModel.setDescription("failed");
						validateOtpRespModel.setMessage("Unable to send OTP");
						validateOtpRespModel.setMessageResp(null);
					}
				} else {
					validateOtpRespModel = new ValidateOtpRespModel();
					validateOtpRespModel.setStatus(false);
					validateOtpRespModel.setMessage("Not an external user");
					validateOtpRespModel.setDescription("Not a external user");
					validateOtpRespModel.setMessageResp(null);
				}
			} else {
				validateOtpRespModel = new ValidateOtpRespModel();
				validateOtpRespModel.setStatus(false);
				validateOtpRespModel.setMessage("Not an external user");
				validateOtpRespModel.setDescription("Not an external user");
				validateOtpRespModel.setMessageResp(null);
			}
		} catch (Exception e) {
			RecordLog.writeLogFile("Exception occured at: "+Arrays.toString(e.getStackTrace())+" Exception name: "+e.getMessage());
			validateOtpRespModel = new ValidateOtpRespModel();
			validateOtpRespModel.setStatus(false);
			validateOtpRespModel.setMessage("error occured");
			validateOtpRespModel.setDescription("failed");
			validateOtpRespModel.setMessageResp(null);
			e.printStackTrace();
		}
		return validateOtpRespModel;
	}

	public ValidateOtpRespModel sendOtpForExternalUser(String prefNo,String mobile) {
		Optional<User> user = null;
		ValidateOtpRespModel validateOtpRespModel = null;
		String otp = null;
		String textMessage = null;
		UserPin userPin = null;

		user = userRepository.findByPrefNoAndMobileAndMarkAsEnabled(prefNo,mobile,true);
		if (user.isPresent()) {
			
			
			if(user.get().getWebCheckStatus()==null || user.get().getWebCheckStatus().equals("active")) {
			
			
			if (user.get().getRole() != null && user.get().getRole().equals("external")) {
				otp = new DecimalFormat("000000").format(new Random().nextInt(999999));

				user.get().setOtp(otp);
				user.get().setUpdatedAt(new Timestamp(new Date().getTime()));
				user.get().setOtpExpiredAt(
						new Timestamp(new Date(System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(10)).getTime()));
				userRepository.save(user.get());

				textMessage = otp
						+ " is the OTP for completing Corporate FedMobile Registration Process. Never share this OTP with anyone including Bank officials. Federal Bank";
				RecordLog.writeLogFile("textMessage " + textMessage);
				SendSmsReqModel sendSmsReqModel = new SendSmsReqModel();
//				sendSmsReqModel.setMessageText(textMessage);
//				sendSmsReqModel.setMobileNo(user.get().getMobile());
//				sendSmsReqModel.setShortCode(String.valueOf(new Random().nextInt(9000000) + 1000000));
				
				
				JSONObject req = new JSONObject();

		        req.put("mobileno", user.get().getMobile());
		        req.put("messageText",textMessage );
		        req.put("shortcode", String.valueOf(new Random().nextInt(9000000) + 1000000));
				
		        HttpHeaders headers = new HttpHeaders();
				headers.setContentType(MediaType.APPLICATION_JSON);
		//headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
				HttpEntity<String> entity = new HttpEntity<String>(req.toString(),
						headers);
				

				ResponseEntity<?> smsResponse = restTemplate
						.postForEntity(fblgatewayurl + "/fblgateway_v1/sendmsgtouser", entity, String.class);
				if (smsResponse.getStatusCodeValue() == 200) {
					validateOtpRespModel = new ValidateOtpRespModel();
					validateOtpRespModel.setStatus(true);
					validateOtpRespModel.setDescription("success");
					validateOtpRespModel.setMessage("OTP sent");
					// TODO remove OTP From here.This is just for testing.
					validateOtpRespModel.setMessageResp(otp);
					
					validateOtpRespModel.setFirstTimeWebLogin(true);



					SMEMessage smeMessage=restOtpWebUsersService.generateDummyToken(user.get().getPrefNo(),user.get().getMobile());
										if(smeMessage.status) {
										validateOtpRespModel.setAuthToken(smeMessage.getRecordid());
										}
					
				} else {
					validateOtpRespModel = new ValidateOtpRespModel();
					validateOtpRespModel.setStatus(false);
					validateOtpRespModel.setDescription("failed");
					validateOtpRespModel.setMessage("unable to send otp");
					validateOtpRespModel.setMessageResp(null);
				}
			} else {
				String zeros = "0000";
				Random rnd = new Random();
				String s = Integer.toString(rnd.nextInt(0X10000), 16);
				String pin = zeros.substring(s.length()) + s;
				String hashedOtp = null;

				userPin = new UserPin();
				userPin.setUserId(BigDecimal.valueOf(user.get().getId()));
				userPin.setPin(pin);
				userPin.setType("ForgotPin");
				userPin.setActive(BigDecimal.ONE);
				userPin.setExpiredAt(
						new Timestamp(new Date(System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(10)).getTime()));
				userPin.setCreatedAt(new Timestamp(new Date().getTime()));
				userPin.setUpdatedAt(new Timestamp(new Date().getTime()));
				userPinRepo.save(userPin);
				//otp = new DecimalFormat("000000").format(new Random().nextInt(999999));
				//hashedOtp = passwordHashing.getPasswordHash(otp);
				//user.get().setOtp(hashedOtp);
				user.get().setUpdatedAt(new Timestamp(new Date().getTime()));
				user.get().setOtpExpiredAt(
						new Timestamp(new Date(System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(10)).getTime()));
				userRepository.save(user.get());
				textMessage = "Dear Customer, your password reset token for Corporate FedMobile (FedCorp) Web version is "
						+ pin
						+ ". Please do not share this token with anyone including Bank officials. If the request was not initiated by you, please call 1800-425-1199 immediately-Federal Bank";
				RecordLog.writeLogFile("textMessage " + textMessage);
				SendSmsReqModel sendSmsReqModel = new SendSmsReqModel();
//				sendSmsReqModel.setMessageText(textMessage);
//				sendSmsReqModel.setMobileNo(user.get().getMobile());
//				sendSmsReqModel.setShortCode(String.valueOf(new Random().nextInt(9000000) + 1000000));
				
				
				
				JSONObject req = new JSONObject();

		        req.put("mobileno", user.get().getMobile());
		        req.put("messageText",textMessage );
		        req.put("shortcode", String.valueOf(new Random().nextInt(9000000) + 1000000));
				
		        HttpHeaders headers = new HttpHeaders();
				headers.setContentType(MediaType.APPLICATION_JSON);
		//headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
				HttpEntity<String> entity = new HttpEntity<String>(req.toString(),
						headers);
				
				

				ResponseEntity<?> smsResponse = restTemplate
						.postForEntity(fblgatewayurl + "/fblgateway_v1/sendmsgtouser", entity, String.class);
				if (smsResponse.getStatusCodeValue() == 200) {
					validateOtpRespModel = new ValidateOtpRespModel();
					validateOtpRespModel.setStatus(true);
					validateOtpRespModel.setDescription("success");
					validateOtpRespModel.setMessage("pin sent successful");
					validateOtpRespModel.setMessageResp(null);
					validateOtpRespModel.setFirstTimeWebLogin(false);
				} else {
					validateOtpRespModel = new ValidateOtpRespModel();
					validateOtpRespModel.setStatus(false);
					validateOtpRespModel.setDescription("failed");
					validateOtpRespModel.setMessage("unable to send otp");
					validateOtpRespModel.setMessageResp(null);
				}
			}
			
			
			}else {
				validateOtpRespModel = new ValidateOtpRespModel();
				validateOtpRespModel.setStatus(false);
				validateOtpRespModel.setMessageResp(null);
				if(user.get().getRole()==null) {
				
				validateOtpRespModel.setDescription(globalProperties.getWeb_auth_sign_msg());
				validateOtpRespModel.setMessage(globalProperties.getWeb_auth_sign_msg());
				
				}else {
					validateOtpRespModel.setDescription(globalProperties.getWeb_wrongpassword());
					validateOtpRespModel.setMessage(globalProperties.getWeb_wrongpassword());
				}
			}
			
			
		} else {
			validateOtpRespModel = new ValidateOtpRespModel();
			validateOtpRespModel.setStatus(false);
			validateOtpRespModel.setDescription("failed");
			validateOtpRespModel.setMessage("user not found");
			validateOtpRespModel.setMessageResp(null);
		}

		return validateOtpRespModel;
	}

	public ValidateOtpRespModel sendOtpForExternalUser_v1(String prefNo,String mobile) {
		Optional<User> user = null;
		ValidateOtpRespModel validateOtpRespModel = null;
		String otp = null;
		String textMessage = null;
		UserPin userPin = null;
		UserToken userToken = null;
		String hashedOtp = null;

		user = userRepository.findByPrefNoAndMobileAndMarkAsEnabled(prefNo,mobile,true);
		if (user.isPresent()) {
			if (user.get().getRole() != null && user.get().getRole().equals("external")) {
				otp = new DecimalFormat("000000").format(new Random().nextInt(999999));
				hashedOtp = passwordHashing.getPasswordHash(otp);

				user.get().setOtp(hashedOtp);
				user.get().setUpdatedAt(new Timestamp(new Date().getTime()));
				user.get().setOtpExpiredAt(
						new Timestamp(new Date(System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(10)).getTime()));
//				RecordLog.writeLogFile("time stamp "+
//						new Timestamp(new Date(System.currentTimeMillis() + TimeUnit.HOURS.toMillis(24)).getTime()));
				userRepository.save(user.get());

				textMessage = otp
						+ " is the OTP for completing Corporate FedMobile Registration Process. Never share this OTP with anyone including Bank officials. Federal Bank";
				RecordLog.writeLogFile("textMessage " + textMessage);
//				SendSmsReqModel sendSmsReqModel = new SendSmsReqModel();
//				sendSmsReqModel.setMessageText(textMessage);
//				sendSmsReqModel.setMobileNo(user.get().getMobile());
//				sendSmsReqModel.setShortCode(String.valueOf(new Random().nextInt(9000000) + 1000000));
				
				JSONObject req = new JSONObject();

		        req.put("mobileno", user.get().getMobile());
		        req.put("messageText",textMessage );
		        req.put("shortcode", String.valueOf(new Random().nextInt(9000000) + 1000000));
				
		        HttpHeaders headers = new HttpHeaders();
				headers.setContentType(MediaType.APPLICATION_JSON);
		//headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
				HttpEntity<String> entity = new HttpEntity<String>(req.toString(),
						headers);
				

				ResponseEntity<?> smsResponse = restTemplate
						.postForEntity(fblgatewayurl + "/fblgateway_v1/sendmsgtouser", entity, String.class);
				if (smsResponse.getStatusCodeValue() == 200) {
					validateOtpRespModel = new ValidateOtpRespModel();
					validateOtpRespModel.setStatus(true);
					validateOtpRespModel.setDescription("success");
					validateOtpRespModel.setMessage("OTP sent");
					validateOtpRespModel.setMessageResp(null);
					validateOtpRespModel.setOtp(otp);
				} else {
					validateOtpRespModel = new ValidateOtpRespModel();
					validateOtpRespModel.setStatus(false);
					validateOtpRespModel.setDescription("failed");
					validateOtpRespModel.setMessage("unable to send otp");
					validateOtpRespModel.setMessageResp(null);
					validateOtpRespModel.setOtp(null);
				}
			} else {
				String zeros = "90000009";
				Random rnd = new Random();
				String s = Integer.toString(rnd.nextInt(0X1000000), 16);
				otp = zeros.substring(s.length()) + s;

				/*
				 * userPin = new UserPin();
				 * userPin.setUserId(BigDecimal.valueOf(user.get().getId()));
				 * userPin.setPin(otp); userPin.setType("WebActivationToken");
				 * userPin.setActive(BigDecimal.ONE); userPin.setExpiredAt(new Timestamp(new
				 * Date(System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(15)).getTime()));
				 * userPin.setCreatedAt(new Timestamp(new Date().getTime()));
				 * userPin.setUpdatedAt(new Timestamp(new Date().getTime()));
				 * userPinRepo.save(userPin);
				 */

				// TODO
				userToken = new UserToken();
				userToken.setActive(BigDecimal.ONE);
				userToken.setUser(user.get());
				userToken.setCreatedAt(new Timestamp(new Date().getTime()));
				userToken.setExpiredAt(
						new Timestamp(new Date(System.currentTimeMillis() + TimeUnit.HOURS.toMillis(24)).getTime()));
				userToken.setToken(otp);
				userToken.setType("WebActivationToken");
				userToken.setUpdatedAt(new Timestamp(new Date().getTime()));
				userTokenRepo.save(userToken);

				textMessage = "Dear Customer, your password reset token for Corporate FedMobile (FedCorp) Web version is "
						+ otp
						+ ". Please do not share this token with anyone including Bank officials. If the request was not initiated by you, please call 1800-425-1199 immediately-Federal Bank";
				RecordLog.writeLogFile("textMessage " + textMessage);
//				SendSmsReqModel sendSmsReqModel = new SendSmsReqModel();
//				sendSmsReqModel.setMessageText(textMessage);
//				sendSmsReqModel.setMobileNo(user.get().getMobile());
//				sendSmsReqModel.setShortCode(String.valueOf(new Random().nextInt(9000000) + 1000000));
				
				
				
				JSONObject req = new JSONObject();

		        req.put("mobileno", user.get().getMobile());
		        req.put("messageText",textMessage );
		        req.put("shortcode", String.valueOf(new Random().nextInt(9000000) + 1000000));
				
		        HttpHeaders headers = new HttpHeaders();
				headers.setContentType(MediaType.APPLICATION_JSON);
		//headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
				HttpEntity<String> entity = new HttpEntity<String>(req.toString(),
						headers);
				
				

				ResponseEntity<?> smsResponse = restTemplate
						.postForEntity(fblgatewayurl + "/fblgateway_v1/sendmsgtouser", entity, String.class);
				if (smsResponse.getStatusCodeValue() == 200) {
					validateOtpRespModel = new ValidateOtpRespModel();
					validateOtpRespModel.setStatus(true);
					validateOtpRespModel.setDescription("success");
					validateOtpRespModel.setMessage("pin sent successful");
					validateOtpRespModel.setMessageResp(null);
					validateOtpRespModel.setWebToken(otp);
				} else {
					validateOtpRespModel = new ValidateOtpRespModel();
					validateOtpRespModel.setStatus(false);
					validateOtpRespModel.setDescription("failed");
					validateOtpRespModel.setMessage("unable to send otp");
					validateOtpRespModel.setMessageResp(null);
					validateOtpRespModel.setWebToken(null);
				}
			}
		} else {
			validateOtpRespModel = new ValidateOtpRespModel();
			validateOtpRespModel.setStatus(false);
			validateOtpRespModel.setDescription("failed");
			validateOtpRespModel.setMessage("user not found");
			validateOtpRespModel.setMessageResp(null);
		}

		return validateOtpRespModel;
	}

	public boolean checkPassword(String userPassword, String reqPassword) {
		boolean isPasswordSame = false;
		String hashPassword = null;
		hashPassword = passwordHashing.getPasswordHash(reqPassword);
		if (userPassword.equals(hashPassword)) {
			isPasswordSame = true;
		}
		return isPasswordSame;
	}

	public ValidateOtpRespModel generateOtpWebInternal(Optional<User> user) {
		ValidateOtpRespModel validateOtpRespModel = null;
		String otp = null;
		String textMessage = null;
		String otpHash = null;
		
		
		otp = new DecimalFormat("000000").format(new Random().nextInt(999999));
		otpHash = passwordHashing.getPasswordHash(otp);

		user.get().setOtp(otpHash);
		user.get().setUpdatedAt(new Timestamp(new Date().getTime()));
		user.get().setOtpExpiredAt(
				new Timestamp(new Date(System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(10)).getTime()));
		userRepository.save(user.get());

		textMessage = otp
				+ " is the OTP for completing Corporate FedMobile Registration Process. Never share this OTP with anyone including Bank officials. Federal Bank";
		RecordLog.writeLogFile("textMessage " + textMessage);
		/*SendSmsReqModel sendSmsReqModel = new SendSmsReqModel();
		sendSmsReqModel.setMessageText(textMessage);
		sendSmsReqModel.setMobileNo(user.get().getMobile());
		sendSmsReqModel.setShortCode(String.valueOf(new Random().nextInt(9000000) + 1000000));*/
		
		JSONObject req = new JSONObject();

        req.put("mobileno", user.get().getMobile());
        req.put("messageText",textMessage );
        req.put("shortcode", String.valueOf(new Random().nextInt(9000000) + 1000000));
		
		RecordLog.writeLogFile("User web otp mobile number: " + req.get("mobileno"));
		RecordLog.writeLogFile("User web otp messageText: " + req.get("messageText"));
		RecordLog.writeLogFile("User web otp shortcode: " + req.get("shortcode"));

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
//headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		HttpEntity<String> entity = new HttpEntity<String>(req.toString(),
				headers);
		
		ResponseEntity<?> smsResponse = restTemplate.postForEntity(fblgatewayurl + "/fblgateway_v1/sendmsgtouser",entity, String.class);
		if (smsResponse.getStatusCodeValue() == 200) {
			validateOtpRespModel = new ValidateOtpRespModel();
			validateOtpRespModel.setStatus(true);
			validateOtpRespModel.setDescription("success");
			validateOtpRespModel.setMessage("OTP sent");
			validateOtpRespModel.setMessageResp(null);
			// TODO remove it in actual UAT/LIVE
			// validateOtpRespModel.setOtp(otp);
		} else {
			validateOtpRespModel = new ValidateOtpRespModel();
			validateOtpRespModel.setStatus(false);
			validateOtpRespModel.setDescription("Unable to send OTP");
			validateOtpRespModel.setMessage("Unable to send OTP");
			validateOtpRespModel.setMessageResp(null);
		}
		return validateOtpRespModel;
	}

	public ValidateOtpRespModel verifyCaptcha(String mobileNo, String captchaRequest) {
		String state = null;
		CaptchaReq captchaReq = null;
		CaptchaResp captchaResp = null;
		WebUserSessionModel userSessionModel = null;
		User user = null;
		User userResp = null;
		WebUserSessionApiResp resp = null;
		ValidateOtpRespModel validateOtpRespModel = null;
		Optional<Enterprises> enterprises = null;

		captchaReq = new CaptchaReq();
		captchaReq.setReponse(captchaRequest);

		try {
			ResponseEntity<CaptchaResp> smsResponse = restTemplate.postForEntity(fblgatewayurlExt + "/ext/recaptcha",
					captchaReq, CaptchaResp.class);
			if (smsResponse.getStatusCodeValue() == 200) {
				captchaResp = smsResponse.getBody();
				if (captchaResp.isStatus() == true) {
					user = userRepository.findByMobileAndMarkAsEnabled(mobileNo, true);
					if (user != null) {
						if (user.getWebSignInCount() == null) {
							user.setWebSignInCount(0L);
						}
						if (user.getAuthToken() == null) {
							userSessionModel = new WebUserSessionModel();
							userSessionModel.setPrefNo(user.getPrefNo());
							userSessionModel.setPassword(user.getPassword());

							// RecordLog.writeLogFile(user.getPrefNo() + "----" + user.getPassword());

							resp = customUserDetailsService_V1.generateWebUserAuthToken_V1(userSessionModel);
							enterprises = enterprisesRepository.findById(Long.valueOf(user.getEnterpriseId()));
							if (resp.isStatus() == true) {
								if (user.getWebSignInCount() == 0L) {
									if (user.getRole() != null) {
										if (user.getRole().equals("external")) {
											sendOtpForExternalUser_v1(user.getPrefNo(),user.getMobile());
											state = "pending_otp_activation";

											validateOtpRespModel = new ValidateOtpRespModel();
											validateOtpRespModel.setStatus(true);
											validateOtpRespModel.setMessage("success");
											validateOtpRespModel.setDescription("success");
											validateOtpRespModel.setState(state);
											validateOtpRespModel.setPrefCorp(enterprises.get().getPrefCorp());
											validateOtpRespModel.setAuthToken(
													((WebUserSessionResponse) resp.getRecordid()).getAuthToken());
											validateOtpRespModel.setFirstTimeWebLogin(
													user.getWebSignInCount() == 0L ? true : false);
											validateOtpRespModel.setMessageResp(null);

											// user.setWebSignInCount(user.getWebSignInCount() + 1);
											// userResp = userRepository.save(user);

										} else {
											// TODO
											sendOtpForExternalUser_v1(user.getPrefNo(),user.getMobile());
											state = "pending_web_activation";

											validateOtpRespModel = new ValidateOtpRespModel();
											validateOtpRespModel.setStatus(true);
											validateOtpRespModel.setMessage("success");
											validateOtpRespModel.setDescription("success");
											validateOtpRespModel.setState(state);
											validateOtpRespModel.setPrefCorp(enterprises.get().getPrefCorp());
											validateOtpRespModel.setAuthToken(
													((WebUserSessionResponse) resp.getRecordid()).getAuthToken());
											RecordLog.writeLogFile("User web signIn count: " + user.getWebSignInCount());
											validateOtpRespModel.setFirstTimeWebLogin(
													user.getWebSignInCount() == 0L ? true : false);
											validateOtpRespModel.setMessageResp(null);

											// user.setWebSignInCount(user.getWebSignInCount() + 1);
											// userRepository.save(user);
										}
									} else {
										sendOtpForExternalUser_v1(user.getPrefNo(),user.getMobile());
										state = "pending_web_activation";

										user.setWebSignInCount(user.getWebSignInCount());
										validateOtpRespModel = new ValidateOtpRespModel();
										validateOtpRespModel.setStatus(true);
										validateOtpRespModel.setMessage("success");
										validateOtpRespModel.setDescription("success");
										validateOtpRespModel.setState(state);
										validateOtpRespModel.setPrefCorp(enterprises.get().getPrefCorp());
										validateOtpRespModel.setAuthToken(
												((WebUserSessionResponse) resp.getRecordid()).getAuthToken());
										RecordLog.writeLogFile("User web signIn count: " + user.getWebSignInCount());
										validateOtpRespModel
												.setFirstTimeWebLogin(user.getWebSignInCount() == 0L ? true : false);
										validateOtpRespModel.setMessageResp(null);

										// user.setWebSignInCount(user.getWebSignInCount() + 1);
										// userRepository.save(user);
									}
								} else {
									state = "pending_login";
									validateOtpRespModel = new ValidateOtpRespModel();
									validateOtpRespModel.setStatus(true);
									validateOtpRespModel.setMessage("success");
									validateOtpRespModel.setDescription("success");
									validateOtpRespModel.setState(state);
									validateOtpRespModel.setPrefCorp(enterprises.get().getPrefCorp());
									validateOtpRespModel
											.setAuthToken(((WebUserSessionResponse) resp.getRecordid()).getAuthToken());
									RecordLog.writeLogFile("User web signIn count: " + user.getWebSignInCount());
									validateOtpRespModel.setFirstTimeWebLogin(false);
									validateOtpRespModel.setMessageResp(null);
								}
							} else {
								validateOtpRespModel = new ValidateOtpRespModel();
								validateOtpRespModel.setStatus(false);
								validateOtpRespModel.setMessage("AuthToken generation failed");
								validateOtpRespModel.setDescription("AuthToken generation failed");
								validateOtpRespModel.setState(null);
								validateOtpRespModel.setPrefCorp(enterprises.get().getPrefCorp());
								validateOtpRespModel.setAuthToken(null);
								validateOtpRespModel.setFirstTimeWebLogin(false);
								validateOtpRespModel.setMessageResp(null);
							}
						} else {
							enterprises = enterprisesRepository.findById(Long.valueOf(user.getEnterpriseId()));

							validateOtpRespModel = new ValidateOtpRespModel();
							validateOtpRespModel.setStatus(false);
							validateOtpRespModel.setMessage("Please clear the existing authtoken");
							validateOtpRespModel.setDescription("AuthToken exist");
							validateOtpRespModel.setState("authToken_exist");
							validateOtpRespModel.setPrefCorp(enterprises.get().getPrefCorp());
							validateOtpRespModel.setAuthToken(user.getAuthToken());
							validateOtpRespModel.setFirstTimeWebLogin(false);
							validateOtpRespModel.setMessageResp(null);
						}
					} else {
						validateOtpRespModel = new ValidateOtpRespModel();
						validateOtpRespModel.setStatus(false);
						validateOtpRespModel.setMessage("User not found");
						validateOtpRespModel.setDescription("User not found");
						validateOtpRespModel.setState(null);
						validateOtpRespModel.setPrefCorp(null);
						validateOtpRespModel.setAuthToken(null);
						validateOtpRespModel.setFirstTimeWebLogin(false);
						validateOtpRespModel.setMessageResp(null);
					}
				} else {
					validateOtpRespModel = new ValidateOtpRespModel();
					validateOtpRespModel.setStatus(false);
					validateOtpRespModel.setMessage("reCAPTCHA verification failed");
					validateOtpRespModel.setDescription("reCAPTCHA verification failed");
					validateOtpRespModel.setState(null);
					validateOtpRespModel.setPrefCorp(null);
					validateOtpRespModel.setAuthToken(null);
					validateOtpRespModel.setFirstTimeWebLogin(false);
					validateOtpRespModel.setMessageResp(null);
				}
			} else {
				validateOtpRespModel = new ValidateOtpRespModel();
				validateOtpRespModel.setStatus(false);
				validateOtpRespModel.setMessage("Unable to reach server");
				validateOtpRespModel.setDescription("Unable to reach server");
				validateOtpRespModel.setState(null);
				validateOtpRespModel.setPrefCorp(null);
				validateOtpRespModel.setAuthToken(null);
				validateOtpRespModel.setFirstTimeWebLogin(false);
				validateOtpRespModel.setMessageResp(null);
			}
		} catch (Exception e) {
			RecordLog.writeLogFile("Exception occured at: "+Arrays.toString(e.getStackTrace())+" Exception name: "+e.getMessage());
			e.printStackTrace();
			validateOtpRespModel = new ValidateOtpRespModel();
			validateOtpRespModel.setStatus(false);
			validateOtpRespModel.setMessage("Unable to process your request, please try again later");
			validateOtpRespModel.setDescription("Unable to process your request, please try again later");
			validateOtpRespModel.setState(null);
			validateOtpRespModel.setPrefCorp(null);
			validateOtpRespModel.setAuthToken(null);
			validateOtpRespModel.setFirstTimeWebLogin(false);
			validateOtpRespModel.setMessageResp(null);
		}
		return validateOtpRespModel;
	}

	private String getRandomHexString(int numchars) {
		Random r = new Random();
		StringBuffer sb = new StringBuffer();
		while (sb.length() < numchars) {
			sb.append(Integer.toHexString(r.nextInt()));
		}
		return sb.toString().substring(0, numchars);
	}

	public ValidateOtpRespModel verifyCaptcha_v1(String mobileNo, String captchaRequest) {
		String state = null;
		CaptchaReq captchaReq = null;
		CaptchaResp captchaResp = null;
		WebUserSessionModel userSessionModel = null;
		User user = null;
		User userResp = null;
		// WebUserSessionApiResp resp = null;
		ValidateOtpRespModel validateOtpRespModel = null;
		ValidateOtpRespModel validateOtpRespModelOtp = null;
		Optional<Enterprises> enterprises = null;
		TokenGenerator resp = null;

		captchaReq = new CaptchaReq();
		captchaReq.setReponse(captchaRequest);

		try {
			ResponseEntity<CaptchaResp> smsResponse = restTemplate.postForEntity(fblgatewayurlExt + "/ext/recaptcha",
					captchaReq, CaptchaResp.class);
			if (smsResponse.getStatusCodeValue() == 200) {
				captchaResp = smsResponse.getBody();
				if (captchaResp.isStatus() == true) {
					user = userRepository.findByMobileAndMarkAsEnabled(mobileNo, true);

					 if (user != null) {
						 long days=0l;	
						 long yeardiff=0l;
							if(user.getLastSignInAt()!=null) {
							Date now=new Date(System.currentTimeMillis());
							Date old=Date.from(user.getLastSignInAt().toInstant());
							long timediff=now.getTime()-old.getTime();
							days=(timediff/(1000*60*60*24))%365;
					         yeardiff=(timediff/(1000l*60*60*24*365));
							}
							if(days<=180l && yeardiff<=0l) {
						    if (user.getActivationStatus().equals("activated")) {

							if (user.getWebSignInCount() == null) {
								user.setWebSignInCount(0L);
							}
							enterprises = enterprisesRepository.findById(Long.valueOf(user.getEnterpriseId()));
							if (user.getWebSignInCount() == 0L) {
								user.setAuthToken(null);
								if (user.getAuthToken() == null) {
									resp = new TokenGenerator();
									resp.setToken("");
									resp.setStatus(true);
									/*
									 * userSessionModel = new WebUserSessionModel();
									 * userSessionModel.setPrefNo(user.getPrefNo());
									 * userSessionModel.setPassword(user.getViewPwd()); resp =
									 * customUserDetailsService_V1.generateWebUserAuthToken_V1(userSessionModel);
									 */

									// user.setAuthToken(resp.getToken());
									// userRepository.save(user);
									if (resp.isStatus() == true) {
										if (user.getRole() != null) {
											if (user.getRole().equals("external")) {
												validateOtpRespModelOtp = sendOtpForExternalUser_v1(user.getPrefNo(),user.getMobile());
												state = "pending_otp_activation";

												validateOtpRespModel = new ValidateOtpRespModel();
												validateOtpRespModel.setStatus(true);
												validateOtpRespModel.setMessage("success");
												validateOtpRespModel.setDescription("success");
												validateOtpRespModel.setState(state);
												validateOtpRespModel.setPrefNo(user.getPrefNo());
												validateOtpRespModel.setOtp(validateOtpRespModelOtp.getOtp());
												validateOtpRespModel.setWebToken(null);
												validateOtpRespModel.setPrefCorp(enterprises.get().getPrefCorp());
												validateOtpRespModel.setAuthToken("");
												validateOtpRespModel.setFirstTimeWebLogin(
														user.getWebSignInCount() == 0L ? true : false);
												validateOtpRespModel.setMessageResp(null);
											} else {
												validateOtpRespModelOtp = sendOtpForExternalUser_v1(user.getPrefNo(),user.getMobile());
												state = "pending_web_activation";

												validateOtpRespModel = new ValidateOtpRespModel();
												validateOtpRespModel.setStatus(true);
												validateOtpRespModel.setMessage("success");
												validateOtpRespModel.setDescription("success");
												validateOtpRespModel.setState(state);
												validateOtpRespModel.setPrefNo(user.getPrefNo());
												validateOtpRespModel.setPrefCorp(enterprises.get().getPrefCorp());
												validateOtpRespModel.setWebToken(validateOtpRespModelOtp.getWebToken());
												validateOtpRespModel.setAuthToken("");
												RecordLog.writeLogFile("User web sign in count: " + user.getWebSignInCount());
												validateOtpRespModel.setOtp(null);
												validateOtpRespModel.setWebToken(null);
												validateOtpRespModel.setFirstTimeWebLogin(
														user.getWebSignInCount() == 0L ? true : false);
												validateOtpRespModel.setMessageResp(null);
											}
										} else {
											validateOtpRespModelOtp = sendOtpForExternalUser_v1(user.getPrefNo(),user.getMobile());
											state = "pending_web_activation";

											user.setWebSignInCount(user.getWebSignInCount());
											validateOtpRespModel = new ValidateOtpRespModel();
											validateOtpRespModel.setStatus(true);
											validateOtpRespModel.setMessage("success");
											validateOtpRespModel.setDescription("success");
											validateOtpRespModel.setState(state);
											validateOtpRespModel.setPrefNo(user.getPrefNo());
											validateOtpRespModel.setPrefCorp(enterprises.get().getPrefCorp());
											validateOtpRespModel.setAuthToken("");
											RecordLog.writeLogFile("User web sign in count: " + user.getWebSignInCount());
											validateOtpRespModel.setWebToken(validateOtpRespModelOtp.getWebToken());
											validateOtpRespModel.setFirstTimeWebLogin(
													user.getWebSignInCount() == 0L ? true : false);
											validateOtpRespModel.setMessageResp(null);
										}
									} else {
										validateOtpRespModel = new ValidateOtpRespModel();
										validateOtpRespModel.setStatus(false);
										validateOtpRespModel.setMessage("failed");
										validateOtpRespModel.setDescription("AuthToken generation failed ");
										validateOtpRespModel.setState(null);
										validateOtpRespModel.setPrefCorp(enterprises.get().getPrefCorp());
										validateOtpRespModel.setPrefNo(null);
										validateOtpRespModel.setAuthToken(null);
										validateOtpRespModel.setFirstTimeWebLogin(false);
										validateOtpRespModel.setMessageResp(null);
									}
								} else {
									enterprises = enterprisesRepository.findById(Long.valueOf(user.getEnterpriseId()));
									validateOtpRespModel = new ValidateOtpRespModel();
									validateOtpRespModel.setStatus(false);
									validateOtpRespModel.setMessage("Please clear the existing authtoken");
									validateOtpRespModel.setDescription("authToken exist");
									validateOtpRespModel.setState("authToken_exist");
									validateOtpRespModel.setPrefCorp(enterprises.get().getPrefCorp());
									validateOtpRespModel.setPrefNo(user.getPrefNo());
									validateOtpRespModel.setAuthToken(user.getAuthToken());
									validateOtpRespModel.setFirstTimeWebLogin(false);
									validateOtpRespModel.setMessageResp(null);
								}
							} else {
								state = "pending_login";
								validateOtpRespModel = new ValidateOtpRespModel();
								validateOtpRespModel.setStatus(true);
								validateOtpRespModel.setMessage("success");
								validateOtpRespModel.setDescription("success");
								validateOtpRespModel.setState(state);
								validateOtpRespModel.setPrefCorp(enterprises.get().getPrefCorp());
								validateOtpRespModel.setPrefNo(user.getPrefNo());
								validateOtpRespModel.setAuthToken(user.getAuthToken());
								validateOtpRespModel.setFirstTimeWebLogin(false);
								validateOtpRespModel.setMessageResp(null);
							}
						} else {
							validateOtpRespModel = new ValidateOtpRespModel();
							validateOtpRespModel.setStatus(false);
							validateOtpRespModel.setMessage("User is not activated");
							validateOtpRespModel.setDescription("User is not activated");
							validateOtpRespModel.setState(null);
							validateOtpRespModel.setPrefCorp(null);
							validateOtpRespModel.setPrefNo(null);
							validateOtpRespModel.setAuthToken(null);
							validateOtpRespModel.setFirstTimeWebLogin(false);
							validateOtpRespModel.setMessageResp(null);
						}
	
					 }else {
						 
							validateOtpRespModel = new ValidateOtpRespModel();
						  if(user.getRole()!=null && user.getRole().equals("external")) {
						//	user.setActivationStatus("blocked");
							EnterpriseUser enterpriseUser =enterpriseUserRepository.findByMobileAndPrefNoAndStatus(user.getMobile(),user.getPrefNo(),"approved");
							enterpriseUser.setWebBlockStatus("blocked");
							enterpriseUserRepository.save(enterpriseUser);
							
							validateOtpRespModel.setStatus(false);
							validateOtpRespModel.setMessage(globalProperties.getWeb_six_month_gap_extuser());
							validateOtpRespModel.setDescription(globalProperties.getWeb_six_month_gap_extuser());
							validateOtpRespModel.setPrefNo(user.getPrefNo());
							validateOtpRespModel.setRole(user.getRole());
							validateOtpRespModel.setActivity(false);
							validateOtpRespModel.setAuthToken("");	
							
							
							}else {
							validateOtpRespModel.setStatus(false);
							validateOtpRespModel.setMessage(globalProperties.getWeb_six_month_gap());
							validateOtpRespModel.setDescription(globalProperties.getWeb_six_month_gap());
							validateOtpRespModel.setPrefNo(user.getPrefNo());
							validateOtpRespModel.setRole(user.getRole());
							validateOtpRespModel.setActivity(false);
							validateOtpRespModel.setAuthToken("");	
							}
					 }
						 
					} else {
						validateOtpRespModel = new ValidateOtpRespModel();
						validateOtpRespModel.setStatus(false);
						validateOtpRespModel.setMessage("Invalid Mobile Number Please Enter Correct Number!");
						validateOtpRespModel.setDescription("Invalid Mobile Number Please Enter Correct Number!");
						validateOtpRespModel.setState(null);
						validateOtpRespModel.setPrefCorp(null);
						validateOtpRespModel.setPrefNo(null);
						validateOtpRespModel.setAuthToken(null);
						validateOtpRespModel.setFirstTimeWebLogin(false);
						validateOtpRespModel.setMessageResp(null);
					}
				} else {
					validateOtpRespModel = new ValidateOtpRespModel();
					validateOtpRespModel.setStatus(false);
					validateOtpRespModel.setMessage("reCAPTCHA verification failed");
					validateOtpRespModel.setDescription("reCAPTCHA verification failed");
					validateOtpRespModel.setState(null);
					validateOtpRespModel.setPrefCorp(null);
					validateOtpRespModel.setPrefNo(null);
					validateOtpRespModel.setAuthToken(null);
					validateOtpRespModel.setFirstTimeWebLogin(false);
					validateOtpRespModel.setMessageResp(null);
				}
			} else {
				validateOtpRespModel = new ValidateOtpRespModel();
				validateOtpRespModel.setStatus(false);
				validateOtpRespModel.setMessage("Unable to reach gateway server");
				validateOtpRespModel.setDescription("Unable to reach gateway server");
				validateOtpRespModel.setState(null);
				validateOtpRespModel.setPrefCorp(null);
				validateOtpRespModel.setPrefNo(null);
				validateOtpRespModel.setAuthToken(null);
				validateOtpRespModel.setFirstTimeWebLogin(false);
				validateOtpRespModel.setMessageResp(null);
			}
		} catch (Exception e) {
			RecordLog.writeLogFile("Exception occured at: "+Arrays.toString(e.getStackTrace())+" Exception name: "+e.getMessage());
			e.printStackTrace();
			validateOtpRespModel = new ValidateOtpRespModel();
			validateOtpRespModel.setStatus(false);
			validateOtpRespModel.setMessage("Unable to process your request, please try again later");
			validateOtpRespModel.setDescription("Unable to process your request, please try again later");
			validateOtpRespModel.setState(null);
			validateOtpRespModel.setPrefCorp(null);
			validateOtpRespModel.setPrefNo(null);
			validateOtpRespModel.setAuthToken(null);
			validateOtpRespModel.setFirstTimeWebLogin(false);
			validateOtpRespModel.setMessageResp(null);
		}
		return validateOtpRespModel;
	}
}