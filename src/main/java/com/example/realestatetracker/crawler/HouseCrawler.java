package com.example.realestatetracker.crawler;

public interface HouseCrawler {

    /**
     * 是否支持指定站点，如 beike
     */
    boolean support(String site);

    /**
     * 执行指定城市区域的爬虫
     */
    int crawlCity(String city, String region, int maxPage);
}
