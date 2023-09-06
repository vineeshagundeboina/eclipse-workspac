package com.federal.fedmobilesmecore.service;

import java.math.BigDecimal;
import java.security.SecureRandom;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Random;

import javax.net.ssl.SSLContext;

import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.ssl.SSLContexts;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.federal.fedmobilesmecore.config.GlobalProperties;
import com.federal.fedmobilesmecore.config.RecordLog;
import com.federal.fedmobilesmecore.dto.SimBinding;
import com.federal.fedmobilesmecore.dto.User;
import com.federal.fedmobilesmecore.dto.UserToken;
import com.federal.fedmobilesmecore.model.CreateSimBindingReqModel;
import com.federal.fedmobilesmecore.model.CreateSimBindingRespModel;
import com.federal.fedmobilesmecore.model.GetSimBindingReqModel;
import com.federal.fedmobilesmecore.model.GetSimBindingRespModel;
import com.federal.fedmobilesmecore.model.SMEMessage;
import com.federal.fedmobilesmecore.repository.SimBindingRepository;
import com.federal.fedmobilesmecore.repository.UserTokenRepo;

/**
 * @author Debasish_Splenta
 *
 */

@Service
public class SimBindingService {
	private static final int length = 12;
	@Value("${gateway.url}")
	private String gatewayurl;
	@Autowired
	GlobalProperties messages;
	@Autowired
	UserService userService;
	@Autowired
	SimBindingRepository simBindingRepository;
	@Autowired
	UserTokenRepo tokenrepo;
	@Value("${app.isUat}")
	private String isUat;
	@Autowired
	RestTemplate restTemplate;
	
	private String playStore=System.getenv("ptFlag");
	private String mobile=System.getenv("ptMobile");
	
	public SimBinding createSimBinding(CreateSimBindingReqModel bindingReqModel) {
		SimBinding binding = new SimBinding();
		binding.setMobile(bindingReqModel.getMobile());
		binding.setSimHash(bindingReqModel.getSimHash());
		binding.setCreatedAt(new Timestamp(new Date().getTime()));
		binding.setUpdatedAt(new Timestamp(new Date().getTime()));
		binding.setSimRandomNo(bindingReqModel.getSimRandomNo());

		return simBindingRepository.save(binding);
	}

	public SimBinding findbymobileSimhash(GetSimBindingReqModel bindingReqModel) {
		return simBindingRepository.findByMobileAndSimHash(bindingReqModel.getMobile(), bindingReqModel.getSimHash());
	}

	public List<SimBinding> findbymobile_simhash(GetSimBindingReqModel bindingReqModel) {
		return simBindingRepository.findByMobileAndSimHashOrderByUpdatedAt(bindingReqModel.getMobile(),
				bindingReqModel.getSimHash());
	}

	public CreateSimBindingRespModel createSimService(CreateSimBindingReqModel createSimBindingReqModel) {
		CreateSimBindingRespModel bindingRespModel = null;
		GetSimBindingReqModel bindingReqModel = null;
		SimBinding simBinding = null;
		List<SimBinding> respSimBinding = null;
		try {
//			if (createSimBindingReqModel.getMobile().length() == 12) {
				bindingReqModel = new GetSimBindingReqModel();
				bindingReqModel.setMobile(createSimBindingReqModel.getMobile());
				bindingReqModel.setSimHash(createSimBindingReqModel.getSimHash());
				respSimBinding = findbymobile_simhash(bindingReqModel);
				if (respSimBinding == null || respSimBinding.size() == 0) {
					simBinding = createSimBinding(createSimBindingReqModel);
					bindingRespModel = new CreateSimBindingRespModel();
					bindingRespModel.setSimBinding(simBinding);
					// Added by vikas for demonstration
					bindingRespModel.setStatus(messages.isSuccess());
					bindingRespModel.setMessage(messages.getSuccessmsg());
				} else {
					bindingRespModel = new CreateSimBindingRespModel();
					bindingRespModel.setSimBinding(respSimBinding.get(0));
					bindingRespModel.setStatus(messages.isSuccess());
					bindingRespModel.setMessage(messages.getSuccessmsg());
				}
//			} else {
//				bindingRespModel = new CreateSimBindingRespModel();
//				bindingRespModel.setSimBinding(null);
//				// Added by vikas for demonstration with dynamic values
//				bindingRespModel.setStatus(messages.isFailed());
//				bindingRespModel.setMessage(messages.getMsgsimbindinginvalidlength().replace("@length@", "" + length));
//			}
		} catch (Exception e) {
			RecordLog.writeLogFile("Exception occured at: "+Arrays.toString(e.getStackTrace())+" Exception name: "+e.getMessage());
			e.printStackTrace();
			bindingRespModel = new CreateSimBindingRespModel();
			bindingRespModel.setSimBinding(null);
			bindingRespModel.setStatus(messages.isFailed());
			bindingRespModel.setMessage(messages.getExceptionerrmsg());
		}

		return bindingRespModel;
	}

	public GetSimBindingRespModel getSimBinding(GetSimBindingReqModel simBindingReqModel) {
		GetSimBindingRespModel simBindingRespModel = null;
		SimBinding simBindingStatus = null;
		User user = null;
		List<User> userList = null;
		String generatedToken = null;

		try {
			if (simBindingReqModel.getMobile() != null && simBindingReqModel.getSimHash() != null) {
				if (simBindingReqModel.getMobile().length() != 0 && simBindingReqModel.getSimHash().length() != 0) {
					simBindingStatus = findbymobileSimhash(simBindingReqModel);	
					
					RecordLog.writeLogFile("environment flag "+playStore);
					RecordLog.writeLogFile("environment mobile "+mobile);
					if(simBindingStatus!=null) {
						if(playStore.equals("yes") && simBindingReqModel.getMobile().equals(mobile)) {
							RecordLog.writeLogFile("environment setting status as approved for this mobile "+mobile);
							simBindingStatus.setStatus("approved");
							simBindingStatus=simBindingRepository.save(simBindingStatus);
						}
					}

 					if (simBindingStatus == null) {
						simBindingRespModel = new GetSimBindingRespModel();
						simBindingRespModel.setAppToken("");
						simBindingRespModel.setMessage(messages.getSimbinderr02());
						simBindingRespModel.setStatus(messages.isFailed());
					} else if (simBindingStatus.getStatus() == null) {
						simBindingRespModel = new GetSimBindingRespModel();
						simBindingRespModel.setAppToken("");
						simBindingRespModel.setMessage(messages.getSimbinderr03());
						simBindingRespModel.setStatus(messages.isFailed());
					} else if (simBindingStatus.getStatus().equals("approved")) {
						user = userService.getUserDetails(simBindingReqModel.getMobile(), true);
											
						if (user == null) {
							simBindingRespModel = new GetSimBindingRespModel();
							simBindingRespModel.setAppToken("");
							simBindingRespModel.setMessage(messages.getSimbinderr02());
							simBindingRespModel.setStatus(messages.isFailed());
							return simBindingRespModel;
						}
						int x = new SecureRandom().nextInt();
						generatedToken = Integer.toHexString(x);
						user.setAppToken(generatedToken);
						if (user.getAppToken() != null) {
							if (user.getActivationStatus() == null) {
								simBindingRespModel = new GetSimBindingRespModel();
								simBindingRespModel.setAppToken(user.getAppToken());
								simBindingRespModel.setMessage(messages.getGetMsgsimbindingsuccess01());
								simBindingRespModel.setStatus(messages.isSuccess());
							} else if (user.getActivationStatus().equals("activated") || user.getActivationStatus().equals("exceeded")) {
								user.setActivationStatus("pending_activation");
//								SMEMessage msg = generateRegiToken();
//								if( msg.isStatus()) {
//									UserToken usertoken = generateUserToken(user, msg.getRecordid());
//									tokenrepo.save(usertoken);								
//								}
								System.out.println("generating app token  ");
								SMEMessage msgapptoken = generateAppToken();
								System.out.println("generated app token  "+msgapptoken.getRecordid());
								UserToken usertoken1 = generateUserToken(user, msgapptoken.getRecordid());
								tokenrepo.save(usertoken1);
								System.out.println("user token details "+usertoken1);
								user.setWrongActivationTokenCount("0");
								user.setAppToken(msgapptoken.getRecordid());
								userService.SaveUserDetails(user);
								String messageString = messages.getUserregistrationtokenmessage();
								RecordLog.writeLogFile("messageString:" + messageString);
								RecordLog.writeLogFile("Token:" + msgapptoken.getRecordid());
								if(messageString!=null) {
										messageString = messageString.replace("@token@", msgapptoken.getRecordid());
										System.out.println("sending sms  ");
										SMEMessage smsapptoken = sendSMS(messageString, simBindingReqModel.getMobile());
										System.out.println("sms sent  "+smsapptoken.toString());
										if (smsapptoken.isStatus()) {
											RecordLog.writeLogFile(smsapptoken.isStatus() + ":" + smsapptoken.getMessage());
										}
								}
								simBindingRespModel = new GetSimBindingRespModel();
								simBindingRespModel.setAppToken(user.getAppToken());
								simBindingRespModel.setMessage("successfuly generated");
								simBindingRespModel.setStatus(messages.isSuccess());
							} else {
								simBindingRespModel = new GetSimBindingRespModel();
								simBindingRespModel.setAppToken(user.getAppToken());
								simBindingRespModel.setMessage("successfuly generated");
								simBindingRespModel.setStatus(messages.isSuccess());
							}
							user = userService.SaveUserDetails(user);
						} else if (user.getAppToken().length() != 0) {
							simBindingRespModel = new GetSimBindingRespModel();
							simBindingRespModel.setAppToken(user.getAppToken());
							simBindingRespModel.setMessage(messages.getSimbinderr05());
							simBindingRespModel.setStatus(messages.isSuccess());
						} else {
							simBindingRespModel = new GetSimBindingRespModel();
							simBindingRespModel.setAppToken("");
							simBindingRespModel.setMessage(messages.getSimbinderr06());
							simBindingRespModel.setStatus(messages.isFailed());
						}
					} else {
						simBindingRespModel = new GetSimBindingRespModel();
						simBindingRespModel.setAppToken("");
						simBindingRespModel.setMessage(messages.getSimbinderr03());
						simBindingRespModel.setStatus(messages.isFailed());
					}
				} else {
					simBindingRespModel = new GetSimBindingRespModel();
					simBindingRespModel.setAppToken(messages.getSimbinderr07());
					simBindingRespModel.setMessage(messages.getErrormsg());
					simBindingRespModel.setStatus(messages.isFailed());
				}
			} else {
				simBindingRespModel = new GetSimBindingRespModel();
				simBindingRespModel.setAppToken("");
				simBindingRespModel.setMessage(messages.getSimbinderr09());
				simBindingRespModel.setStatus(messages.isFailed());
			}
		}

		catch (IncorrectResultSizeDataAccessException e) {
			RecordLog.writeLogFile("Exception occured at: "+Arrays.toString(e.getStackTrace())+" Exception name: "+e.getMessage());
			e.printStackTrace();
			simBindingRespModel = new GetSimBindingRespModel();
			simBindingRespModel.setMessage(messages.getMsgsimbindingerrmsg02());
			simBindingRespModel.setStatus(messages.isFailed());
		} catch (Exception e) {
			RecordLog.writeLogFile("Exception occured at: "+Arrays.toString(e.getStackTrace())+" Exception name: "+e.getMessage());
			e.printStackTrace();
			simBindingRespModel = new GetSimBindingRespModel();
			simBindingRespModel.setMessage(messages.getMsgsimbindingerrmsg());
			simBindingRespModel.setStatus(messages.isFailed());
		}
		return simBindingRespModel;
	}
	
	
	//new version2.4 started getSimBindingV24	
	public GetSimBindingRespModel getSimBindingV24(GetSimBindingReqModel simBindingReqModel) {
		
		RecordLog.writeLogFile("VERSION2.4 METHOD IS CALLING ");
		
		GetSimBindingRespModel simBindingRespModel = null;
		SimBinding simBindingStatus = null;
		User user = null;
		List<User> userList = null;
		String generatedToken = null;

		try {
			if (simBindingReqModel.getMobile() != null && simBindingReqModel.getSimHash() != null) {
				if (simBindingReqModel.getMobile().length() != 0 && simBindingReqModel.getSimHash().length() != 0) {
					simBindingStatus = findbymobileSimhash(simBindingReqModel);	
					
					RecordLog.writeLogFile("environment flag "+playStore);
					RecordLog.writeLogFile("environment mobile "+mobile);
					if(simBindingStatus!=null) {
						if(playStore.equals("yes") && simBindingReqModel.getMobile().equals(mobile)) {
							RecordLog.writeLogFile("environment setting status as approved for this mobile "+mobile);
							simBindingStatus.setStatus("approved");
							simBindingStatus=simBindingRepository.save(simBindingStatus);
						}
					}

 					if (simBindingStatus == null) {
						simBindingRespModel = new GetSimBindingRespModel();
						simBindingRespModel.setAppToken("");
						simBindingRespModel.setMessage(messages.getSimbinderr02());
						simBindingRespModel.setStatus(messages.isFailed());
					} else if (simBindingStatus.getStatus() == null) {
						simBindingRespModel = new GetSimBindingRespModel();
						simBindingRespModel.setAppToken("");
						simBindingRespModel.setMessage(messages.getSimbinderr03());
						simBindingRespModel.setStatus(messages.isFailed());
					} else if (simBindingStatus.getStatus().equals("approved")) {
						user = userService.getUserDetails(simBindingReqModel.getMobile(), true);
											
						if (user == null) {
							simBindingRespModel = new GetSimBindingRespModel();
							simBindingRespModel.setAppToken("");
							simBindingRespModel.setMessage(messages.getSimbinderr02());
							simBindingRespModel.setStatus(messages.isFailed());
							return simBindingRespModel;
						}
						int x = new SecureRandom().nextInt();
						generatedToken = Integer.toHexString(x);
						user.setAppToken(generatedToken);
						if (user.getAppToken() != null) {
							if (user.getActivationStatus() == null) {
								simBindingRespModel = new GetSimBindingRespModel();
								simBindingRespModel.setAppToken(user.getAppToken());
								simBindingRespModel.setMessage(messages.getGetMsgsimbindingsuccess01());
								simBindingRespModel.setStatus(messages.isSuccess());
							} else if (user.getActivationStatus().equals("activated") || user.getActivationStatus().equals("exceeded")) {
								user.setActivationStatus("pending_activation");
							
								user.setWrongActivationTokenCount("0");
								userService.SaveUserDetails(user);
       							simBindingRespModel = new GetSimBindingRespModel();
								simBindingRespModel.setAppToken(user.getAppToken());
								simBindingRespModel.setMessage("successfuly generated");
								simBindingRespModel.setStatus(messages.isSuccess());
							} else {
								simBindingRespModel = new GetSimBindingRespModel();
								simBindingRespModel.setAppToken(user.getAppToken());
								simBindingRespModel.setMessage("successfuly generated");
								simBindingRespModel.setStatus(messages.isSuccess());
							}
							user = userService.SaveUserDetails(user);
						} else if (user.getAppToken().length() != 0) {
							simBindingRespModel = new GetSimBindingRespModel();
							simBindingRespModel.setAppToken(user.getAppToken());
							simBindingRespModel.setMessage(messages.getSimbinderr05());
							simBindingRespModel.setStatus(messages.isSuccess());
						} else {
							simBindingRespModel = new GetSimBindingRespModel();
							simBindingRespModel.setAppToken("");
							simBindingRespModel.setMessage(messages.getSimbinderr06());
							simBindingRespModel.setStatus(messages.isFailed());
						}
					} else {
						simBindingRespModel = new GetSimBindingRespModel();
						simBindingRespModel.setAppToken("");
						simBindingRespModel.setMessage(messages.getSimbinderr03());
						simBindingRespModel.setStatus(messages.isFailed());
					}
				} else {
					simBindingRespModel = new GetSimBindingRespModel();
					simBindingRespModel.setAppToken(messages.getSimbinderr07());
					simBindingRespModel.setMessage(messages.getErrormsg());
					simBindingRespModel.setStatus(messages.isFailed());
				}
			} else {
				simBindingRespModel = new GetSimBindingRespModel();
				simBindingRespModel.setAppToken("");
				simBindingRespModel.setMessage(messages.getSimbinderr09());
				simBindingRespModel.setStatus(messages.isFailed());
			}
		}

		catch (IncorrectResultSizeDataAccessException e) {
			RecordLog.writeLogFile("Exception occured at: "+Arrays.toString(e.getStackTrace())+" Exception name: "+e.getMessage());
			e.printStackTrace();
			simBindingRespModel = new GetSimBindingRespModel();
			simBindingRespModel.setMessage(messages.getMsgsimbindingerrmsg02());
			simBindingRespModel.setStatus(messages.isFailed());
		} catch (Exception e) {
			RecordLog.writeLogFile("Exception occured at: "+Arrays.toString(e.getStackTrace())+" Exception name: "+e.getMessage());
			e.printStackTrace();
			simBindingRespModel = new GetSimBindingRespModel();
			simBindingRespModel.setMessage(messages.getMsgsimbindingerrmsg());
			simBindingRespModel.setStatus(messages.isFailed());
		}
		return simBindingRespModel;
	}
	//new version2.4 ended getSimBindingV24
	

	public CreateSimBindingRespModel createsimerr() {
		CreateSimBindingRespModel bindingRespModel = null;
		bindingRespModel = new CreateSimBindingRespModel();
		bindingRespModel.setSimBinding(null);
		bindingRespModel.setStatus(messages.isFailed());
		bindingRespModel.setMessage(messages.getErrormsg());
		return bindingRespModel;
	}

	public GetSimBindingRespModel getsimerr() {
		GetSimBindingRespModel simBindingRespModel = null;
		simBindingRespModel = new GetSimBindingRespModel();
		simBindingRespModel.setAppToken("");
		simBindingRespModel.setMessage(messages.getErrormsg());
		simBindingRespModel.setStatus(messages.isFailed());
		return simBindingRespModel;
	}
	
	public SMEMessage generateRegiToken() {

		SMEMessage message = new SMEMessage();
		String random = getRandomHexString(8);
		message.setStatus(true);
		message.setRecordid(random);
		return message;
	}
	public SMEMessage generateAppToken() {

		SMEMessage message = new SMEMessage();
		String random = getRandomHexString(6);
		message.setStatus(true);
		message.setRecordid(random);
		return message;
	}
	
	private String getRandomHexString(int numchars) {
		Random r = new Random();
		StringBuffer sb = new StringBuffer();
		while (sb.length() < numchars) {
			sb.append(Integer.toHexString(r.nextInt()));
		}

		return sb.toString().substring(0, numchars);
	}
	
	public UserToken generateUserToken(User user, String tokentxt) {

		Date date = new Date();
		Timestamp timestamp = new Timestamp(date.getTime());
		ZoneId defaultZoneId = ZoneId.systemDefault();
		LocalDate localDate = LocalDate.now().plusDays(1);
		Date enddate = Date.from(localDate.atStartOfDay(defaultZoneId).toInstant());

		UserToken token = new UserToken();
		token.setActive(BigDecimal.ONE);
		token.setUser(user);
		token.setCreatedAt(timestamp);
		token.setUpdatedAt(timestamp);
		token.setExpiredAt(new Timestamp(enddate.getTime()));
		token.setToken(tokentxt);
		token.setType("RegistrationToken");
		return token;
	}

	public SMEMessage sendSMS(String message, String mobileno) {
		SMEMessage messageobj = new SMEMessage();
//		SendSmsReqModel sendSmsReqModel = new SendSmsReqModel();
//		sendSmsReqModel.setMessageText(message);
//		sendSmsReqModel.setMobileNo(mobileno);
//		sendSmsReqModel.setShortCode(String.valueOf(new Random().nextInt(9000000) + 1000000));
		JSONObject req = new JSONObject();
        req.put("mobileno",mobileno);
        req.put("messageText",message );
        req.put("shortcode", String.valueOf(new Random().nextInt(9000000) + 1000000));
		
        HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
//headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		HttpEntity<String> entity = new HttpEntity<String>(req.toString(),
				headers);
		try {
			SSLContext sslcontext = SSLContexts.custom().loadTrustMaterial(null, (chain, authType) -> true).build();
			// FIXME In future issue might come to enhance it to TLSv1.2
			SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslcontext, new String[] { "TLSv1" },
					null, new NoopHostnameVerifier());
			HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory(
					HttpClientBuilder.create().setSSLSocketFactory(sslsf).build());
			requestFactory.setReadTimeout(60000);
			requestFactory.setConnectTimeout(60000);

			restTemplate.setRequestFactory(requestFactory);
			System.out.println("send sms request for mobile "+mobileno+req.toString());
			ResponseEntity<?> smsResponse = restTemplate.postForEntity(gatewayurl + "/fblgateway_v1/sendmsgtouser",
					entity, String.class);
			RecordLog.writeLogFile("smsResponse for mobile "+mobileno+"    " + smsResponse);
			if (smsResponse.getStatusCode() == HttpStatus.OK) {
				String resp = (String) smsResponse.getBody();
				if (resp.equals("Failure")) {
					messageobj.setStatus(false);
					messageobj.setMessage("Unable to send Message");
				} else {
					messageobj.setStatus(true);
					messageobj.setMessage("SMS Sent succesfully");
				}
			} else {
				messageobj.setStatus(false);
				messageobj.setMessage("Unable to send Message");

			}
		} catch (Exception e) {
			RecordLog.writeLogFile("Exception occured at: mobile "+mobileno+"   "+Arrays.toString(e.getStackTrace())+" Exception name: "+e.getMessage());
			e.printStackTrace();
			messageobj.setStatus(true);
			messageobj.setMessage("Error Occured while sending SMS.");
		}

		return messageobj;
	}
}
