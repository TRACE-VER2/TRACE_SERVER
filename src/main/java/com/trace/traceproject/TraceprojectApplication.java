package com.trace.traceproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class TraceprojectApplication {

    public static void main(String[] args) {

        SpringApplication.run(TraceprojectApplication.class, args);

    }
}
