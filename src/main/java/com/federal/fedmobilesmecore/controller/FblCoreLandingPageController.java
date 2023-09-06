package com.federal.fedmobilesmecore.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.federal.fedmobilesmecore.config.RecordLog;
import com.federal.fedmobilesmecore.model.APIResponse;
import com.federal.fedmobilesmecore.model.ActivateApplicationUserModel;
import com.federal.fedmobilesmecore.model.FavouriteAccountRequest;
import com.federal.fedmobilesmecore.model.FundImpsTransferResponse;
import com.federal.fedmobilesmecore.model.ImpsLimitAPIRequest;
import com.federal.fedmobilesmecore.model.LastLoginDetailsRequest;
import com.federal.fedmobilesmecore.model.PendingTranscationRequest;
import com.federal.fedmobilesmecore.model.QuickPayLimitRequest;
import com.federal.fedmobilesmecore.service.FblLandingPageService;

@RestController
@RequestMapping("/core/landingPage")
public class FblCoreLandingPageController {

	@Autowired
	FblLandingPageService fblLandingPageService;

	@PostMapping("/impsLimit")
	@io.swagger.v3.oas.annotations.Operation(summary = "Imps Limit", description = "Imps Limit")
	public ResponseEntity<APIResponse> getBalanceEnquiry(@RequestBody ImpsLimitAPIRequest impsLimitAPIRequest) {
		RecordLog.writeLogFile("FblCoreLandingPageController /impsLimit api is calling. API request: "+impsLimitAPIRequest);
		APIResponse apiResponse = fblLandingPageService.getImpsLimit(impsLimitAPIRequest);
		RecordLog.writeLogFile("FblCoreLandingPageController /impsLimit api completed. API response: "+apiResponse);
		return ResponseEntity.ok(apiResponse);

	}

	@PostMapping("/pendingTranscation")
	@io.swagger.v3.oas.annotations.Operation(summary = "Pending Transcation", description = "Pending Transcation")
	public ResponseEntity<FundImpsTransferResponse> getpendingTranscation(
			@RequestBody PendingTranscationRequest pendingTranscationRequest) {
		RecordLog.writeLogFile("FblCoreLandingPageController /pendingTranscation api is calling. API request: "+pendingTranscationRequest);
		FundImpsTransferResponse apiResponse = fblLandingPageService.pendingTranscation(pendingTranscationRequest);
		RecordLog.writeLogFile("FblCoreLandingPageController /pendingTranscation api completed. API response: "+apiResponse);
		return ResponseEntity.ok(apiResponse);

	}

	@PostMapping("/lastLoginDetails")
	@io.swagger.v3.oas.annotations.Operation(summary = "lastLoginDetails", description = "Last Login Details")
	public ResponseEntity<APIResponse> lastLoginDetails(@RequestBody LastLoginDetailsRequest lastLoginDetails) {
		RecordLog.writeLogFile("FblCoreLandingPageController /lastLoginDetails api is calling. API request: "+lastLoginDetails);
		APIResponse apiResponse = fblLandingPageService.lastLoginDetails(lastLoginDetails);
		RecordLog.writeLogFile("FblCoreLandingPageController /lastLoginDetails api completed. API response: "+apiResponse);
		return ResponseEntity.ok(apiResponse);

	}

	@PostMapping("/setFavouriteAccount")
	@io.swagger.v3.oas.annotations.Operation(summary = "setFavouriteAccount", description = "setFavouriteAccount")
	public ResponseEntity<APIResponse> setFavouriteAccount(
			@RequestBody FavouriteAccountRequest favouriteAccountRequest) {
		RecordLog.writeLogFile("FblCoreLandingPageController /setFavouriteAccount api is calling. API request: "+favouriteAccountRequest);
		APIResponse apiResponse = fblLandingPageService.setFavouriteAccount(favouriteAccountRequest);
		// pendingTranscation(pendingTranscationRequest);
		RecordLog.writeLogFile("FblCoreLandingPageController /setFavouriteAccount api completed. API response: "+apiResponse);
		return ResponseEntity.ok(apiResponse);

	}

	@PostMapping("/quickPayLimit")
	@io.swagger.v3.oas.annotations.Operation(summary = "quickPayLimit", description = "quickPayLimit")
	public ResponseEntity<APIResponse> getQuickPayLimit(@RequestBody QuickPayLimitRequest payLimitRequest) {
		RecordLog.writeLogFile("FblCoreLandingPageController /quickPayLimit api is calling. API request: "+payLimitRequest);
		APIResponse apiResponse = fblLandingPageService.getQuickPayLimit(payLimitRequest);
		RecordLog.writeLogFile("FblCoreLandingPageController /quickPayLimit api completed. API response: "+apiResponse);
		return ResponseEntity.ok(apiResponse);

	}

	@PostMapping("/application_user/activate")
	@io.swagger.v3.oas.annotations.Operation(summary = "App Activation Token", description = "App Activation Token")
	public ResponseEntity<APIResponse> activateApplicationUser(@RequestBody ActivateApplicationUserModel activateuser) {
		RecordLog.writeLogFile("FblCoreLandingPageController /application_user/activate api is calling. API request: "+activateuser);
		APIResponse apiResponse = fblLandingPageService.activateApplicationUser(activateuser);
		RecordLog.writeLogFile("FblCoreLandingPageController /application_user/activate api completed. API response: "+apiResponse);
		return ResponseEntity.ok(apiResponse);
	}

}
