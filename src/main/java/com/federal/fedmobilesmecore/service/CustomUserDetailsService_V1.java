package com.federal.fedmobilesmecore.service;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.federal.fedmobilesmecore.config.GlobalProperties;
import com.federal.fedmobilesmecore.config.PasswordHashing;
import com.federal.fedmobilesmecore.config.RecordLog;
import com.federal.fedmobilesmecore.dto.Enterprises;
import com.federal.fedmobilesmecore.dto.User;
import com.federal.fedmobilesmecore.dto.UsersAuthTokens;
import com.federal.fedmobilesmecore.model.CreateBeneficiaryResponse;
import com.federal.fedmobilesmecore.model.DestorySession;
import com.federal.fedmobilesmecore.model.MobileUserSessionModel;
import com.federal.fedmobilesmecore.model.RefreshTokenmodel;
import com.federal.fedmobilesmecore.model.SMEMessage;
import com.federal.fedmobilesmecore.model.WebUserSessionApiResp;
import com.federal.fedmobilesmecore.model.WebUserSessionModel;
import com.federal.fedmobilesmecore.model.WebUserSessionResponse;
import com.federal.fedmobilesmecore.repository.EnterprisesRepository_V1;
import com.federal.fedmobilesmecore.repository.UserRepository_V1;
import com.federal.fedmobilesmecore.repository.UserTokensRepository;

@Service
public class CustomUserDetailsService_V1 {

	@Autowired
	UserRepository_V1 userRepository;

	@Autowired
	EnterprisesRepository_V1 enterprisesRepository;
	
	
	@Autowired
	UserTokensRepository userTokensRepository;

	@Autowired
	JwtTokenProvider tokenProvider;
	@Autowired
	GlobalProperties globalProperties;
	
	@Autowired
	private SimBindingService simBindingService;
	
	@Autowired
	PasswordHashing passwordHashing;

	public SMEMessage generateMobileUserAuthToken(MobileUserSessionModel request) {
		SMEMessage apiResponse = new SMEMessage();
		try {
			if (request.getPrefNo() != null && request.getMpin() != null) {
				User user = userRepository.findByPrefNoAndMobileAndMarkAsEnabled(request.getPrefNo(),request.getMobile(), true).orElse(null);
				// decrypt the password user.getMpin().equals(request.getMpin())
				if (user != null) {
					if (user.getMpin().equals(request.getMpin())) {
						user.setAuthToken(null);
						if (user.getAuthToken() == null) {
							String auth_token = generateAuthToken(user);
							//String refreshToken = refreshToken(user);
							user.setAuthToken(auth_token);
							user.setUpdatedAt(new Timestamp(new Date().getTime()));
							user.setLastActivityAt(new Timestamp(new Date().getTime()));
							userRepository.save(user);
							apiResponse.setStatus(true);
							apiResponse.setMessage(globalProperties.getSuccessmsg());
							// apiResponse.setDescription(auth_token);
							apiResponse.setDescription("Auth Token Generated for Mobile User Successfully!");
							apiResponse.setRecordid(auth_token);// here refresh token is Auth token which will be
																	// saving in db in AUTH_TOKEN column
						} else {

							apiResponse.setStatus(false);
							apiResponse.setMessage(globalProperties.getAuthTokenActive());
							apiResponse.setRecordid(globalProperties.getAuthTokenActive());
						}
					} else {
						apiResponse.setStatus(false);
						apiResponse.setMessage(globalProperties.getInvalidPassOrMpin());
						apiResponse.setRecordid(globalProperties.getInvalidPassOrMpin());
					}

				} else {
					apiResponse.setStatus(false);
//					apiResponse.setMessage(
//							"User Not Found with prefNo: " + request.getPrefNo() + " or User is In-Active ");
					apiResponse.setMessage("Re-install the app and try to login");
				}
			} else {
				apiResponse.setStatus(false);
				apiResponse.setMessage(globalProperties.getInvalidPrefNoOrMpin());
				apiResponse.setRecordid(globalProperties.getInvalidPrefNoOrMpin());
			}

		} catch (Exception e) {
			RecordLog.writeLogFile("Exception occured at: "+Arrays.toString(e.getStackTrace())+" Exception name: "+e.getMessage());
			e.printStackTrace();
			apiResponse.setStatus(false);
			apiResponse.setMessage("Failure");
			apiResponse.setDescription(e.getMessage());
			apiResponse.setRecordid(globalProperties.getExceptionerrmsg());
		}

		return apiResponse;
	}

	@SuppressWarnings("unused")
	public WebUserSessionApiResp generateWebUserAuthToken(WebUserSessionModel request) {
		WebUserSessionApiResp apiResponse = new WebUserSessionApiResp();
		WebUserSessionResponse response = new WebUserSessionResponse();
		try {
			if (request.getPrefNo() != null && request.getPassword() != null) {
				User user = userRepository.findByPrefNoAndMobileAndMarkAsEnabled(request.getPrefNo(),request.getMobileNo(), true).orElse(null);

				// decrypt the password user.getPassword().equals(request.getPassword())

				if (user != null) {
					Optional<Enterprises> enterprises = enterprisesRepository
							.findById(Long.valueOf(user.getEnterpriseId()));
					if (enterprises.isPresent()) {
						if (user.getMobile().contentEquals(request.getMobileNo())) {
							if (user.getViewPwd().equals(request.getPassword())) {
								user.setAuthToken(null);
								if (user.getAuthToken() == null) {
									String auth_token = generateAuthToken(user);
									user.setAuthToken(auth_token);
									user.setUpdatedAt(new Timestamp(new Date().getTime()));
									user.setLastActivityAt(new Timestamp(new Date().getTime()));	
									userRepository.save(user);

									response.setAuthToken(auth_token);
									response.setAccNo(enterprises.get().getAccNo());
									response.setCustomerName(user.getCustomerName());
									response.setUserName(user.getUserName());
									response.setRole(user.getRole());
									response.setFavouriteAccount(user.getFavouriteAccount());
									response.setFirstTimelogin(user.getWebSignInCount() == 0L ? false : true);
									response.setMobile(user.getMobile());
									response.setPrefno(user.getPrefNo());
									response.setEnterpriseCorpId(enterprises.get().getPrefCorp());
									response.setCompanyName(enterprises.get().getAccName());
									response.setCustid(user.getCustNo());
									apiResponse.setStatus(true);
									apiResponse.setMessage(globalProperties.getSuccessmsg());
									apiResponse.setRecordid(response);
								} else {

									/*
									 * apiResponse.setStatus(false);
									 * apiResponse.setMessage(globalProperties.getAuthTokenActive());
									 * apiResponse.setRecordid(globalProperties.getAuthTokenActive());
									 */
									response.setAuthToken(user.getAuthToken());
									response.setAccNo(enterprises.get().getAccNo());
									response.setCustomerName(user.getCustomerName());
									response.setUserName(user.getUserName());
									response.setRole(user.getRole());
									response.setFavouriteAccount(user.getFavouriteAccount());
									response.setFirstTimelogin(user.getWebSignInCount() == 0L ? false : true);
									response.setMobile(user.getMobile());
									response.setPrefno(user.getPrefNo());
									response.setEnterpriseCorpId(enterprises.get().getPrefCorp());
									response.setCompanyName(enterprises.get().getAccName());
									response.setCustid(user.getCustNo());
									apiResponse.setStatus(true);
									apiResponse.setMessage(globalProperties.getSuccessmsg());
									apiResponse.setRecordid(response);
								}
							} else {
								apiResponse.setStatus(false);
								apiResponse.setMessage(globalProperties.getInvalidPassOrMpin());
								apiResponse.setRecordid(globalProperties.getInvalidPassOrMpin());
							}
						} else {
							apiResponse.setStatus(false);
							apiResponse.setMessage("Mobile no doesnt match for the prefNo");
							apiResponse
									.setDescription("Mobile no doesnt match for the prefNo : " + request.getPrefNo());
						}
					} else {
						apiResponse.setStatus(false);
						apiResponse.setMessage("Enterprise Not Found for the prefNo");
						apiResponse.setDescription("Enterprise Not Found for the prefNo : " + request.getPrefNo());
					}
				} else {
					apiResponse.setStatus(false);
//					apiResponse
//							.setMessage("User Not Found with prefNo: " + request.getPrefNo() + " or User is In-Active");
					apiResponse.setMessage("User Not Found");

				}
			} else {
				apiResponse.setStatus(false);
				apiResponse.setMessage("Failure");
				apiResponse.setDescription("PrefNo and password is mandatory");
			}

		} catch (Exception e) {
			RecordLog.writeLogFile("Exception occured at: "+Arrays.toString(e.getStackTrace())+" Exception name: "+e.getMessage());
			e.printStackTrace();
			apiResponse.setStatus(false);
			apiResponse.setMessage("Failure");
			apiResponse.setDescription(e.getMessage());
			apiResponse.setRecordid(globalProperties.getExceptionerrmsg());
		}
		return apiResponse;
	}
	
	
	
	
	//new version v2.4 started
	 
		@SuppressWarnings("unused")
		public WebUserSessionApiResp generateWebUserAuthTokenV24(WebUserSessionModel request) {
			WebUserSessionApiResp apiResponse = new WebUserSessionApiResp();
			WebUserSessionResponse response = new WebUserSessionResponse();
			try {
				if (request.getPrefNo() != null && request.getPassword() != null) {
					User user1 = userRepository.findByPrefNoAndMobileAndMarkAsEnabled(request.getPrefNo(),request.getMobileNo(), true).orElse(null);

					if(user1 != null) {
						
						if(user1.getViewPwdHash() == null) {
							
							Optional<Enterprises> enterprises = enterprisesRepository
									.findById(Long.valueOf(user1.getEnterpriseId()));
							if (enterprises.isPresent()) {
								if (user1.getMobile().contentEquals(request.getMobileNo())) {
									String webCheckStatus=user1.getWebCheckStatus()==null?"":user1.getWebCheckStatus();
									
									
									
									if (user1.getViewPwd().equals(request.getPassword()) && (!webCheckStatus.equals("blocked"))) {
							
							
							      String hashedPassword = passwordHashing.getPasswordHash(request.getPassword());
							
							
							user1.setViewPwdHash(hashedPassword);
							userRepository.save(user1);
									}
								}
							}
							
							
						}
						
						}
					
					
					
					
					
					
					User user = userRepository.findByPrefNoAndMobileAndMarkAsEnabled(request.getPrefNo(),request.getMobileNo(), true).orElse(null);
					String passWordToCheck ="";
					if (user != null) {	
						if(user.getViewPwdHash() != null) {
							
							passWordToCheck = user.getViewPwdHash();
						}
						Timestamp lastWebSignInAt=user.getLastSignInAt();
						Optional<Enterprises> enterprises = enterprisesRepository
								.findById(Long.valueOf(user.getEnterpriseId()));
						if (enterprises.isPresent()) {
							if (user.getMobile().contentEquals(request.getMobileNo())) {
								String webCheckStatus=user.getWebCheckStatus()==null?"":user.getWebCheckStatus();
								String doubleCheckHashedPassword = passwordHashing.getPasswordHash(request.getPassword());
								if (passWordToCheck.equals(doubleCheckHashedPassword) && (!webCheckStatus.equals("blocked"))) {
									
									user.setAuthToken(null);
									if (user.getAuthToken() == null) {
										String auth_token = generateAuthToken(user);
										user.setAuthToken(auth_token);
										user.setUpdatedAt(new Timestamp(new Date().getTime()));
									//	user.setLastActivityAt(new Timestamp(new Date().getTime()));
										user.setLastSignInAt(new Timestamp(new Date().getTime()));
										user.setWebCheckStatus("active");
										user.setWrongWebPasswordCount("0");
										userRepository.save(user);

										response.setAuthToken(auth_token);
										response.setAccNo(enterprises.get().getAccNo());
										response.setCustomerName(user.getCustomerName());
										response.setUserName(user.getUserName());
										response.setRole(user.getRole());
										response.setFavouriteAccount(user.getFavouriteAccount());
										response.setFirstTimelogin(user.getWebSignInCount() == 0L ? false : true);
										response.setMobile(user.getMobile());
										response.setPrefno(user.getPrefNo());
										response.setEnterpriseCorpId(enterprises.get().getPrefCorp());
										response.setCompanyName(enterprises.get().getAccName());
										response.setCustid(user.getCustNo());
										apiResponse.setStatus(true);
										apiResponse.setIsAccepted(user.getIsAccepted());
										apiResponse.setMessage(globalProperties.getSuccessmsg());
										apiResponse.setRecordid(response);
									} else {
										response.setAuthToken(user.getAuthToken());
										response.setAccNo(enterprises.get().getAccNo());
										response.setCustomerName(user.getCustomerName());
										response.setUserName(user.getUserName());
										response.setRole(user.getRole());
										response.setFavouriteAccount(user.getFavouriteAccount());
										response.setFirstTimelogin(user.getWebSignInCount() == 0L ? false : true);
										response.setMobile(user.getMobile());
										response.setPrefno(user.getPrefNo());
										response.setEnterpriseCorpId(enterprises.get().getPrefCorp());
										response.setCompanyName(enterprises.get().getAccName());
										response.setCustid(user.getCustNo());
										apiResponse.setStatus(true);
										apiResponse.setMessage(globalProperties.getSuccessmsg());
										apiResponse.setRecordid(response);
										apiResponse.setIsAccepted(user.getIsAccepted());
									}
								} else {
				
									if(user.getWebCheckStatus()!=null && user.getWebCheckStatus().equals("blocked")) {
										if(user.getRole()==null) {
											apiResponse.setStatus(false);
											apiResponse.setMessage(globalProperties.getWeb_auth_sign_msg());
											apiResponse.setRecordid(globalProperties.getWeb_auth_sign_msg());
										}else {
										apiResponse.setStatus(false);
										apiResponse.setMessage(globalProperties.getWeb_wrongpassword());
										apiResponse.setRecordid(globalProperties.getWeb_wrongpassword());
										}
										
									}else {
										String wrongcount=user.getWrongWebPasswordCount()==null?"0":user.getWrongWebPasswordCount();
										int count=Integer.valueOf(wrongcount);
										count++;
										user.setWrongWebPasswordCount(String.valueOf(count));
										if(count == 1) {
											SMEMessage smsapptoken1 = simBindingService.sendSMS(globalProperties.getWeb_blocked_message_for_single_wrong_attempts(), user.getMobile());
											System.out.println("sms sent for Wrong Password(Upto 3 Attempts) : "+smsapptoken1.toString());
											if (smsapptoken1.isStatus()) {
											RecordLog.writeLogFile("Wrong Password(Upto 3 Attempts) response of send sms api for mobile  "+user.getMobile()+" response "+smsapptoken1.isStatus() + ":" + smsapptoken1.getMessage());
											}
											}

											if(count == 2) {
																					
											SMEMessage smsapptoken1 = simBindingService.sendSMS(globalProperties.getWeb_blocked_message_for_second_wrong_attempts(), user.getMobile());
											System.out.println("sms sent for Wrong Password(Upto 3 Attempts) : "+smsapptoken1.toString());
											if (smsapptoken1.isStatus()) {
											RecordLog.writeLogFile("Wrong Password(Upto 3 Attempts) response of send sms api for mobile  "+user.getMobile()+" response "+smsapptoken1.isStatus() + ":" + smsapptoken1.getMessage());
											}
																					
											}
										if(count>=3) {
											user.setWebCheckStatus("blocked");

											SMEMessage smsapptoken = simBindingService.sendSMS(globalProperties.getWeb_blocked_message(), user.getMobile());
											System.out.println("sms sent  "+smsapptoken.toString());
											if (smsapptoken.isStatus()) {
												RecordLog.writeLogFile("response of send sms api for mobile "+user.getMobile()+" response "+smsapptoken.isStatus() + ":" + smsapptoken.getMessage());
											}
											
										}
										apiResponse.setStatus(false);
										apiResponse.setMessage(String.format(globalProperties.getWeb_blocked_message_count(), String.valueOf(3-count)));
										apiResponse.setRecordid(String.format(globalProperties.getWeb_blocked_message_count(), String.valueOf(3-count)));
										userRepository.save(user);
									}
								}
							} else {
								apiResponse.setStatus(false);
								apiResponse.setMessage("Mobile no doesnt match for the prefNo");
								apiResponse
										.setDescription("Mobile no doesnt match for the prefNo : " + request.getPrefNo());
							}
						} else {
							apiResponse.setStatus(false);
							apiResponse.setMessage("Enterprise Not Found for the prefNo");
							apiResponse.setDescription("Enterprise Not Found for the prefNo : " + request.getPrefNo());
						}
					
					} else {
						apiResponse.setStatus(false);
						apiResponse.setMessage("User Not Found");

					}
				} else {
					apiResponse.setStatus(false);
					apiResponse.setMessage("Failure");
					apiResponse.setDescription("PrefNo and password is mandatory");
				}

			} catch (Exception e) {
				RecordLog.writeLogFile("Exception occured at: "+Arrays.toString(e.getStackTrace())+" Exception name: "+e.getMessage());
				e.printStackTrace();
				apiResponse.setStatus(false);
				apiResponse.setMessage("Failure");
				apiResponse.setDescription(e.getMessage());
				apiResponse.setRecordid(globalProperties.getExceptionerrmsg());
			}
			return apiResponse;
		}
		
		//new version v2.4 ended
	
	
	
	
	

	public WebUserSessionApiResp generateWebUserAuthToken_V1(WebUserSessionModel request) {
		WebUserSessionApiResp apiResponse = new WebUserSessionApiResp();
		WebUserSessionResponse response = new WebUserSessionResponse();
		try {
			if (request.getPrefNo() != null && request.getPassword() != null) {
				User user = userRepository.findByPrefNoAndMobileAndMarkAsEnabled(request.getPrefNo(),request.getMobileNo(),true).orElse(null);
				// decrypt the password user.getPassword().equals(request.getPassword())

				if (user != null) {
					if (user.getViewPwd().equals(request.getPassword())) {
						if (user.getAuthToken() == null) {
							String auth_token = generateAuthToken(user);
							user.setAuthToken(auth_token);
							user.setUpdatedAt(new Timestamp(new Date().getTime()));
							user.setLastActivityAt(new Timestamp(new Date().getTime()));
							userRepository.save(user);

							Optional<Enterprises> enterprises = enterprisesRepository
									.findById(Long.valueOf(user.getEnterpriseId()));

							response.setAuthToken(auth_token);
							response.setAccNo(enterprises.get().getAccNo());
							response.setCustomerName(user.getCustomerName());
							response.setUserName(user.getUserName());
							response.setRole(user.getRole());
							response.setFavouriteAccount(user.getFavouriteAccount());
							response.setFirstTimelogin(user.getWebSignInCount() == 0L ? false : true);
							response.setMobile(user.getMobile());
							response.setPrefno(user.getPrefNo());
							response.setEnterpriseCorpId(enterprises.get().getPrefCorp());

							apiResponse.setStatus(true);
							apiResponse.setMessage(globalProperties.getSuccessmsg());
							apiResponse.setRecordid(response);
						} else {

							apiResponse.setStatus(false);
							apiResponse.setMessage(globalProperties.getAuthTokenActive());
							apiResponse.setRecordid(globalProperties.getAuthTokenActive());
						}
					} else {
						apiResponse.setStatus(false);
						apiResponse.setMessage(globalProperties.getInvalidPassOrMpin());
						apiResponse.setRecordid(globalProperties.getInvalidPassOrMpin());
					}

				} else {
					apiResponse.setStatus(false);
//					apiResponse.setMessage("User Not Found with prefNo: " + request.getPrefNo());
					apiResponse.setMessage("User Not Found");

				}
			} else {
				apiResponse.setStatus(false);
				apiResponse.setMessage(globalProperties.getInvalidPrefNoOrPass());
				apiResponse.setRecordid(globalProperties.getInvalidPrefNoOrPass());
			}

		} catch (Exception e) {
			RecordLog.writeLogFile("Exception occured at: "+Arrays.toString(e.getStackTrace())+" Exception name: "+e.getMessage());
			apiResponse.setStatus(false);
			apiResponse.setMessage(e.getLocalizedMessage());

		}

		return apiResponse;
	}

	private String generateAuthToken(User user) {

		return tokenProvider.generateToken(user);
	}

	public User loadUserByPrefNO(String prefNo,String mobile) {
		User user = null;
		try {
			user = userRepository.findByPrefNoAndMobileAndMarkAsEnabled(prefNo,mobile, true).orElse(null);
		} catch (Exception e) {
			RecordLog.writeLogFile("Exception occured at: "+Arrays.toString(e.getStackTrace())+" Exception name: "+e.getMessage());
			e.printStackTrace();
		}
		return user;
	}

	public SMEMessage destorySession(DestorySession request) {
		SMEMessage apiResponse = new SMEMessage();
		User user = userRepository.findByPrefNoAndMobileAndMarkAsEnabled(request.getPrefNo(),request.getMobile(),true)
				.orElseThrow(() -> new RuntimeException(globalProperties.getUsernotfoundmsg()));

		if (user != null && user.getAuthToken() != null) {
			user.setAuthToken(null);
			// user.setWebSignInCount(0L);
			user.setLastActivityAt(new Timestamp(new Date().getTime()));
			userRepository.save(user);
			apiResponse.setStatus(true);
			apiResponse.setMessage(globalProperties.getSuccessmsg());
			apiResponse.setRecordid(globalProperties.getSuccessmsg());

		} else {
			apiResponse.setStatus(false);
			apiResponse.setMessage(globalProperties.getUserNotFound());
			apiResponse.setRecordid(globalProperties.getUsernotfoundmsg());
		}

		return apiResponse;
	}

	public SMEMessage refreshToken(RefreshTokenmodel refreshToken) {
		SMEMessage apiResponse = new SMEMessage();
		UsersAuthTokens usersToken = new UsersAuthTokens();
		try {
			if (refreshToken.getRefreshToken() != null) {
				User user = userRepository.findByAuthToken(refreshToken.getRefreshToken()).orElse(null);

//				RecordLog.writeLogFile("User in refresh token " + user);

				if (user != null) {
					if (user.getAuthToken() != null) {
						if (user.getAuthToken().equals(refreshToken.getRefreshToken())) {
							if (tokenProvider.validateRefreshToken(refreshToken.getRefreshToken(), user)) {
								user = userRepository.getOne(user.getId());
								String auth_token = generateAuthToken(user);
								// String refresh_token = refreshToken(user);
								user.setAuthToken(auth_token);
								// user.setAppToken(refresh_token);
								user.setUpdatedAt(new Timestamp(new Date().getTime()));
								user.setLastActivityAt(new Timestamp(new Date().getTime()));
								userRepository.save(user);
								//Inserting Tokens to new Table
								usersToken.setPrefNo(user.getPrefNo());
								usersToken.setAuthToken(auth_token);
								usersToken.setCreatedAt(new Timestamp(new Date().getTime()));
								userTokensRepository.save(usersToken);
								apiResponse.setStatus(true);
								apiResponse.setMessage(globalProperties.getSuccessmsg());
								apiResponse.setRecordid(auth_token);
								apiResponse.setDescription(user.getAuthToken());
							} else {
								apiResponse.setStatus(false);
								apiResponse.setMessage(globalProperties.getAppTokenExpired());
								apiResponse.setRecordid(null);

							}
						} else {
//							RecordLog.writeLogFile("Auth Token is Not Matching " + user.getAuthToken());
							apiResponse.setStatus(false);
							apiResponse.setMessage("Auth Token is Not Matching");
							apiResponse.setRecordid(null);

						}

					} else {
						apiResponse.setStatus(false);
						apiResponse.setMessage("User Auth Token is Empty");
						apiResponse.setRecordid(null);
					}

				} else {
					apiResponse.setStatus(false);
					apiResponse.setMessage("User Not Found with RefreshToken: " + refreshToken.getRefreshToken());

				}
			} else {
				apiResponse.setStatus(false);
				apiResponse.setMessage("Refresh Token Not Sent");
				apiResponse.setRecordid(null);
			}

		} catch (Exception e) {
			RecordLog.writeLogFile("Exception occured at: "+Arrays.toString(e.getStackTrace())+" Exception name: "+e.getMessage());
			apiResponse.setStatus(false);
			apiResponse.setMessage(e.getLocalizedMessage());

		}

		return apiResponse;
	}

	private String refreshToken(User user) {
		return tokenProvider.refreshToken(user);
	}

}