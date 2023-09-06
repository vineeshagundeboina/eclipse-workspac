package com.federal.fedmobilesmecore.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.federal.fedmobilesmecore.config.GlobalProperties;
import com.federal.fedmobilesmecore.dto.ApplicationEnterpris;
import com.federal.fedmobilesmecore.dto.PasswordHistory;
import com.federal.fedmobilesmecore.dto.User;
import com.federal.fedmobilesmecore.repository.AppTokenCheckRepository;
import com.federal.fedmobilesmecore.repository.UserRepository;

@Service
public class MpinCreateService {
	@Autowired
	UserRepository userRepository;
	@Autowired
	AppTokenCheckRepository passwdRepo;
	@Autowired
	GlobalProperties messages;

	public Map<String, Object> MpinCreate(String apptoken, boolean isEnabled, String mpinhash) {

		Map<String, Object> MpinCreateServiceResp = null;
		ArrayList<String> applicationpasswd = new ArrayList<>();
		List<User> userEnterpris = userRepository.findByAppTokenAndMarkAsEnabled(apptoken, isEnabled);

		if (userEnterpris.size() > 0) {

			long user_id = userEnterpris.get(0).getId();

			List<PasswordHistory> pwdhistories = passwdRepo.findFirst05ByUserIdOrderByIdDesc(user_id);

			if (pwdhistories != null && pwdhistories.size() > 0) {

				for (int i = 0; i < pwdhistories.size(); i++) {
					applicationpasswd.add(pwdhistories.get(i).getMpin());

					if (mpinhash.equals(applicationpasswd.get(i))) {
						MpinCreateServiceResp = new HashMap<String, Object>();
						MpinCreateServiceResp.put("status", false);
						MpinCreateServiceResp.put("description", messages.getmpincreatehasherr);
						MpinCreateServiceResp.put("message", "");
					} else {

						List<User> GetuserResp = userRepository.findByAppTokenAndMarkAsEnabled(apptoken, isEnabled);
						GetuserResp.get(0).setMpin(mpinhash);
						GetuserResp.get(0).setUpdatedAt(new Timestamp(new Date().getTime()));
						User userResp = userRepository.save(GetuserResp.get(0));

						PasswordHistory history = new PasswordHistory();
						history.setMpin(mpinhash);
						history.setUserId(user_id);
						history.setCreatedAt(new Timestamp(new Date().getTime()));
						history.setUpdatedAt(new Timestamp(new Date().getTime()));
						passwdRepo.save(history);

						MpinCreateServiceResp = new HashMap<String, Object>();
						MpinCreateServiceResp.put("status", true);
						MpinCreateServiceResp.put("description", "Success");
						MpinCreateServiceResp.put("message", "");
						MpinCreateServiceResp.put("pref_no", GetuserResp.get(0).getPrefNo());

					}

				}

			}

			else {
				List<User> GetuserResp = userRepository.findByAppTokenAndMarkAsEnabled(apptoken, isEnabled);
				GetuserResp.get(0).setMpin(mpinhash);
				GetuserResp.get(0).setUpdatedAt(new Timestamp(new Date().getTime()));
				User userResp = userRepository.save(GetuserResp.get(0));

				PasswordHistory history = new PasswordHistory();
				history.setMpin(mpinhash);
				history.setUserId(user_id);
				history.setCreatedAt(new Timestamp(new Date().getTime()));
				history.setUpdatedAt(new Timestamp(new Date().getTime()));
				passwdRepo.save(history);

				MpinCreateServiceResp = new HashMap<String, Object>();
				MpinCreateServiceResp.put("status", true);
				MpinCreateServiceResp.put("description", "Success");
				MpinCreateServiceResp.put("message", "");
				MpinCreateServiceResp.put("pref_no", GetuserResp.get(0).getPrefNo());

			}

		}

		else {

			MpinCreateServiceResp = new HashMap<String, Object>();
			MpinCreateServiceResp.put("status", false);
			MpinCreateServiceResp.put("description", messages.getmpincreateerr);
			MpinCreateServiceResp.put("message", "");

		}

		return MpinCreateServiceResp;
	}

}
