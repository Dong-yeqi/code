package com.example.realestatetracker.controller;

import com.example.realestatetracker.common.api.CommonResult;
import com.example.realestatetracker.service.StatisticService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/public/stats")
public class StatisticController {

    @Resource
    private StatisticService statisticService;

    @GetMapping("/overview")
    public CommonResult<Map<String, Object>> overview(
            @RequestParam("city") String city
    ) {
        return CommonResult.success(statisticService.getOverview(city));
    }

    @GetMapping("/priceTrend")
    public CommonResult<Map<String, Object>> priceTrend(
            @RequestParam("city") String city,
            @RequestParam(value = "region", required = false) String region,
            @RequestParam(value = "range", defaultValue = "3m") String range
    ) {
        return CommonResult.success(statisticService.getPriceTrend(city, region, range));
    }

    @GetMapping("/regionCompare")
    public CommonResult<Map<String, Object>> regionCompare(
            @RequestParam("city") String city,
            @RequestParam(value = "metric", defaultValue = "avgPrice") String metric
    ) {
        return CommonResult.success(statisticService.getRegionCompare(city, metric));
    }

    @GetMapping("/areaStructure")
    public CommonResult<Map<String, Object>> areaStructure(
            @RequestParam("city") String city
    ) {
        return CommonResult.success(statisticService.getAreaStructure(city));
    }

    @GetMapping("/layoutStructure")
    public CommonResult<Map<String, Object>> layoutStructure(
            @RequestParam("city") String city
    ) {
        return CommonResult.success(statisticService.getLayoutStructure(city));
    }
}
