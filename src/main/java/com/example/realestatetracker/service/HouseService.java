package com.example.realestatetracker.service;

import com.example.realestatetracker.crawler.model.HouseRawData;
import com.example.realestatetracker.entity.HouseInfo;

public interface HouseService {

    void saveFromRaw(HouseRawData raw);

    void saveOrUpdate(HouseInfo houseInfo);
}
