package com.example.realestatetracker.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 房源信息
 */
@Data
public class HouseInfo {

    private Long id;

    private String sourceId;
    private String sourceSite;

    private String title;

    private BigDecimal totalPrice; // 单位：万
    private BigDecimal unitPrice;  // 单位：元/㎡
    private BigDecimal area;       // 单位：㎡

    private String layout;
    private String floorInfo;
    private String orientation;
    private String decoration;

    private String communityName;
    private String city;
    private String region;
    private String address;

    private LocalDateTime publishTime;
    private LocalDateTime crawlTime;

    private Integer isValid;       // 1 有效 0 无效

    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
