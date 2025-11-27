package com.example.realestatetracker.scheduler;

import com.example.realestatetracker.crawler.service.CrawlExecutor;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 定时调度爬虫任务
 */
@Slf4j
@Component
public class CrawlScheduler {

    @Resource
    private CrawlExecutor crawlExecutor;

    /**
     * 示例：每小时执行一次
     */
    @Scheduled(cron = "0 0 * * * ?")
    public void run() {
        log.info("定时任务启动：执行所有启用的爬虫任务");
        crawlExecutor.executeAllEnabledTasks();
    }
}
