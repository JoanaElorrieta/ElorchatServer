package com.reto.elorchat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ElorchatApplication {

	public static void main(String[] args) {
		SpringApplication application = new SpringApplication(ElorchatApplication.class);
		// incluir ssl para https
		application.setAdditionalProfiles("ssl");
		application.run(args);
	}

}
