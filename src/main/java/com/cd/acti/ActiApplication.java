package com.cd.acti;

import org.activiti.spring.boot.SecurityAutoConfiguration;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@MapperScan(basePackages = "com.cd.acti.dao")
@EnableTransactionManagement        //开启springboot事务支持
@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
public class ActiApplication {

    public static void main(String[] args) {
        SpringApplication.run(ActiApplication.class, args);
    }

}
