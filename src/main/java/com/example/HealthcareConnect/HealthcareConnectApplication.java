package com.example.HealthcareConnect;

import com.example.HealthcareConnect.storage.StorageProperties;
import com.example.HealthcareConnect.storage.StorageService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;


@SpringBootApplication
//@EnableConfigurationProperties(StorageProperties.class)
public class HealthcareConnectApplication {

	public static void main(String[] args) {

		SpringApplication.run(HealthcareConnectApplication.class, args);
	}

	@Primary
	@Bean
	CommandLineRunner init(StorageService storageService) {
		return (args) -> {
			storageService.deleteAll();
			storageService.init();
		};
	}

}
