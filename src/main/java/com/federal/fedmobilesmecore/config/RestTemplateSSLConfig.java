package com.federal.fedmobilesmecore.config;

import java.io.FileInputStream;
import java.io.InputStream;
import java.security.KeyStore;

import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class RestTemplateSSLConfig {

	@Value("${KeyStorepass}")
	private String keyStorePassword;

	@Value("${server.ssl.key-store-type}")
	private String keyStoreType;

	@Value("${keystorelocation}")
	private String keystorelocation;

	@Value("${trustlocation}")
	private String trustlocation;

	@Value("${trustpass}")
	private String trustpass;

	@Value("${spring.profiles.active}")
	private String activeProfile;


	public RestTemplate restTemplate() {
		HttpComponentsClientHttpRequestFactory requestFactory = null;
		try {
			InputStream inputStream = null;
			InputStream fileinput1 = null;

			System.out.println("keyStoreType" + keyStoreType);
			KeyStore keyStore = KeyStore.getInstance(keyStoreType);
			KeyStore trustStore = KeyStore.getInstance(keyStoreType);
			if (activeProfile.equals("dev") || activeProfile.equals("uat")) {
				/*inputStream = new ClassPathResource("PMJDY.JKS").getInputStream();
				fileinput1 = new ClassPathResource("PMJDY.JKS").getInputStream();
				keyStore.load(inputStream, keyStorePassword.toCharArray());
				trustStore.load(fileinput1, trustpass.toCharArray());*/

				String jksPath = System.getenv("jksPath");
				System.out.println(jksPath);
				inputStream = new FileInputStream(jksPath);
				fileinput1 = new FileInputStream(jksPath);
				keyStore.load(new FileInputStream(jksPath), keyStorePassword.toCharArray());
				trustStore.load(new FileInputStream(jksPath), trustpass.toCharArray());
			} else {
				
				String jksPath = System.getenv("jksPath");
				System.out.println(jksPath);
				inputStream = new FileInputStream(jksPath);
				fileinput1 = new FileInputStream(jksPath);
				keyStore.load(new FileInputStream(jksPath), keyStorePassword.toCharArray());
				trustStore.load(new FileInputStream(jksPath), trustpass.toCharArray());
				
				//inputStream = new ClassPathResource("PMJDY.JKS").getInputStream();
				//fileinput1 = new ClassPathResource("PMJDY.JKS").getInputStream();
				//keyStore.load(inputStream, keyStorePassword.toCharArray());
				//trustStore.load(inputStream, trustpass.toCharArray());
			}

			
			inputStream.close();
			fileinput1.close();

			SSLContextBuilder sslbuilder = new SSLContextBuilder();
			sslbuilder.setProtocol("TLSv1.2");
			sslbuilder.loadKeyMaterial(keyStore, keyStorePassword.toCharArray());
			sslbuilder.loadTrustMaterial(trustStore, new TrustSelfSignedStrategy());
			SSLConnectionSocketFactory connectionSocketFactory = new SSLConnectionSocketFactory(sslbuilder.build());
			CloseableHttpClient client = HttpClients.custom().setSSLSocketFactory(connectionSocketFactory).build();
			requestFactory = new HttpComponentsClientHttpRequestFactory();
			requestFactory.setHttpClient(client);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new RestTemplate(requestFactory);

	}

}
