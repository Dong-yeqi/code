package com.example.realestatetracker.controller.admin;

import com.example.realestatetracker.common.api.CommonResult;
import com.example.realestatetracker.entity.CrawlLog;
import com.example.realestatetracker.service.CrawlLogService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin/logs")
public class CrawlLogController {

    @Resource
    private CrawlLogService crawlLogService;

    @GetMapping
    public CommonResult<List<CrawlLog>> listAll() {
        return CommonResult.success(crawlLogService.listAll());
    }
}
