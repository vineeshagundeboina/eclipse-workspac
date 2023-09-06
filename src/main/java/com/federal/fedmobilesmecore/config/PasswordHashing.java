package com.federal.fedmobilesmecore.config;



import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

import org.springframework.stereotype.Component;

@Component
public class PasswordHashing {

	
public String getPasswordHash(String password) {
		
		MessageDigest digest;
		final StringBuilder hexHashString = new StringBuilder();
		
		try {
		digest = MessageDigest.getInstance("SHA-256");
		byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
		for (int i = 0; i < hash.length; i++) {
            final String hex = Integer.toHexString(0xff & hash[i]);
            if(hex.length() == 1) 
            	hexHashString.append('0');
                hexHashString.append(hex);
        }
		
		
	}
		
		
	catch(Exception e) {
		e.printStackTrace();
		
		
	}
		return hexHashString.toString();
		
	}
}
