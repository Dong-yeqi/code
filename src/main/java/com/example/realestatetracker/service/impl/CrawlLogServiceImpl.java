package com.example.realestatetracker.service.impl;

import com.example.realestatetracker.entity.CrawlLog;
import com.example.realestatetracker.mapper.CrawlLogMapper;
import com.example.realestatetracker.service.CrawlLogService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CrawlLogServiceImpl implements CrawlLogService {

    @Resource
    private CrawlLogMapper crawlLogMapper;

    @Override
    public List<CrawlLog> listAll() {
        return crawlLogMapper.selectAll();
    }

    /**
     * save = insert or update
     * 根据 log.id 是否为 null 自动判断
     */
    @Override
    public void save(CrawlLog log) {
        if (log.getId() == null) {
            crawlLogMapper.insert(log);
        } else {
            crawlLogMapper.update(log);
        }
    }
}
