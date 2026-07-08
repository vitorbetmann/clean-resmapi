package com.vitorbetmann.clean_resmapi;

import org.springframework.boot.SpringApplication;

public class TestCleanResmapiApplication {

	public static void main(String[] args) {
		SpringApplication.from(CleanResmapiApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
