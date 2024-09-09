package com.example.demo;

import java.util.Arrays;


import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;



//@SpringBootApplication(exclude = {org.springframework.boot.logging.logback.LogbackLoggingSystem.class})
@SpringBootApplication
@ComponentScan(basePackages = {"com.example.demo.repo","com.example.demo.controller","com.example.demo.service","com.example.demo.mapper",
		"com.example.demo.aspects","com.example.demo.exception"})
@EnableJpaRepositories("com.example.demo.repo")
@EnableAspectJAutoProxy

public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}
	
}
