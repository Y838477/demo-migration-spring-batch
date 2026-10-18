package com.example.batch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.example.batch.repository")
@EntityScan(basePackages = "com.example.batch.model")
public class SpringBatchAuthorsApplication {

    public static void main(String[] args) {
        System.setProperty("spring.batch.job.enabled", "true");
        SpringApplication.run(SpringBatchAuthorsApplication.class, args);
    }
}
