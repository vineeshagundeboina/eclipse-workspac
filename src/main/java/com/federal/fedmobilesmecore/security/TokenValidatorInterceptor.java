package com.federal.fedmobilesmecore.security;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.federal.fedmobilesmecore.dto.User;
import com.federal.fedmobilesmecore.model.GetExternalUserModelResp;
import com.federal.fedmobilesmecore.repository.UserRepository;

@Component
public class TokenValidatorInterceptor implements HandlerInterceptor {

	@Autowired
	UserRepository userRepository;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		final String authorizationHeaderValue = request.getHeader("Authorization");
		if (authorizationHeaderValue != null && authorizationHeaderValue.startsWith("Bearer")) {
			String token = authorizationHeaderValue.substring(7, authorizationHeaderValue.length());
			User user = userRepository.findByAuthToken(token);
			if (user == null) {
				ObjectMapper mapper = new ObjectMapper();
				response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
				GetExternalUserModelResp externalUserModelResp = new GetExternalUserModelResp();
				externalUserModelResp.setStatus(false);
				externalUserModelResp.setMessage("Invalid User Token");
				response.setContentType("application/json");	
				response.getWriter().write(mapper.writeValueAsString(externalUserModelResp));
				return false;
			}

		}
		return true;
	}

}
