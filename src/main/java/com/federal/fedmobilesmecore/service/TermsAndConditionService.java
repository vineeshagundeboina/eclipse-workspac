package com.federal.fedmobilesmecore.service;

import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.federal.fedmobilesmecore.config.GlobalProperties;
import com.federal.fedmobilesmecore.model.TCUserAcceptanceResp;
import com.federal.fedmobilesmecore.model.UserTCAcceptanceRequest;
import com.federal.fedmobilesmecore.repository.UserRepository;

@Service
public class TermsAndConditionService {

	@Autowired
	UserRepository userRepo;
	@Autowired
	GlobalProperties globalProperties;

	@Value("${flatFilePath}")
	private String flatFilePath;

	private static final Logger log4j = LogManager.getLogger(TermsAndConditionService.class);

	public TCUserAcceptanceResp updateUserTCAcceptance(UserTCAcceptanceRequest request) {
		int result = 0;
		TCUserAcceptanceResp response = new TCUserAcceptanceResp();
		Date date = new Date();

		try {
			if (StringUtils.isNotEmpty(request.getMobileNo())) {
				result = userRepo.updateUsersTCByMobileNo(request.getIsAccepted(), request.getMobileNo(), date);
			} else if (StringUtils.isNotEmpty(request.getUserId())) {
				result = userRepo.updateUsersTCByUserId(request.getIsAccepted(), request.getUserId(), date);
			}
			if (result == 0) {
				response.setDescription(globalProperties.getUpdateFailure());
				response.setMessage(globalProperties.getFailuremsg());
				response.setStatus(false);
			} else {
				response.setDescription(globalProperties.getUpdateSuccess());
				response.setMessage(globalProperties.getSuccessmsg());
				response.setStatus(true);
			}
		} catch (Exception e) {
			e.printStackTrace();
			response.setDescription(e.getMessage());
			response.setMessage(globalProperties.getFailuremsg());
			response.setStatus(false);
		}
		return response;

	}

	public TCUserAcceptanceResp getTermsAndConditionsFromFile(String lang) {
		TCUserAcceptanceResp response = new TCUserAcceptanceResp();
		String valueOfCoressLang = "";
		try {
			System.out.println("flatFilePath: " + flatFilePath);
			JSONParser parser = new JSONParser();
			JSONObject jsonObject = (JSONObject) parser.parse(new InputStreamReader(new FileInputStream(flatFilePath)));

			if (jsonObject != null) {
				System.out.println("content of JSON file: " + jsonObject.toString());
				if (jsonObject.get(lang) == null) {

					response.setDescription("Invalid Language");
					response.setMessage("Failed");
					response.setStatus(false);
				} else {
					valueOfCoressLang = jsonObject.get(lang).toString().trim();
					System.out.println("value Of Coressponding Lang: " + valueOfCoressLang);
					response.setDescription(valueOfCoressLang);
					response.setMessage("Success");
					response.setStatus(true);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			response.setDescription("Something went wrong!!!");
			response.setMessage("Failed");
			response.setStatus(false);
		}
		return response;
	}
}
