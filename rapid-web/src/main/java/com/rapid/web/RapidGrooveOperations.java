package com.rapid.web;

import com.rapid.dao.RoleRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@SpringBootApplication
@Configuration
@EnableJpaRepositories("com.rapid.dao")
@EntityScan("com.rapid.core")
@ComponentScan(basePackages  = {"com.rapid.service","com.rapid.security","com.rapid.security.service"})



 public class RapidGrooveOperations extends SpringBootServletInitializer {
    public static void main(String[] args) {
        SpringApplication.run(RapidGrooveOperations.class, args);
        System.out.println("Hello World!");
    }


}
