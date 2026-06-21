package com.hma.distribution;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = {"com.hma.distribution", "com.hma.admin"})
@EntityScan(basePackages = "com.hma.admin.entity")
@EnableJpaRepositories(basePackages = "com.hma.admin.repository")
public class DistributionServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(DistributionServiceApplication.class, args);
    }
}
