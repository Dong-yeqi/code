package com.example.realestatetracker.mapper;

import com.example.realestatetracker.entity.CrawlLog;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CrawlLogMapper {

    int insert(CrawlLog log);

    int update(CrawlLog log);

    CrawlLog selectById(Long id);

    List<CrawlLog> selectAll();
}
