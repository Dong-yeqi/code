package com.example.realestatetracker.service;

import com.example.realestatetracker.entity.CrawlTask;

import java.util.List;

public interface CrawlTaskService {

    List<CrawlTask> list();

    CrawlTask get(Long id);

    void save(CrawlTask task);

    void delete(Long id);

    /** 直接按 ID 执行（后台手动） */
    void run(Long id);

    /** 返回启用的任务（enabled=1） */
    List<CrawlTask> listEnabledTasks();

    List<CrawlTask> listAll();

    /** 手动执行任务（controller 用） */
    int runTaskNow(Long id);

    /** ★ 自动执行 cron 任务（定时调度用） */
    void runByCron();
}
