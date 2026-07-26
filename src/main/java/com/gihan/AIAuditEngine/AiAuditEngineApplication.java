package com.gihan.AIAuditEngine;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class AiAuditEngineApplication {

	public static void main(String[] args) {
		SpringApplication.run(AiAuditEngineApplication.class, args);
	}

}
