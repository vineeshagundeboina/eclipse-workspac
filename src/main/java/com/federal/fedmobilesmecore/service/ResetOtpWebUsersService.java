package com.federal.fedmobilesmecore.service;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Base64;
import java.util.Base64.Encoder;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.federal.fedmobilesmecore.config.PasswordHashing;
import com.federal.fedmobilesmecore.config.RecordLog;
import com.federal.fedmobilesmecore.dto.ApplicationForm;
import com.federal.fedmobilesmecore.dto.ApplicationUser;
import com.federal.fedmobilesmecore.dto.BulkUpload;
import com.federal.fedmobilesmecore.dto.Enterprises;
import com.federal.fedmobilesmecore.dto.User;
import com.federal.fedmobilesmecore.model.CommonResetOtpWebUsersReqModel;
import com.federal.fedmobilesmecore.model.SMEMessage;
import com.federal.fedmobilesmecore.model.ValidateOtpRespModel;
import com.federal.fedmobilesmecore.model.WebUserSessionApiResp;
import com.federal.fedmobilesmecore.model.WebUserSessionResponse;
import com.federal.fedmobilesmecore.repository.ApplicationFormRepository;
import com.federal.fedmobilesmecore.repository.ApplicationUserRepository;
import com.federal.fedmobilesmecore.repository.BulkUploadRepository;
import com.federal.fedmobilesmecore.repository.EnterprisesRepository;
import com.federal.fedmobilesmecore.repository.UserRepository_V1;

@Service
public class ResetOtpWebUsersService {
	private static final Logger log4j = LogManager.getLogger(ResetOtpWebUsersService.class);
	@Autowired
	ApplicationFormRepository applicationFormRepository;
	@Autowired
	ApplicationUserRepository applicationUserRepository;
	@Autowired
	UserRepository_V1 userRepository_V1;
	@Autowired
	EnterprisesRepository enterprisesRepository;
	@Autowired
	ResetOtpWebUsersInternalService resetOtpWebUsersInternalService;
	@Autowired
	BulkUploadRepository bulkUploadRepository;
	@Autowired
	CustomUserDetailsService_V1 customUserDetailsService_V1;
	@Autowired
	PasswordHashing passwordHashing;

	@Value("${proxy.api.service.id}")
	private String APIServiceID;

	public void getResendOtp(String formId) {
		Optional<ApplicationForm> applicationForm = null;
		Optional<List<ApplicationUser>> listApplicationUser = null;
		ApplicationUser applicationUser = null;
		applicationForm = applicationFormRepository.findById(Long.valueOf(formId));
		if (applicationForm.isPresent()) {
			listApplicationUser = applicationUserRepository.findByApplicationFormId(formId);
			if (listApplicationUser.isPresent()) {
				for (int i = 0; i < listApplicationUser.get().size(); i++) {
					applicationUser = listApplicationUser.get().get(i);
					// random 6 digit
					int otp = new Random().nextInt(900000) + 100000;
					applicationUser.setOtp(String.valueOf(otp));
					applicationUserRepository.save(applicationUser);
				}
			}
		}
	}

	public ValidateOtpRespModel validateOtp(CommonResetOtpWebUsersReqModel commonResetOtpWebUsersReqModel) {
		Optional<User> user = null;
		User userResp = null;
		boolean isValidOtp = false;
		ValidateOtpRespModel otpRespModel = null;
		Enterprises enterprises = null;
		String hashedOtp = null;

		user = userRepository_V1.findByMobileAndMarkAsEnabled(commonResetOtpWebUsersReqModel.getMobile(), true);
		// user =
		// userRepository_V1.findByAuthTokenAndMarkAsEnabled(commonResetOtpWebUsersReqModel.getAuthToken(),
		// true);
		if (user.isPresent()) {
			hashedOtp = passwordHashing.getPasswordHash(commonResetOtpWebUsersReqModel.getOtp());
			if (commonResetOtpWebUsersReqModel.getMobile().equals(user.get().getMobile())) {
				isValidOtp = resetOtpWebUsersInternalService.isValidOtp(user.get().getOtp(),
						hashedOtp, user.get().getOtpExpiredAt());
				if (isValidOtp) {
					enterprises = enterprisesRepository.findByIdAndActive(Long.valueOf(user.get().getEnterpriseId()),
							true);
					if (enterprises != null && enterprises.getPrefCorp() != null) {
						user.get().setActivationStatus("activated");
						userResp = userRepository_V1.save(user.get());

						otpRespModel = new ValidateOtpRespModel();
						otpRespModel.setStatus(true);
						otpRespModel.setDescription("activated");
						otpRespModel.setMessage("success");
						otpRespModel.setAuthToken(userResp.getAuthToken());
						otpRespModel.setPrefCorp(enterprises.getPrefCorp());
					} else {
						otpRespModel = new ValidateOtpRespModel();
						otpRespModel.setStatus(false);
						otpRespModel.setDescription("pref corp id not found");
						otpRespModel.setMessage("failed");
						otpRespModel.setAuthToken(null);
						otpRespModel.setPrefCorp(null);
					}
				} else {
					otpRespModel = new ValidateOtpRespModel();
					otpRespModel.setStatus(false);
					otpRespModel.setDescription("invalid otp");
					otpRespModel.setMessage("failed");
					otpRespModel.setAuthToken(null);
					otpRespModel.setPrefCorp(null);
				}
			} else {
				otpRespModel = new ValidateOtpRespModel();
				otpRespModel.setStatus(false);
				otpRespModel.setDescription("mobile number didn't match with the AuthToken user's mobile number");
				otpRespModel.setMessage("failed");
				otpRespModel.setAuthToken(null);
				otpRespModel.setPrefCorp(null);
			}

		} else {
			otpRespModel = new ValidateOtpRespModel();
			otpRespModel.setStatus(false);
			otpRespModel.setDescription("user not found");
			otpRespModel.setMessage("failed");
			otpRespModel.setAuthToken(null);
			otpRespModel.setPrefCorp(null);
		}
		return otpRespModel;
	}

	public ValidateOtpRespModel validateOtpWithoutMobile(
			CommonResetOtpWebUsersReqModel commonResetOtpWebUsersReqModel) {
		Optional<User> user = null;
		User userResp = null;
		boolean isValidOtp = false;
		ValidateOtpRespModel otpRespModel = null;
		Enterprises enterprises = null;
		String hashedOtp =null;

		user = userRepository_V1.findByAuthTokenAndMarkAsEnabled(commonResetOtpWebUsersReqModel.getAuthToken(), true);

		if (user.isPresent()) {
			hashedOtp = passwordHashing.getPasswordHash(commonResetOtpWebUsersReqModel.getOtp());
			isValidOtp = resetOtpWebUsersInternalService.isValidOtp(user.get().getOtp(),
					hashedOtp, user.get().getOtpExpiredAt());
			if (isValidOtp) {
				enterprises = enterprisesRepository.findByIdAndActive(Long.valueOf(user.get().getEnterpriseId()), true);
				if (enterprises != null && enterprises.getPrefCorp() != null) {
					user.get().setActivationStatus("activated");
					userResp = userRepository_V1.save(user.get());

					otpRespModel = new ValidateOtpRespModel();
					otpRespModel.setStatus(true);
					otpRespModel.setDescription("activated");
					otpRespModel.setMessage("success");
					otpRespModel.setAuthToken(userResp.getAuthToken());
					otpRespModel.setPrefCorp(enterprises.getPrefCorp());
				} else {
					otpRespModel = new ValidateOtpRespModel();
					otpRespModel.setStatus(false);
					otpRespModel.setDescription("pref corp id not found");
					otpRespModel.setMessage("failed");
					otpRespModel.setAuthToken(null);
					otpRespModel.setPrefCorp(null);
				}
			} else {
				otpRespModel = new ValidateOtpRespModel();
				otpRespModel.setStatus(false);
				otpRespModel.setDescription("invalid otp");
				otpRespModel.setMessage("failed");
				otpRespModel.setAuthToken(null);
				otpRespModel.setPrefCorp(null);
			}

		} else {
			otpRespModel = new ValidateOtpRespModel();
			otpRespModel.setStatus(false);
			otpRespModel.setDescription("user not found");
			otpRespModel.setMessage("failed");
			otpRespModel.setAuthToken(null);
			otpRespModel.setPrefCorp(null);
		}
		return otpRespModel;
	}

	public ValidateOtpRespModel resetPasswordByOtp(CommonResetOtpWebUsersReqModel commonResetOtpWebUsersReqModel) {
		String viewPassword = null;
		Optional<User> user = null;
		String resetPasswordResp = null;
		ValidateOtpRespModel otpRespModel = null;
		String hashedOtp = null;

		user = userRepository_V1.findByAuthTokenAndMarkAsEnabled(commonResetOtpWebUsersReqModel.getAuthToken(), true);
		if (user.isPresent()) {
			hashedOtp = passwordHashing.getPasswordHash(commonResetOtpWebUsersReqModel.getOtp());
			byte[] decodedBytes = Base64.getDecoder().decode(commonResetOtpWebUsersReqModel.getExFlag());
			viewPassword = new String(decodedBytes);
			if (resetOtpWebUsersInternalService.isValidOtp(user.get().getOtp(),hashedOtp,
					user.get().getOtpExpiredAt())) {
				resetPasswordResp = resetPassword(commonResetOtpWebUsersReqModel.getNewPassword(),
						commonResetOtpWebUsersReqModel.getConfirmPassword(), viewPassword, user);
				if (resetPasswordResp.equals("success")) {
					otpRespModel = new ValidateOtpRespModel();
					otpRespModel.setStatus(true);
					otpRespModel.setDescription("Your password has been reset successfully!.");
					otpRespModel.setMessage("success");
				} else {
					if (resetPasswordResp.equals("exFlagNotMatch")) {
						otpRespModel = new ValidateOtpRespModel();
						otpRespModel.setStatus(false);
						otpRespModel.setDescription("confirm password didn't match with exFlag");
						otpRespModel.setMessage("confPassNotMatch");
					} else if (resetPasswordResp.equals("confPassNotMatch")) {
						otpRespModel = new ValidateOtpRespModel();
						otpRespModel.setStatus(false);
						otpRespModel.setDescription("password , confirm password didn't match");
						otpRespModel.setMessage("failed");
					} else {
						otpRespModel = new ValidateOtpRespModel();
						otpRespModel.setStatus(false);
						otpRespModel.setDescription("some error occured , please try later");
						otpRespModel.setMessage("failed");
					}
				}
			} else {
				otpRespModel = new ValidateOtpRespModel();
				otpRespModel.setStatus(false);
				otpRespModel.setDescription("invalid otp");
				otpRespModel.setMessage("failed");
			}
		} else {
			otpRespModel = new ValidateOtpRespModel();
			otpRespModel.setStatus(false);
			otpRespModel.setDescription("user not found");
			otpRespModel.setMessage("failed");
		}
		return otpRespModel;
	}

	public ValidateOtpRespModel resetPasswordFromForgot(CommonResetOtpWebUsersReqModel commonResetOtpWebUsersReqModel) {
		String viewPassword = null;
		Optional<User> user = null;
		String resetPasswordResp = null;
		ValidateOtpRespModel otpRespModel = null;

		user = userRepository_V1.findByAuthTokenAndMarkAsEnabled(commonResetOtpWebUsersReqModel.getAuthToken(), true);
		if (user.isPresent()) {
			byte[] decodedBytes = Base64.getDecoder().decode(commonResetOtpWebUsersReqModel.getExFlag());
			viewPassword = new String(decodedBytes);

			resetPasswordResp = resetPassword(commonResetOtpWebUsersReqModel.getNewPassword(),
					commonResetOtpWebUsersReqModel.getConfirmPassword(), viewPassword, user);
			if (resetPasswordResp.equals("success")) {
				// Removing the dummy token.
				removeTempToken(commonResetOtpWebUsersReqModel.getAuthToken());

				otpRespModel = new ValidateOtpRespModel();
				otpRespModel.setStatus(true);
				otpRespModel.setDescription("Your password has been reset successfully!.");
				otpRespModel.setMessage("success");
			} else {
				if (resetPasswordResp.equals("exFlagNotMatch")) {
					otpRespModel = new ValidateOtpRespModel();
					otpRespModel.setStatus(false);
					otpRespModel.setDescription("confirm password didn't match with exFlag");
					otpRespModel.setMessage("confPassNotMatch");
				} else if (resetPasswordResp.equals("confPassNotMatch")) {
					otpRespModel = new ValidateOtpRespModel();
					otpRespModel.setStatus(false);
					otpRespModel.setDescription("password , confirm password didn't match");
					otpRespModel.setMessage("failed");
				} else {
					otpRespModel = new ValidateOtpRespModel();
					otpRespModel.setStatus(false);
					otpRespModel.setDescription("some error occured , please try later");
					otpRespModel.setMessage("failed");
				}
			}

		} else {
			otpRespModel = new ValidateOtpRespModel();
			otpRespModel.setStatus(false);
			otpRespModel.setDescription("User not found.");
			otpRespModel.setMessage("failed");
		}
		return otpRespModel;
	}

	/*
	 * commonResetOtpWebUsersReqModel.getNewPassword(),
	 * commonResetOtpWebUsersReqModel.getConfirmPassword(), viewPassword, user
	 */
	public String resetPassword(String newPassword, String confirmPassword, String decExFlag, Optional<User> user) {
		String isResetSuccess = null;
		User currUser = null;
		if (newPassword.equals(confirmPassword)) {
			if (confirmPassword.equals(decExFlag)) {
				String passwordHash = passwordHashing.getPasswordHash(newPassword);
				String encryptedPswd = Base64.getEncoder().encodeToString(newPassword.getBytes());
				currUser = user.get();
				currUser.setEncryptedPassword(encryptedPswd);
				currUser.setUpdatedAt(new Timestamp(new Date().getTime()));
				currUser.setViewPwdHash(passwordHash);
				//currUser.setViewPwd(newPassword);
				currUser.setPasswordChangedAt(new Timestamp(new Date().getTime()));
				userRepository_V1.save(currUser);
				isResetSuccess = "success";
			} else {
				isResetSuccess = "exFlagNotMatch";
			}
		} else {
			isResetSuccess = "confPassNotMatch";
		}
		return isResetSuccess;
	}

	public ValidateOtpRespModel validateWebToken(CommonResetOtpWebUsersReqModel commonResetOtpWebUsersReqModel) {
		Optional<User> user = null;
		boolean validateWebActivationToken = false;
		ValidateOtpRespModel otpRespModel = null;

		try {
			user = userRepository_V1.findByMobileAndMarkAsEnabled(commonResetOtpWebUsersReqModel.getMobile(), true);
			// user =
			// userRepository_V1.findByAuthTokenAndMarkAsEnabled(commonResetOtpWebUsersReqModel.getAuthToken(),true);
//			RecordLog.writeLogFile("user present " + user.isPresent());
			if (user.isPresent()) {
				validateWebActivationToken = resetOtpWebUsersInternalService.validateWebActivationTokenAndEnable(user,
						commonResetOtpWebUsersReqModel.getActivationToken());
//				RecordLog.writeLogFile("valid web activation Token: " + validateWebActivationToken);
				if (validateWebActivationToken) {
					otpRespModel = new ValidateOtpRespModel();
					otpRespModel.setStatus(true);
					otpRespModel.setDescription("activated");
					otpRespModel.setAuthToken(user.get().getAuthToken());
					otpRespModel.setPrefCorp(user.get().getPrefNo());
					otpRespModel.setMessage("success");
					otpRespModel.setFirstTimeWebLogin(user.get().getWebSignInCount() == 0L ? true : false);
					// Code added by vikasramireddy according to discussion Mr.Partha.
					String temptoken = generateHash(user.get().getMobile());
					otpRespModel.setTemptoken(temptoken);
					updateAuthToken(temptoken, user.get().getId());

				} else {
					otpRespModel = new ValidateOtpRespModel();
					otpRespModel.setStatus(false);
					otpRespModel.setDescription("Web Activation Token Failed");
					otpRespModel.setMessage("failed");
				}
			} else {
				otpRespModel = new ValidateOtpRespModel();
				otpRespModel.setStatus(false);
				otpRespModel.setDescription("unable to fetch user details");
				otpRespModel.setMessage("failed");
			}
		} catch (Exception e) {
			RecordLog.writeLogFile("Exception occured at: "+Arrays.toString(e.getStackTrace())+" Exception name: "+e.getMessage());
			otpRespModel = new ValidateOtpRespModel();
			otpRespModel.setStatus(false);
			otpRespModel.setDescription("error occured , please try later");
			otpRespModel.setMessage("failed");
			e.printStackTrace();
		}
		return otpRespModel;
	}

	private boolean updateAuthToken(String dummytoken, Long user_id) {
		boolean status = false;

		User user = userRepository_V1.findById(user_id).orElse(null);
		if (user != null) {
			user.setAuthToken(dummytoken);
			userRepository_V1.save(user);
			status = true;
		}

		return status;
	}

	public boolean validateTempToken(String dummytoken) {
		boolean status = false;
		User user = userRepository_V1.findByAuthTokenAndMarkAsEnabled(dummytoken, true).orElse(null);
		if (user != null) {
			if (dummytoken.equals(user.getAuthToken())) {
				status = true;
			}
		}
		return status;
	}

	public boolean removeTempToken(String dummytoken) {
		boolean status = false;
		User user = userRepository_V1.findByAuthTokenAndMarkAsEnabled(dummytoken, true).orElse(null);
		if (user != null) {
			if (dummytoken.equals(user.getAuthToken())) {
				user.setAuthToken(null);
				userRepository_V1.save(user);
				status = true;
			}
		}
		return status;
	}

	private String generateHash(String mobileno) {
		String sha256hex = DigestUtils.sha256Hex(mobileno);
		return sha256hex;
	}

	public SMEMessage generateEncToken(String input) {
		SMEMessage message = new SMEMessage();
		try {
			String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
			String Serviceid = timeStamp + APIServiceID + input + timeStamp;
			Encoder encoder = Base64.getEncoder();
			String output = encoder.encodeToString(Serviceid.getBytes());
			message.setStatus(true);
			message.setMessage("success");
			message.setRecordid(output);
		} catch (Exception e) {
			RecordLog.writeLogFile("Exception occured at: "+Arrays.toString(e.getStackTrace())+" Exception name: "+e.getMessage());
			e.printStackTrace();
			message.setStatus(false);
		}

		return message;
	}

	public ValidateOtpRespModel validateWebPin(CommonResetOtpWebUsersReqModel commonResetOtpWebUsersReqModel) {
		Optional<User> user = null;
		boolean validateWebActivationToken = false;
		ValidateOtpRespModel otpRespModel = null;

		try {
			user = userRepository_V1.findByAuthTokenAndMarkAsEnabled(commonResetOtpWebUsersReqModel.getAuthToken(),
					true);
//			RecordLog.writeLogFile("user present " + user.isPresent());
			if (user.isPresent()) {
				validateWebActivationToken = resetOtpWebUsersInternalService.validateWebPinAndEnable(user,
						commonResetOtpWebUsersReqModel.getForgotPin());
//				RecordLog.writeLogFile("validateWebActivationToken " + validateWebActivationToken);
				if (validateWebActivationToken) {
					otpRespModel = new ValidateOtpRespModel();
					otpRespModel.setStatus(true);
					otpRespModel.setDescription("activated");
					otpRespModel.setAuthToken(user.get().getAuthToken());
					otpRespModel.setPrefCorp(user.get().getPrefNo());
					otpRespModel.setMessage("success");
				} else {
					otpRespModel = new ValidateOtpRespModel();
					otpRespModel.setStatus(false);
					otpRespModel.setDescription("Web Activation Token Failed");
					otpRespModel.setMessage("failed");
				}
			} else {
				otpRespModel = new ValidateOtpRespModel();
				otpRespModel.setStatus(false);
				otpRespModel.setDescription("unable to fetch user details");
				otpRespModel.setMessage("failed");
			}
		} catch (Exception e) {
			RecordLog.writeLogFile("Exception occured at: "+Arrays.toString(e.getStackTrace())+" Exception name: "+e.getMessage());
			otpRespModel = new ValidateOtpRespModel();
			otpRespModel.setStatus(false);
			otpRespModel.setDescription("error occured , please try later");
			otpRespModel.setMessage("failed");
			e.printStackTrace();
		}
		return otpRespModel;
	}

	public ValidateOtpRespModel updateUserCredentials(CommonResetOtpWebUsersReqModel commonResetOtpWebUsersReqModel) {
		Optional<User> user = null;
		User userResp = null;
		String viewPassword = null;
		String resetPasswordResp = null;
		ValidateOtpRespModel otpRespModel = null;
		Optional<ApplicationUser> applicationUser = null;
		long webSignInCount = 0;
		String webSignInCountCheck = null;
		User userResp_ = null;
		ApplicationUser applicationUserResp = null;
		ValidateOtpRespModel otpResp = null;

		try {
			user = userRepository_V1.findByAuthTokenAndMarkAsEnabled(commonResetOtpWebUsersReqModel.getAuthToken(),
					true);
			if (user.isPresent()) {
				webSignInCountCheck = String.valueOf(user.get().getWebSignInCount());
				RecordLog.writeLogFile("Web signIn count: " + webSignInCountCheck);
				if (webSignInCountCheck == "null" || webSignInCountCheck.equals("null")) {
					webSignInCount = 0l;
					user.get().setWebSignInCount(webSignInCount);
					userRepository_V1.save(user.get());
				}
				viewPassword = new String(Base64.getDecoder().decode(commonResetOtpWebUsersReqModel.getExFlag()));
				resetPasswordResp = resetPassword(commonResetOtpWebUsersReqModel.getNewPassword(),
						commonResetOtpWebUsersReqModel.getConfirmPassword(), viewPassword, user);
				if (resetPasswordResp.equals("success")) {
					user.get().setPrefNo(commonResetOtpWebUsersReqModel.getPrefNo());
					String passwordHash = passwordHashing.getPasswordHash(viewPassword);
					//user.get().setViewPwd(viewPassword);
					user.get().setViewPwdHash(passwordHash);
					userResp = userRepository_V1.save(user.get());
					RecordLog.writeLogFile("UserResp Custno : " + userResp.getCustNo() + "UserResp authToken"
							+ userResp.getAuthToken());
					if (userResp != null) {
						if(userResp.getRole() != null) {
							if(userResp.getRole().equals("external")) {
								otpRespModel = new ValidateOtpRespModel();
                          otpRespModel.setStatus(true);
                          otpRespModel.setDescription("");
                          otpRespModel.setMessage("success");
						}
						}
						
						else {
						applicationUser = applicationUserRepository.getApplicationUserSQL(userResp.getAuthToken(),
								userResp.getCustNo());
						if (applicationUser.isPresent()) {
							applicationUser.get().setPrefNo(commonResetOtpWebUsersReqModel.getPrefNo());
							applicationUserResp = applicationUserRepository.save(applicationUser.get());
							if (applicationUserResp != null) {
								webSignInCount = webSignInCount + 1;
								user.get().setWebSignInCount(webSignInCount);
								userResp_ = userRepository_V1.save(user.get());
								if (userResp_ != null) {
									otpRespModel = new ValidateOtpRespModel();
									otpRespModel.setStatus(true);
									otpRespModel.setDescription("");
									otpRespModel.setMessage("success");
								} else {
									otpRespModel = new ValidateOtpRespModel();
									otpRespModel.setStatus(false);
									otpRespModel.setDescription("unable to update in users");
									otpRespModel.setMessage("failed");
								}
							} else {
								otpRespModel = new ValidateOtpRespModel();
								otpRespModel.setStatus(false);
								otpRespModel.setDescription("unable to update in application User");
								otpRespModel.setMessage("failed");
							}

						} else {
							otpRespModel = new ValidateOtpRespModel();
							otpRespModel.setStatus(false);
							otpRespModel.setDescription("user not found in application User");
							otpRespModel.setMessage("failed");
						}
					}
					} else {
						otpRespModel = new ValidateOtpRespModel();
						otpRespModel.setStatus(false);
						otpRespModel.setDescription("unable to update the user details");
						otpRespModel.setMessage("failed");
					}
				} else {
					if (resetPasswordResp.equals("exFlagNotMatch")) {
						otpRespModel = new ValidateOtpRespModel();
						otpRespModel.setStatus(false);
						otpRespModel.setDescription("confirm password didn't match with exFlag");
						otpRespModel.setMessage("decoded exFlag NotMatch with confirm password");
					} else if (resetPasswordResp.equals("confPassNotMatch")) {
						otpRespModel = new ValidateOtpRespModel();
						otpRespModel.setStatus(false);
						otpRespModel.setDescription("password , confirm password didn't match");
						otpRespModel.setMessage("failed");
					} else {
						otpRespModel = new ValidateOtpRespModel();
						otpRespModel.setStatus(false);
						otpRespModel.setDescription("some error occured , please try later");
						otpRespModel.setMessage("failed");
					}
				}
			} else {
				otpRespModel = new ValidateOtpRespModel();
				otpRespModel.setStatus(false);
				otpRespModel.setDescription("user not found");
				otpRespModel.setMessage("failed");
			}
		} catch (Exception e) {
			RecordLog.writeLogFile("Exception occured at: "+Arrays.toString(e.getStackTrace())+" Exception name: "+e.getMessage());
			otpRespModel = new ValidateOtpRespModel();
			otpRespModel.setStatus(false);
			otpRespModel.setDescription("exception occured , please try after sometime");
			otpRespModel.setMessage("failed");
			e.printStackTrace();
		}
		return otpRespModel;
	}

	public ValidateOtpRespModel resetRequestByWebPin(CommonResetOtpWebUsersReqModel commonResetOtpWebUsersReqModel) {
		boolean validForgotPin = false;
		ValidateOtpRespModel otpRespModel = null;
//		WebUserSessionApiResp authToken = null;
		String authToken = "";

		validForgotPin = resetOtpWebUsersInternalService.validForgotPin(commonResetOtpWebUsersReqModel.getForgotPin(),
				commonResetOtpWebUsersReqModel.getPrefNo(),commonResetOtpWebUsersReqModel.getMobile());
		if (validForgotPin) {
//			authToken = resetOtpWebUsersInternalService.generateAuthenticationToken(
//					commonResetOtpWebUsersReqModel.getPrefNo(), commonResetOtpWebUsersReqModel.getPassword());

			SMEMessage status = generateDummyToken(commonResetOtpWebUsersReqModel.getPrefNo(),commonResetOtpWebUsersReqModel.getMobile());
			if (status.isStatus()) {
				authToken = status.getRecordid();
				if (authToken != null) {

					otpRespModel = new ValidateOtpRespModel();
					otpRespModel.setStatus(true);
					otpRespModel.setDescription("auth token generated");
					otpRespModel.setMessage("success");
//					otpRespModel.setAuthToken(((WebUserSessionResponse) authToken.getRecordid()).getAuthToken());
					otpRespModel.setAuthToken(authToken);
				} else {
					otpRespModel = new ValidateOtpRespModel();
					otpRespModel.setStatus(false);
					otpRespModel.setDescription("authToken generation failed");
					otpRespModel.setMessage("AuthToken generation failed");
					otpRespModel.setAuthToken(null);
				}

			} else {
				otpRespModel = new ValidateOtpRespModel();
				otpRespModel.setStatus(false);
				otpRespModel.setDescription("unable to generate auth token");
				otpRespModel.setMessage("Unable to generate auth token");
			}
		} else {
			otpRespModel = new ValidateOtpRespModel();
			otpRespModel.setStatus(false);
			otpRespModel.setDescription("Invalid PIN");
			otpRespModel.setMessage("Invalid PIN");
		}
		return otpRespModel;
	}
	
	
	
	
	
	
	public ValidateOtpRespModel resetRequestByWebPinForForgotPassword(CommonResetOtpWebUsersReqModel commonResetOtpWebUsersReqModel) {
		boolean validForgotPin = false;
		ValidateOtpRespModel otpRespModel = null;
//		WebUserSessionApiResp authToken = null;
		String authToken = "";

		validForgotPin = resetOtpWebUsersInternalService.validForgotPin(commonResetOtpWebUsersReqModel.getForgotPin(),
				commonResetOtpWebUsersReqModel.getPrefNo(),commonResetOtpWebUsersReqModel.getMobile());
		if (validForgotPin) {
//			authToken = resetOtpWebUsersInternalService.generateAuthenticationToken(
//					commonResetOtpWebUsersReqModel.getPrefNo(), commonResetOtpWebUsersReqModel.getPassword());

			SMEMessage status = generateDummyToken(commonResetOtpWebUsersReqModel.getPrefNo(),commonResetOtpWebUsersReqModel.getMobile());
			if (status.isStatus()) {
				authToken = status.getRecordid();
				if (authToken != null) {

					otpRespModel = new ValidateOtpRespModel();
					otpRespModel.setStatus(true);
					otpRespModel.setDescription("auth token generated");
					otpRespModel.setMessage("success");
//					otpRespModel.setAuthToken(((WebUserSessionResponse) authToken.getRecordid()).getAuthToken());
					otpRespModel.setAuthToken(authToken);
				} else {
					otpRespModel = new ValidateOtpRespModel();
					otpRespModel.setStatus(false);
					otpRespModel.setDescription("authToken generation failed");
					otpRespModel.setMessage("AuthToken generation failed");
					otpRespModel.setAuthToken(null);
				}

			} else {
				otpRespModel = new ValidateOtpRespModel();
				otpRespModel.setStatus(false);
				otpRespModel.setDescription("unable to generate auth token");
				otpRespModel.setMessage("Unable to generate auth token");
			}
		} else {
			otpRespModel = new ValidateOtpRespModel();
			otpRespModel.setStatus(false);
			otpRespModel.setDescription("Invalid PIN");
			otpRespModel.setMessage("Invalid PIN");
		}
		return otpRespModel;
	}

	
	

	public ValidateOtpRespModel requestResetPassword(CommonResetOtpWebUsersReqModel commonResetOtpWebUsersReqModel) {
		String currentPassword = null;
		String authToken = null;
		Optional<User> user = null;
		ValidateOtpRespModel validateOtpRespModel = null;

		currentPassword = commonResetOtpWebUsersReqModel.getPassword();
		authToken = commonResetOtpWebUsersReqModel.getAuthToken();

		try {
			user = userRepository_V1.findByAuthTokenAndMarkAsEnabled(authToken, true);
			if (user.isPresent()) {
				validateOtpRespModel = resetOtpWebUsersInternalService.generateTwoFactorAuth(user);
			} else {
				validateOtpRespModel = new ValidateOtpRespModel();
				validateOtpRespModel.setStatus(false);
				validateOtpRespModel.setMessage("user not found");
				validateOtpRespModel.setDescription("user not found");
				validateOtpRespModel.setMessageResp(null);
			}
		} catch (Exception e) {
			RecordLog.writeLogFile("Exception occured at: "+Arrays.toString(e.getStackTrace())+" Exception name: "+e.getMessage());
			validateOtpRespModel = new ValidateOtpRespModel();
			validateOtpRespModel.setStatus(false);
			validateOtpRespModel.setMessage("error occured");
			validateOtpRespModel.setDescription("error occured");
			validateOtpRespModel.setMessageResp(null);
			e.printStackTrace();
		}
		return validateOtpRespModel;
	}

	public ValidateOtpRespModel getValidateUser(CommonResetOtpWebUsersReqModel commonResetOtpWebUsersReqModel) {
		ValidateOtpRespModel validateOtpRespModel = null;
		try {
			validateOtpRespModel = resetOtpWebUsersInternalService
					.sendOtpForExternalUser(commonResetOtpWebUsersReqModel.getPrefNo(),commonResetOtpWebUsersReqModel.getMobile());
		} catch (Exception e) {
			RecordLog.writeLogFile("Exception occured at: "+Arrays.toString(e.getStackTrace())+" Exception name: "+e.getMessage());
			e.printStackTrace();
			validateOtpRespModel = new ValidateOtpRespModel();
			validateOtpRespModel.setStatus(false);
			validateOtpRespModel.setMessage("error occured");
			validateOtpRespModel.setDescription("error occured");
			validateOtpRespModel.setMessageResp(null);
		}
		return validateOtpRespModel;
	}
	
	
	
	public ValidateOtpRespModel getValidateUserForForgotPassword(CommonResetOtpWebUsersReqModel commonResetOtpWebUsersReqModel) {
		ValidateOtpRespModel validateOtpRespModel = null;
		try {
			validateOtpRespModel = resetOtpWebUsersInternalService
					.sendOtpForExternalUser(commonResetOtpWebUsersReqModel.getPrefNo(),commonResetOtpWebUsersReqModel.getMobile());
		} catch (Exception e) {
			RecordLog.writeLogFile("Exception occured at: "+Arrays.toString(e.getStackTrace())+" Exception name: "+e.getMessage());
			e.printStackTrace();
			validateOtpRespModel = new ValidateOtpRespModel();
			validateOtpRespModel.setStatus(false);
			validateOtpRespModel.setMessage("error occured");
			validateOtpRespModel.setDescription("error occured");
			validateOtpRespModel.setMessageResp(null);
		}
		return validateOtpRespModel;
	}
	

	public ValidateOtpRespModel getVerifyOtpWeb(CommonResetOtpWebUsersReqModel commonResetOtpWebUsersReqModel) {
		Enterprises enterprise = null;
		Optional<User> user = null;
		BulkUpload bulkUpload = null;
		BulkUpload respBulkUpload = null;
		ValidateOtpRespModel validateOtpRespModel = null;
		boolean isSamePassword = false;
		boolean isValidOtp = false;

		enterprise = enterprisesRepository.findByPrefCorpAndActive(commonResetOtpWebUsersReqModel.getPrefCorp(), true);
		if (enterprise != null) {
			user = userRepository_V1.findByAuthToken(commonResetOtpWebUsersReqModel.getAuthToken());
			if (user.isPresent()) {
				if (user.get().getViewPwdHash() != null && user.get().getOtp() != null) {
					isSamePassword = resetOtpWebUsersInternalService.checkPassword(user.get().getViewPwdHash(),
							commonResetOtpWebUsersReqModel.getPassword());
					String hashedOtp = passwordHashing.getPasswordHash(commonResetOtpWebUsersReqModel.getOtp());
					isValidOtp = resetOtpWebUsersInternalService.isValidOtp(user.get().getOtp(),
							hashedOtp, user.get().getOtpExpiredAt());
					if (isSamePassword && isValidOtp) {
						bulkUpload = new BulkUpload();
						bulkUpload.setChecksum(commonResetOtpWebUsersReqModel.getCheckSum());
						bulkUpload.setCreatedAt(new Timestamp(new Date().getTime()));
						bulkUpload.setUpdatedAt(new Timestamp(new Date().getTime()));
						respBulkUpload = bulkUploadRepository.save(bulkUpload);

						if (respBulkUpload != null) {
							validateOtpRespModel = new ValidateOtpRespModel();
							validateOtpRespModel.setStatus(true);
							validateOtpRespModel.setDescription(String.valueOf(respBulkUpload.getId()));
							validateOtpRespModel.setMessage("success");
							validateOtpRespModel.setMessageResp(null);
						} else {
							validateOtpRespModel = new ValidateOtpRespModel();
							validateOtpRespModel.setStatus(false);
							validateOtpRespModel.setDescription("unable to store bulkupload");
							validateOtpRespModel.setMessage("failed");
							validateOtpRespModel.setMessageResp(null);
						}
					} else {
						validateOtpRespModel = new ValidateOtpRespModel();
						validateOtpRespModel.setStatus(false);
						if (!isSamePassword && !isValidOtp) {
							validateOtpRespModel.setDescription("Password and otp didn't match");
							validateOtpRespModel.setMessage("Password and otp didn't match");
						} else if (!isSamePassword && isValidOtp) {
							validateOtpRespModel.setDescription("Incorrect Password. Please Enter Correct Password.");
							validateOtpRespModel.setMessage("Incorrect Password. Please Enter Correct Password.");
						} else if (isSamePassword && !isValidOtp) {
							validateOtpRespModel.setDescription("Incorrect OTP. Please Enter Correct OTP.");
							validateOtpRespModel.setMessage("Incorrect OTP. Please Enter Correct OTP.");
						}
						validateOtpRespModel.setMessageResp(null);
					}
				} else {
					validateOtpRespModel = new ValidateOtpRespModel();
					validateOtpRespModel.setStatus(false);
					validateOtpRespModel.setDescription("User password or otp is empty");
					validateOtpRespModel.setMessage("failed");
					validateOtpRespModel.setMessageResp(null);
				}
			} else {
				validateOtpRespModel = new ValidateOtpRespModel();
				validateOtpRespModel.setStatus(false);
				validateOtpRespModel.setDescription("user not found");
				validateOtpRespModel.setMessage("failed");
				validateOtpRespModel.setMessageResp(null);
			}
		} else {
			validateOtpRespModel = new ValidateOtpRespModel();
			validateOtpRespModel.setStatus(false);
			validateOtpRespModel.setDescription("enterprise not found");
			validateOtpRespModel.setMessage("failed");
			validateOtpRespModel.setMessageResp(null);
		}
		return validateOtpRespModel;
	}

	public ValidateOtpRespModel generateotpweb(CommonResetOtpWebUsersReqModel commonResetOtpWebUsersReqModel) {
		Enterprises enterprise = null;
		Optional<User> user = null;
		ValidateOtpRespModel validateOtpRespModel = null;

		enterprise = enterprisesRepository.findByPrefCorpAndActive(commonResetOtpWebUsersReqModel.getPrefCorp(), true);
		if (enterprise != null) {
			user = userRepository_V1.findByAuthToken(commonResetOtpWebUsersReqModel.getAuthToken());
			if (user.isPresent()) {
				validateOtpRespModel = resetOtpWebUsersInternalService.generateOtpWebInternal(user);
			} else {
				validateOtpRespModel = new ValidateOtpRespModel();
				validateOtpRespModel.setStatus(false);
				validateOtpRespModel.setMessage("user not found");
				validateOtpRespModel.setDescription("failed");
				validateOtpRespModel.setMessageResp(null);
			}
		} else {
			validateOtpRespModel = new ValidateOtpRespModel();
			validateOtpRespModel.setStatus(false);
			validateOtpRespModel.setMessage("enterprise not found");
			validateOtpRespModel.setDescription("failed");
			validateOtpRespModel.setMessageResp(null);
		}
		return validateOtpRespModel;
	}

	public ValidateOtpRespModel activate(CommonResetOtpWebUsersReqModel commonResetOtpWebUsersReqModel) {
		ValidateOtpRespModel validateOtpRespModel = null;
		validateOtpRespModel = resetOtpWebUsersInternalService.verifyCaptcha_v1(
				commonResetOtpWebUsersReqModel.getMobile(), commonResetOtpWebUsersReqModel.getCaptcha());
		return validateOtpRespModel;
	}

	/**
	 * @param commonResetOtpWebUsersReqModel
	 * @return SMEMessage
	 */
	public ValidateOtpRespModel reset_request_by_otp(CommonResetOtpWebUsersReqModel commonResetOtpWebUsersReqModel) {
		ValidateOtpRespModel validateOtpRespModel = new ValidateOtpRespModel();
		validateOtpRespModel = validateOtp(commonResetOtpWebUsersReqModel);
		if (validateOtpRespModel.isStatus()) {
			// Added this change to ensure with out a dummy token re-set password cannot be
			// performed.
			SMEMessage status = generateDummyToken(commonResetOtpWebUsersReqModel.getPrefNo(),commonResetOtpWebUsersReqModel.getMobile());
			if (status.isStatus()) {
				validateOtpRespModel.setAuthToken(status.getRecordid());
			}
		}
		return validateOtpRespModel;
	}

	public SMEMessage generateDummyToken(String pref_no,String mobile) {

		SMEMessage message = new SMEMessage();
		Optional<User> user = userRepository_V1.findByPrefNoAndMobileAndMarkAsEnabled(pref_no,mobile,true);
		if (user.isPresent()) {
			String temptoken = generateHash(user.get().getMobile());
			User usertemp = user.get();
			usertemp.setAuthToken(temptoken);
			userRepository_V1.save(usertemp);
			message.setStatus(true);
			message.setRecordid(temptoken);
		}
		return message;
	}

	public ValidateOtpRespModel checkUserPrefNoAndMarkAsEnabled(
			CommonResetOtpWebUsersReqModel commonResetOtpWebUsersReqModel) {
		ValidateOtpRespModel validateOtpRespModel = new ValidateOtpRespModel();
		String hashPwd = passwordHashing.getPasswordHash(commonResetOtpWebUsersReqModel.getViewPassword());
		Optional<User> user = userRepository_V1.findByPrefNoAndMobileAndMarkAsEnabled(commonResetOtpWebUsersReqModel.getPrefNo(),commonResetOtpWebUsersReqModel.getMobile(),
				true);
		
		if (user.isPresent()) {
			validateOtpRespModel.setStatus(true);
			validateOtpRespModel.setDescription("User Already Available with pref no and marked");
			validateOtpRespModel.setMessage("success");
		} else {
			validateOtpRespModel.setStatus(false);
			validateOtpRespModel.setDescription("checkUserPrefNoAndMarkAsEnabled success");
			validateOtpRespModel.setMessage("success");
		}
		if(user.get().getViewPwdHash().equals(hashPwd)) {
//			RecordLog.writeLogFile("<=================================>1111111");
			validateOtpRespModel.setStatus(true);
			validateOtpRespModel.setDescription("correct Current Password");
			validateOtpRespModel.setMessage("success");
		}
		else {
//			RecordLog.writeLogFile("<=================================>22222222");
			validateOtpRespModel.setStatus(false);
			validateOtpRespModel.setDescription("Incorrect Current Password");
			validateOtpRespModel.setMessage("success");
		}
		return validateOtpRespModel;
	}
	
	
	public ValidateOtpRespModel checkUserPrefNoAndMarkAsEnabledForNewLogin(
			CommonResetOtpWebUsersReqModel commonResetOtpWebUsersReqModel) {
		ValidateOtpRespModel validateOtpRespModel = new ValidateOtpRespModel();
		Optional<User> user = userRepository_V1.findByPrefNoAndMobileAndMarkAsEnabled(commonResetOtpWebUsersReqModel.getPrefNo(),commonResetOtpWebUsersReqModel.getMobile(),
				true);
		
		if (user.isPresent()) {
			validateOtpRespModel.setStatus(true);
			validateOtpRespModel.setDescription("User Already Available with pref no and marked");
			validateOtpRespModel.setMessage("success");
		} else {
			validateOtpRespModel.setStatus(true);
			validateOtpRespModel.setDescription("checkUserPrefNoAndMarkAsEnabled success");
			validateOtpRespModel.setMessage("success");
		}
//		if(user.get().getViewPwd().equals(commonResetOtpWebUsersReqModel.getViewPassword())) {
////			RecordLog.writeLogFile("<=================================>1111111");
//			validateOtpRespModel.setStatus(true);
//			validateOtpRespModel.setDescription("correct Current Password");
//			validateOtpRespModel.setMessage("success");
//		}
//		else {
////			RecordLog.writeLogFile("<=================================>22222222");
//			validateOtpRespModel.setStatus(false);
//			validateOtpRespModel.setDescription("Incorrect Current Password");
//			validateOtpRespModel.setMessage("success");
//		}
		return validateOtpRespModel;
	}
	
	

}