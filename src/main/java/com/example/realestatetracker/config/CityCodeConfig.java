package com.example.realestatetracker.config;

import jakarta.annotation.PostConstruct;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
     * 贝壳网城市代码列表
     */
    private List<CityCodeMapping> beike = new ArrayList<>();

    /**
     * 安居客城市代码列表
     */
    private List<CityCodeMapping> anjuke = new ArrayList<>();

    /**
     * 吉屋网城市代码列表
     */
    private List<CityCodeMapping> jiwu = new ArrayList<>();

    private Map<String, String> beikeLookup = new HashMap<>();
    private Map<String, String> anjukeLookup = new HashMap<>();
    private Map<String, String> jiwuLookup = new HashMap<>();

    @PostConstruct
    public void initLookups() {
        beikeLookup = toMap(beike);
        anjukeLookup = toMap(anjuke);
        jiwuLookup = toMap(jiwu);
    }

    private Map<String, String> toMap(List<CityCodeMapping> mappings) {
        if (mappings == null) {
            return new HashMap<>();
        }
        return mappings.stream()
                .filter(mapping -> mapping.getCity() != null && mapping.getCode() != null)
                .collect(Collectors.toMap(CityCodeMapping::getCity, CityCodeMapping::getCode, (a, b) -> b));
    }

    /**
     * 获取贝壳网城市代码
     */
    public String getBeikeCode(String city) {
        return beikeLookup.get(city);
    }

    /**
     * 获取安居客城市代码
     */
    public String getAnjukeCode(String city) {
        return anjukeLookup.get(city);
    }

    /**
     * 获取吉屋网城市代码
     */
    public String getJiwuCode(String city) {
        return jiwuLookup.get(city);
    }

    @Data
    public static class CityCodeMapping {
        /**
         * 城市中文名
         */
        private String city;

        /**
         * 站点城市代码
         */
        private String code;
    }
}

