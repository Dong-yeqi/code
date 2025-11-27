package com.example.realestatetracker.service.impl;

import com.example.realestatetracker.crawler.HouseCrawler;
import com.example.realestatetracker.service.CrawlService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CrawlServiceImpl implements CrawlService {

    /** 注入所有 HouseCrawler 实现 */
    @Resource
    private List<HouseCrawler> crawlers;

    @Override
    public int crawlCity(String site, String city, String region, int maxPage) {

        // 根据 site 找到对应实现
        HouseCrawler crawler = crawlers.stream()
                .filter(c -> c.support(site))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("未找到爬虫实现: " + site));

        return crawler.crawlCity(city, region, maxPage);
    }
}
