package com.example.realestatetracker.entity;

import java.time.LocalDateTime;

/**
 * 爬虫任务配置实体
 * 对应表：t_crawl_task
 */
public class CrawlTask {

    private Long id;

    /**
     * 任务名称
     */
    private String taskName;

    /**
     * 目标站点，如：beike
     */
    private String targetSite;

    /**
     * 城市中文名，如：北京
     */
    private String city;

    /**
     * 区域中文名，如：朝阳区（可选）
     */
    private String region;

    /**
     * 列表页 URL 模板（预留字段，可不必使用）
     */
    private String listUrlTemplate;

    /**
     * 定时表达式（如使用 Spring @Scheduled + cron）
     */
    private String cronExpr;

    /**
     * 是否启用：1 启用，0 禁用
     */
    private Integer enabled;

    /**
     * 最大爬取页数，防止一次抓太多
     */
    private Integer maxPage;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    // =================== getter / setter ===================

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getTargetSite() {
        return targetSite;
    }

    public void setTargetSite(String targetSite) {
        this.targetSite = targetSite;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getListUrlTemplate() {
        return listUrlTemplate;
    }

    public void setListUrlTemplate(String listUrlTemplate) {
        this.listUrlTemplate = listUrlTemplate;
    }

    public String getCronExpr() {
        return cronExpr;
    }

    public void setCronExpr(String cronExpr) {
        this.cronExpr = cronExpr;
    }

    public Integer getEnabled() {
        return enabled;
    }

    public void setEnabled(Integer enabled) {
        this.enabled = enabled;
    }

    public Integer getMaxPage() {
        return maxPage;
    }

    public void setMaxPage(Integer maxPage) {
        this.maxPage = maxPage;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }
}
