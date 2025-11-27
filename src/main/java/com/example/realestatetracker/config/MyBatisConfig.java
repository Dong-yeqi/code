package com.example.realestatetracker.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan("com.example.realestatetracker.mapper")
public class MyBatisConfig {
}
