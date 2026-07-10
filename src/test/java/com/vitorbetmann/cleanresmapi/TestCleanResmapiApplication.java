package com.vitorbetmann.cleanresmapi;

import org.springframework.boot.SpringApplication;

public class TestCleanResmapiApplication {

    static void main(String[] args) {
        SpringApplication.from(CleanResmapiApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}
