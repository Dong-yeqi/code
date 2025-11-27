package com.example.realestatetracker.crawler.service;

import com.example.realestatetracker.crawler.HouseCrawler;
import com.example.realestatetracker.entity.CrawlLog;
import com.example.realestatetracker.entity.CrawlTask;
import com.example.realestatetracker.mapper.CrawlLogMapper;
import com.example.realestatetracker.mapper.CrawlTaskMapper;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
public class CrawlExecutor {

    @Resource
    private CrawlTaskMapper taskMapper;

    @Resource
    private CrawlLogMapper logMapper;

    @Resource
    private List<HouseCrawler> crawlers;

    /**
     * 执行所有“启用”的任务
     */
    public void executeAllEnabledTasks() {
        List<CrawlTask> tasks = taskMapper.selectEnabled();
        for (CrawlTask t : tasks) {
            executeTask(t);
        }
    }

    /**
     * 执行单个任务，返回成功写入条数
     */
    public int executeTask(CrawlTask task) {
        CrawlLog logEntity = new CrawlLog();
        logEntity.setTaskId(task.getId());
        logEntity.setTaskName(task.getTaskName());
        logEntity.setStartTime(LocalDateTime.now());
        // 先插入一条日志，后续再更新
        logMapper.insert(logEntity);

        int success = 0;
        int fail = 0;

        try {
            HouseCrawler crawler = crawlers.stream()
                    .filter(c -> c.support(task.getTargetSite()))
                    .findFirst()
                    .orElse(null);

            if (crawler == null) {
                log.error("未找到爬虫实现: {}", task.getTargetSite());
                logEntity.setStatus("FAILED");
                logEntity.setMessage("未找到爬虫实现: " + task.getTargetSite());
            } else {
                success = crawler.crawlCity(task.getCity(), task.getRegion(), task.getMaxPage());
                logEntity.setStatus("SUCCESS");
                logEntity.setMessage("成功写入 " + success + " 条");
            }

        } catch (Exception e) {
            fail = 1;
            log.error("执行任务异常，taskId={}", task.getId(), e);
            logEntity.setStatus("FAILED");
            logEntity.setMessage(e.getMessage());
        }

        logEntity.setEndTime(LocalDateTime.now());
        logEntity.setSuccessCount(success);
        logEntity.setFailCount(fail);

        logMapper.update(logEntity);

        return success;
    }
}
