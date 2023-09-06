package com.federal.fedmobilesmecore.controller;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.federal.fedmobilesmecore.config.RecordLog;
import com.federal.fedmobilesmecore.dto.User;
import com.federal.fedmobilesmecore.model.DestorySession;
import com.federal.fedmobilesmecore.model.LoginDetailsRequest;
import com.federal.fedmobilesmecore.model.MobileUserSessionModel;
import com.federal.fedmobilesmecore.model.RefreshTokenmodel;
import com.federal.fedmobilesmecore.model.SMEMessage;
import com.federal.fedmobilesmecore.model.WebUserSessionApiResp;
import com.federal.fedmobilesmecore.model.WebUserSessionModel;
import com.federal.fedmobilesmecore.repository.UserRepository;
import com.federal.fedmobilesmecore.service.CustomUserDetailsService_V1;
import com.federal.fedmobilesmecore.service.LoginDetailsService;

@RestController
@RequestMapping("/core/auth")
public class SessionController_V1 {

	@Autowired
	UserRepository userRepository;

	@Autowired
	CustomUserDetailsService_V1 userdetails;
	
	@Autowired
	private LoginDetailsService loginDetailsService;

	@PostMapping("/mobile_user_session")
	public ResponseEntity<SMEMessage> createMobileSession(@RequestBody MobileUserSessionModel request) {
		RecordLog.writeLogFile("SessionController_V1 /mobile_user_session api is calling. API request: "+request);
		SMEMessage apiResponse = userdetails.generateMobileUserAuthToken(request);
		RecordLog.writeLogFile("SessionController_V1 /mobile_user_session api completed. API response: "+apiResponse);
		return ResponseEntity.ok(apiResponse);
	}

	@PostMapping("/web_user_session")
	public ResponseEntity<WebUserSessionApiResp> createWebUsersSession(@RequestBody WebUserSessionModel request) {
		RecordLog.writeLogFile("SessionController_V1 /web_user_session api is calling. API request: "+request);
		WebUserSessionApiResp apiResponse = userdetails.generateWebUserAuthToken(request);
		RecordLog.writeLogFile("SessionController_V1 /web_user_session api completed. API response: "+apiResponse);
		return ResponseEntity.ok(apiResponse);

	}
	
	//new version v2.4 started
	
		@PostMapping("/web_user_sessionv2_4")
		public ResponseEntity<WebUserSessionApiResp> createWebUsersSessionV24(@RequestBody WebUserSessionModel request) {
			RecordLog.writeLogFile("SessionController_V1 /web_user_sessionv2_4 api is calling. API request: "+request);
			WebUserSessionApiResp apiResponse = userdetails.generateWebUserAuthTokenV24(request);
			RecordLog.writeLogFile("SessionController_V1 /web_user_sessionv2_4 api completed. API response: "+apiResponse);
			return ResponseEntity.ok(apiResponse);

		}
		
		//new version v2.4 ended

	@PostMapping("/destory_mobile_session")
	public ResponseEntity<SMEMessage> destoryMobileUserSession(@RequestBody DestorySession request) {
		RecordLog.writeLogFile("SessionController_V1 /destory_mobile_session api is calling. API request: "+request);
		SMEMessage apiResponse = userdetails.destorySession(request);
		RecordLog.writeLogFile("SessionController_V1 /destory_mobile_session api completed. API response: "+apiResponse);
		return ResponseEntity.ok(apiResponse);
	}

	@PostMapping("/destory_web_session")
	public ResponseEntity<SMEMessage> destoryWebUserSession(@RequestBody DestorySession request) {
		RecordLog.writeLogFile("SessionController_V1 /destory_web_session api is calling. API request: "+request);
		SMEMessage apiResponse = userdetails.destorySession(request);
		RecordLog.writeLogFile("SessionController_V1 /destory_web_session api completed. API response: "+apiResponse);
		return ResponseEntity.ok(apiResponse);
	}

	@GetMapping("/load_pref_no/{prefNo}")
	public ResponseEntity<SMEMessage> loadUser(@PathVariable String prefNo) {
		RecordLog.writeLogFile("SessionController_V1 /load_pref_no/{prefNo} api is calling. API request: "+prefNo);
		SMEMessage message = new SMEMessage();
		try {
		JSONObject json=new JSONObject(prefNo);
		String pref=json.optString("prefNo","");
		String mobile=json.optString("mobile","");
		message.setStatus(false);
		User apiResponse = userdetails.loadUserByPrefNO(pref, mobile);
		if (apiResponse != null) {
			if (apiResponse.getPrefNo() != null) {
				message.setStatus(true);
			}
		}
		}catch(JSONException e) {
			e.printStackTrace();
			message.setStatus(false);
			message.setMessage("Json formate exception");
		}
		RecordLog.writeLogFile("SessionController_V1 /load_pref_no/{prefNo} api completed. API response: "+message);
		return ResponseEntity.ok(message);
	}

	@PostMapping("/refreshtoken")
	public ResponseEntity<SMEMessage> refresh(@RequestBody RefreshTokenmodel refreshToken) {
		RecordLog.writeLogFile("SessionController_V1 /refreshtoken api is calling. API request: "+refreshToken);
		SMEMessage apiResponse = userdetails.refreshToken(refreshToken);
		RecordLog.writeLogFile("SessionController_V1 /refreshtoken api completed. API response: "+apiResponse);
		return ResponseEntity.ok(apiResponse);
	}
	
	@Async(value = "taskexecutor")
	@PostMapping(path="/add-logindetails",consumes = MediaType.APPLICATION_JSON_VALUE)
	public CompletableFuture<Object> addLoginDetails(@RequestBody LoginDetailsRequest loginDetails) {
		RecordLog.writeLogFile("SessionController_V1 /add-logindetails api is calling. API request: "+loginDetails);
		System.out.println("request came");
		CompletableFuture<Object> completed=loginDetailsService.createLoginDetails(loginDetails);
		System.out.println("final response sent");
		RecordLog.writeLogFile("SessionController_V1 /add-logindetails api completed. API response: "+loginDetails.getMobile());
		return CompletableFuture.completedFuture("completed");
	}
	@PostMapping("validate/prefno")
	public ResponseEntity<SMEMessage> validatePrefNumber(@RequestBody Map<String,String> prefNum){
		RecordLog.writeLogFile("SessionController_V1  validate/prefno api is calling. API request: "+prefNum);
		SMEMessage message = new SMEMessage();
		try {
		Optional<User> users=userRepository.findByPrefNoAndMarkAsEnabled(prefNum.get("userId"), true);
		if(users.isPresent()) {
			message.setMessage("UserId already exists.");
			message.setDescription("UserId already exists.");
			message.setStatus(false);
		}else {
			message.setMessage("success");
			message.setDescription("success");
			message.setStatus(true);
		}
		}catch(Exception e) {
			e.printStackTrace();
			RecordLog.writeLogFile("Exception occurred while validating prefnum: "+prefNum+Arrays.toString(e.getStackTrace()));
		}
		RecordLog.writeLogFile("SessionController_V1  validate/prefno api is completed. API response: "+prefNum +message);
		return ResponseEntity.ok(message);
	}
}