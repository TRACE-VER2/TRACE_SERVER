package com.trace.traceproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

//@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
@SpringBootApplication
public class TraceprojectApplication {

    public static void main(String[] args) {

        SpringApplication.run(TraceprojectApplication.class, args);

    }

}
