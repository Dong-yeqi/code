package com.example.realestatetracker.controller.admin;

import com.example.realestatetracker.common.api.CommonResult;
import com.example.realestatetracker.entity.HouseInfo;
import com.example.realestatetracker.service.HouseService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/houses")
public class HouseController {

    @Resource
    private HouseService houseService;

    /**
     * 简单示例：保存或更新房源
     */
    @PostMapping
    public CommonResult<Void> save(@RequestBody HouseInfo houseInfo) {
        houseService.saveOrUpdate(houseInfo);
        return CommonResult.success(null);
    }
}
