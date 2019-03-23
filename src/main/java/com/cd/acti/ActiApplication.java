package com.cd.acti;

import org.mybatis.spring.annotation.MapperScan;
import org.activiti.spring.boot.SecurityAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
@MapperScan(basePackages = {"com.cd.acti.dao"})
public class ActiApplication {

    public static void main(String[] args) {
        SpringApplication.run(ActiApplication.class, args);
    }

}
