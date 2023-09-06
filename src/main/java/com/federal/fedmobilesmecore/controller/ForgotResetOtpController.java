package com.federal.fedmobilesmecore.controller;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.federal.fedmobilesmecore.config.GlobalProperties;
import com.federal.fedmobilesmecore.config.RecordLog;
import com.federal.fedmobilesmecore.dto.Enterprises;
import com.federal.fedmobilesmecore.dto.User;
import com.federal.fedmobilesmecore.dto.UserPin;
import com.federal.fedmobilesmecore.model.ForgotResetOtpInput;
import com.federal.fedmobilesmecore.model.SMEMessage;
import com.federal.fedmobilesmecore.repository.EnterprisesRepository;
import com.federal.fedmobilesmecore.repository.UserPinRepo;
import com.federal.fedmobilesmecore.repository.UserRepository;

/**
 * @author Syed
 *
 */
@RestController
@RequestMapping(path = "/core/user")
public class ForgotResetOtpController {

	@Autowired
	GlobalProperties properties;

	@Autowired
	UserRepository userrepo;

	@Autowired
	UserPinRepo userpinrepo;

	@Autowired
	EnterprisesRepository entrepo;

	@Value("${fedsmecore.passworddurationinminutes}")
	Integer passwordexpirymin;

	@io.swagger.v3.oas.annotations.Operation(summary = "Generated password will be stored in database.")
	@PostMapping(path = "/forgot_password_web", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> forgotpassword(@RequestBody ForgotResetOtpInput input) {
		RecordLog.writeLogFile("ForgotResetOtpController /forgot_password_web api is calling. API request: "+input);
		SMEMessage message = new SMEMessage();

		if (StringUtils.isNotBlank(input.getAuth_token()) && StringUtils.isNotBlank(input.getPref_corp())
				&& StringUtils.isNotBlank(input.getForgot_pin())) {
			User user = userrepo.findByAuthTokenAndMarkAsEnabled(input.getAuth_token(), true);
			if (user != null) {
				Optional<Enterprises> enterprise = entrepo.findById(Long.parseLong(user.getEnterpriseId()));
				if (enterprise.isPresent()) {
					if (enterprise.get().getPrefCorp().equals(input.getPref_corp())) {
						UserPin pin = createPin(input, "ForgotPin", new BigDecimal(user.getId()));
						if (pin != null) {
							message.setStatus(true);
							message.setMessage("Success");
						} else {
							message.setStatus(false);
							message.setMessage("Unable to save password");
						}
					} else {
						message.setStatus(false);
						message.setMessage("User and enterprise mismatch.");
					}
				} else {
					message.setStatus(false);
					message.setMessage("User related enterprise is not available.");
				}
			} else {
				message.setStatus(false);
				message.setMessage("User not logged in");
			}

		} else {
			message.setStatus(false);
			message.setMessage(properties.getReqinputsna());
		}
		RecordLog.writeLogFile("ForgotResetOtpController /forgot_password_web api completed. API response: "+message);
		return ResponseEntity.ok(message);

	}

	@io.swagger.v3.oas.annotations.Operation(summary = "Verify the pin")
	@PostMapping(path = "/verify_forgot_pin_web", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> forgotpin(@RequestBody ForgotResetOtpInput input) {
		RecordLog.writeLogFile("ForgotResetOtpController /verify_forgot_pin_web api is calling. API request: "+input);
		SMEMessage message = new SMEMessage();

		if (StringUtils.isNotBlank(input.getApp_token()) && StringUtils.isNotBlank(input.getPref_corp())
				&& StringUtils.isNotBlank(input.getForgot_pin())) {
			Enterprises enterprise = entrepo.findByPrefCorpAndActive(input.getPref_corp(),true);
			if (enterprise != null) {
				List<User> users = userrepo.findByAppTokenAndEnterpriseId(input.getApp_token(),
						"" + enterprise.getId());
				if (!CollectionUtils.isEmpty(users)) {
					List<UserPin> userpinlist = userpinrepo.findByUserIdAndTypeAndPin(
							new BigDecimal(users.get(0).getId()), "ForgotPin", input.getForgot_pin());
					if (!CollectionUtils.isEmpty(userpinlist)) {
						boolean status = false;

						for (UserPin pin : userpinlist) {
							
							
							RecordLog.writeLogFile("ajay0502"+pin.getExpiredAt());
							RecordLog.writeLogFile("ajay0502"+new Date());
							RecordLog.writeLogFile("ajay0502"+pin.getExpiredAt().after(new Date()));
							
							
							if (pin.getExpiredAt().after(new Date())
									&& pin.getActive().compareTo(BigDecimal.ONE) == 0) {
								status = true;
								pin.setActive(BigDecimal.ZERO);
								pin.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
								userpinrepo.save(pin);
								// -->Existing query<-- UPDATE "users" SET "updated_at" = Time.now,
								// "wrong_mpin_count"
								// = 0,
								// "mpin_check_status" = 'active' WHERE "users"."id" = pin_status.user_id;
								User user_web = users.get(0);
								user_web.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
								user_web.setWrongMpinCount("0");
								user_web.setMpinCheckStatus("active");
								userrepo.save(user_web);
							}
						}
						if (status == true) {
							message.setStatus(true);
							message.setMessage("Success");
						} else {
							message.setStatus(false);
							message.setMessage("Pin is expired");
						}
					} else {
						message.setStatus(false);
						message.setMessage("Invalid PIN");
					}
				} else {
					message.setStatus(false);
					message.setMessage("Invalid User");
				}
			} else {
				message.setStatus(false);
				message.setMessage("Invalid Corporate");
			}
		} else {
			message.setStatus(false);
			message.setMessage(properties.getReqinputsna());
		}
		RecordLog.writeLogFile("ForgotResetOtpController /verify_forgot_pin_web api completed. API response: "+message);
		return ResponseEntity.ok(message);
	}

	@io.swagger.v3.oas.annotations.Operation(summary = "Web Pin generation")
	@PostMapping(path = "/web_pin", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> verifyotp(@RequestBody ForgotResetOtpInput input) {
		RecordLog.writeLogFile("ForgotResetOtpController /web_pin api is calling. API request: "+input);
		SMEMessage message = new SMEMessage();

		if (StringUtils.isNotBlank(input.getApp_token()) && StringUtils.isNotBlank(input.getPref_corp())
				&& StringUtils.isNotBlank(input.getForgot_pin())) {
			List<User> users = userrepo.findByAppTokenAndMarkAsEnabled(input.getApp_token(), true);
			if (!CollectionUtils.isEmpty(users)) {
				UserPin pin = createPin(input, "WebPin", new BigDecimal(users.get(0).getId()));
				if (pin != null) {
					message.setStatus(true);
					message.setMessage("Success");
				} else {
					message.setStatus(false);
					message.setMessage("Unable to save password");
				}
			} else {
				message.setStatus(false);
				message.setMessage("Invalid User");
			}

		} else {
			message.setStatus(false);
			message.setMessage(properties.getReqinputsna());
		}
		RecordLog.writeLogFile("ForgotResetOtpController /web_pin api completed. API response: "+message);
		return ResponseEntity.ok(message);
	}

	@io.swagger.v3.oas.annotations.Operation(summary = "Generated password MPIN will be stored in database and return mpin.")
	@PostMapping(path = "/forgot_password_mpin", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> forgotpasswordMpin(@RequestBody ForgotResetOtpInput input) {
		RecordLog.writeLogFile("ForgotResetOtpController /forgot_password_mpin api is calling. API request: "+input);
		SMEMessage message = new SMEMessage();

		if (StringUtils.isNotBlank(input.getAuth_token()) && StringUtils.isNotBlank(input.getPref_corp())) {
			User user = userrepo.findByAuthTokenAndMarkAsEnabled(input.getAuth_token(), true);
			if (user != null) {
				Optional<Enterprises> enterprise = entrepo.findById(Long.parseLong(user.getEnterpriseId()));
				if (enterprise.isPresent()) {
					if (enterprise.get().getPrefCorp().equals(input.getPref_corp())) {
						input.setForgot_pin(getRandowMpin());
						UserPin pin = createPin(input, "ForgotPin", new BigDecimal(user.getId()));
						if (pin != null) {
							message.setStatus(true);
							message.setMessage("Success");
							// sending Mpin
							message.setRecordid(input.getForgot_pin());
						} else {
							message.setStatus(false);
							message.setMessage("Unable to save password");
						}
					} else {
						message.setStatus(false);
						message.setMessage("User and enterprise mismatch.");
					}
				} else {
					message.setStatus(false);
					message.setMessage("User related enterprise is not available.");
				}
			} else {
				message.setStatus(false);
				message.setMessage("User not logged in");
			}

		} else {
			message.setStatus(false);
			message.setMessage(properties.getReqinputsna());
		}
		RecordLog.writeLogFile("ForgotResetOtpController /forgot_password_mpin api completed. API response: "+message);
		return ResponseEntity.ok(message);

	}

	/**
	 * Method created to generate Mpin
	 */
	private String getRandowMpin() {
		Random random = new Random();
		String generatedString = random.ints(97, 123).limit(6)
				.collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append).toString();
		return generatedString;
	}

	/**
	 * Method to create a new Object and save in db.
	 * 
	 * @param input
	 * @param type
	 * @param user_id
	 * @return
	 */
	public UserPin createPin(ForgotResetOtpInput input, String type, BigDecimal user_id) {
		UserPin pin = new UserPin();
		pin.setCreatedAt(new Timestamp(System.currentTimeMillis()));
		pin.setActive(BigDecimal.ONE);
		pin.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
		long expiryTime = System.currentTimeMillis() + (passwordexpirymin * 60 * 1000);
		pin.setExpiredAt(new Timestamp(expiryTime));
		pin.setPin(input.getForgot_pin());
		pin.setType(type);
		pin.setUserId(user_id);
		userpinrepo.save(pin);
		return pin;
	}

}
