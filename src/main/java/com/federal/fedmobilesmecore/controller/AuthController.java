package com.federal.fedmobilesmecore.controller;

import java.util.Arrays;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.federal.fedmobilesmecore.config.RecordLog;
import com.federal.fedmobilesmecore.model.CommonResetOtpWebUsersReqModel;
import com.federal.fedmobilesmecore.model.EncryptionRequest;
import com.federal.fedmobilesmecore.model.SMEMessage;
import com.federal.fedmobilesmecore.model.ValidateOtpRespModel;
import com.federal.fedmobilesmecore.service.ResetOtpWebUsersService;

@RestController
@RequestMapping(path = "/core/auth")
public class AuthController {
	private static final Logger log4j = LogManager.getLogger(AuthController.class);
	@Autowired
	ResetOtpWebUsersService resetOtpWebUsersService;

	@io.swagger.v3.oas.annotations.Operation(summary = "activated")
	@PostMapping(path = "/activated", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ValidateOtpRespModel activated(@RequestBody CommonResetOtpWebUsersReqModel commonResetOtpWebUsersReqModel) {
		RecordLog.writeLogFile("AuthController /activated api is calling. API request: "+commonResetOtpWebUsersReqModel);
		ValidateOtpRespModel validateOtpRespModel = null;
		if (commonResetOtpWebUsersReqModel != null) {
			if (StringUtils.isNotEmpty(commonResetOtpWebUsersReqModel.getMobile())
					&& StringUtils.isNotEmpty(commonResetOtpWebUsersReqModel.getCaptcha())) {
				validateOtpRespModel = resetOtpWebUsersService.activate(commonResetOtpWebUsersReqModel);
				RecordLog.writeLogFile("User validated successFully");
			} else {
				validateOtpRespModel = new ValidateOtpRespModel();
				validateOtpRespModel.setStatus(true);
				validateOtpRespModel.setDescription("mandatory fields not passed");
				validateOtpRespModel.setMessage("failed");
				validateOtpRespModel.setAuthToken(null);
				validateOtpRespModel.setPrefCorp(null);
			}
		} else {
			validateOtpRespModel = new ValidateOtpRespModel();
			validateOtpRespModel.setStatus(true);
			validateOtpRespModel.setDescription("Request Body can not be empty");
			validateOtpRespModel.setMessage("failed");
			validateOtpRespModel.setAuthToken(null);
			validateOtpRespModel.setPrefCorp(null);
		}
		RecordLog.writeLogFile("AuthController /activated api completed. API response: "+validateOtpRespModel);
		return validateOtpRespModel;
	}
	
	@io.swagger.v3.oas.annotations.Operation(summary = "Validate Web Token")
	@PostMapping(path = "/validatewebtoken", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> validateWebToken(
			@RequestBody CommonResetOtpWebUsersReqModel commonResetOtpWebUsersReqModel) {
		RecordLog.writeLogFile("AuthController /validatewebtoken api is calling. API request: "+commonResetOtpWebUsersReqModel);
		ValidateOtpRespModel validateOtpRespModel = null;
		if (commonResetOtpWebUsersReqModel != null) {
			if (StringUtils.isNotEmpty(commonResetOtpWebUsersReqModel.getActivationToken())
					&& StringUtils.isNotEmpty(commonResetOtpWebUsersReqModel.getMobile())) {
				validateOtpRespModel = resetOtpWebUsersService.validateWebToken(commonResetOtpWebUsersReqModel);
			} else {
				validateOtpRespModel = new ValidateOtpRespModel();
				validateOtpRespModel.setStatus(true);
				validateOtpRespModel.setDescription("mandatory fields not passed , AuthToken & OTP");
				validateOtpRespModel.setMessage("failed");
				validateOtpRespModel.setAuthToken(null);
				validateOtpRespModel.setPrefCorp(null);
			}
		} else {
			validateOtpRespModel = new ValidateOtpRespModel();
			validateOtpRespModel.setStatus(true);
			validateOtpRespModel.setDescription("Request Body can not be empty");
			validateOtpRespModel.setMessage("failed");
			validateOtpRespModel.setAuthToken(null);
			validateOtpRespModel.setPrefCorp(null);
		}
		RecordLog.writeLogFile("AuthController /validatewebtoken api completed. API response: "+validateOtpRespModel);
		return ResponseEntity.ok(validateOtpRespModel);
	}

	@io.swagger.v3.oas.annotations.Operation(summary = "Validate otp")
	@PostMapping(path = "/validateotp", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> validateOtp(@RequestBody CommonResetOtpWebUsersReqModel commonResetOtpWebUsersReqModel) {
		RecordLog.writeLogFile("AuthController /validateotp api is calling. API request: "+commonResetOtpWebUsersReqModel);
		ValidateOtpRespModel validateOtpRespModel = null;
		if (commonResetOtpWebUsersReqModel != null) {
			if (StringUtils.isNotEmpty(commonResetOtpWebUsersReqModel.getOtp())
					&& StringUtils.isNotEmpty(commonResetOtpWebUsersReqModel.getMobile())) {
				validateOtpRespModel = resetOtpWebUsersService.validateOtp(commonResetOtpWebUsersReqModel);
			} else {
				validateOtpRespModel = new ValidateOtpRespModel();
				validateOtpRespModel.setStatus(true);
				validateOtpRespModel.setDescription("mandatory fields not passed , AuthToken & OTP");
				validateOtpRespModel.setMessage("failed");
				validateOtpRespModel.setAuthToken(null);
				validateOtpRespModel.setPrefCorp(null);
			}
		} else {
			validateOtpRespModel = new ValidateOtpRespModel();
			validateOtpRespModel.setStatus(true);
			validateOtpRespModel.setDescription("Request Body can not be empty");
			validateOtpRespModel.setMessage("failed");
			validateOtpRespModel.setAuthToken(null);
			validateOtpRespModel.setPrefCorp(null);
		}
		RecordLog.writeLogFile("AuthController /validateotp api completed. API response: "+validateOtpRespModel);
		return ResponseEntity.ok(validateOtpRespModel);
	}

	@io.swagger.v3.oas.annotations.Operation(summary = "Update User Credentials")
	@PostMapping(path = "/updateusercredentials", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> updateUserCredentials(
			@RequestBody CommonResetOtpWebUsersReqModel commonResetOtpWebUsersReqModel) {
		RecordLog.writeLogFile("AuthController /updateusercredentials api is calling. API request: "+commonResetOtpWebUsersReqModel);
		ValidateOtpRespModel validateOtpRespModel = null;
		if (commonResetOtpWebUsersReqModel != null) {
			if (StringUtils.isNotEmpty(commonResetOtpWebUsersReqModel.getAuthToken())
					&& StringUtils.isNotEmpty(commonResetOtpWebUsersReqModel.getPrefNo())
					&& StringUtils.isNotEmpty(commonResetOtpWebUsersReqModel.getViewPassword()) 
					&& StringUtils.isNotEmpty(commonResetOtpWebUsersReqModel.getNewPassword())
					&& StringUtils.isNotEmpty(commonResetOtpWebUsersReqModel.getConfirmPassword())
					&& StringUtils.isNotEmpty(commonResetOtpWebUsersReqModel.getExFlag())) {
				// Validate temptoken here. If it is valid go for the next step.
				boolean status = resetOtpWebUsersService
						.validateTempToken(commonResetOtpWebUsersReqModel.getAuthToken());
				if (status) {
					// check user by perf_num and marked enable by sahil
					validateOtpRespModel = resetOtpWebUsersService
							.checkUserPrefNoAndMarkAsEnabled(commonResetOtpWebUsersReqModel);
					RecordLog.writeLogFile("validateOtpRespModel-?>"+validateOtpRespModel);
					if (!validateOtpRespModel.isStatus()) {
						return ResponseEntity.ok(validateOtpRespModel);
					}
					validateOtpRespModel = resetOtpWebUsersService
							.updateUserCredentials(commonResetOtpWebUsersReqModel);
					resetOtpWebUsersService.removeTempToken(commonResetOtpWebUsersReqModel.getAuthToken());
				} else {
					validateOtpRespModel = new ValidateOtpRespModel();
					validateOtpRespModel.setStatus(false);
					validateOtpRespModel.setDescription(
							"Temp token is invalid. Pass the token generated from validatewebtoken API.");
					validateOtpRespModel.setMessage("Invalid Auth Token");
					validateOtpRespModel.setAuthToken(null);
					validateOtpRespModel.setPrefCorp(null);
				}

			} else {
				validateOtpRespModel = new ValidateOtpRespModel();
				validateOtpRespModel.setStatus(true);
				validateOtpRespModel.setDescription("mandatory fields not passed");
				validateOtpRespModel.setMessage("failed");
				validateOtpRespModel.setAuthToken(null);
				validateOtpRespModel.setPrefCorp(null);
			}
		} else {
			validateOtpRespModel = new ValidateOtpRespModel();
			validateOtpRespModel.setStatus(true);
			validateOtpRespModel.setDescription("Request Body can not be empty");
			validateOtpRespModel.setMessage("failed");
			validateOtpRespModel.setAuthToken(null);
			validateOtpRespModel.setPrefCorp(null);
		}
		RecordLog.writeLogFile("AuthController /updateusercredentials api completed. API response: "+validateOtpRespModel);
		return ResponseEntity.ok(validateOtpRespModel);
	}
	
	
	
	
	
	@io.swagger.v3.oas.annotations.Operation(summary = "Update User Credentials For New Login")
	@PostMapping(path = "/updateusercredentialsfornewlogin", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> updateusercredentialsfornewlogin(
			@RequestBody CommonResetOtpWebUsersReqModel commonResetOtpWebUsersReqModel) {
		ValidateOtpRespModel validateOtpRespModel = null;
		if (commonResetOtpWebUsersReqModel != null) {
			if (StringUtils.isNotEmpty(commonResetOtpWebUsersReqModel.getAuthToken())
					&& StringUtils.isNotEmpty(commonResetOtpWebUsersReqModel.getPrefNo())
					&& StringUtils.isNotEmpty(commonResetOtpWebUsersReqModel.getNewPassword())
					&& StringUtils.isNotEmpty(commonResetOtpWebUsersReqModel.getConfirmPassword())
					&& StringUtils.isNotEmpty(commonResetOtpWebUsersReqModel.getExFlag())) {
				// Validate temptoken here. If it is valid go for the next step.
				boolean status = resetOtpWebUsersService
						.validateTempToken(commonResetOtpWebUsersReqModel.getAuthToken());
				if (status) {
					// check user by perf_num and marked enable by sahil
					validateOtpRespModel = resetOtpWebUsersService
							.checkUserPrefNoAndMarkAsEnabledForNewLogin(commonResetOtpWebUsersReqModel);
					RecordLog.writeLogFile("validateOtpRespModel--------------------?>"+validateOtpRespModel);
					if (!validateOtpRespModel.isStatus()) {
						return ResponseEntity.ok(validateOtpRespModel);
					}
					validateOtpRespModel = resetOtpWebUsersService
							.updateUserCredentials(commonResetOtpWebUsersReqModel);
					resetOtpWebUsersService.removeTempToken(commonResetOtpWebUsersReqModel.getAuthToken());
				} else {
					validateOtpRespModel = new ValidateOtpRespModel();
					validateOtpRespModel.setStatus(false);
					validateOtpRespModel.setDescription(
							"Temp token is invalid. Pass the token generated from validatewebtoken API.");
					validateOtpRespModel.setMessage("Invalid Auth Token");
					validateOtpRespModel.setAuthToken(null);
					validateOtpRespModel.setPrefCorp(null);
				}

			} else {
				validateOtpRespModel = new ValidateOtpRespModel();
				validateOtpRespModel.setStatus(true);
				validateOtpRespModel.setDescription("mandatory fields not passed");
				validateOtpRespModel.setMessage("failed");
				validateOtpRespModel.setAuthToken(null);
				validateOtpRespModel.setPrefCorp(null);
			}
		} else {
			validateOtpRespModel = new ValidateOtpRespModel();
			validateOtpRespModel.setStatus(true);
			validateOtpRespModel.setDescription("Request Body can not be empty");
			validateOtpRespModel.setMessage("failed");
			validateOtpRespModel.setAuthToken(null);
			validateOtpRespModel.setPrefCorp(null);
		}
		return ResponseEntity.ok(validateOtpRespModel);
	}

	
	
	

	// Moved here from resetcontroller because it should be open for all.
	@io.swagger.v3.oas.annotations.Operation(summary = "validate user")
	@PostMapping(path = "/validateuser", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> validateUser(@RequestBody CommonResetOtpWebUsersReqModel commonResetOtpWebUsersReqModel) {
		RecordLog.writeLogFile("AuthController /validateuser api is calling. API request: "+commonResetOtpWebUsersReqModel);
		ValidateOtpRespModel validateOtpRespModel = null;
		try {
			if (commonResetOtpWebUsersReqModel != null) {
				if (StringUtils.isNotEmpty(commonResetOtpWebUsersReqModel.getMobile())
						&& StringUtils.isNotEmpty(commonResetOtpWebUsersReqModel.getPassword())
						&& StringUtils.isNotEmpty(commonResetOtpWebUsersReqModel.getPrefNo())) {
					validateOtpRespModel = resetOtpWebUsersService.getValidateUser(commonResetOtpWebUsersReqModel);
				} else {
					validateOtpRespModel = new ValidateOtpRespModel();
					validateOtpRespModel.setStatus(true);
					validateOtpRespModel.setDescription("mandatory fields not passed");
					validateOtpRespModel.setMessage("failed");
					validateOtpRespModel.setAuthToken(null);
					validateOtpRespModel.setPrefCorp(null);
				}
			} else {
				validateOtpRespModel = new ValidateOtpRespModel();
				validateOtpRespModel.setStatus(true);
				validateOtpRespModel.setDescription("Request Body can not be empty");
				validateOtpRespModel.setMessage("failed");
				validateOtpRespModel.setAuthToken(null);
				validateOtpRespModel.setPrefCorp(null);
			}
		} catch (Exception e) {
			RecordLog.writeLogFile("Exception occured at: for mobile "+commonResetOtpWebUsersReqModel.getMobile() +Arrays.toString(e.getStackTrace())+" Exception name: "+e.getMessage());
			e.printStackTrace();
			validateOtpRespModel = new ValidateOtpRespModel();
			validateOtpRespModel.setStatus(false);
			validateOtpRespModel.setDescription("error occured , please try again later");
			validateOtpRespModel.setMessage("failed");
		}
		RecordLog.writeLogFile("AuthController /validateuser api completed. API response for mobile: "+commonResetOtpWebUsersReqModel.getMobile() +validateOtpRespModel);
		return ResponseEntity.ok(validateOtpRespModel);
	}
	
	
	
	
	
	
	
	@io.swagger.v3.oas.annotations.Operation(summary = "validate user")
	@PostMapping(path = "/validateuserForForgotPassword", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> validateUservalidateuserForForgotPassword(@RequestBody CommonResetOtpWebUsersReqModel commonResetOtpWebUsersReqModel) {
		RecordLog.writeLogFile("AuthController /validateuser api is calling. API request: "+commonResetOtpWebUsersReqModel);
		ValidateOtpRespModel validateOtpRespModel = null;
		try {
			if (commonResetOtpWebUsersReqModel != null) {
				if (StringUtils.isNotEmpty(commonResetOtpWebUsersReqModel.getMobile())
						&& StringUtils.isNotEmpty(commonResetOtpWebUsersReqModel.getPrefNo())) {
					validateOtpRespModel = resetOtpWebUsersService.getValidateUserForForgotPassword(commonResetOtpWebUsersReqModel);
				} else {
					validateOtpRespModel = new ValidateOtpRespModel();
					validateOtpRespModel.setStatus(true);
					validateOtpRespModel.setDescription("mandatory fields not passed");
					validateOtpRespModel.setMessage("failed");
					validateOtpRespModel.setAuthToken(null);
					validateOtpRespModel.setPrefCorp(null);
				}
			} else {
				validateOtpRespModel = new ValidateOtpRespModel();
				validateOtpRespModel.setStatus(true);
				validateOtpRespModel.setDescription("Request Body can not be empty");
				validateOtpRespModel.setMessage("failed");
				validateOtpRespModel.setAuthToken(null);
				validateOtpRespModel.setPrefCorp(null);
			}
		} catch (Exception e) {
			RecordLog.writeLogFile("Exception occured at: for mobile "+commonResetOtpWebUsersReqModel.getMobile()+Arrays.toString(e.getStackTrace())+" Exception name: "+e.getMessage());
			e.printStackTrace();
			validateOtpRespModel = new ValidateOtpRespModel();
			validateOtpRespModel.setStatus(false);
			validateOtpRespModel.setDescription("error occured , please try again later");
			validateOtpRespModel.setMessage("failed");
		}
		RecordLog.writeLogFile("AuthController /validateuser api completed. API response:for mobile "+commonResetOtpWebUsersReqModel.getMobile() +validateOtpRespModel);
		return ResponseEntity.ok(validateOtpRespModel);
	}
	
	
	
	
	
	
	@io.swagger.v3.oas.annotations.Operation(summary = "Reset Request By Web Pin")
	@PostMapping(path = "/resetrequestbywebpin", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> resetRequestByWebPin(
			@RequestBody CommonResetOtpWebUsersReqModel commonResetOtpWebUsersReqModel) {
		RecordLog.writeLogFile("AuthController /resetrequestbywebpin api is calling. API request: "+commonResetOtpWebUsersReqModel);
		ValidateOtpRespModel otpRespModel = null;
		if (commonResetOtpWebUsersReqModel != null) {
			if (StringUtils.isNotEmpty(commonResetOtpWebUsersReqModel.getPassword())
					&& StringUtils.isNotEmpty(commonResetOtpWebUsersReqModel.getMobile())
					&& StringUtils.isNotEmpty(commonResetOtpWebUsersReqModel.getPrefNo())
					&& StringUtils.isNotEmpty(commonResetOtpWebUsersReqModel.getForgotPin())) {
				otpRespModel = resetOtpWebUsersService.resetRequestByWebPin(commonResetOtpWebUsersReqModel);
			} else {
				otpRespModel = new ValidateOtpRespModel();
				otpRespModel.setStatus(false);
				otpRespModel.setDescription("required parameters missing");
				otpRespModel.setMessage("failed");
			}
		} else {
			otpRespModel = new ValidateOtpRespModel();
			otpRespModel.setStatus(false);
			otpRespModel.setDescription("request body can not be empty");
			otpRespModel.setMessage("failed");
		}
		RecordLog.writeLogFile("AuthController /resetrequestbywebpin api completed. API response: "+otpRespModel);
		return ResponseEntity.ok(otpRespModel);
	}
	
	
	
	
	
	
	@io.swagger.v3.oas.annotations.Operation(summary = "Reset Request By Web Pin")
	@PostMapping(path = "/resetrequestbywebpinForForgotPassword", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> resetrequestbywebpinForForgotPassword(
			@RequestBody CommonResetOtpWebUsersReqModel commonResetOtpWebUsersReqModel) {
		RecordLog.writeLogFile("AuthController /resetrequestbywebpin api is calling. API request: "+commonResetOtpWebUsersReqModel);
		ValidateOtpRespModel otpRespModel = null;
		if (commonResetOtpWebUsersReqModel != null) {
			if (StringUtils.isNotEmpty(commonResetOtpWebUsersReqModel.getMobile())
					&& StringUtils.isNotEmpty(commonResetOtpWebUsersReqModel.getPrefNo())
					&& StringUtils.isNotEmpty(commonResetOtpWebUsersReqModel.getForgotPin())) {
				otpRespModel = resetOtpWebUsersService.resetRequestByWebPinForForgotPassword(commonResetOtpWebUsersReqModel);
			} else {
				otpRespModel = new ValidateOtpRespModel();
				otpRespModel.setStatus(false);
				otpRespModel.setDescription("required parameters missing");
				otpRespModel.setMessage("failed");
			}
		} else {
			otpRespModel = new ValidateOtpRespModel();
			otpRespModel.setStatus(false);
			otpRespModel.setDescription("request body can not be empty");
			otpRespModel.setMessage("failed");
		}
		RecordLog.writeLogFile("AuthController /resetrequestbywebpin api completed. API response: "+otpRespModel);
		return ResponseEntity.ok(otpRespModel);
	}
	
	
	
	
	

	@io.swagger.v3.oas.annotations.Operation(summary = "reset_request_by_otp")
	@PostMapping(path = "/reset_request_by_otp", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> reset_request_by_otp(
			@RequestBody CommonResetOtpWebUsersReqModel commonResetOtpWebUsersReqModel) {
		RecordLog.writeLogFile("AuthController /reset_request_by_otp api is calling. API request: "+commonResetOtpWebUsersReqModel);
		ValidateOtpRespModel validateOtpRespModel = null;

		try {
			if (commonResetOtpWebUsersReqModel != null) {
				if (StringUtils.isNotEmpty(commonResetOtpWebUsersReqModel.getOtp())
						&& StringUtils.isNotEmpty(commonResetOtpWebUsersReqModel.getMobile())
						&& StringUtils.isNotEmpty(commonResetOtpWebUsersReqModel.getPrefNo())) {
					validateOtpRespModel = resetOtpWebUsersService.reset_request_by_otp(commonResetOtpWebUsersReqModel);
				} else {
					validateOtpRespModel = new ValidateOtpRespModel();
					validateOtpRespModel.setStatus(true);
					validateOtpRespModel.setDescription("OTP,Mobile,Pref No are mandatory");
					validateOtpRespModel.setMessage("failed");
				}
			} else {
				validateOtpRespModel = new ValidateOtpRespModel();
				validateOtpRespModel.setStatus(true);
				validateOtpRespModel.setDescription("Request Body can not be empty");
				validateOtpRespModel.setMessage("failed");
			}
		} catch (Exception e) {
			RecordLog.writeLogFile("Exception occured at:for mobile  "+commonResetOtpWebUsersReqModel.getMobile() +Arrays.toString(e.getStackTrace())+" Exception name: "+e.getMessage());
			e.printStackTrace();
			validateOtpRespModel = new ValidateOtpRespModel();
			validateOtpRespModel.setStatus(false);
			validateOtpRespModel.setDescription("error occured , please try again later");
			validateOtpRespModel.setMessage("failed");
		}
		RecordLog.writeLogFile("AuthController /reset_request_by_otp api completed. API response: for mobile "+commonResetOtpWebUsersReqModel.getMobile() +validateOtpRespModel);
		return ResponseEntity.ok(validateOtpRespModel);
	}

	@io.swagger.v3.oas.annotations.Operation(summary = "Reset Password From forgot password menu.")
	@PostMapping(path = "/reset", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> reset_password(
			@RequestBody CommonResetOtpWebUsersReqModel commonResetOtpWebUsersReqModel) {
		RecordLog.writeLogFile("AuthController /reset api is calling. API request: "+commonResetOtpWebUsersReqModel);
		ValidateOtpRespModel validateOtpRespModel = null;

		try {
			if (commonResetOtpWebUsersReqModel != null) {
				if (StringUtils.isNotEmpty(commonResetOtpWebUsersReqModel.getNewPassword())
						&& StringUtils.isNotEmpty(commonResetOtpWebUsersReqModel.getConfirmPassword())
						&& StringUtils.isNotEmpty(commonResetOtpWebUsersReqModel.getExFlag())
						&& StringUtils.isNotEmpty(commonResetOtpWebUsersReqModel.getAuthToken())) {
					validateOtpRespModel = resetOtpWebUsersService
							.resetPasswordFromForgot(commonResetOtpWebUsersReqModel);
				} else {
					validateOtpRespModel = new ValidateOtpRespModel();
					validateOtpRespModel.setStatus(true);
					validateOtpRespModel
							.setDescription("New password,Confirm Password,EXFLAG,Auth token are mandatory");
					validateOtpRespModel.setMessage("failed");
				}
			} else {
				validateOtpRespModel = new ValidateOtpRespModel();
				validateOtpRespModel.setStatus(true);
				validateOtpRespModel.setDescription("Request Body can not be empty");
				validateOtpRespModel.setMessage("failed");
			}
		} catch (Exception e) {
			RecordLog.writeLogFile("Exception occured at:for mobile  "+commonResetOtpWebUsersReqModel.getMobile() +Arrays.toString(e.getStackTrace())+" Exception name: "+e.getMessage());
			e.printStackTrace();
			validateOtpRespModel = new ValidateOtpRespModel();
			validateOtpRespModel.setStatus(false);
			validateOtpRespModel.setDescription("error occured , please try again later");
			validateOtpRespModel.setMessage("failed");
		}
		RecordLog.writeLogFile("AuthController /reset api completed. API response: for mobile "+commonResetOtpWebUsersReqModel.getMobile() +validateOtpRespModel);
		return ResponseEntity.ok(validateOtpRespModel);
	}

	@io.swagger.v3.oas.annotations.Operation(summary = "Clear dummy token")
	@PostMapping(path = "/cleardummytoken", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> deletedummytoken(
			@RequestBody CommonResetOtpWebUsersReqModel commonResetOtpWebUsersReqModel) {
		RecordLog.writeLogFile("AuthController /cleardummytoken api is calling. API request: "+commonResetOtpWebUsersReqModel);
		ValidateOtpRespModel validateOtpRespModel = null;

		try {
			if (commonResetOtpWebUsersReqModel != null) {
				if (StringUtils.isNotEmpty(commonResetOtpWebUsersReqModel.getAuthToken())) {
					if (!commonResetOtpWebUsersReqModel.getAuthToken().contains(".")) {
						boolean dummyexists = resetOtpWebUsersService
								.validateTempToken(commonResetOtpWebUsersReqModel.getAuthToken());
						if (dummyexists) {
							boolean temptoken_status = resetOtpWebUsersService
									.removeTempToken(commonResetOtpWebUsersReqModel.getAuthToken());
							validateOtpRespModel = new ValidateOtpRespModel();
							if (temptoken_status) {
								validateOtpRespModel.setStatus(true);
								validateOtpRespModel.setMessage("Dummy token cleared successfully");
							} else {
								validateOtpRespModel.setStatus(false);
								validateOtpRespModel.setMessage("Failed to clear the auth token.");
							}

						} else {
							validateOtpRespModel = new ValidateOtpRespModel();
							validateOtpRespModel.setStatus(false);
							validateOtpRespModel.setMessage("Token is not available for re-set");
						}

					} else {
						validateOtpRespModel = new ValidateOtpRespModel();
						validateOtpRespModel.setStatus(false);
						validateOtpRespModel.setMessage("Token is not a dummy token.");
					}

				} else {
					validateOtpRespModel = new ValidateOtpRespModel();
					validateOtpRespModel.setStatus(false);
					validateOtpRespModel
							.setDescription("New password,Confirm Password,EXFLAG,Auth token are mandatory");
					validateOtpRespModel.setMessage("failed");
				}
			} else {
				validateOtpRespModel = new ValidateOtpRespModel();
				validateOtpRespModel.setStatus(false);
				validateOtpRespModel.setDescription("Request Body can not be empty");
				validateOtpRespModel.setMessage("failed");
			}
		} catch (Exception e) {
			RecordLog.writeLogFile("Exception occured at: "+Arrays.toString(e.getStackTrace())+" Exception name: "+e.getMessage());
			e.printStackTrace();
			validateOtpRespModel = new ValidateOtpRespModel();
			validateOtpRespModel.setStatus(false);
			validateOtpRespModel.setDescription("error occured , please try again later");
			validateOtpRespModel.setMessage("failed");
		}
		RecordLog.writeLogFile("AuthController /cleardummytoken api completed. API response: "+validateOtpRespModel);
		return ResponseEntity.ok(validateOtpRespModel);
	}

	@io.swagger.v3.oas.annotations.Operation(summary = "Generate Encyption token")
	@PostMapping(path = "/generateenctoken", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> generateenctoken(@RequestBody EncryptionRequest request) {
		RecordLog.writeLogFile("AuthController /generateenctoken api is calling. API request: "+request);
		SMEMessage message = new SMEMessage();

		try {
			if (request != null) {
				if (StringUtils.isNotEmpty(request.getServiceid())) {
					message = resetOtpWebUsersService.generateEncToken(request.getServiceid());
				} else {
					message.setStatus(false);
					message.setDescription("ServiceId is mandatory");
					message.setMessage("failed");
				}
			} else {
				message.setStatus(false);
				message.setDescription("Request Body can not be empty");
				message.setMessage("failed");
			}
		} catch (Exception e) {
			RecordLog.writeLogFile("Exception occured at: "+Arrays.toString(e.getStackTrace())+" Exception name: "+e.getMessage());
			e.printStackTrace();
			message.setStatus(false);
			message.setDescription("error occured , please try again later");
			message.setMessage("failed");
		}
		RecordLog.writeLogFile("AuthController /generateenctoken api completed. API response: "+message);
		return ResponseEntity.ok(message);
	}

}
