package com.federal.fedmobilesmecore.controller;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.federal.fedmobilesmecore.model.ValidateExtUserPrefNoReqModel;
import com.federal.fedmobilesmecore.repository.UserRepository;
import com.federal.fedmobilesmecore.config.GlobalProperties;
import com.federal.fedmobilesmecore.config.RecordLog;
import com.federal.fedmobilesmecore.model.ExternalUserCommonReq;
import com.federal.fedmobilesmecore.model.ExternalUserCreationReqModel;
import com.federal.fedmobilesmecore.model.GetExternalUserModelResp;
import com.federal.fedmobilesmecore.model.ValidateExtMobileNoReqModel;
import com.federal.fedmobilesmecore.service.ExternalUserService;
import com.federal.fedmobilesmecore.service.MakerCheckerService;

/**
 * @author Debasish_Splenta
 *
 */
@RestController
@RequestMapping(path = "/core/extuser")
public class ExternalUserController {

	@Autowired
	ExternalUserService externalUserService;
	@Autowired
	MakerCheckerService makerCheckerService;
	@Autowired
	GlobalProperties messages;
	/**
	 * for testing purpose userRepository.findById(3L).get() is used
	 *
	 */
	@Autowired
	UserRepository userRepository;

	@PostMapping(path = "/extvalidatebymobile", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> validateMobile(@RequestBody ValidateExtMobileNoReqModel mobileNoReqModel) {
		RecordLog.writeLogFile("ExternalUserController /extvalidatebymobile api is calling. API request: "+mobileNoReqModel);
		GetExternalUserModelResp serviceResp = null;
		serviceResp = externalUserService.validateExternalUserMobileNo(mobileNoReqModel.getAuthToken(),
				mobileNoReqModel.getMobile(),mobileNoReqModel.getPrefCorp());
		RecordLog.writeLogFile("ExternalUserController /extvalidatebymobile api completed. API response: "+serviceResp);
		return ResponseEntity.ok(serviceResp);
	}

	@PostMapping(path = "/extvalidationbypref", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> ExtUserPrefNumberValidation(
			@RequestBody ValidateExtUserPrefNoReqModel userPrefNoReqModel) {
		RecordLog.writeLogFile("ExternalUserController /extvalidationbypref api is calling. API request: "+userPrefNoReqModel);
		GetExternalUserModelResp serviceResp = null;

		if (userPrefNoReqModel != null) {
			if (StringUtils.isNotEmpty(userPrefNoReqModel.getAuthToken())
					&& StringUtils.isNotEmpty(userPrefNoReqModel.getPrefCorp())
					&& StringUtils.isNotEmpty(userPrefNoReqModel.getPrefNo())) {
				serviceResp = externalUserService.validateExternalUserPrefNo(userPrefNoReqModel);
			} else {
				serviceResp = new GetExternalUserModelResp();
				serviceResp.setStatus(messages.isFailed());
				serviceResp.setMessage(messages.getMandatoryNotPassed());
				serviceResp.setDescription("");
				serviceResp.setRecordId(null);
			}
		} else {
			serviceResp = new GetExternalUserModelResp();
			serviceResp.setStatus(messages.isFailed());
			serviceResp.setMessage(messages.getBodyIsEmpty());
			serviceResp.setDescription("");
			serviceResp.setRecordId(null);
		}
		RecordLog.writeLogFile("ExternalUserController /extvalidationbypref api completed. API response: "+serviceResp);
		return ResponseEntity.ok(serviceResp);
	}

	/**
	 * @api : def create external user
	 *
	 */
	@PostMapping(path = "/extusercreation", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> ExtEnterpriseUserCreation(@RequestBody ExternalUserCreationReqModel userCreationReqModel) {
		RecordLog.writeLogFile("ExternalUserController /extusercreation api is calling. API request: "+userCreationReqModel);
		GetExternalUserModelResp serviceResp = null;
		serviceResp = externalUserService.createExtUser(userCreationReqModel);
		RecordLog.writeLogFile("ExternalUserController /extusercreation api completed. API response: "+serviceResp);
		return ResponseEntity.ok(serviceResp);
	}

	/**
	 * @api : def approve
	 * 
	 */

	@PostMapping(path = "/extuserapprove", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> ExtEnterpriseApprove(@RequestBody ExternalUserCommonReq externalUserCommonReq) {
		RecordLog.writeLogFile("ExternalUserController /extuserapprove api is calling. API request: "+externalUserCommonReq);
		GetExternalUserModelResp approveResp = null;
		approveResp = externalUserService.approveExtUser(externalUserCommonReq.getRefNo().toUpperCase(),
				externalUserCommonReq.getAuthToken());
		RecordLog.writeLogFile("ExternalUserController /extusercreation api completed. API response: "+approveResp);
		return ResponseEntity.ok(approveResp);
	}

	/**
	 * @api : def reject
	 *
	 */

	@PostMapping(path = "/extuserreject", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> ExtEnterpriseReject(@RequestBody ExternalUserCommonReq externalUserCommonReq) {
		RecordLog.writeLogFile("ExternalUserController /extuserreject api is calling. API request: "+externalUserCommonReq);
		GetExternalUserModelResp rejectResp = null;
		rejectResp = externalUserService.rejectExtUser(externalUserCommonReq.getRefNo().toUpperCase(),
				externalUserCommonReq.getAuthToken());
		RecordLog.writeLogFile("ExternalUserController /extuserreject api completed. API response: "+rejectResp);
		return ResponseEntity.ok(rejectResp);
	}

	/**
	 * @api : def user_details
	 *
	 */

	@PostMapping(path = "/extuserdetails", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> ExtEnterpriseUserDetails(@RequestBody ExternalUserCommonReq externalUserCommonReq) {
		RecordLog.writeLogFile("ExternalUserController /extuserdetails api is calling. API request: "+externalUserCommonReq);
		GetExternalUserModelResp rejectResp = null;
		rejectResp = externalUserService.getExtUserDetails(externalUserCommonReq.getRefNo().toUpperCase());
		RecordLog.writeLogFile("ExternalUserController /extuserdetails api completed. API response: "+rejectResp);
		return ResponseEntity.ok(rejectResp);
	}

	/**
	 * @api : def destroy_user
	 *
	 */

	@PostMapping(path = "/extuserdestroy", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> ExtEnterpriseDesUser(@RequestBody ExternalUserCommonReq externalUserCommonReq) {
		RecordLog.writeLogFile("ExternalUserController /extuserdestroy api is calling. API request: "+externalUserCommonReq);
		GetExternalUserModelResp rejectResp = new GetExternalUserModelResp();
		if (StringUtils.isNotBlank(externalUserCommonReq.getPrefNo())
				&& StringUtils.isNotBlank(externalUserCommonReq.getPrefCorpNo())) {
			rejectResp = externalUserService.destroyUserDetailsByPrefNo(externalUserCommonReq);
		} else {
			rejectResp.setStatus(false);
			rejectResp.setDescription("Username & Enterprise Id is mandatory.");
			rejectResp.setMessage("Mandatory fields are missing");
		}
		RecordLog.writeLogFile("ExternalUserController /extuserdestroy api completed. API response: "+rejectResp);
		return ResponseEntity.ok(rejectResp);
	}

	/**
	 * @api : def pending_external_specific
	 *
	 */

	@PostMapping(path = "/pendingexternalspecific", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> pendingExternalSpecific(@RequestBody ExternalUserCommonReq externalUserCommonReq) {
		RecordLog.writeLogFile("ExternalUserController /pendingexternalspecific api is calling. API request: "+externalUserCommonReq);
		GetExternalUserModelResp rejectResp = null;

		if (externalUserCommonReq != null) {
			if (StringUtils.isNotEmpty(externalUserCommonReq.getPrefCorpNo())
					&& StringUtils.isNotEmpty(externalUserCommonReq.getAuthToken())) {
				rejectResp = externalUserService.getPendingExternalSpecific(externalUserCommonReq.getPrefCorpNo(),
						externalUserCommonReq.getAuthToken());
			} else {
				rejectResp = new GetExternalUserModelResp();
				rejectResp.setStatus(messages.isFailed());
				rejectResp.setMessage(messages.getMandatoryNotPassed());
				rejectResp.setDescription("");
				rejectResp.setRecordId(null);
			}
		} else {
			rejectResp = new GetExternalUserModelResp();
			rejectResp.setStatus(messages.isFailed());
			rejectResp.setMessage(messages.getBodyIsEmpty());
			rejectResp.setDescription("");
			rejectResp.setRecordId(null);
		}
		RecordLog.writeLogFile("ExternalUserController /pendingexternalspecific api completed. API response: "+rejectResp);
		return ResponseEntity.ok(rejectResp);
	}

	/**
	 * @api : def approved
	 *
	 */

	@PostMapping(path = "/approvedexternalspecific", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> extUserApproved(@RequestBody ExternalUserCommonReq externalUserCommonReq) {
		RecordLog.writeLogFile("ExternalUserController /approvedexternalspecific api is calling. API request: "+externalUserCommonReq);
		GetExternalUserModelResp rejectResp = null;
        rejectResp = externalUserService.getApprovedExternalSpecific(externalUserCommonReq.getPrefCorpNo(),
		externalUserCommonReq.getAuthToken());
		RecordLog.writeLogFile("ExternalUserController /approvedexternalspecific api completed. API response: "+rejectResp);
		return ResponseEntity.ok(rejectResp);
	}

	/**
	 * @api : def rejected
	 *
	 */

	@PostMapping(path = "/rejectedexternalspecific", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> extUserRejected(@RequestBody ExternalUserCommonReq externalUserCommonReq) {
		RecordLog.writeLogFile("ExternalUserController /rejectedexternalspecific api is calling. API request: "+externalUserCommonReq);
		GetExternalUserModelResp rejectResp = null;
		rejectResp = externalUserService.getRejectedExternalSpecific(externalUserCommonReq.getPrefCorpNo(),
				externalUserCommonReq.getAuthToken());
		RecordLog.writeLogFile("ExternalUserController /rejectedexternalspecific api completed. API response: "+rejectResp);
		return ResponseEntity.ok(rejectResp);
	}

	/**
	 * @api : def pending
	 *
	 */

	@PostMapping(path = "/extuserpending", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> extUserPending(@RequestBody ExternalUserCommonReq externalUserCommonReq) {
		RecordLog.writeLogFile("ExternalUserController /extuserpending api is calling. API request: "+externalUserCommonReq);
		GetExternalUserModelResp rejectResp = null;
		rejectResp = externalUserService.getPendingExternalUsers(externalUserCommonReq.getPrefCorpNo(),
				externalUserCommonReq.getAuthToken());
		RecordLog.writeLogFile("ExternalUserController /extuserpending api completed. API response: "+rejectResp);
		return ResponseEntity.ok(rejectResp);
	}

	// Gives a list of approved external users

	// @PostMapping(path = "/extuserapproved", consumes =
	// MediaType.APPLICATION_JSON_VALUE, produces =
	// MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> extuserapproved(@RequestBody ExternalUserCommonReq externalUserCommonReq) {
		GetExternalUserModelResp rejectResp = null;
		rejectResp = externalUserService.getApprovedExternalUsers(externalUserCommonReq.getPrefCorpNo(),
				externalUserCommonReq.getAuthToken());
		return ResponseEntity.ok(rejectResp);
	}

	@PostMapping(path = "/maker")
	public ResponseEntity<?> testingMaker() {
		makerCheckerService.testChecker();
		return ResponseEntity.ok("a");
	}
	
	
	@PostMapping(path="/unblock")
	public ResponseEntity<?> unblockExternalUser(@RequestBody Map<String,Object> input){
		RecordLog.writeLogFile("ExternalUserController /unblock api is calling request " + input.toString());
		Map<String, Object> response = new HashMap<>();
		try {
			if (input.containsKey("mobile") && input.containsKey("refNo") && input.containsKey("operationType")) {
				response = externalUserService.unblockExtService(input.get("refNo").toString(),
						input.get("mobile").toString(),input.get("operationType").toString());
			} else {
				response.put("status", false);
				response.put("message", "Mandatory inputs not passed");
			}
			RecordLog.writeLogFile("unblock service response for mobile "+input.get("mobile").toString() + response.toString());
		} catch (Exception e) {
			e.printStackTrace();
			response.put("status", false);
			response.put("message", "Not able to unblock external user");
			RecordLog.writeLogFile("Exception occured for refno " + input.get("refNo").toString() + " "
					+ Arrays.toString(e.getStackTrace()) + " message " + e.getMessage());
		}
		RecordLog.writeLogFile("ExternalUserController /unblock api is completed response " + response.toString());
		return ResponseEntity.ok(response);
	}
}
