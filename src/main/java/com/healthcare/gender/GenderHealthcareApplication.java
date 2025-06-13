package com.healthcare.gender;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan("com.healthcare.gender.model.entity")
@EnableJpaRepositories("com.healthcare.gender.repository")
public class GenderHealthcareApplication {

    public static void main(String[] args) {
        SpringApplication.run(GenderHealthcareApplication.class, args);
    }
}