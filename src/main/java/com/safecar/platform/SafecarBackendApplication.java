package com.safecar.platform;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class SafecarBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(SafecarBackendApplication.class, args);
	}
}
