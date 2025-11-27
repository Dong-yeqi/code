package com.example.realestatetracker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 房产数据跟踪系统 启动类
 */
@SpringBootApplication
@EnableScheduling // 开启 Spring 定时任务
public class RealEstateTrackerApplication {

	public static void main(String[] args) {
		SpringApplication.run(RealEstateTrackerApplication.class, args);
	}
}
