package com.example.realestatetracker.service.impl;

import com.example.realestatetracker.mapper.HouseInfoMapper;
import com.example.realestatetracker.service.StatisticService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

/**
 * 统计服务实现类
 * 说明：全部使用 JDK8 可用的语法（不使用增强 switch、record 等）
 */
@Service
public class StatisticServiceImpl implements StatisticService {

    @Resource
    private HouseInfoMapper houseInfoMapper;

    /**
     * 总览信息：均价、房源数量、最近更新时间
     */
    @Override
    public Map<String, Object> getOverview(String city) {
        Map<String, Object> db = houseInfoMapper.selectOverview(city);
        Map<String, Object> result = new HashMap<String, Object>();
        if (db != null) {
            result.put("avgPrice", db.get("avg_price"));
            result.put("houseCount", db.get("house_count"));
            result.put("lastUpdateTime", db.get("last_update_time"));
        } else {
            result.put("avgPrice", 0);
            result.put("houseCount", 0);
            result.put("lastUpdateTime", null);
        }
        return result;
    }

    /**
     * 价格趋势（按天）
     */
    @Override
    public Map<String, Object> getPriceTrend(String city, String region, String range) {
        LocalDate today = LocalDate.now();
        LocalDate startDate;

        // JDK8 普通 switch 写法
        switch (range) {
            case "1m":
                startDate = today.minusMonths(1);
                break;
            case "1y":
                startDate = today.minusYears(1);
                break;
            case "3m":
            default:
                startDate = today.minusMonths(3);
                break;
        }

        LocalDateTime startTime = startDate.atStartOfDay();

        List<Map<String, Object>> rows = houseInfoMapper.selectPriceTrend(city, region, startTime);
        List<String> dates = new ArrayList<String>();
        List<Object> values = new ArrayList<Object>();
        for (Map<String, Object> row : rows) {
            dates.add(String.valueOf(row.get("day")));
            values.add(row.get("avg_price") != null ? row.get("avg_price") : 0);
        }
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("dates", dates);
        result.put("avgPrices", values);
        return result;
    }

    /**
     * 区域对比（均价 / 数量）
     */
    @Override
    public Map<String, Object> getRegionCompare(String city, String metric) {
        List<Map<String, Object>> rows = houseInfoMapper.selectRegionCompare(city, metric);
        List<String> regions = new ArrayList<String>();
        List<Object> values = new ArrayList<Object>();
        for (Map<String, Object> row : rows) {
            regions.add(String.valueOf(row.get("region")));
            values.add(row.get("value"));
        }
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("regions", regions);
        result.put("values", values);
        return result;
    }

    /**
     * 面积结构分布
     */
    @Override
    public Map<String, Object> getAreaStructure(String city) {
        List<Map<String, Object>> rows = houseInfoMapper.selectAreaStructure(city);
        List<String> labels = new ArrayList<String>();
        List<Object> counts = new ArrayList<Object>();
        for (Map<String, Object> row : rows) {
            labels.add(String.valueOf(row.get("range_label")));
            counts.add(row.get("cnt"));
        }
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("ranges", labels);
        result.put("counts", counts);
        return result;
    }

    /**
     * 户型结构分布
     */
    @Override
    public Map<String, Object> getLayoutStructure(String city) {
        List<Map<String, Object>> rows = houseInfoMapper.selectLayoutStructure(city);
        List<String> labels = new ArrayList<String>();
        List<Object> counts = new ArrayList<Object>();
        for (Map<String, Object> row : rows) {
            labels.add(String.valueOf(row.get("layout_label")));
            counts.add(row.get("cnt"));
        }
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("labels", labels);
        result.put("values", counts);
        return result;
    }

}
