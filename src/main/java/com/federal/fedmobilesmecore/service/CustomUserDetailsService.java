package com.federal.fedmobilesmecore.service;

import java.sql.Timestamp;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.federal.fedmobilesmecore.config.GlobalProperties;
import com.federal.fedmobilesmecore.config.RecordLog;
import com.federal.fedmobilesmecore.dto.User;
import com.federal.fedmobilesmecore.model.APIResponse;
import com.federal.fedmobilesmecore.model.DestorySession;
import com.federal.fedmobilesmecore.model.MobileUserSessionModel;
import com.federal.fedmobilesmecore.model.SMEMessage;
import com.federal.fedmobilesmecore.model.WebUserSessionModel;
import com.federal.fedmobilesmecore.repository.UserRepository;
import com.federal.fedmobilesmecore.repository.UserRepository_V1;

@Service
public class CustomUserDetailsService {

	@Autowired
	UserRepository_V1 userRepository;

	@Autowired
	JwtTokenProvider tokenProvider;
	@Autowired
	GlobalProperties globalProperties;

	public SMEMessage generateMobileUserAuthToken(MobileUserSessionModel request) {
		SMEMessage apiResponse = new SMEMessage();
		try {
			if (request.getPrefNo() != null && request.getMpin() != null) {
				User user = userRepository.findByPrefNoAndMobileAndMarkAsEnabled(request.getPrefNo(),request.getMobile(),true).orElse(null);
				// decrypt the password user.getMpin().equals(request.getMpin())
				if (user != null) {
					if (user.getMpin().equals(request.getMpin())) {
						if (user.getAuthToken() == null) {
							String auth_token = generateAuthToken(user);
							user.setAuthToken(auth_token);
							user.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
							userRepository.save(user);
							apiResponse.setStatus(true);
							apiResponse.setMessage(globalProperties.getSuccessmsg());
							apiResponse.setRecordid(auth_token);
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
				apiResponse.setMessage(globalProperties.getInvalidPrefNoOrMpin());
				apiResponse.setRecordid(globalProperties.getInvalidPrefNoOrMpin());
			}

		} catch (Exception e) {
			RecordLog.writeLogFile("Exception occured at: "+Arrays.toString(e.getStackTrace())+" Exception name: "+e.getMessage());
			apiResponse.setStatus(false);
			apiResponse.setMessage(e.getLocalizedMessage());
			apiResponse.setRecordid(globalProperties.getExceptionerrmsg());
		}

		return apiResponse;
	}

	public SMEMessage generateWebUserAuthToken(WebUserSessionModel request) {
		SMEMessage apiResponse = new SMEMessage();
		try {
			if (request.getPrefNo() != null && request.getPassword() != null) {
				User user = userRepository.findByPrefNoAndMobileAndMarkAsEnabled(request.getPrefNo(),request.getMobileNo(),true).orElse(null);
				// decrypt the password user.getPassword().equals(request.getPassword())

				if (user != null) {
					if (user.getPassword().equals(request.getPassword())) {
						if (user.getAuthToken() == null) {
							String auth_token = generateAuthToken(user);
							user.setAuthToken(auth_token);
							user.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
							userRepository.save(user);
							apiResponse.setStatus(true);
							apiResponse.setMessage(globalProperties.getSuccessmsg());
							apiResponse.setRecordid(auth_token);
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
		User user = userRepository.findByPrefNoAndMobileAndMarkAsEnabled(prefNo,mobile,true).orElse(null);
//		RecordLog.writeLogFile("user" + user);
		return user;
	}

	public SMEMessage destorySession(DestorySession request) {
		SMEMessage apiResponse = new SMEMessage();
		User user = userRepository.findByPrefNoAndMobileAndMarkAsEnabled(request.getPrefNo(),request.getMobile(),true)
				.orElseThrow(() -> new RuntimeException(globalProperties.getUsernotfoundmsg()));

		if (user != null && user.getAuthToken() != null) {
			user.setAuthToken(null);
			userRepository.save(user);
			apiResponse.setStatus(true);
			apiResponse.setMessage(globalProperties.getSuccessmsg());
			apiResponse.setRecordid(globalProperties.getSuccessmsg());

		} else {
			apiResponse.setStatus(false);
			apiResponse.setMessage(globalProperties.getUserNotFound());
			apiResponse.setDescription(globalProperties.getUsernotfoundmsg());
			apiResponse.setRecordid(null);
		}

		return apiResponse;
	}

	public SMEMessage refreshToken(String apptoken) {
		SMEMessage apiResponse = new SMEMessage();
		try {
			if (apptoken != null && apptoken != null) { // need to change condition based on requirement
				User user = userRepository.findByAppToken(apptoken).orElse(null);
				// decrypt the password user.getPassword().equals(request.getPassword())

				if (user != null) {
					if (user.getAppToken().equals(apptoken)) {
						if (tokenProvider.validateRefreshToken(apptoken, user)) {
							if (user.getAppToken() != null) {
								user = userRepository.getOne(user.getId());
								String auth_token = generateAuthToken(user);
								String refresh_token = refreshToken(user);
								user.setAuthToken(auth_token);
								user.setAppToken(refresh_token);
								user.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
								userRepository.save(user);
								apiResponse.setStatus(true);
								apiResponse.setMessage(globalProperties.getSuccessmsg());
								apiResponse.setRecordid(auth_token);
								apiResponse.setDescription(refresh_token);
							} else {

								apiResponse.setStatus(false);
								apiResponse.setMessage(globalProperties.getInvalidApptoken());
								apiResponse.setRecordid(null);
							}
						} else {
							apiResponse.setStatus(false);
							apiResponse.setMessage(globalProperties.getAppTokenExpired());
							apiResponse.setRecordid(null);
						}

					} else {
						apiResponse.setStatus(false);
						apiResponse.setMessage(globalProperties.getInvalidApptoken());
						apiResponse.setRecordid(null);
					}

				} else {
					apiResponse.setStatus(false);
					apiResponse.setMessage("User Not Found with appToken: " + apptoken);

				}
			} else {
				apiResponse.setStatus(false);
				apiResponse.setMessage(globalProperties.getInvalidApptoken());
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