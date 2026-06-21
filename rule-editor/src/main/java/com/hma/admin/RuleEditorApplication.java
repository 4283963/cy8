package com.hma.admin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan(basePackages = "com.hma.admin.entity")
@EnableJpaRepositories(basePackages = "com.hma.admin.repository")
public class RuleEditorApplication {

    public static void main(String[] args) {
        SpringApplication.run(RuleEditorApplication.class, args);
    }
}
