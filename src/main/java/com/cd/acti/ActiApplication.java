package com.cd.acti;

import org.activiti.spring.boot.SecurityAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class ActiApplication {

    public static void main(String[] args) {
        SpringApplication.run(ActiApplication.class, args);
    }

}
