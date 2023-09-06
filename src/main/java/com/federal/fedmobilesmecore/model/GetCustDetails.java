package com.federal.fedmobilesmecore.model;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.federal.fedmobilesmecore.config.RecordLog;
import com.federal.fedmobilesmecore.config.RestTemplateSSLConfig;

@Service
public class GetCustDetails {

	// private RestTemplate resttemplate;

//	@Value("${UserId}")
	private String user_id = System.getenv("getCustomerUserId");
//	@Value("${Password}")
	private String pasword = System.getenv("getCustomerUserPassword");
	/*
	 * @Value("${host}") private String host;
	 * 
	 * @Value("${port}") private int port;
	 */
//	@Value("${GetCustDetailsUrl}")
	private String GetCustDetailsUrl = System.getenv("GetCustDetailsUrl");

	//@Value("${x-ibm-client-id}")
	private String clientId = System.getenv("custDetailsClientId");

	//@Value("${x-ibm-client-secret}")
	private String clientSecret = System.getenv("custDetailsClientSecret");


	/*
	 * @Autowired public GetCustDetails(RestTemplateBuilder restTemplateBuilder){
	 * this.resttemplate=restTemplateBuilder.build(); }
	 */
	@Autowired
	RestTemplateSSLConfig restconfig;

	public String getData(String acc_no) {
		RestTemplate restTemplate = restconfig.restTemplate();
		String custDetailRequest = "<GetCustomerDetailsReq>" + "<UserId>" + user_id + "</UserId>" + "<Password>"
				+ pasword + "</Password>" + "<CustomerID></CustomerID>" + "<AccountNumber>" + acc_no
				+ "</AccountNumber>" + "<MobileNumber></MobileNumber>" + "</GetCustomerDetailsReq>";
		RecordLog.writeLogFile("XML in getdata is:"+custDetailRequest);
		try {
			/*
			 * Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(host, port));
			 * SimpleClientHttpRequestFactory requestFactory = new
			 * SimpleClientHttpRequestFactory(); requestFactory.setProxy(proxy);
			 */
			// RestTemplate resttemplate=new RestTemplate();
			HttpHeaders httpHeaders = new HttpHeaders();
			httpHeaders.set("x-ibm-client-id", clientId);
			httpHeaders.set("x-ibm-client-secret", clientSecret);
			httpHeaders.setContentType(MediaType.APPLICATION_XML);
			httpHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_XML));
			HttpEntity<String> entity = new HttpEntity<String>(custDetailRequest, httpHeaders);
			ResponseEntity<?> response = restTemplate.exchange(GetCustDetailsUrl, HttpMethod.POST, entity,
					String.class);
			RecordLog.writeLogFile("Fedcorp response >>:" + response);
			return restTemplate.exchange(GetCustDetailsUrl, HttpMethod.POST, entity, String.class).getBody();
		} catch (Exception e) {
			RecordLog.writeLogFile("Exception occured at: "+Arrays.toString(e.getStackTrace())+" Exception name: "+e.getMessage());
			e.printStackTrace();
			RecordLog.writeLogFile("Fedcorp:" + e);
			return custDetailRequest;
		}
	}
}