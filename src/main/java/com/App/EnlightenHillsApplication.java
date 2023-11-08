package com.App;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories("com.App.Repository")
@EntityScan("com.App.Model")
@EnableJpaAuditing(auditorAwareRef = "audit")
public class EnlightenHillsApplication {

	public static void main(String[] args) {
		SpringApplication.run(EnlightenHillsApplication.class, args);
	}

}
