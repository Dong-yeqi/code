package com.example.realestatetracker.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * 城市代码配置
 * 用于将中文城市名映射到各网站的城市代码
 * 
 * 配置示例（application.yml）：
 * crawler:
 *   city-code:
 *     beike:
 *       北京: bj
 *       上海: sh
 */
@Data
@Configuration
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "crawler.city-code")
public class CityCodeConfig {

    /**
     * 贝壳网城市代码映射
     */
    private Map<String, String> beike = new HashMap<>();

    /**
     * 安居客城市代码映射
     */
    private Map<String, String> anjuke = new HashMap<>();

    /**
     * 吉屋网城市代码映射
     */
    private Map<String, String> jiwu = new HashMap<>();

    /**
     * 获取贝壳网城市代码
     */
    public String getBeikeCode(String city) {
        return beike.get(city);
    }

    /**
     * 获取安居客城市代码
     */
    public String getAnjukeCode(String city) {
        return anjuke.get(city);
    }

    /**
     * 获取吉屋网城市代码
     */
    public String getJiwuCode(String city) {
        return jiwu.get(city);
    }
}

