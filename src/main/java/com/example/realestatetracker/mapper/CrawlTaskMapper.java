package com.example.realestatetracker.mapper;

import com.example.realestatetracker.entity.CrawlTask;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CrawlTaskMapper {

    int insert(CrawlTask task);

    int update(CrawlTask task);

    int delete(Long id);

    CrawlTask selectById(Long id);

    List<CrawlTask> selectAll();

    List<CrawlTask> selectEnabled(); // ⭐ 必须有
}
