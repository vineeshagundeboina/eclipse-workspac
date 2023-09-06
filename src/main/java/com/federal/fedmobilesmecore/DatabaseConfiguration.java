package com.federal.fedmobilesmecore;

import javax.sql.DataSource;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.web.client.RestTemplate;

@Configuration
@Profile("prod")
public class DatabaseConfiguration {

	@Bean
	@Primary
	public DataSource dataSources() {
		return DataSourceBuilder.create().driverClassName(System.getenv("DB_driverName"))
			.url("jdbc:oracle:thin:@" + System.getenv("DB_HOSTNAME") + ":" + System.getenv("DB_PORT") + ":"
						+ System.getenv("DB_DATABASE"))
			.username(System.getenv("DB_USERNAME")).password(System.getenv("DB_PASSWORD")).build();

		
	}
	
//	@Primary
//    @Bean
//    @ConfigurationProperties(prefix="spring.datasource")
//   public DataSource dataSources() {
//   return  DataSourceBuilder.create().build();
//     }
	
	
	
	@Bean
	RestTemplate restTemplate() {
		return new RestTemplate();
	}
}
