package com.example.realestatetracker.crawler.model;

import lombok.Data;

/**
 * 爬虫抓到的原始房源数据（全部是字符串）
 */
@Data
public class HouseRawData {

    private String sourceId;
    private String sourceSite;

    private String title;

    private String totalPriceStr;
    private String unitPriceStr;
    private String areaStr;

    private String layout;
    private String floorInfo;
    private String orientation;
    private String decoration;

    private String communityName;
    private String city;
    private String region;
    private String address;

    private String publishTimeStr;
}
