package com.example.realestatetracker.service;

import com.example.realestatetracker.entity.CrawlLog;

import java.util.List;

public interface CrawlLogService {

    void save(CrawlLog log);

    List<CrawlLog> listAll();
}
