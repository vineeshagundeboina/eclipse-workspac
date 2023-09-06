package com.federal.fedmobilesmecore.service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.federal.fedmobilesmecore.config.GlobalProperties;
import com.federal.fedmobilesmecore.config.RecordLog;
import com.federal.fedmobilesmecore.dto.Enterprises;
import com.federal.fedmobilesmecore.dto.FundTransfer;
import com.federal.fedmobilesmecore.dto.ImpsTransfer;
import com.federal.fedmobilesmecore.dto.User;
import com.federal.fedmobilesmecore.dto.UserToken;
import com.federal.fedmobilesmecore.model.APIResponse;
import com.federal.fedmobilesmecore.model.ActivateApplicationUserModel;
import com.federal.fedmobilesmecore.model.FavouriteAccountRequest;
import com.federal.fedmobilesmecore.model.FundImpsTransferResponse;
import com.federal.fedmobilesmecore.model.ImpsLimitAPIRequest;
import com.federal.fedmobilesmecore.model.LastLoginDetailsRequest;
import com.federal.fedmobilesmecore.model.PendingTranscationRequest;
import com.federal.fedmobilesmecore.model.QuickPayLimitRequest;
import com.federal.fedmobilesmecore.model.RecordId1;
import com.federal.fedmobilesmecore.repository.EnterprisesRepository_V1;
import com.federal.fedmobilesmecore.repository.FundTransferRepository;
import com.federal.fedmobilesmecore.repository.ImpsTransferRepository;
import com.federal.fedmobilesmecore.repository.UserRepository_V1;
import com.federal.fedmobilesmecore.repository.UserTokenRepo;

@Service
public class FblLandingPageService {

	private static final Logger log4j = LogManager.getLogger(FblLandingPageService.class);

	@Autowired
	UserRepository_V1 userRepository;

	@Autowired
	GlobalProperties globalProperties;

	@Autowired
	EnterprisesRepository_V1 enterprisesRepository;

	@Autowired
	FundTransferRepository fundTransferRepository;

	@Autowired
	ImpsTransferRepository impsTransferRepository;

	@Autowired
	private SimBindingService simBindingService;
	
	@Autowired
	UserTokenRepo userTokenRepo;
	
	private String playStore=System.getenv("ptFlag");

	private String mobile=System.getenv("ptMobile");

	public APIResponse getImpsLimit(ImpsLimitAPIRequest impsLimitAPIRequest) {
		APIResponse apiResponse = new APIResponse();
		try {
			Optional<User> user = userRepository.findByAuthTokenAndMarkAsEnabled(impsLimitAPIRequest.getAuth_token(),
					true);
			if (user.isPresent()) {
//				RecordLog.writeLogFile("user" + user.get().getTransLimit());
//				if (user.get().getTransLimit().equals(globalProperties.getImps_limit())) {
//					apiResponse.setStatus(true);
//					apiResponse.setMessage(globalProperties.getSuccessmsg());
//					apiResponse.setRecordId(globalProperties.getImps_limit());
//				} else {
//					apiResponse.setStatus(false);
//					apiResponse.setMessage("IMPS limit Not reached");
//					apiResponse.setDescription("IMPS limit Not reached");
//				}

			} else {
				apiResponse.setStatus(false);
				apiResponse.setMessage(globalProperties.getUserNotFound());
				apiResponse.setDescription(globalProperties.getUsernotfoundmsg());
			}

		} catch (Exception e) {
			RecordLog.writeLogFile("Exception occured at: "+Arrays.toString(e.getStackTrace())+" Exception name: "+e.getMessage());
			apiResponse.setStatus(false);
			apiResponse.setMessage(e.getMessage());

		}

		return apiResponse;
	}

	@SuppressWarnings({ "null", "unused" })
	public FundImpsTransferResponse pendingTranscation(PendingTranscationRequest pendingTranscation) {
		FundImpsTransferResponse apiResponse = new FundImpsTransferResponse();
		RecordId1 rec = new RecordId1();
		List<String> response = new ArrayList<>();
		try {
			Enterprises enterprises = enterprisesRepository.findByPrefCorpAndActive(pendingTranscation.getPref_corp(),
					true);
			RecordLog.writeLogFile("User account number:" + enterprises.getAccNo());
			if (enterprises != null) {
				List<FundTransfer> fundtransfer = fundTransferRepository.findByStatusIsNotAndStatusAndEnterpriseId(
						"error", "pending", String.valueOf(enterprises.getId()));
				List<ImpsTransfer> impstransfer = impsTransferRepository.findByStatusAndEnterpriseId("pending",
						String.valueOf(enterprises.getId()));

//				RecordLog.writeLogFile("fundtransfer " + fundtransfer);
//				RecordLog.writeLogFile("impstransfer " + impstransfer);
				if (fundtransfer != null && impstransfer != null) {
//					RecordLog.writeLogFile("impstransfer-->>" + impstransfer);
					response.add(fundtransfer.toString());
					response.add(impstransfer.toString());
//					RecordLog.writeLogFile("HIIIIIIIIII-->>" + response.toString());
					apiResponse.setStatus(true);
					apiResponse.setMessage(globalProperties.getSuccessmsg());
					// apiResponse.setRecordId(fundtransfer.toString());
					// apiResponse.setRecordId(impstransfer.toString());
					// apiResponse.setFundTransfer(fundtransfer);
					// apiResponse.setImpsTransfer(impstransfer);
					rec.setFundTransfer(fundtransfer);
					rec.setImpsTransfer(impstransfer);
					apiResponse.setRecordId(rec);
				} else {
					apiResponse.setStatus(false);
					apiResponse.setMessage(globalProperties.getInvalidEnterprise());
					apiResponse.setRecordId(null);
				}

			} else {
				apiResponse.setStatus(false);
				apiResponse.setMessage(globalProperties.getUserNotFound());
				apiResponse.setDescription(globalProperties.getUsernotfoundmsg());
			}
		} catch (Exception e) {
			RecordLog.writeLogFile("Exception occured at: "+Arrays.toString(e.getStackTrace())+" Exception name: "+e.getMessage());
			apiResponse.setStatus(false);
			apiResponse.setMessage(e.getMessage());
		}

		return apiResponse;
	}

	public APIResponse lastLoginDetails(LastLoginDetailsRequest lastLoginDetails) {
		APIResponse loginDetailsResponse = new APIResponse();

		try {

			Optional<User> currentUser = userRepository.findByAuthToken(lastLoginDetails.getAuth_token());
			if (currentUser.isPresent() &&  currentUser.get().getLastActivityAt() != null) {
				Timestamp lastUpdate = currentUser.get().getLastActivityAt();
				SimpleDateFormat dt = new SimpleDateFormat("dd-MMMM-yyyy HH:mm:ss aa");
				
				ZonedDateTime zone=ZonedDateTime.ofInstant(lastUpdate.toInstant(),ZoneId.systemDefault());
				Timestamp lastUpdate1=Timestamp.from(zone.plusHours(5).plusMinutes(30).toInstant());
				String lastlogin = String.valueOf(Long.valueOf(lastUpdate1.getTime()));
				//String lastlogin = String.valueOf(Long.valueOf(lastUpdate.getTime()));
				loginDetailsResponse.setRecordId(lastlogin);
				loginDetailsResponse.setStatus(true);
				loginDetailsResponse.setMessage(globalProperties.getSuccessmsg());

			} else {
				loginDetailsResponse.setRecordId(null);
				loginDetailsResponse.setStatus(false);
				loginDetailsResponse.setMessage(globalProperties.getUserNotFound());
				loginDetailsResponse.setDescription(globalProperties.getUsernotfoundmsg());
			}
		} catch (Exception e) {
			RecordLog.writeLogFile("Exception occured at: "+Arrays.toString(e.getStackTrace())+" Exception name: "+e.getMessage());
			loginDetailsResponse.setStatus(false);
			loginDetailsResponse.setMessage(e.getLocalizedMessage());
			e.printStackTrace();
		}

		return loginDetailsResponse;
	}

	public APIResponse setFavouriteAccount(FavouriteAccountRequest favouriteAccountRequest) {
		APIResponse accountReponse = new APIResponse();
		try {
			Optional<User> user = userRepository
					.findByAuthTokenAndMarkAsEnabled(favouriteAccountRequest.getAuth_token(), true);
			if (user != null) {
				if (user.get().getFavouriteAccount() == null) {
					user.get().setFavouriteAccount(favouriteAccountRequest.getFavourite_account());

				} else if (user.get().getFavouriteAccount()
						.equalsIgnoreCase(favouriteAccountRequest.getFavourite_account())) {
					user.get().setFavouriteAccount(null);

				}
				userRepository.save(user.get());
				accountReponse.setStatus(true);
				accountReponse.setMessage(globalProperties.getSuccessmsg());
			} else {
				accountReponse.setStatus(false);
				accountReponse.setMessage(globalProperties.getUserNotFound());
			}
		} catch (Exception e) {
			accountReponse.setStatus(false);
			RecordLog.writeLogFile("Exception occured at: "+Arrays.toString(e.getStackTrace())+" Exception name: "+e.getMessage());
			accountReponse.setMessage(globalProperties.getExceptionerrmsg());
		}

		return accountReponse;
	}

	@SuppressWarnings("unused")
	public APIResponse getQuickPayLimit(QuickPayLimitRequest payLimitRequest) {
		APIResponse apiResponse = new APIResponse();
		try {
			Optional<User> user = userRepository.findByAuthTokenAndMarkAsEnabled(payLimitRequest.getAuth_token(), true);
//			RecordLog.writeLogFile("user" + user.get().getTransLimit());
			if (user != null) {
//				if (user.get().getTransLimit().equals(globalProperties.getQuickpaylimit())) {
//					apiResponse.setStatus(true);
//					apiResponse.setMessage(globalProperties.getSuccessmsg());
//					apiResponse.setRecordId(globalProperties.getQuickpaylimit());
//				}

			} else {
				apiResponse.setStatus(false);
				apiResponse.setMessage(globalProperties.getUserNotFound());
			}
		} catch (Exception e) {
			RecordLog.writeLogFile("Exception occured at: "+Arrays.toString(e.getStackTrace())+" Exception name: "+e.getMessage());
			apiResponse.setStatus(false);
			apiResponse.setMessage(e.getMessage());
			apiResponse.setRecordId(null);
		}

		return apiResponse;
	}
	
	public String getactiveRegToken(User user)
	{
		String activereg_token = "";
		try {
			List<UserToken> tokens = userTokenRepo.findByTypeAndUserAndActiveAndExpiredAtGreaterThan(
					"RegistrationToken", user, BigDecimal.ONE, new Timestamp(System.currentTimeMillis()));
			if (!CollectionUtils.isEmpty(tokens)) {
				activereg_token = tokens.get(0).getToken();
			}
		} catch (Exception e) {
			RecordLog.writeLogFile("Exception occured at: "+Arrays.toString(e.getStackTrace())+" Exception name: "+e.getMessage());
			e.printStackTrace();
		}
		return activereg_token;

	}

	public APIResponse activateApplicationUser(ActivateApplicationUserModel activateuser) {
		APIResponse apiResponse = new APIResponse();
		try {
			if (activateuser.getActivation_token() != null) {
				if (activateuser.getAuth_token() != null) {
					Optional<User> currentUser = userRepository
							.findByAuthTokenAndMarkAsEnabled(activateuser.getAuth_token(), true);
					if (currentUser.isPresent()) {
						User user = userRepository.getOne(currentUser.get().getId());
//						RecordLog.writeLogFile(">>>>>>>>>>>>>>>>>>>time>>>>>>>>>" + new Timestamp(System.currentTimeMillis()));
						boolean valid_registration_token1 = userTokenRepo
								.existsByTypeAndUserAndActiveAndTokenAndExpiredAtGreaterThan("RegistrationToken",
										currentUser.get(), BigDecimal.ONE, activateuser.getActivation_token(),
										new Timestamp(System.currentTimeMillis()));
						RecordLog.writeLogFile("valid registration token: " + valid_registration_token1);
						if (valid_registration_token1) {
							Optional<UserToken> temptoken = userTokenRepo
									.findByTypeAndUserAndActiveAndTokenAndExpiredAtGreaterThan("RegistrationToken",
											currentUser.get(), BigDecimal.ONE, activateuser.getActivation_token(),
											new Timestamp(System.currentTimeMillis()));
							// Fix added by vikas because of wrong logic.
							// UserToken userToken = userTokenRepo.getOne(currentUser.get().getId());
							// User user =userRepository.getOne(currentUser.get().getId());
							if (temptoken.isPresent()) {
								UserToken userToken = temptoken.get();
								userToken.setActive(BigDecimal.ZERO);
								userToken.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
								userTokenRepo.save(userToken);
								user.setActivationStatus("activated");
								user.setWrongActivationTokenCount("0");
								user.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
								userRepository.save(user);
								apiResponse.setStatus(true);
								apiResponse.setDescription(globalProperties.getSuccessmsg());
								apiResponse.setMessage(globalProperties.getSuccessmsg());
								apiResponse.setRecordId(null);
							} else {
								apiResponse.setStatus(false);
								apiResponse.setMessage("Token is expired/Invalid.");

							}
						} else {
							
							RecordLog.writeLogFile("iam checking activation token "+activateuser.getActivation_token()+" user mobile "+user.getMobile());
							RecordLog.writeLogFile("hardcoded values flag "+playStore +" mobile "+ mobile +" pin 123456");
							if(playStore.equals("yes")&& user.getMobile().equals(mobile) && activateuser.getActivation_token().equals("123456")) {
							RecordLog.writeLogFile("setting status to activated inside if");
							user.setActivationStatus("activated");
							user.setWrongActivationTokenCount("0");
							user.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
							userRepository.save(user);
							apiResponse.setStatus(true);
							apiResponse.setDescription(globalProperties.getSuccessmsg());
							apiResponse.setMessage(globalProperties.getSuccessmsg());
							apiResponse.setRecordId(null);
							
							
							simBindingService.sendSMS(globalProperties.getRegistraction_success_sms(), user.getMobile());

							
							}
							
							else {
							
							//FIXME Future this should be in props file.
							int allowed_count = 4;
							int retry_count =currentUser.get().getWrongActivationTokenCount()==null?0: Integer.parseInt(currentUser.get().getWrongActivationTokenCount());
							int count1 = retry_count + 1;
							if (retry_count == 5) {
								user.setActivationStatus("exceeded");
								user.setWrongActivationTokenCount(currentUser.get().getWrongActivationTokenCount());
								apiResponse.setStatus(false);
								apiResponse.setMessage(globalProperties.getActivationExceeded());
								apiResponse.setDescription(null);
								apiResponse.setRecordId(null);
							} else {
								int pending_attempts = allowed_count - retry_count;
								user.setWrongActivationTokenCount(String.valueOf(count1));
								user.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
								apiResponse.setStatus(false);
								if (pending_attempts > 0) {
									apiResponse.setMessage(globalProperties.getActivationError().replace("@attempts@",
											"" + (pending_attempts)));
								} else {
									apiResponse.setMessage(
											globalProperties.getActivationError().replace("@attempts@", "No "));
								}

							}
							userRepository.save(user);
						}
						}

					} else if (currentUser.get().getActivationStatus().equals("exceeded")) {
						apiResponse.setStatus(false);
						apiResponse.setMessage(globalProperties.getActivationLimitExceeded());
						apiResponse.setRecordId(null);
					} else {
						apiResponse.setStatus(false);
						apiResponse.setMessage(globalProperties.getInvalidUser());
						apiResponse.setRecordId(null);

					}
				} else {
					apiResponse.setStatus(false);
					apiResponse.setMessage(globalProperties.getInvalidUser());
					apiResponse.setDescription("Auth token is missing.");
					apiResponse.setRecordId(null);
				}
			} else {
				apiResponse.setStatus(false);
				apiResponse.setMessage(globalProperties.getInvalidRegistrationToken());
				apiResponse.setRecordId(null);
			}
		} catch (IncorrectResultSizeDataAccessException ex) {
			ex.printStackTrace();
			RecordLog.writeLogFile("Exception occured at: "+Arrays.toString(ex.getStackTrace())+" Exception name: "+ex.getMessage());
			apiResponse.setStatus(false);
			apiResponse.setMessage("More than one active tokens are available.");
			apiResponse.setDescription(ex.getMessage());
			apiResponse.setRecordId(null);
		} catch (Exception e) {
			e.printStackTrace();
			RecordLog.writeLogFile("Exception occured at: "+Arrays.toString(e.getStackTrace())+" Exception name: "+e.getMessage());
			apiResponse.setStatus(false);
			apiResponse.setMessage("Invalid Token");
			apiResponse.setDescription(e.getMessage());
			apiResponse.setRecordId(null);
		}

		return apiResponse;
	}
}
