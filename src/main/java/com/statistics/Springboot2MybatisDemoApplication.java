package com.statistics;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@MapperScan("com.statistics.dao")
@EnableScheduling
public class Springboot2MybatisDemoApplication  extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(Springboot2MybatisDemoApplication.class, args);
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(Springboot2MybatisDemoApplication.class);
	}
}
