package com.federal.fedmobilesmecore.service;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.federal.fedmobilesmecore.config.RecordLog;
import com.federal.fedmobilesmecore.dto.User;
import com.google.common.base.Function;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtTokenProvider {
	private static final Logger log4j = LogManager.getLogger(JwtTokenProvider.class);

	public static final long JWT_TOKEN_VALIDITY = 5 * 60 * 60;

	@Value("${app.jwtSecret}")
	private String secret;

	@Value("${app.jwtExpirationInMs}")
	private String jwtExpirationInMs;

	@Value("${refreshtokenExpiration}")
	private String refreshtokenExpiration;

	public String getPrefNoFromToken(String token) {
		return getClaimFromToken(token, Claims::getSubject);
	}

	// retrieve expiration date from jwt token
	public Date getExpirationDateFromToken(String token) {
		return getClaimFromToken(token, Claims::getExpiration);
	}

	public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = getAllClaimsFromToken(token);
		return claimsResolver.apply(claims);
	}

	// for retrieveing any information from token we will need the secret key
	private Claims getAllClaimsFromToken(String token) {
		return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
	}

	// check if the token has expired
	private Boolean isTokenExpired(String token) {
		Date expiration = getExpirationDateFromToken(token);
		return expiration.before(new Date());
	}

//	check if the token has expired after adding one minute
	private Boolean canTokenBeRefreshed(String token) {
		Date expiration = getExpirationDateFromToken(token);
		Calendar cal = Calendar.getInstance();
		cal.setTime(expiration);
		cal.add(Calendar.SECOND, 180);
		expiration = cal.getTime();
		// return cal.after(new Date());
//		RecordLog.writeLogFile("cal.after(new Date()): " + cal.after(new Date()));
//		RecordLog.writeLogFile("expiration.after(new Date()): " + expiration.after(new Date()));
		return expiration.after(new Date());
	}

	// generate token for user
	public String generateToken(User userDetails) {
		Map<String, Object> claims = new HashMap<>();
//		log4j.info("JWT token proved" + userDetails.getPrefNo());
		JSONObject json=new JSONObject();
		json.put("prefNo", userDetails.getPrefNo());
		json.put("mobile", userDetails.getMobile());
		System.out.println("generating token for this:"+json.toString());
		return doGenerateToken(claims, json.toString());
	}

	private String doGenerateToken(Map<String, Object> claims, String subject) {

		return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + Long.valueOf(jwtExpirationInMs)))
				.signWith(SignatureAlgorithm.HS512, secret).compact();
	}

	// refresh token for user
	public String refreshToken(User userDetails) {
		Map<String, Object> claims = new HashMap<>();
//		log4j.info("JWTTOkenproved" + userDetails.getPrefNo());
		return doRefreshToken(claims, userDetails.getPrefNo());
	}

	private String doRefreshToken(Map<String, Object> claims, String subject) {

		return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + Long.valueOf(refreshtokenExpiration)))
				.signWith(SignatureAlgorithm.HS512, secret).compact();
	}

	public Boolean validateRefreshToken(String token, User userDetails) {
		String apptoken = getPrefNoFromToken(token);
		JSONObject json=new JSONObject(apptoken);
		String prefNo=json.getString("prefNo");
		String mobile=json.getString("mobile");
//		RecordLog.writeLogFile("userName: " + apptoken);
//		RecordLog.writeLogFile("userDetails.getPrefNo(): " + userDetails.getPrefNo());
//		RecordLog.writeLogFile("apptoken.equals(userDetails.getPrefNo()): " + apptoken.equals(userDetails.getPrefNo()));
		return (prefNo.equals(userDetails.getPrefNo()) && mobile.equals(userDetails.getMobile()) && canTokenBeRefreshed(token));
	}

	/*
	 * private String getAppToken(String token) { return getClaimFromToken(token,
	 * Claims::getSubject); }
	 */

}
