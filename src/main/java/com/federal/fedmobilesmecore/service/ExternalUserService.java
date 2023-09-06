package com.federal.fedmobilesmecore.service;

import java.security.SecureRandom;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.federal.fedmobilesmecore.config.GlobalProperties;
import com.federal.fedmobilesmecore.config.PasswordHashing;
import com.federal.fedmobilesmecore.config.RecordLog;
import com.federal.fedmobilesmecore.dto.ApplicationEnterpris;
import com.federal.fedmobilesmecore.dto.EnterpriseUser;
import com.federal.fedmobilesmecore.dto.Enterprises;
import com.federal.fedmobilesmecore.dto.User;
import com.federal.fedmobilesmecore.httpservice.FedExternalUserGatewayApiCall;
import com.federal.fedmobilesmecore.model.ExternalUserCommonReq;
import com.federal.fedmobilesmecore.model.ExternalUserCreationReqModel;
import com.federal.fedmobilesmecore.model.GetExternalUserModelResp;
import com.federal.fedmobilesmecore.model.MakerCheckerListModel;
import com.federal.fedmobilesmecore.model.RecordIdExtUser;
import com.federal.fedmobilesmecore.model.SMEMessage;
import com.federal.fedmobilesmecore.model.SendSmsReqModel;
import com.federal.fedmobilesmecore.model.ValidateExtUserPrefNoReqModel;
import com.federal.fedmobilesmecore.repository.ApplicationEnterprisRepository;
import com.federal.fedmobilesmecore.repository.EnterpriseUserRepository;
import com.federal.fedmobilesmecore.repository.EnterprisesRepository;
import com.federal.fedmobilesmecore.repository.UserRepository;
import com.federal.fedmobilesmecore.repository.UserRepository_V1;

@Service
public class ExternalUserService {

	@Autowired
	UserRepository userRepository;
	@Autowired
	UserRepository_V1 userRepository_V1;
	@Autowired
	EnterpriseUserRepository enterpriseUserRepository;
	@Autowired
	EnterprisesRepository enterprisesRepository;
	@Autowired
	GlobalProperties messages;
	@Autowired
	RestTemplate restTemplate;
	@Autowired
	MakerCheckerService makerCheckerService;
	@Autowired
	CommonExternalService commonExternalService;
	@Autowired
	FedExternalUserGatewayApiCall fedExternalUserGatewayApiCall;
	@Autowired
	ApplicationEnterprisRepository applicationEnterprisRepository;
	
	@Autowired
	private SimBindingService simBindingService;
	
	@Autowired
	PasswordHashing passwordHashing; 

	private static final Logger log4j = LogManager.getLogger(ExternalUserService.class);

	@Value("${fblgateway.url.http}")
	private String fblgatewayurl;

	public GetExternalUserModelResp validateExternalUserMobileNo(String authToken, String mobileNumber,String prefCorp) {
		Boolean mobNoExist=false;
		String enterpriseId;
		GetExternalUserModelResp externalUserModelResp = null;

		User user = userRepository.findByAuthToken(authToken);
		try {
			if (user == null) {
				// 401
				externalUserModelResp = new GetExternalUserModelResp();
				externalUserModelResp.setStatus(messages.isFailed());
				externalUserModelResp.setMessage(messages.getUsernotfoundmsg());
				externalUserModelResp.setDescription("");
				externalUserModelResp.setRecordId(null);
			} else if(!user.getMobile().equals(mobileNumber)){
				if (user.getEnterpriseId() == null) {
					externalUserModelResp = new GetExternalUserModelResp();
					externalUserModelResp.setStatus(messages.isFailed());
					externalUserModelResp.setMessage(messages.getUserenterpriseidnotfoundmsg());
					externalUserModelResp.setDescription("");
					externalUserModelResp.setRecordId(null);
				} else {
					enterpriseId = user.getEnterpriseId();
					List<String> status=new ArrayList<>();
					status.add("deleted");
					status.add("rejected");
					List<EnterpriseUser> enterpriseUser = enterpriseUserRepository.findByMobileAndStatusNotIn(mobileNumber,status);
//					RecordLog.writeLogFile("Enterprise User is " + enterpriseUser);
					if (enterpriseUser.size() == 0) {
						RecordLog.writeLogFile("Enterprise User is null");
						User userExist = userRepository.findByMobileAndMarkAsEnabledAndEnterpriseIdNot(mobileNumber,
								true, enterpriseId);
						RecordLog.writeLogFile("User exisits: " + userExist);
						if (userExist == null) {
							mobNoExist = false;
						} else {
							mobNoExist = true;
						}
					} else if (enterpriseUser.get(0) instanceof EnterpriseUser) {
						mobNoExist = true;
					} else {
						User userExist = userRepository.findByMobileAndMarkAsEnabledAndEnterpriseIdNot(mobileNumber,
								true, enterpriseId);
						if (userExist == null) {
							mobNoExist = false;
						} else {
							mobNoExist = true;
						}
					}
					externalUserModelResp = new GetExternalUserModelResp();
					externalUserModelResp.setStatus(mobNoExist == true ? messages.isFailed() : messages.isSuccess());
					externalUserModelResp
							.setMessage(mobNoExist == true ? messages.getNoalreadyregmsg() : messages.getSuccessmsg());
					externalUserModelResp.setDescription("");
					externalUserModelResp.setRecordId(null);
				}
			}else {
				externalUserModelResp = new GetExternalUserModelResp();
				externalUserModelResp.setStatus(messages.isFailed());
				externalUserModelResp
						.setMessage("External mobile number cannot be same as User mobile number");
				externalUserModelResp.setDescription("");
				externalUserModelResp.setRecordId(null);
			}
		} catch (Exception e) {
			RecordLog.writeLogFile("Exception occured at: "+Arrays.toString(e.getStackTrace())+" Exception name: "+e.getMessage());
			e.printStackTrace();
			externalUserModelResp = new GetExternalUserModelResp();
			externalUserModelResp.setStatus(messages.isFailed());
			externalUserModelResp.setMessage(messages.getExceptionerrmsg());
			externalUserModelResp.setDescription("");
			externalUserModelResp.setRecordId(null);
		}
		return externalUserModelResp;
	}

	public GetExternalUserModelResp validateExternalUserPrefNo(ValidateExtUserPrefNoReqModel prefNoReqModel) {
		String enterpriseId = null;
		String enterpriseUser = null;
		boolean extUsedIdExist = false;
		GetExternalUserModelResp externalUserModelResp = null;

		User user = userRepository.findByAuthToken(prefNoReqModel.getAuthToken());
		try {
			if (user == null) {
				externalUserModelResp = new GetExternalUserModelResp();
				externalUserModelResp.setStatus(messages.isFailed());
				externalUserModelResp.setMessage(messages.getUsernotfoundmsg());
				externalUserModelResp.setDescription("");
				externalUserModelResp.setRecordId(null);
			} else if(!user.getPrefNo().equals(prefNoReqModel.getPrefNo())){
				if (user.getEnterpriseId() == null) {
					externalUserModelResp = new GetExternalUserModelResp();
					externalUserModelResp.setStatus(messages.isFailed());
					externalUserModelResp.setMessage(messages.getUserenterpriseidnotfoundmsg());
					externalUserModelResp.setDescription("");
					externalUserModelResp.setRecordId(null);
				} else {
					enterpriseId = user.getEnterpriseId();
					enterpriseUser = enterpriseUserRepository.isEnterpriseUserExistForPrefNoVal(enterpriseId,
							prefNoReqModel.getPrefNo());
					if (enterpriseUser == null) {
						extUsedIdExist = false;
					} else if (enterpriseUser.equals("1")) {
						extUsedIdExist = true;
					} else {
						extUsedIdExist = false;
					}
					externalUserModelResp = new GetExternalUserModelResp();
					externalUserModelResp
							.setStatus(extUsedIdExist == true ? messages.isFailed() : messages.isSuccess());
					externalUserModelResp.setMessage(
							extUsedIdExist == true ? messages.getUserIdalreadyregmsg() : messages.getSuccessmsg());
					externalUserModelResp.setDescription("");
					externalUserModelResp.setRecordId(null);
				}
			}else {
				externalUserModelResp = new GetExternalUserModelResp();
				externalUserModelResp
						.setStatus( messages.isFailed());
				externalUserModelResp.setMessage("External UserID cannot be same as Customer UserID" );
				externalUserModelResp.setDescription("");
				externalUserModelResp.setRecordId(null);
			}
		} catch (Exception e) {
			RecordLog.writeLogFile("Exception occured at: "+Arrays.toString(e.getStackTrace())+" Exception name: "+e.getMessage());
			log4j.error(e.getMessage());
			externalUserModelResp = new GetExternalUserModelResp();
			externalUserModelResp.setStatus(messages.isFailed());
			externalUserModelResp.setMessage(messages.getExceptionerrmsg());
			externalUserModelResp.setDescription("");
			externalUserModelResp.setRecordId(null);
		}
		return externalUserModelResp;
	}

	public String generateRefNumber() {
		String zeros = "000000";
		Random rnd = new Random();
		String s = Integer.toString(rnd.nextInt(0X1000000), 16);
		s = zeros.substring(s.length()) + s;
		s = "EXT" + s;
		return s.toUpperCase();
	}

	public Optional<User> getCurrentUserDetail(String authToken) {
		Optional<User> getCurrentUserDetail = null;
		getCurrentUserDetail = userRepository_V1.findByAuthToken(authToken);
		return getCurrentUserDetail;
	}

	public GetExternalUserModelResp createExtUser(ExternalUserCreationReqModel creationReqModel) {
		String enterpriseId = null;
		String viewPwd = null;
		String genRefNo = null;
		EnterpriseUser isGenRefNoExist = null;

		GetExternalUserModelResp externalUserModelResp = null;
		EnterpriseUser enterpriseUser = null;
		Enterprises getEnterpriseResp = null;
		Optional<Enterprises> getEnterpriseElsResp = null;
		User getUserData = null;
		Optional<User> getCurrentUserDetails = null;
		EnterpriseUser getStoredenterpriseUser = null;
		EnterpriseUser UserResponse = null;

		try {
			// getCurrentUserDetails = userRepository.findById(1L);
			getCurrentUserDetails = getCurrentUserDetail(creationReqModel.getAuthToken());
			RecordLog.writeLogFile("getUserDetails " + getCurrentUserDetails);
			if (getCurrentUserDetails.isPresent() == false) {
				externalUserModelResp = new GetExternalUserModelResp();
				externalUserModelResp.setStatus(false);
				externalUserModelResp.setMessage("user not found");
				externalUserModelResp.setDescription("failed");
				externalUserModelResp.setRecordId(null);
			} else {
//				RecordLog.writeLogFile("creationReqModel.getPrefCorp() " + creationReqModel.getPrefCorp());
				getEnterpriseResp = enterprisesRepository.findByActiveAndPrefCorp(true, creationReqModel.getPrefCorp());
				if (getEnterpriseResp == null) {
					externalUserModelResp = new GetExternalUserModelResp();
					externalUserModelResp.setStatus(messages.isFailed());
					externalUserModelResp.setMessage("no record found in enterprise");
					externalUserModelResp.setDescription("failed");
					externalUserModelResp.setRecordId(null);
				} else {
//					RecordLog.writeLogFile("getEnterpriseResp " + getEnterpriseResp.toString());

					genRefNo = generateRefNumber();
					for (int i = 0; i < 3; i++) {
						isGenRefNoExist = enterpriseUserRepository.findByRefNo(genRefNo);
						if (isGenRefNoExist == null) {
							break;
						} else {
							genRefNo = generateRefNumber();
							isGenRefNoExist = enterpriseUserRepository.findByRefNo(genRefNo);
						}
					}

					RecordLog.writeLogFile("external user refNo " + genRefNo);

					enterpriseId = String.valueOf(getEnterpriseResp.getId());
					byte[] decodedBytes = Base64.getDecoder().decode(creationReqModel.getExFlag());
					viewPwd = new String(decodedBytes);
					String encryptedPswd = Base64.getEncoder()
							.encodeToString(creationReqModel.getPassword().getBytes());
					
					String viewPwdHash = passwordHashing.getPasswordHash(viewPwd);

//					RecordLog.writeLogFile("viewPwd " + viewPwd);
					enterpriseUser = new EnterpriseUser();
					enterpriseUser.setUserName(creationReqModel.getUserName());
					enterpriseUser.setRole(creationReqModel.getRole());
					enterpriseUser.setEnterpriseId(enterpriseId);
					//enterpriseUser.setPassword(encryptedPswd);
					enterpriseUser.setMobile(creationReqModel.getMobile());
					enterpriseUser.setRefNo(genRefNo);
					enterpriseUser.setPrefNo(creationReqModel.getPrefNo());
					enterpriseUser.setCreatedAt(new Timestamp(new Date().getTime()));
					enterpriseUser.setUpdatedAt(new Timestamp(new Date().getTime()));
					//enterpriseUser.setViewPwd(viewPwd);
					enterpriseUser.setViewPwdHash(viewPwdHash);
					enterpriseUser.setStatus("pending");
//					RecordLog.writeLogFile("enterpriseUser " + enterpriseUser);

					getStoredenterpriseUser = enterpriseUserRepository.save(enterpriseUser);
					if (getStoredenterpriseUser == null) {
						externalUserModelResp = new GetExternalUserModelResp();
						externalUserModelResp.setStatus(messages.isFailed());
						externalUserModelResp.setMessage("unable to store data , please try after sometime");
						externalUserModelResp.setDescription("failed");
						externalUserModelResp.setRecordId(null);
					} else {
						// getStoredenterpriseUser.setViewPwd("*******");
						UserResponse = getStoredenterpriseUser;
						if (commonExternalService.enterprise_sole_proprietorship(getCurrentUserDetails)
								|| commonExternalService
										.enterprise_zero_external_user_authorize(getCurrentUserDetails)) {
							makerCheckerService.maker(getCurrentUserDetails, "external user", getStoredenterpriseUser,
									0);
							makerCheckerService.checker(getCurrentUserDetails, getStoredenterpriseUser);

							String encryptedUsrPswd = Base64.getEncoder().encodeToString(viewPwd.getBytes());

							getStoredenterpriseUser.setStatus("approved");
							getStoredenterpriseUser.setUpdatedAt(new Timestamp(new Date().getTime()));

							EnterpriseUser updateStoredenterpriseUser = enterpriseUserRepository
									.save(getStoredenterpriseUser);

							User user = new User();
							user.setUserName(updateStoredenterpriseUser.getUserName());
							user.setMarkAsEnabled(true);
							user.setRole(updateStoredenterpriseUser.getRole());
							user.setEnterpriseId(updateStoredenterpriseUser.getEnterpriseId());
							// user.setPassword(null);
							user.setMobile(updateStoredenterpriseUser.getMobile());
							user.setPrefNo(updateStoredenterpriseUser.getPrefNo());
							//user.setViewPwd(viewPwd);
							user.setViewPwdHash(viewPwdHash);
							user.setEncryptedPassword(encryptedUsrPswd);
							user.setActivationStatus("activated");
							user.setCreatedAt(new Timestamp(new Date().getTime()));
							user.setUpdatedAt(new Timestamp(new Date().getTime()));
							user.setPasswordChangedAt(new Timestamp(new Date().getTime()));
							user.setSignInCount(0);
//							RecordLog.writeLogFile("user " + user);
							getUserData = userRepository.save(user);
//							RecordLog.writeLogFile("getUserData " + getUserData);

							// updateStoredenterpriseUser.setViewPwd("*******");
							UserResponse = updateStoredenterpriseUser;

							if (getUserData == null) {
								externalUserModelResp = new GetExternalUserModelResp();
								externalUserModelResp.setStatus(messages.isFailed());
								externalUserModelResp
										.setMessage("unable to store user data , please try after sometime");
								externalUserModelResp.setDescription("failed");
								externalUserModelResp.setRecordId(null);
							} else {
//								RecordLog.writeLogFile("getUserData.getMobile()" + getUserData.getMobile());
								if (getUserData.getMobile() == null || getUserData.getMobile().length() < 1) {
									externalUserModelResp = new GetExternalUserModelResp();
									externalUserModelResp.setStatus(messages.isFailed());
									externalUserModelResp.setMessage("unable to find mobile number for the user");
									externalUserModelResp.setDescription("failed");
									externalUserModelResp.setRecordId(null);
								} else {
									String gatewayMessage = String.format(messages.getEnterpriseUserWelcomeMessage(),
											getUserData.getPrefNo(), viewPwd);

//									RecordLog.writeLogFile("welcome message: "+gatewayMessage);
//									RecordLog.writeLogFile(getUserData.getMobile());
									/*
									 * ResponseEntity<?> smsResponse = fedExternalUserGatewayApiCall
									 * .getCustomerDetailsServiceCall(gatewayMessage, getUserData.getMobile());
									 * System.out.println("smsResponse" + smsResponse);
									 */
//									SendSmsReqModel sendSmsReqModel = new SendSmsReqModel();
//									sendSmsReqModel.setMessageText(gatewayMessage);
//									sendSmsReqModel.setMobileNo(getUserData.getMobile());
//									sendSmsReqModel
//											.setShortCode(String.valueOf(new Random().nextInt(9000000) + 1000000));
									
									
									
									JSONObject req = new JSONObject();

							        req.put("mobileno", getUserData.getMobile());
							        req.put("messageText",gatewayMessage );
							        req.put("shortcode", String.valueOf(new Random().nextInt(9000000) + 1000000));
									
							        HttpHeaders headers = new HttpHeaders();
									headers.setContentType(MediaType.APPLICATION_JSON);
							//headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
									HttpEntity<String> entity = new HttpEntity<String>(req.toString(),
											headers);
									

									ResponseEntity<?> smsResponse = restTemplate.postForEntity(
											fblgatewayurl + "/fblgateway_v1/sendmsgtouser", entity,
											String.class);
									if (smsResponse.getStatusCodeValue() == 200) {
										RecordIdExtUser recordIdResp = new RecordIdExtUser();
										getUserData.setViewPwd("*********");

										recordIdResp.setStatus("success");
										recordIdResp.setRefNo(UserResponse.getRefNo());
										recordIdResp.setExternalUser(getUserData);
										recordIdResp.setExternalEnterpriseUser(null);

										externalUserModelResp = new GetExternalUserModelResp();
										externalUserModelResp.setStatus(messages.isSuccess());
										externalUserModelResp.setMessage(
												"SMS has been send to mobile number " + getUserData.getMobile());
										externalUserModelResp.setDescription("success");
										externalUserModelResp.setRecordId(recordIdResp);

									} else {
										externalUserModelResp = new GetExternalUserModelResp();
										externalUserModelResp.setStatus(messages.isSuccess());
										externalUserModelResp.setMessage("unable to send SMS");
										externalUserModelResp.setDescription("failed");
										externalUserModelResp.setRecordId(null);
									}
								}
							}
						} else {
							getEnterpriseElsResp = enterprisesRepository
									.findById(Long.valueOf(getCurrentUserDetails.get().getEnterpriseId()));
//							RecordLog.writeLogFile("getCurrentUserDetails.get().getEnterpriseId()) "
//									+ getCurrentUserDetails.get().getEnterpriseId());
							if (getEnterpriseElsResp.isPresent()) {
								ApplicationEnterpris applicationEnterpris=applicationEnterprisRepository.findByApplicationFormIdAndPrefCorpIgnoreCase(getEnterpriseElsResp.get().getApplicationFormId(),getEnterpriseElsResp.get().getPrefCorp());
								String authExt = applicationEnterpris.getAuthExt();
								if (authExt == null) {
									authExt = "0";
								}

							String makerStatus =makerCheckerService.maker(getCurrentUserDetails, "external user",
										getStoredenterpriseUser, Integer.valueOf(authExt));
								
                             if(makerStatus.equals("success")) {
									
									String gatewayMessage = String.format(messages.getEnterpriseUserWelcomeMessagePending(),
											getStoredenterpriseUser.getPrefNo(),viewPwd);
									
									
									JSONObject req = new JSONObject();

							        req.put("mobileno", getStoredenterpriseUser.getMobile());
							        req.put("messageText",gatewayMessage );
							        req.put("shortcode", String.valueOf(new SecureRandom().nextInt(9000000) + 1000000));
									
							        HttpHeaders headers = new HttpHeaders();
									headers.setContentType(MediaType.APPLICATION_JSON);
									HttpEntity<String> entity = new HttpEntity<String>(req.toString(),
											headers);
									

									ResponseEntity<?> smsResponse = restTemplate.postForEntity(
											fblgatewayurl + "/fblgateway_v1/sendmsgtouser", entity,
											String.class);
									if (smsResponse.getStatusCodeValue() == 200) {
										RecordLog.writeLogFile("SMS has been sent After External User Creation which is Pending for Mobile Number: "+getStoredenterpriseUser.getMobile());
									}
									
									
									
								}


								getStoredenterpriseUser.setViewPwd("***********");

								RecordIdExtUser recordIdResp = new RecordIdExtUser();
								recordIdResp.setStatus("initiated");
								recordIdResp.setRefNo(getStoredenterpriseUser.getRefNo());
								recordIdResp.setExternalEnterpriseUser(getStoredenterpriseUser);
								recordIdResp.setExternalUser(null);

								externalUserModelResp = new GetExternalUserModelResp();
								externalUserModelResp.setStatus(messages.isSuccess());
								externalUserModelResp.setMessage("EnterpriseUser is saved & initiated");
								externalUserModelResp.setDescription("");
								externalUserModelResp.setRecordId(recordIdResp);

							} else {
								externalUserModelResp = new GetExternalUserModelResp();
								externalUserModelResp.setStatus(messages.isFailed());
								externalUserModelResp.setMessage("Enterprise data not found");
								externalUserModelResp.setDescription("failed");
								externalUserModelResp.setRecordId(null);
							}
						}
					}
				}
			}
		} catch (Exception e) {
			RecordLog.writeLogFile("Exception occured at: "+Arrays.toString(e.getStackTrace())+" Exception name: "+e.getMessage());
			externalUserModelResp = new GetExternalUserModelResp();
			externalUserModelResp.setStatus(messages.isFailed());
			externalUserModelResp.setMessage("Error occured in server , please try after sometime");
			externalUserModelResp.setDescription("failed");
			externalUserModelResp.setRecordId(null);
			e.printStackTrace();
		}
		return externalUserModelResp;
	}

	public GetExternalUserModelResp approveExtUser(String refNo, String authToken) {
		EnterpriseUser enterpriseUser = null;
		EnterpriseUser enterpriseUserResp = null;
		GetExternalUserModelResp externalUserModelResp = null;
		User userResp = null;
		String checkerResp = null;
		Optional<User> getCurrentUserDetails = null;
		String viewPwdHash = null;

		RecordLog.writeLogFile("enterpriseUser refNo " + refNo);
		enterpriseUser = enterpriseUserRepository.findByRefNo(refNo);
		if (enterpriseUser == null) {
//			RecordLog.writeLogFile("enterpriseUser null");
			externalUserModelResp = new GetExternalUserModelResp();
			externalUserModelResp.setStatus(messages.isFailed());
			externalUserModelResp.setMessage("enterprise User is null || empty");
			externalUserModelResp.setDescription("failed");
			externalUserModelResp.setRecordId(null);
		} else {
//			RecordLog.writeLogFile("enterpriseUser " + enterpriseUser.toString());
			getCurrentUserDetails = getCurrentUserDetail(authToken);
			checkerResp = makerCheckerService.checker(getCurrentUserDetails, enterpriseUser);
			RecordLog.writeLogFile("checker response: " + checkerResp);
			if (checkerResp == "success") {
				enterpriseUser.setStatus("approved");
				enterpriseUserResp = enterpriseUserRepository.save(enterpriseUser);
				if (enterpriseUserResp == null) {
					externalUserModelResp = new GetExternalUserModelResp();
					externalUserModelResp.setStatus(messages.isSuccess());
					externalUserModelResp.setMessage("success");
					externalUserModelResp.setDescription("success");
					externalUserModelResp.setRecordId(null);
				} else {
					viewPwdHash = enterpriseUserResp.getViewPwdHash();
					User user = new User();
					user.setUserName(enterpriseUserResp.getUserName());
					user.setMarkAsEnabled(true);
					user.setRole(enterpriseUserResp.getRole());
					user.setEnterpriseId(enterpriseUserResp.getEnterpriseId());
					//user.setPassword(enterpriseUserResp.getPassword());
					user.setMobile(enterpriseUserResp.getMobile());
					user.setPrefNo(enterpriseUserResp.getPrefNo());
					//user.setViewPwd(enterpriseUserResp.getViewPwd());
					user.setViewPwdHash(viewPwdHash);
					user.setCreatedAt(new Timestamp(new Date().getTime()));
					user.setUpdatedAt(new Timestamp(new Date().getTime()));
					user.setPasswordChangedAt(new Timestamp(new Date().getTime()));
					//user.setEncryptedPassword(enterpriseUserResp.getPassword());
					user.setActivationStatus("activated");
					userResp = userRepository.save(user);
					if (userResp == null) {
						externalUserModelResp = new GetExternalUserModelResp();
						externalUserModelResp.setStatus(messages.isFailed());
						externalUserModelResp.setMessage("used data is null || empty");
						externalUserModelResp.setDescription("failed");
						externalUserModelResp.setRecordId(null);
					} else {
						if (userResp.getMobile() == null || userResp.getMobile().length() < 1) {
							externalUserModelResp = new GetExternalUserModelResp();
							externalUserModelResp.setStatus(messages.isFailed());
							externalUserModelResp.setMessage("mobile number is null || empty");
							externalUserModelResp.setDescription("failed");
							externalUserModelResp.setRecordId(null);
						} else {
							// sms gateway api call
							String gatewayMessage = String.format(messages.getEnterpriseUserWelcomeMessageApprove());

//							SendSmsReqModel sendSmsReqModel = new SendSmsReqModel();
//							sendSmsReqModel.setMessageText(gatewayMessage);
//							sendSmsReqModel.setMobileNo(userResp.getMobile());
//							sendSmsReqModel.setShortCode(String.valueOf(new Random().nextInt(9000000) + 1000000));
							
							
							
							
							JSONObject req = new JSONObject();

					        req.put("mobileno", userResp.getMobile());
					        req.put("messageText",gatewayMessage );
					        req.put("shortcode", String.valueOf(new Random().nextInt(9000000) + 1000000));
							
					        HttpHeaders headers = new HttpHeaders();
							headers.setContentType(MediaType.APPLICATION_JSON);
					//headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
							HttpEntity<String> entity = new HttpEntity<String>(req.toString(),
									headers);

							ResponseEntity<?> smsResponse = restTemplate.postForEntity(
									fblgatewayurl + "/fblgateway_v1/sendmsgtouser", entity, String.class);
							if (smsResponse.getStatusCodeValue() == 200) {
								externalUserModelResp = new GetExternalUserModelResp();
								externalUserModelResp.setStatus(messages.isSuccess());
								externalUserModelResp
										.setMessage("SMS has been send to mobile number " + userResp.getMobile());
								externalUserModelResp.setDescription("success");
								externalUserModelResp.setRecordId(null);
							} else {
								externalUserModelResp = new GetExternalUserModelResp();
								externalUserModelResp.setStatus(messages.isSuccess());
								externalUserModelResp.setMessage("unable to send SMS");
								externalUserModelResp.setDescription("failed");
								externalUserModelResp.setRecordId(null);
							}
						}
					}
				}
			} else if (checkerResp == "signed") {
				externalUserModelResp = new GetExternalUserModelResp();
				externalUserModelResp.setStatus(messages.isSuccess());
				externalUserModelResp.setMessage("signed ");
				externalUserModelResp.setDescription("success");
				externalUserModelResp.setRecordId(null);
			} else if (checkerResp == "already_approved") {
				externalUserModelResp = new GetExternalUserModelResp();
				externalUserModelResp.setStatus(messages.isFailed());
				externalUserModelResp.setMessage("already_approved");
				externalUserModelResp.setDescription("failed");
				externalUserModelResp.setRecordId(null);
			} else {
				externalUserModelResp = new GetExternalUserModelResp();
				externalUserModelResp.setStatus(messages.isFailed());
				externalUserModelResp.setMessage("already success , signed , already_approved");
				externalUserModelResp.setDescription("failed");
				externalUserModelResp.setRecordId(null);
			}
		}
		return externalUserModelResp;
	}

	public GetExternalUserModelResp rejectExtUser(String refNo, String authToken) {
		EnterpriseUser enterpriseUser = null;
		EnterpriseUser enterpriseUserResp = null;
		String makerCheckerResp = null;
		GetExternalUserModelResp externalUserModelResp = null;
		Optional<User> user = null;
		try {
			enterpriseUser = enterpriseUserRepository.findByRefNo(refNo);
//			RecordLog.writeLogFile("enterpriseUser " + enterpriseUser);
			if (enterpriseUser == null) {
				externalUserModelResp = new GetExternalUserModelResp();
				externalUserModelResp.setStatus(messages.isFailed());
				externalUserModelResp.setMessage(messages.getInvalident());
				externalUserModelResp.setDescription("Invalid pref_corp number is passed.");
				externalUserModelResp.setRecordId(null);
			} else {
				user = getCurrentUserDetail(authToken);
				if (user.isPresent()) {
					makerCheckerResp = makerCheckerService.reject(user.get(), enterpriseUser);
					RecordLog.writeLogFile("maker checker response: " + makerCheckerResp);
					if (makerCheckerResp == "success" || makerCheckerResp == "signed") {
						enterpriseUser.setStatus("rejected");
						enterpriseUserResp = enterpriseUserRepository.save(enterpriseUser);
						enterpriseUserResp.setViewPwd("********");

						externalUserModelResp = new GetExternalUserModelResp();
						externalUserModelResp.setStatus(messages.isSuccess());
						externalUserModelResp.setMessage(messages.getSuccessmsg());
						externalUserModelResp.setDescription("");
						externalUserModelResp.setRecordId(null);
					} else if (makerCheckerResp == "already_approved") {
						externalUserModelResp = new GetExternalUserModelResp();
						externalUserModelResp.setStatus(messages.isFailed());
						externalUserModelResp.setMessage("already_approved");
						externalUserModelResp.setDescription("");
						externalUserModelResp.setRecordId(null);
					} else {
						externalUserModelResp = new GetExternalUserModelResp();
						externalUserModelResp.setStatus(messages.isFailed());
						externalUserModelResp.setMessage("status is in pending | new | pending_approval");
						externalUserModelResp.setDescription("");
						externalUserModelResp.setRecordId(null);
					}
				} else {
					externalUserModelResp = new GetExternalUserModelResp();
					externalUserModelResp.setStatus(messages.isFailed());
					externalUserModelResp.setMessage("ubable to fetch live user data");
					externalUserModelResp.setDescription("");
					externalUserModelResp.setRecordId(null);
				}
			}
		} catch (Exception e) {
			RecordLog.writeLogFile("Exception occured at: "+Arrays.toString(e.getStackTrace())+" Exception name: "+e.getMessage());
			e.printStackTrace();
			externalUserModelResp = new GetExternalUserModelResp();
			externalUserModelResp.setStatus(messages.isFailed());
			externalUserModelResp.setMessage(messages.getExceptionerrmsg());
			externalUserModelResp.setDescription("");
			externalUserModelResp.setRecordId(null);
		}
		return externalUserModelResp;
	}

	public GetExternalUserModelResp getExtUserDetails(String refNo) {
		EnterpriseUser enterpriseUser = null;
		GetExternalUserModelResp externalUserModelResp = null;
		try {
			enterpriseUser = enterpriseUserRepository.findByRefNo(refNo);
//			RecordLog.writeLogFile("enterpriseUser " + enterpriseUser);
			if (enterpriseUser == null) {
				externalUserModelResp = new GetExternalUserModelResp();
				externalUserModelResp.setStatus(messages.isFailed());
				externalUserModelResp.setMessage("enterprise user data is empty");
				externalUserModelResp.setDescription("");
				externalUserModelResp.setRecordId(null);
			} else {
				RecordIdExtUser recordIdResp = new RecordIdExtUser();
				recordIdResp.setStatus("");
				recordIdResp.setRefNo("");
				recordIdResp.setExternalEnterpriseUser(enterpriseUser);
				recordIdResp.setExternalUser(null);

				externalUserModelResp = new GetExternalUserModelResp();
				externalUserModelResp.setStatus(messages.isSuccess());
				externalUserModelResp.setMessage(messages.getSuccessmsg());
				externalUserModelResp.setDescription("");
				externalUserModelResp.setRecordId(recordIdResp);
			}
		} catch (Exception e) {
			RecordLog.writeLogFile("Exception occured at: "+Arrays.toString(e.getStackTrace())+" Exception name: "+e.getMessage());
			e.printStackTrace();
			externalUserModelResp = new GetExternalUserModelResp();
			externalUserModelResp.setStatus(messages.isFailed());
			externalUserModelResp.setMessage("Error getting external user details " + e.getLocalizedMessage());
			externalUserModelResp.setDescription("");
			externalUserModelResp.setRecordId(null);
		}
		return externalUserModelResp;
	}

	public GetExternalUserModelResp destroyUserDetailsByPrefNo(ExternalUserCommonReq externalUserCommonReq) {
		Enterprises enterprises = null;
		EnterpriseUser enterpriseUser = null;
		User user = null;
		GetExternalUserModelResp externalUserModelResp = null;

		try {
			enterprises = enterprisesRepository.findByPrefCorpAndActive(externalUserCommonReq.getPrefCorpNo(),true);
			if (enterprises == null) {
//				RecordLog.writeLogFile("Enterprise not found");
				externalUserModelResp = new GetExternalUserModelResp();
				externalUserModelResp.setStatus(messages.isFailed());
				externalUserModelResp.setMessage("Invalid Enterprise");
				externalUserModelResp.setDescription("");
				externalUserModelResp.setRecordId(null);
			} else {
//				RecordLog.writeLogFile("Enterprise : " + enterprises.getPrefCorp());
				// Modified status from modified to rejected.
				List<String> status=Arrays.asList("deleted","rejected");
				enterpriseUser = enterpriseUserRepository.findByPrefNoAndStatusNotInAndEnterpriseId(
						externalUserCommonReq.getPrefNo(), status , String.valueOf(enterprises.getId()));
				enterpriseUser.setStatus("deleted");
				enterpriseUserRepository.save(enterpriseUser);
				user = userRepository.findByRoleAndPrefNoAndMarkAsEnabled("external", externalUserCommonReq.getPrefNo(),
						true);
				if (user == null) {
//					RecordLog.writeLogFile("user not found");
					externalUserModelResp = new GetExternalUserModelResp();
					externalUserModelResp.setStatus(messages.isFailed());
					externalUserModelResp.setMessage("User not found or may be disabled");
					externalUserModelResp.setDescription("");
					externalUserModelResp.setRecordId(null);
				} else {
					RecordLog.writeLogFile("user updated as enabled");
					user.setMarkAsEnabled(false);
					userRepository.save(user);
					externalUserModelResp = new GetExternalUserModelResp();
					externalUserModelResp.setStatus(messages.isSuccess());
					externalUserModelResp.setMessage("user is updated");
					externalUserModelResp.setDescription("");
					externalUserModelResp.setRecordId(null);
				}
				externalUserModelResp = new GetExternalUserModelResp();
				externalUserModelResp.setStatus(messages.isSuccess());
				externalUserModelResp.setMessage("Enterprise User deleted");
				externalUserModelResp.setDescription("");
				externalUserModelResp.setRecordId(null);
			}
		} catch (IncorrectResultSizeDataAccessException ex) {
			log4j.error(ex);
			externalUserModelResp = new GetExternalUserModelResp();
			externalUserModelResp.setStatus(messages.isFailed());
			externalUserModelResp.setMessage("Duplicate Username in ENTERPRISE_USERS.");
			externalUserModelResp.setDescription("Duplicate Username in ENTERPRISE_USERS.");
			externalUserModelResp.setRecordId(null);
		} catch (Exception e) {
			RecordLog.writeLogFile("Exception occured at: "+Arrays.toString(e.getStackTrace())+" Exception name: "+e.getMessage());
			log4j.error(e);
			externalUserModelResp = new GetExternalUserModelResp();
			externalUserModelResp.setStatus(messages.isFailed());
			externalUserModelResp.setMessage("Error deleting enterprise user " + e.getMessage());
			externalUserModelResp.setDescription(e.getMessage());
			externalUserModelResp.setRecordId(null);
		}
		return externalUserModelResp;
	}

	public GetExternalUserModelResp getPendingExternalSpecific(String prefCorpNo, String authToken) {
		Enterprises enterprises;
		List<EnterpriseUser> enterpriseUsers;
		MakerCheckerListModel makerCheckerListModel;
		List<MakerCheckerListModel> getMakerCheckerListModel = null;
		GetExternalUserModelResp externalUserModelResp = null;
		Optional<User> user = null;

		try {
			enterprises = enterprisesRepository.findByPrefCorpAndActive(prefCorpNo,true);
//			RecordLog.writeLogFile("enterprises " + enterprises);
			if (enterprises == null) {
				externalUserModelResp = new GetExternalUserModelResp();
				externalUserModelResp.setStatus(messages.isFailed());
				externalUserModelResp.setMessage(messages.getInvalident());
				externalUserModelResp.setDescription("Invalid pref_corp number is passed.");
				externalUserModelResp.setRecordId(null);
			} else {
				enterpriseUsers = enterpriseUserRepository.findByStatusAndEnterpriseIdOrderByCreatedAt("pending",
						String.valueOf(enterprises.getId()));
				if (enterpriseUsers == null) {
					externalUserModelResp = new GetExternalUserModelResp();
					externalUserModelResp.setStatus(messages.isFailed());
					externalUserModelResp.setMessage(messages.getInvalident());
					externalUserModelResp.setDescription("Invalid pref_corp number is passed.");
					externalUserModelResp.setRecordId(null);
				} else if (enterpriseUsers.size() > 0) {
					getMakerCheckerListModel = new ArrayList<MakerCheckerListModel>();
					for (int i = 0; i < enterpriseUsers.size(); i++) {
						user = getCurrentUserDetail(authToken);
						if (user.isPresent()) {
							makerCheckerListModel = makerCheckerService.makerCheckerList_v1(user,
									enterpriseUsers.get(i));
							if (makerCheckerListModel.isUserNull() == true) {
								externalUserModelResp = new GetExternalUserModelResp();
								externalUserModelResp.setStatus(messages.isFailed());
								externalUserModelResp.setMessage("shared authtoken user's role is null");
								externalUserModelResp.setDescription("");
								externalUserModelResp.setRecordId(null);
								return externalUserModelResp;
							} else {
								if (makerCheckerListModel.isOperationNull() == false) {
									getMakerCheckerListModel.add(makerCheckerListModel);
								}
							}
						} else {
							externalUserModelResp = new GetExternalUserModelResp();
							externalUserModelResp.setStatus(messages.isFailed());
							externalUserModelResp.setMessage(messages.getUserNotFound());
							externalUserModelResp.setDescription(messages.getUsernotfoundmsg());
							externalUserModelResp.setRecordId(null);
							return externalUserModelResp;
						}
					}
				}
				if (getMakerCheckerListModel != null) {
					if (getMakerCheckerListModel.size() != 0) {
						Collections.reverse(getMakerCheckerListModel);

						RecordIdExtUser extUser = new RecordIdExtUser();
						extUser.setGetMakerCheckerListModel(getMakerCheckerListModel);

						externalUserModelResp = new GetExternalUserModelResp();
						externalUserModelResp.setStatus(messages.isSuccess());
						externalUserModelResp.setMessage(messages.getSuccessmsg());
						externalUserModelResp.setDescription("");
						externalUserModelResp.setRecordId(extUser);
					} else {
						externalUserModelResp = new GetExternalUserModelResp();
						externalUserModelResp.setStatus(messages.isFailed());
						externalUserModelResp.setMessage("operation id is null or no record found");
						externalUserModelResp.setDescription("");
						externalUserModelResp.setRecordId(null);
					}
				} else {
					externalUserModelResp = new GetExternalUserModelResp();
					externalUserModelResp.setStatus(messages.isFailed());
					externalUserModelResp.setMessage("No Pending External Users");
					externalUserModelResp.setDescription("");
					externalUserModelResp.setRecordId(null);
				}
			}
		} catch (Exception e) {
			RecordLog.writeLogFile("Exception occured at: "+Arrays.toString(e.getStackTrace())+" Exception name: "+e.getMessage());
			e.printStackTrace();
			externalUserModelResp = new GetExternalUserModelResp();
			externalUserModelResp.setStatus(messages.isFailed());
			externalUserModelResp.setMessage("Error getting pending external users");
			externalUserModelResp.setDescription("Error getting pending external users");
			externalUserModelResp.setRecordId(null);
		}
		return externalUserModelResp;
	}

	public GetExternalUserModelResp getApprovedExternalSpecific(String prefNo, String authToken) {
		Enterprises enterprises = null;
		List<EnterpriseUser> enterpriseUsers = null;
		List<MakerCheckerListModel> getMakerCheckerListModel = null;
		MakerCheckerListModel makerCheckerListModel;
		GetExternalUserModelResp externalUserModelResp = null;
		Optional<User> user = null;

		try {
			enterprises = enterprisesRepository.findByPrefCorpAndActive(prefNo,true);
			if (enterprises == null) {
				externalUserModelResp = new GetExternalUserModelResp();
				externalUserModelResp.setStatus(messages.isFailed());
				externalUserModelResp.setMessage("enterprises data is null || empty");
				externalUserModelResp.setDescription("No data found");
				externalUserModelResp.setRecordId(null);
			} else {
				enterpriseUsers = enterpriseUserRepository.findByStatusAndEnterpriseIdOrderByUpdatedAt("approved",
						String.valueOf(enterprises.getId()));
				if (enterpriseUsers == null) {
					externalUserModelResp = new GetExternalUserModelResp();
					externalUserModelResp.setStatus(messages.isFailed());
					externalUserModelResp.setMessage(messages.getInvalident());
					externalUserModelResp.setDescription("Invalid pref_corp number is passed.");
					externalUserModelResp.setRecordId(null);
				} else if (enterpriseUsers.size() > 0) {
					getMakerCheckerListModel = new ArrayList<MakerCheckerListModel>();
					for (int i = 0; i < enterpriseUsers.size(); i++) {
						user = getCurrentUserDetail(authToken);
						if (user.isPresent()) {
							makerCheckerListModel = makerCheckerService.makerCheckerList(user.get(),
									enterpriseUsers.get(i));
							if (makerCheckerListModel.isUserNull() == true) {
								externalUserModelResp = new GetExternalUserModelResp();
								externalUserModelResp.setStatus(messages.isFailed());
								externalUserModelResp.setMessage("shared authtoken user's role is null");
								externalUserModelResp.setDescription("");
								externalUserModelResp.setRecordId(null);
								return externalUserModelResp;
							} else {
								if (makerCheckerListModel.isOperationNull() == false) {
									getMakerCheckerListModel.add(makerCheckerListModel);
								}
							}
						} else {
							externalUserModelResp = new GetExternalUserModelResp();
							externalUserModelResp.setStatus(messages.isFailed());
							externalUserModelResp.setMessage(messages.getUserNotFound());
							externalUserModelResp.setDescription(messages.getUsernotfoundmsg());
							externalUserModelResp.setRecordId(null);
							return externalUserModelResp;
						}
					}
				}
				if (getMakerCheckerListModel != null) {
					if (getMakerCheckerListModel.size() != 0) {
						Collections.reverse(getMakerCheckerListModel);

						RecordIdExtUser extUser = new RecordIdExtUser();
						extUser.setGetMakerCheckerListModel(getMakerCheckerListModel);

						externalUserModelResp = new GetExternalUserModelResp();
						externalUserModelResp.setStatus(messages.isSuccess());
						externalUserModelResp.setMessage(messages.getSuccessmsg());
						externalUserModelResp.setDescription("");
						externalUserModelResp.setRecordId(extUser);
					} else {
						externalUserModelResp = new GetExternalUserModelResp();
						externalUserModelResp.setStatus(messages.isFailed());
						externalUserModelResp.setMessage("Error getting maker checker details");
						externalUserModelResp.setDescription("Operation id is null or no record found");
						externalUserModelResp.setRecordId(null);
					}
				} else {
					externalUserModelResp = new GetExternalUserModelResp();
					externalUserModelResp.setStatus(messages.isFailed());
					externalUserModelResp.setMessage("Error getting maker checker details");
					externalUserModelResp.setDescription("No record found");
					externalUserModelResp.setRecordId(null);
				}
			}
		} catch (Exception e) {
			RecordLog.writeLogFile("Exception occured at: "+Arrays.toString(e.getStackTrace())+" Exception name: "+e.getMessage());
			e.printStackTrace();
			externalUserModelResp = new GetExternalUserModelResp();
			externalUserModelResp.setStatus(messages.isFailed());
			externalUserModelResp.setMessage("Error getting approved list of external users");
			externalUserModelResp.setDescription("Error getting approved list of external users" + e.getMessage());
			externalUserModelResp.setRecordId(null);
		}
		return externalUserModelResp;
	}

	public GetExternalUserModelResp getPendingExternalUsers(String prefCorpNo, String authToken) {
		Enterprises enterprises = null;
		List<EnterpriseUser> enterpriseUsers = null;
		List<MakerCheckerListModel> getMakerCheckerListModel = null;
		MakerCheckerListModel makerCheckerListModel = null;
		GetExternalUserModelResp externalUserModelResp = null;
		Optional<User> user = null;

		try {
			enterprises = enterprisesRepository.findByPrefCorpAndActive(prefCorpNo,true);
			if (enterprises == null) {
				externalUserModelResp = new GetExternalUserModelResp();
				externalUserModelResp.setStatus(messages.isFailed());
				externalUserModelResp.setMessage("enterprises data is null || empty");
				externalUserModelResp.setDescription("");
				externalUserModelResp.setRecordId(null);
			} else {
				enterpriseUsers = enterpriseUserRepository.findByStatusAndEnterpriseIdOrderByUpdatedAt("pending",
						String.valueOf(enterprises.getId()));
				if (enterpriseUsers == null) {
					externalUserModelResp = new GetExternalUserModelResp();
					externalUserModelResp.setStatus(messages.isFailed());
					externalUserModelResp.setMessage(messages.getInvalident());
					externalUserModelResp.setDescription("Invalid pref_corp number is passed.");
					externalUserModelResp.setRecordId(null);
				} else if (enterpriseUsers.size() > 0) {
					getMakerCheckerListModel = new ArrayList<MakerCheckerListModel>();
					for (int i = 0; i < enterpriseUsers.size(); i++) {
						user = getCurrentUserDetail(authToken);
						if (user.isPresent()) {
							makerCheckerListModel = makerCheckerService.makerCheckerList(user.get(),
									enterpriseUsers.get(i));
							if (makerCheckerListModel.isUserNull() == true) {
								externalUserModelResp = new GetExternalUserModelResp();
								externalUserModelResp.setStatus(messages.isFailed());
								externalUserModelResp.setMessage("shared authtoken user's role is null");
								externalUserModelResp.setDescription("");
								externalUserModelResp.setRecordId(null);
								return externalUserModelResp;
							} else {
								if (makerCheckerListModel.isOperationNull() == false) {
									getMakerCheckerListModel.add(makerCheckerListModel);
								}
							}
						} else {
							externalUserModelResp = new GetExternalUserModelResp();
							externalUserModelResp.setStatus(messages.isFailed());
							externalUserModelResp.setMessage("user not found");
							externalUserModelResp.setDescription("");
							externalUserModelResp.setRecordId(null);
							return externalUserModelResp;
						}
					}
				}
				if (getMakerCheckerListModel != null) {
					if (getMakerCheckerListModel.size() != 0) {
						Collections.reverse(getMakerCheckerListModel);

						RecordIdExtUser extUser = new RecordIdExtUser();
						extUser.setGetMakerCheckerListModel(getMakerCheckerListModel);

						externalUserModelResp = new GetExternalUserModelResp();
						externalUserModelResp.setStatus(messages.isSuccess());
						externalUserModelResp.setMessage(messages.getSuccessmsg());
						externalUserModelResp.setDescription("");
						externalUserModelResp.setRecordId(extUser);
					} else {
						externalUserModelResp = new GetExternalUserModelResp();
						externalUserModelResp.setStatus(messages.isFailed());
						externalUserModelResp.setMessage("Error getting maker checker list");
						externalUserModelResp.setDescription("operation id is null or no record found");
						externalUserModelResp.setRecordId(null);
					}
				} else {
					externalUserModelResp = new GetExternalUserModelResp();
					externalUserModelResp.setStatus(messages.isFailed());
					externalUserModelResp.setMessage(messages.getUserNotFound());
					externalUserModelResp.setDescription(messages.getUsernotfoundmsg());
					externalUserModelResp.setRecordId(null);
				}
			}
		} catch (Exception e) {
			RecordLog.writeLogFile("Exception occured at: "+Arrays.toString(e.getStackTrace())+" Exception name: "+e.getMessage());
			e.printStackTrace();
			externalUserModelResp = new GetExternalUserModelResp();
			externalUserModelResp.setStatus(messages.isFailed());
			externalUserModelResp.setMessage("Error getting pending list of external users");
			externalUserModelResp.setDescription("Error getting pending list of external users" + e.getMessage());
			externalUserModelResp.setRecordId(null);
		}
		return externalUserModelResp;
	}

	public GetExternalUserModelResp getApprovedExternalUsers(String prefCorpNo, String authToken) {
		Enterprises enterprises = null;
		List<EnterpriseUser> enterpriseUsers = null;
		List<MakerCheckerListModel> getMakerCheckerListModel = null;
		MakerCheckerListModel makerCheckerListModel = null;
		GetExternalUserModelResp externalUserModelResp = null;
		Optional<User> user = null;

		try {
			enterprises = enterprisesRepository.findByPrefCorpAndActive(prefCorpNo,true);
			if (enterprises == null) {
				externalUserModelResp = new GetExternalUserModelResp();
				externalUserModelResp.setStatus(messages.isFailed());
				externalUserModelResp.setMessage("enterprises data is null || empty");
				externalUserModelResp.setDescription("");
				externalUserModelResp.setRecordId(null);
			} else {
				enterpriseUsers = enterpriseUserRepository.findByStatusAndEnterpriseIdOrderByUpdatedAt("approved",
						String.valueOf(enterprises.getId()));
				if (enterpriseUsers == null) {
					externalUserModelResp = new GetExternalUserModelResp();
					externalUserModelResp.setStatus(messages.isFailed());
					externalUserModelResp.setMessage(messages.getInvalident());
					externalUserModelResp.setDescription("Invalid pref_corp number is passed.");
					externalUserModelResp.setRecordId(null);
				} else if (enterpriseUsers.size() > 0) {
					getMakerCheckerListModel = new ArrayList<MakerCheckerListModel>();
					for (int i = 0; i < enterpriseUsers.size(); i++) {
						user = getCurrentUserDetail(authToken);
						if (user.isPresent()) {
							makerCheckerListModel = makerCheckerService.makerCheckerList(user.get(),
									enterpriseUsers.get(i));
							if (makerCheckerListModel.isUserNull() == true) {
								externalUserModelResp = new GetExternalUserModelResp();
								externalUserModelResp.setStatus(messages.isFailed());
								externalUserModelResp.setMessage("shared authtoken user's role is null");
								externalUserModelResp.setDescription("");
								externalUserModelResp.setRecordId(null);
								return externalUserModelResp;
							} else {
								if (makerCheckerListModel.isOperationNull() == false) {
									getMakerCheckerListModel.add(makerCheckerListModel);
								}
							}
						} else {
							externalUserModelResp = new GetExternalUserModelResp();
							externalUserModelResp.setStatus(messages.isFailed());
							externalUserModelResp.setMessage(messages.getUserNotFound());
							externalUserModelResp.setDescription(messages.getUsernotfoundmsg());
							externalUserModelResp.setRecordId(null);
							return externalUserModelResp;
						}
					}
				}
				if (getMakerCheckerListModel != null) {
					if (getMakerCheckerListModel.size() != 0) {
						Collections.reverse(getMakerCheckerListModel);

						RecordIdExtUser extUser = new RecordIdExtUser();
						extUser.setGetMakerCheckerListModel(getMakerCheckerListModel);

						externalUserModelResp = new GetExternalUserModelResp();
						externalUserModelResp.setStatus(messages.isSuccess());
						externalUserModelResp.setMessage(messages.getSuccessmsg());
						externalUserModelResp.setDescription("");
						externalUserModelResp.setRecordId(extUser);
					} else {
						externalUserModelResp = new GetExternalUserModelResp();
						externalUserModelResp.setStatus(messages.isFailed());
						externalUserModelResp.setMessage("Error getting maker checker list");
						externalUserModelResp.setDescription("operation id is null or no record found");
						externalUserModelResp.setRecordId(null);
					}
				} else {
					externalUserModelResp = new GetExternalUserModelResp();
					externalUserModelResp.setStatus(messages.isFailed());
					externalUserModelResp.setMessage("Error getting maker checker list");
					externalUserModelResp.setDescription("");
					externalUserModelResp.setRecordId(null);
				}
			}
		} catch (Exception e) {
			RecordLog.writeLogFile("Exception occured at: "+Arrays.toString(e.getStackTrace())+" Exception name: "+e.getMessage());
			e.printStackTrace();
			externalUserModelResp = new GetExternalUserModelResp();
			externalUserModelResp.setStatus(messages.isFailed());
			externalUserModelResp.setMessage("Error getting approved list of external users");
			externalUserModelResp.setDescription("Error getting approved list of external users" + e.getMessage());
			externalUserModelResp.setRecordId(null);
		}
		return externalUserModelResp;
	}

	public GetExternalUserModelResp getRejectedExternalSpecific(String prefCorpNo, String authToken) {
		Enterprises enterprises = null;
		List<EnterpriseUser> enterpriseUsers = null;
		List<MakerCheckerListModel> getMakerCheckerListModel = null;
		MakerCheckerListModel makerCheckerListModel = null;
		GetExternalUserModelResp externalUserModelResp = null;
		Optional<User> user = null;

		try {
			enterprises = enterprisesRepository.findByPrefCorpAndActive(prefCorpNo,true);
			if (enterprises == null) {
				externalUserModelResp = new GetExternalUserModelResp();
				externalUserModelResp.setStatus(messages.isFailed());
				externalUserModelResp.setMessage("enterprises data is null || empty");
				externalUserModelResp.setDescription("");
				externalUserModelResp.setRecordId(null);
			} else {
//				RecordLog.writeLogFile("enterprises.getId() " + enterprises.getId());
				enterpriseUsers = enterpriseUserRepository.findByStatusAndEnterpriseIdOrderByUpdatedAt("rejected",
						String.valueOf(enterprises.getId()));
				if (enterpriseUsers == null) {
					externalUserModelResp = new GetExternalUserModelResp();
					externalUserModelResp.setStatus(messages.isFailed());
					externalUserModelResp.setMessage(messages.getInvalident());
					externalUserModelResp.setDescription("Invalid pref_corp number is passed.");
					externalUserModelResp.setRecordId(null);
				} else if (enterpriseUsers.size() > 0) {
//					RecordLog.writeLogFile("enterpriseUsers.size()" + enterpriseUsers.size());
					getMakerCheckerListModel = new ArrayList<MakerCheckerListModel>();

					user = getCurrentUserDetail(authToken);
					if (user.isPresent()) {
						for (int i = 0; i < enterpriseUsers.size(); i++) {
							makerCheckerListModel = makerCheckerService.makeAndRejectedCheckerList(user.get(),
									enterpriseUsers.get(i));
							RecordLog.writeLogFile("makerCheckerListModel: " + makerCheckerListModel.toString());
							if (makerCheckerListModel.isOperationNull() == false) {
								getMakerCheckerListModel.add(makerCheckerListModel);
							}
						}
					} else {
						externalUserModelResp = new GetExternalUserModelResp();
						externalUserModelResp.setStatus(messages.isFailed());
						externalUserModelResp.setMessage(messages.getUserNotFound());
						externalUserModelResp.setDescription(messages.getUsernotfoundmsg());
						externalUserModelResp.setRecordId(null);
						return externalUserModelResp;
					}
				} else {
					externalUserModelResp = new GetExternalUserModelResp();
					externalUserModelResp.setStatus(messages.isFailed());
					externalUserModelResp.setMessage("No Rejected External Users");
					externalUserModelResp.setDescription("");
					externalUserModelResp.setRecordId(null);
					return externalUserModelResp;
				}
				if (getMakerCheckerListModel != null) {
					if (getMakerCheckerListModel.size() != 0) {
						Collections.reverse(getMakerCheckerListModel);

						RecordIdExtUser extUser = new RecordIdExtUser();
						extUser.setGetMakerCheckerListModel(getMakerCheckerListModel);

						externalUserModelResp = new GetExternalUserModelResp();
						externalUserModelResp.setStatus(messages.isSuccess());
						externalUserModelResp.setMessage(messages.getSuccessmsg());
						externalUserModelResp.setDescription("");
						externalUserModelResp.setRecordId(extUser);
					} else {
						externalUserModelResp = new GetExternalUserModelResp();
						externalUserModelResp.setStatus(messages.isFailed());
						externalUserModelResp.setMessage("Error getting maker checker list");
						externalUserModelResp.setDescription("operation id is null or no record found");
						externalUserModelResp.setRecordId(null);
					}
				} else {
					externalUserModelResp = new GetExternalUserModelResp();
					externalUserModelResp.setStatus(messages.isFailed());
					externalUserModelResp.setMessage("No User Found");
					externalUserModelResp.setDescription("");
					externalUserModelResp.setRecordId(null);
				}
			}
		} catch (Exception e) {
			RecordLog.writeLogFile("Exception occured at: "+Arrays.toString(e.getStackTrace())+" Exception name: "+e.getMessage());
			e.printStackTrace();
			externalUserModelResp = new GetExternalUserModelResp();
			externalUserModelResp.setStatus(messages.isFailed());
			externalUserModelResp.setMessage("Error getting rejected list of external users");
			externalUserModelResp.setDescription("Error getting rejected list of external users" + e.getMessage());
			externalUserModelResp.setRecordId(null);
		}
		return externalUserModelResp;
	}
	
	public Map<String,Object> unblockExtService(String refNo,String mobile,String flag){
		RecordLog.writeLogFile("unblock service is calling for mobile"+mobile);
		Map<String, Object> response = new HashMap<>();
		try {
			SecureRandom rand = new SecureRandom();
			String passwoord = Integer.toString(rand.nextInt(0X100000), 16);
			
			String passwordHash = null;
			
			EnterpriseUser enterpriseUser = enterpriseUserRepository.findByRefNoAndStatusAndMobile(refNo, "approved",
					mobile);
			User user = userRepository.findByMobileAndMarkAsEnabled(mobile, true);
			if (enterpriseUser != null && user != null) {
				passwoord=user.getPrefNo()+passwoord;
				String encodedPass = Base64.getEncoder().encodeToString(passwoord.getBytes());
				passwordHash = passwordHashing.getPasswordHash(passwoord);
				
//				enterpriseUser.setViewPwd(passwoord);
//				enterpriseUser.setPassword(encodedPass);
				if(flag.equals("lastActivity")) {
				enterpriseUser.setWebBlockStatus("active");
				user.setLastSignInAt(new Timestamp(new Date(System.currentTimeMillis()).getTime()));
				}else {
					user.setWebCheckStatus("active");
					user.setWrongWebPasswordCount("0");
				}
				enterpriseUserRepository.save(enterpriseUser);
				
				//user.setViewPwd(passwoord);
				//user.setEncryptedPassword(encodedPass);
				//user.setPassword(encodedPass);
				user.setViewPwdHash(passwordHash);
				userRepository.save(user);
				String smsMessage = String.format(messages.getEnterpriseUserUnblockSms(), user.getPrefNo(), passwoord);
				SMEMessage smeMessageResponse=simBindingService.sendSMS(smsMessage, mobile);
				RecordLog.writeLogFile("sms respe for mobile "+mobile+smeMessageResponse);
				response.put("status", true);
				response.put("message", "External user unblocked successfully");
			} else {
				response.put("status", false);
				response.put("message", "External user record not found");
			}
		} catch (Exception e) {
			e.printStackTrace();
			response.put("status", false);
			response.put("message", "Not able to unblock external user");
			RecordLog.writeLogFile("Exception occured for refno " + refNo + " " + Arrays.toString(e.getStackTrace())
					+ " message " + e.getMessage());
		}
		RecordLog.writeLogFile("unblock service is completed for mobile"+mobile+response.toString());

		return response;
	}
	
	
}