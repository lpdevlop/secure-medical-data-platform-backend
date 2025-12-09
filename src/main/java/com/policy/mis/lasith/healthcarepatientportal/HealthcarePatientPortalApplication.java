package com.policy.mis.lasith.healthcarepatientportal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class HealthcarePatientPortalApplication {

    public static void main(String[] args) {
        SpringApplication.run(HealthcarePatientPortalApplication.class, args);
    }

}
