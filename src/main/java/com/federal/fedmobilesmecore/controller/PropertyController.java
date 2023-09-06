package com.federal.fedmobilesmecore.controller;

import java.util.Base64;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/core/core/public")
public class PropertyController {

	@Value("${proxy.api.service.id}")
	private String APIServiceID;

	@Value("${proxy.api.service.iv.id}")
	private String APIServiceIVID;

//	@GetMapping("/api-service-id")
//	public String getAPIServiceID() {
//		return APIServiceID;
//	}
//
//	@GetMapping("/api-service-ivid")
//	public String getAPIServiceIVID() {
//		return APIServiceIVID;
//	}
	
	@PostMapping("/api-services-id")
	public String getkeyIv(@RequestBody String mobNo){
		
		String encDataKey = Base64.getEncoder().encodeToString(APIServiceID.getBytes());
		String encDataIv = Base64.getEncoder().encodeToString(APIServiceIVID.getBytes());
		String decDataKey = null;
		String decDataIv = null;
		
//		System.out.println("Encoded Data here"+encDataKey);
		Map<String, String> details = new HashMap<>();
		details.put("keyy",encDataKey);
		details.put("iv", encDataIv);
		byte[] byte1 = Base64.getDecoder().decode(details.get("keyy"));
		decDataKey = new String(byte1);
		byte[] byte2 = Base64.getDecoder().decode(details.get("iv"));
		decDataIv = new String(byte2);
		
		
		System.out.println("decDataKey"+decDataKey);
		System.out.println("decDataIv"+decDataIv);
		
		
		
		
		
		return encDataKey;
	}
}
