package com.example.realestatetracker.service;

import java.util.Map;

public interface StatisticService {

    Map<String, Object> getOverview(String city);

    Map<String, Object> getPriceTrend(String city, String region, String range);

    Map<String, Object> getRegionCompare(String city, String metric);

    Map<String, Object> getAreaStructure(String city);

    Map<String, Object> getLayoutStructure(String city);
}
