package com.example.realestatetracker.mapper;

import com.example.realestatetracker.entity.HouseInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Mapper
public interface HouseInfoMapper {

    HouseInfo selectBySource(@Param("sourceSite") String sourceSite,
                             @Param("sourceId") String sourceId);

    int insert(HouseInfo info);

    int update(HouseInfo info);

    /** ★★★ 关键：爬虫用这个 ★★★ */
    int insertOrUpdateBySource(HouseInfo info);

    Map<String, Object> selectOverview(@Param("city") String city);

    List<Map<String, Object>> selectPriceTrend(@Param("city") String city,
                                               @Param("region") String region,
                                               @Param("startTime") LocalDateTime startTime);

    List<Map<String, Object>> selectRegionCompare(@Param("city") String city,
                                                  @Param("metric") String metric);

    List<Map<String, Object>> selectAreaStructure(@Param("city") String city);

    List<Map<String, Object>> selectLayoutStructure(@Param("city") String city);
}
