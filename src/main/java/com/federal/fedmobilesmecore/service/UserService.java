package com.federal.fedmobilesmecore.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.federal.fedmobilesmecore.dto.User;
import com.federal.fedmobilesmecore.repository.UserRepository;

@Service
public class UserService {
	@Autowired
	UserRepository userRepository;

	public User getUserDetails(String mobileNumber, boolean isEnabled) {
		return userRepository.findByMobileAndMarkAsEnabled(mobileNumber, isEnabled);
	}

	public User SaveUserDetails(User user) {
		return userRepository.save(user);

	}
}
