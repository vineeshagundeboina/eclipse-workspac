package com.federal.fedmobilesmecore.security;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class EncryptDecrypt {

	private static final Logger log4j = LogManager.getLogger(EncryptDecrypt.class);
	JSONParser parser = new JSONParser();

	@Value("${proxy.api.service.id}")
	private String APIServiceID;

	@Value("${proxy.api.service.iv.id}")
	private String APIServiceIVID;

	public String Encryption(String indata)
			throws JSONException, SecurityException, IOException, InvalidKeyException, NoSuchAlgorithmException,
			NoSuchPaddingException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
		log4j.info(APIServiceID + ":Encservice~~~Request~~~" + indata);
		String output = AESCipher.aesEncryptString(indata.toString(), APIServiceID, APIServiceIVID);

		return output;
	}

	public String Decryption(String indata) throws JSONException, SecurityException, IOException, InvalidKeyException,
			NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException,
			IllegalBlockSizeException, BadPaddingException, ParseException {
		String output = "";
		org.json.simple.JSONObject jsonObject = new org.json.simple.JSONObject();
		log4j.info("Decservice~~~Request~~~" + indata);
		output = AESCipher.aesDecryptString(indata, APIServiceID, APIServiceIVID);
		output = output.substring(14, output.length() - 14);
		log4j.info("Decservice~~~output~~~" + output);
		Object objnew = parser.parse(output);
		jsonObject = (org.json.simple.JSONObject) objnew;
		return jsonObject.toString();
	}

}
