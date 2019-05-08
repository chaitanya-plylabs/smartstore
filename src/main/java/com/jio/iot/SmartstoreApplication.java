package com.jio.iot;

import org.axonframework.springboot.autoconfig.JpaAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EnableAutoConfiguration(exclude={JpaAutoConfiguration.class})
@EntityScan(basePackages = {"com.jio.iot", "org.axonframework.eventhandling.tokenstore.jpa"})
public class SmartstoreApplication {
    public static void main(final String[] args) {
        SpringApplication.run(SmartstoreApplication.class, args);
    }
}
