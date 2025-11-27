package com.example.realestatetracker.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 爬虫执行日志
 */
@Data
public class CrawlLog {

    private Long id;
    private Long taskId;
    private String taskName;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Integer successCount;
    private Integer failCount;
    private String status;     // SUCCESS / FAILED / PARTIAL
    private String message;
}
