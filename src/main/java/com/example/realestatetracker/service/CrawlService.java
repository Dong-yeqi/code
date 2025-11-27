package com.example.realestatetracker.service;

/**
 * 爬虫服务接口
 * 说明：对外暴露统一的爬虫调用入口，根据 site 自动选择实现
 */
public interface CrawlService {

    /**
     * 执行爬虫
     *
     * @param site    站点：beike / anjuke / jiwu
     * @param city    城市中文名，如：北京、上海、成都
     * @param region  可选区域
     * @param maxPage 最大页
     */
    int crawlCity(String site, String city, String region, int maxPage);
}
