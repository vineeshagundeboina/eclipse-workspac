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
import com.federal.fedmobilesmecore.model.ValidateOtpRespModel;
import com.federal.fedmobilesmecore.service.ResetOtpWebUsersService;

@RestController
@RequestMapping(path = "/core/resetotpwebusers")
public class ResetOtpWebUsersController {
	private static final Logger log4j = LogManager.getLogger(ResetOtpWebUsersController.class);
	@Autowired
	ResetOtpWebUsersService resetOtpWebUsersService;

	// NOT THERE IN API CREATION SHEET
	@io.swagger.v3.oas.annotations.Operation(summary = "Resend otp")
	@PostMapping(path = "/resendotp", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> resendOtp(@RequestBody CommonResetOtpWebUsersReqModel commonResetOtpWebUsersReqModel) {
		RecordLog.writeLogFile("ResetOtpWebUsersController /resendotp api is calling. API request: "+commonResetOtpWebUsersReqModel);
		if (commonResetOtpWebUsersReqModel != null) {
			if (StringUtils.isNotEmpty(commonResetOtpWebUsersReqModel.getApplicationFormId())) {
				resetOtpWebUsersService.getResendOtp(commonResetOtpWebUsersReqModel.getApplicationFormId());
			} else {

			}
		} else {

		}
		return null;
	}

	@io.swagger.v3.oas.annotations.Operation(summary = "Reset Password By Otp")
	@PostMapping(path = "/resetpasswordbyotp", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> resetPasswordByOtp(
			@RequestBody CommonResetOtpWebUsersReqModel commonResetOtpWebUsersReqModel) {
		RecordLog.writeLogFile("ResetOtpWebUsersController /resetpasswordbyotp api is calling. API request: "+commonResetOtpWebUsersReqModel);
		ValidateOtpRespModel otpRespModel = null;
		if (commonResetOtpWebUsersReqModel != null) {
			if (StringUtils.isNotEmpty(commonResetOtpWebUsersReqModel.getNewPassword())
					&& StringUtils.isNotEmpty(commonResetOtpWebUsersReqModel.getConfirmPassword())
					&& StringUtils.isNotEmpty(commonResetOtpWebUsersReqModel.getOtp())
					&& StringUtils.isNotEmpty(commonResetOtpWebUsersReqModel.getExFlag())
					&& StringUtils.isNotEmpty(commonResetOtpWebUsersReqModel.getAuthToken())) {
				otpRespModel = resetOtpWebUsersService.resetPasswordByOtp(commonResetOtpWebUsersReqModel);
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
		RecordLog.writeLogFile("ResetOtpWebUsersController /resetpasswordbyotp api completed. API response: "+otpRespModel);
		return ResponseEntity.ok(otpRespModel);
	}

	@io.swagger.v3.oas.annotations.Operation(summary = "Request Reset Password")
	@PostMapping(path = "/requestresetpassword", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> requestResetPassword(
			@RequestBody CommonResetOtpWebUsersReqModel commonResetOtpWebUsersReqModel) {
		RecordLog.writeLogFile("ResetOtpWebUsersController /requestresetpassword api is calling. API request: "+commonResetOtpWebUsersReqModel);
		ValidateOtpRespModel otpRespModel = null;
		if (commonResetOtpWebUsersReqModel != null) {
			if (StringUtils.isNotEmpty(commonResetOtpWebUsersReqModel.getPassword())
					&& StringUtils.isNotEmpty(commonResetOtpWebUsersReqModel.getAuthToken())) {
				otpRespModel = resetOtpWebUsersService.requestResetPassword(commonResetOtpWebUsersReqModel);
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
		RecordLog.writeLogFile("ResetOtpWebUsersController /requestresetpassword api completed. API response: "+otpRespModel);
		return ResponseEntity.ok(otpRespModel);
	}

	@io.swagger.v3.oas.annotations.Operation(summary = "Reset Request By Web Pin")
	@PostMapping(path = "/resetrequestbywebpin", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> resetRequestByWebPin(
			@RequestBody CommonResetOtpWebUsersReqModel commonResetOtpWebUsersReqModel) {
		RecordLog.writeLogFile("ResetOtpWebUsersController /resetrequestbywebpin api is calling. API request: "+commonResetOtpWebUsersReqModel);
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
		RecordLog.writeLogFile("ResetOtpWebUsersController /resetrequestbywebpin api completed. API response: "+otpRespModel);
		return ResponseEntity.ok(otpRespModel);
	}

	@io.swagger.v3.oas.annotations.Operation(summary = "Validate otp")
	@PostMapping(path = "/validateotp", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> validateOtp(@RequestBody CommonResetOtpWebUsersReqModel commonResetOtpWebUsersReqModel) {
		RecordLog.writeLogFile("ResetOtpWebUsersController /validateotp api is calling. API request: "+commonResetOtpWebUsersReqModel);
		ValidateOtpRespModel validateOtpRespModel = null;
		if (commonResetOtpWebUsersReqModel != null) {
			if (StringUtils.isNotEmpty(commonResetOtpWebUsersReqModel.getAuthToken())
					&& StringUtils.isNotEmpty(commonResetOtpWebUsersReqModel.getOtp()))
//				&& StringUtils.isNotEmpty(commonResetOtpWebUsersReqModel.getMobile()) Removed and handle inside for without mobileno scenario
			{
				if (StringUtils.isNotEmpty(commonResetOtpWebUsersReqModel.getMobile())) {
					validateOtpRespModel = resetOtpWebUsersService.validateOtp(commonResetOtpWebUsersReqModel);
				} else {
					validateOtpRespModel = resetOtpWebUsersService
							.validateOtpWithoutMobile(commonResetOtpWebUsersReqModel);
				}
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
		RecordLog.writeLogFile("ResetOtpWebUsersController /validateotp api completed. API response: "+validateOtpRespModel);
		return ResponseEntity.ok(validateOtpRespModel);
	}

	@io.swagger.v3.oas.annotations.Operation(summary = "Validate Web Token")
	@PostMapping(path = "/validatewebtoken", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> validateWebToken(
			@RequestBody CommonResetOtpWebUsersReqModel commonResetOtpWebUsersReqModel) {
		RecordLog.writeLogFile("ResetOtpWebUsersController /validatewebtoken api is calling. API request: "+commonResetOtpWebUsersReqModel);
		ValidateOtpRespModel validateOtpRespModel = null;
		if (commonResetOtpWebUsersReqModel != null) {
			if (StringUtils.isNotEmpty(commonResetOtpWebUsersReqModel.getAuthToken())
					&& StringUtils.isNotEmpty(commonResetOtpWebUsersReqModel.getActivationToken())
					&& StringUtils.isNotEmpty(commonResetOtpWebUsersReqModel.getMobile())) {
				validateOtpRespModel = resetOtpWebUsersService.validateWebToken(commonResetOtpWebUsersReqModel);
			} else {
				validateOtpRespModel = new ValidateOtpRespModel();
				validateOtpRespModel.setStatus(true);
				validateOtpRespModel.setDescription(
						"Following Mandatory fields are not passed: AuthToken & Activation token & Mobile number");
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
		RecordLog.writeLogFile("ResetOtpWebUsersController /validatewebtoken api completed. API response: "+validateOtpRespModel);
		return ResponseEntity.ok(validateOtpRespModel);
	}

	@io.swagger.v3.oas.annotations.Operation(summary = "Validate Web Token")
	@PostMapping(path = "/validatewebpin", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> validateWebPin(
			@RequestBody CommonResetOtpWebUsersReqModel commonResetOtpWebUsersReqModel) {
		RecordLog.writeLogFile("ResetOtpWebUsersController /validatewebpin api is calling. API request: "+commonResetOtpWebUsersReqModel);
		ValidateOtpRespModel validateOtpRespModel = null;
		if (commonResetOtpWebUsersReqModel != null) {
			if (StringUtils.isNotEmpty(commonResetOtpWebUsersReqModel.getAuthToken())
					&& StringUtils.isNotEmpty(commonResetOtpWebUsersReqModel.getForgotPin())
					&& StringUtils.isNotEmpty(commonResetOtpWebUsersReqModel.getMobile())) {
				validateOtpRespModel = resetOtpWebUsersService.validateWebPin(commonResetOtpWebUsersReqModel);
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
		RecordLog.writeLogFile("ResetOtpWebUsersController /validatewebpin api completed. API response: "+validateOtpRespModel);
		return ResponseEntity.ok(validateOtpRespModel);
	}

	@io.swagger.v3.oas.annotations.Operation(summary = "Update User Credentials")
	@PostMapping(path = "/updateusercredentials", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> updateUserCredentials(
			@RequestBody CommonResetOtpWebUsersReqModel commonResetOtpWebUsersReqModel) {
		RecordLog.writeLogFile("ResetOtpWebUsersController /updateusercredentials api is calling. API request: "+commonResetOtpWebUsersReqModel);
		ValidateOtpRespModel validateOtpRespModel = null;
		if (commonResetOtpWebUsersReqModel != null) {
			if (StringUtils.isNotEmpty(commonResetOtpWebUsersReqModel.getAuthToken())
					&& StringUtils.isNotEmpty(commonResetOtpWebUsersReqModel.getPrefNo())
					&& StringUtils.isNotEmpty(commonResetOtpWebUsersReqModel.getNewPassword())
					&& StringUtils.isNotEmpty(commonResetOtpWebUsersReqModel.getConfirmPassword())
					&& StringUtils.isNotEmpty(commonResetOtpWebUsersReqModel.getExFlag())) {
				validateOtpRespModel = resetOtpWebUsersService.updateUserCredentials(commonResetOtpWebUsersReqModel);
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
		RecordLog.writeLogFile("ResetOtpWebUsersController /updateusercredentials api completed. API response: "+validateOtpRespModel);
		return ResponseEntity.ok(validateOtpRespModel);
	}

	@io.swagger.v3.oas.annotations.Operation(summary = "activated")
	@PostMapping(path = "/activated", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ValidateOtpRespModel activated(@RequestBody CommonResetOtpWebUsersReqModel commonResetOtpWebUsersReqModel) {
		RecordLog.writeLogFile("ResetOtpWebUsersController /activated api is calling. API request: "+commonResetOtpWebUsersReqModel);
		ValidateOtpRespModel validateOtpRespModel = null;
		if (commonResetOtpWebUsersReqModel != null) {
			if (StringUtils.isNotEmpty(commonResetOtpWebUsersReqModel.getMobile())
					&& StringUtils.isNotEmpty(commonResetOtpWebUsersReqModel.getCaptcha())) {
				validateOtpRespModel = resetOtpWebUsersService.activate(commonResetOtpWebUsersReqModel);
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
		RecordLog.writeLogFile("ResetOtpWebUsersController /activated api completed. API response: "+validateOtpRespModel);
		return validateOtpRespModel;
	}

	@io.swagger.v3.oas.annotations.Operation(summary = "validate user")
	@PostMapping(path = "/validateuser", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> validateUser(@RequestBody CommonResetOtpWebUsersReqModel commonResetOtpWebUsersReqModel) {
		RecordLog.writeLogFile("ResetOtpWebUsersController /validateuser api is calling. API request: "+commonResetOtpWebUsersReqModel);
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
			RecordLog.writeLogFile("Exception occured at:for mobile "+commonResetOtpWebUsersReqModel.getMobile()+Arrays.toString(e.getStackTrace())+" Exception name: "+e.getMessage());
			e.printStackTrace();
			validateOtpRespModel = new ValidateOtpRespModel();
			validateOtpRespModel.setStatus(false);
			validateOtpRespModel.setDescription("error occured , please try again later");
			validateOtpRespModel.setMessage("failed");
		}
		RecordLog.writeLogFile("ResetOtpWebUsersController /validateuser api completed. API response: for mobile "+commonResetOtpWebUsersReqModel.getMobile()+validateOtpRespModel);
		return ResponseEntity.ok(validateOtpRespModel);
	}

	@io.swagger.v3.oas.annotations.Operation(summary = "validate user")
	@PostMapping(path = "/verifyotpweb", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> verifyOtpWeb(@RequestBody CommonResetOtpWebUsersReqModel commonResetOtpWebUsersReqModel) {
		RecordLog.writeLogFile("ResetOtpWebUsersController /verifyotpweb api is calling. API request: "+commonResetOtpWebUsersReqModel);
		ValidateOtpRespModel validateOtpRespModel = null;

		try {
			if (commonResetOtpWebUsersReqModel != null) {
				if (StringUtils.isNotEmpty(commonResetOtpWebUsersReqModel.getPrefCorp())
						&& StringUtils.isNotEmpty(commonResetOtpWebUsersReqModel.getOtp())
						&& StringUtils.isNotEmpty(commonResetOtpWebUsersReqModel.getPassword())
						&& StringUtils.isNotEmpty(commonResetOtpWebUsersReqModel.getCheckSum())
						&& StringUtils.isNotEmpty(commonResetOtpWebUsersReqModel.getAuthToken())) {
					validateOtpRespModel = resetOtpWebUsersService.getVerifyOtpWeb(commonResetOtpWebUsersReqModel);
				} else {
					validateOtpRespModel = new ValidateOtpRespModel();
					validateOtpRespModel.setStatus(false);
					validateOtpRespModel.setDescription("mandatory fields not passed");
					validateOtpRespModel.setMessage("failed");
					validateOtpRespModel.setAuthToken(null);
					validateOtpRespModel.setPrefCorp(null);
				}
			} else {
				validateOtpRespModel = new ValidateOtpRespModel();
				validateOtpRespModel.setStatus(false);
				validateOtpRespModel.setDescription("Request Body can not be empty");
				validateOtpRespModel.setMessage("failed");
				validateOtpRespModel.setAuthToken(null);
				validateOtpRespModel.setPrefCorp(null);
			}
		} catch (Exception e) {
			RecordLog.writeLogFile("Exception occured at:for mobile "+commonResetOtpWebUsersReqModel.getPrefCorp()+Arrays.toString(e.getStackTrace())+" Exception name: "+e.getMessage());
			e.printStackTrace();
			validateOtpRespModel = new ValidateOtpRespModel();
			validateOtpRespModel.setStatus(false);
			validateOtpRespModel.setDescription("error occured , please try again later");
			validateOtpRespModel.setMessage("failed");
		}
		RecordLog.writeLogFile("ResetOtpWebUsersController /verifyotpweb api completed. API response: for mobile "+commonResetOtpWebUsersReqModel.getPrefCorp()+validateOtpRespModel);
		return ResponseEntity.ok(validateOtpRespModel);
	}

	@io.swagger.v3.oas.annotations.Operation(summary = "generate otp web")
	@PostMapping(path = "/generateotpweb", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> generateOtpWeb(
			@RequestBody CommonResetOtpWebUsersReqModel commonResetOtpWebUsersReqModel) {
		RecordLog.writeLogFile("ResetOtpWebUsersController /generateotpweb api is calling. API request: "+commonResetOtpWebUsersReqModel);
		ValidateOtpRespModel validateOtpRespModel = null;

		try {
			if (commonResetOtpWebUsersReqModel != null) {
				if (StringUtils.isNotEmpty(commonResetOtpWebUsersReqModel.getPrefCorp())
						&& StringUtils.isNotEmpty(commonResetOtpWebUsersReqModel.getAuthToken())) {
					validateOtpRespModel = resetOtpWebUsersService.generateotpweb(commonResetOtpWebUsersReqModel);
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
				validateOtpRespModel.setStatus(false);
				validateOtpRespModel.setDescription("Request Body can not be empty");
				validateOtpRespModel.setMessage("failed");
				validateOtpRespModel.setAuthToken(null);
				validateOtpRespModel.setPrefCorp(null);
			}
		} catch (Exception e) {
			RecordLog.writeLogFile("Exception occured at: for mobile "+commonResetOtpWebUsersReqModel.getPrefCorp()+Arrays.toString(e.getStackTrace())+" Exception name: "+e.getMessage());
			e.printStackTrace();
			validateOtpRespModel = new ValidateOtpRespModel();
			validateOtpRespModel.setStatus(false);
			validateOtpRespModel.setDescription("error occured , please try again later");
			validateOtpRespModel.setMessage("failed");
		}
		RecordLog.writeLogFile("ResetOtpWebUsersController /generateotpweb api completed. API response:for mobile "+commonResetOtpWebUsersReqModel.getPrefCorp()+validateOtpRespModel);
		return ResponseEntity.ok(validateOtpRespModel);
	}
}