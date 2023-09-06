package com.federal.fedmobilesmecore.service;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Timer;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.federal.fedmobilesmecore.dto.Enterprises;
import com.federal.fedmobilesmecore.dto.LoginDetails;
import com.federal.fedmobilesmecore.dto.User;
import com.federal.fedmobilesmecore.model.AccountStatementResponse;
import com.federal.fedmobilesmecore.model.LoginDetailsRequest;
import com.federal.fedmobilesmecore.repository.EnterprisesRepository;
import com.federal.fedmobilesmecore.repository.LoginDetailsRepository;
import com.federal.fedmobilesmecore.repository.UserRepository;
import com.federal.fedmobilesmecore.repository.UserRepository_V1;

@Service
public class LoginDetailsService {
	
	@Autowired
	private LoginDetailsRepository loginDetailsRepository;
	
	@Autowired
	ObjectMapper mapper;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private EnterprisesRepository enterprises;
	
	
	@Async(value = "taskexecutor")
	public CompletableFuture<Object> createLoginDetails(LoginDetailsRequest loginDetailsRequest){
		System.out.println("in service came");

		LoginDetails loginDetails =mapper.convertValue(loginDetailsRequest, LoginDetails.class);
		
		
		User user=userRepository.findByMobileAndMarkAsEnabled(loginDetailsRequest.getMobile(), true);
        Optional<Enterprises> enterpris=enterprises.findById(Long.valueOf(user.getEnterpriseId()));
        if(enterpris.isPresent()) {
         loginDetails.setAccountName(enterpris.get().getAccName());
         loginDetails.setAccountNumber(enterpris.get().getAccNo());
          }
		
		
		
		
		loginDetails.setCreatedAt(Timestamp.from(new Date(System.currentTimeMillis()).toInstant()));
		loginDetailsRepository.save(loginDetails);
		System.out.println("service completed");
		return  CompletableFuture.completedFuture("completed");
		
	}

}
