package com.federal.fedmobilesmecore;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.core.task.TaskExecutor;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import com.federal.fedmobilesmecore.repository.UserRepository;

/**
 * @author Debasish_Splenta
 *
 */
@EnableAsync
@EnableDiscoveryClient
// @EnableDiscoveryClient
@EnableCircuitBreaker
@SpringBootApplication
//@EnableScheduling
@RestController
public class FedmobileSmeCoreApplication {
	@Value("${spring.application.name}")
	private String projectName;
	@Value("${server.port}")
	private int portNo;
	@Autowired
	UserRepository userRepository;

	public static void main(String[] args) {
		SpringApplication.run(FedmobileSmeCoreApplication.class, args);
	}

	@RequestMapping(path = "/pr0deta1l3")
	public ResponseEntity<?> getProjectDetails() {
		Map<String, Object> createDetails = new HashMap<String, Object>();
		createDetails.put("projectName", projectName);
		createDetails.put("port", portNo);
		return ResponseEntity.ok(createDetails);
	}

	// Added CorsFilter
	@Bean
	public CorsFilter corsFilter() {
		final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		final CorsConfiguration config = new CorsConfiguration();
		config.setAllowCredentials(true);
		config.addAllowedOrigin("*");
		config.addAllowedHeader("*");
		config.addAllowedMethod("OPTIONS");
		config.addAllowedMethod("HEAD");
		config.addAllowedMethod("GET");
		config.addAllowedMethod("PUT");
		config.addAllowedMethod("POST");
		config.addAllowedMethod("DELETE");
		config.addAllowedMethod("PATCH");
		source.registerCorsConfiguration("/**", config);
		return new CorsFilter(source);
	}

	@Bean(name="taskexecutor")
	public TaskExecutor getPoolExcutor() {
		ThreadPoolTaskExecutor executor=new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(5);
        executor.setMaxPoolSize(50);
     //   executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.setThreadNamePrefix("Async-");
        executor.initialize();
        return executor;
		
	}
}
