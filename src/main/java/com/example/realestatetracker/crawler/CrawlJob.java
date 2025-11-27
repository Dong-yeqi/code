package com.example.realestatetracker.crawler;

import com.example.realestatetracker.service.CrawlTaskService;
import jakarta.annotation.Resource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class CrawlJob {

    @Resource
    private CrawlTaskService crawlTaskService;

    /**
     * 每 1 分钟执行一次：由系统自动根据 cron_expr 筛选任务
     *
     * 说明：
     *   - CrawlTaskService.runByCron() 内部会：
     *       1. 读取数据库 cron_expr
     *       2. 判断当前时间是否满足 cron
     *       3. 调用 CrawlExecutor 执行
     *       4. 写 CrawlLog
     */
    @Scheduled(cron = "0 */1 * * * ?")
    public void scanCronTasks() {
        System.out.println("[CrawlJob] 定时扫描 cron 任务……");
        crawlTaskService.runByCron();
    }
}
