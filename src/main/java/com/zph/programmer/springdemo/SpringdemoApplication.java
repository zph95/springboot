package com.zph.programmer.springdemo;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.zph.programmer.springdemo.config.StorageConfig;

@EnableCaching
@EnableScheduling
@SpringBootApplication
@EnableConfigurationProperties(StorageConfig.class)
@MapperScan("com.zph.programmer.springdemo.dao")
public class SpringdemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringdemoApplication.class, args);
	}

}
