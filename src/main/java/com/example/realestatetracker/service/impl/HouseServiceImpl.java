package com.example.realestatetracker.service.impl;

import com.example.realestatetracker.crawler.model.HouseRawData;
import com.example.realestatetracker.entity.HouseInfo;
import com.example.realestatetracker.mapper.HouseInfoMapper;
import com.example.realestatetracker.service.HouseService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@Service
public class HouseServiceImpl implements HouseService {

    @Resource
    private HouseInfoMapper houseInfoMapper;

    @Override
    public void saveFromRaw(HouseRawData raw) {
        if (raw == null) return;

        HouseInfo exist = houseInfoMapper.selectBySource(raw.getSourceSite(), raw.getSourceId());
        HouseInfo house = exist != null ? exist : new HouseInfo();
        house.setSourceSite(raw.getSourceSite());
        house.setSourceId(raw.getSourceId());
        house.setTitle(raw.getTitle());
        house.setCommunityName(raw.getCommunityName());
        house.setCity(raw.getCity());
        house.setRegion(raw.getRegion());
        house.setAddress(raw.getAddress());
        house.setLayout(raw.getLayout());
        house.setFloorInfo(raw.getFloorInfo());
        house.setOrientation(raw.getOrientation());
        house.setDecoration(raw.getDecoration());
        house.setCrawlTime(LocalDateTime.now());
        house.setIsValid(1);

        house.setTotalPrice(parseNumber(raw.getTotalPriceStr()));
        house.setUnitPrice(parseNumber(raw.getUnitPriceStr()));
        house.setArea(parseNumber(raw.getAreaStr()));

        house.setPublishTime(parsePublishTime(raw.getPublishTimeStr()));

        if (exist == null) {
            houseInfoMapper.insert(house);
            log.info("新增房源：{} - {}", house.getCity(), house.getTitle());
        } else {
            houseInfoMapper.update(house);
            log.info("更新房源：{} - {}", house.getCity(), house.getTitle());
        }
    }

    @Override
    public void saveOrUpdate(HouseInfo houseInfo) {
        if (houseInfo.getId() == null) {
            houseInfoMapper.insert(houseInfo);
        } else {
            houseInfoMapper.update(houseInfo);
        }
    }

    private BigDecimal parseNumber(String s) {
        if (s == null) return BigDecimal.ZERO;
        String t = s.replaceAll("[^0-9.]", "");
        if (t.isEmpty()) return BigDecimal.ZERO;
        return new BigDecimal(t);
    }

    private LocalDateTime parsePublishTime(String s) {
        if (s == null) return null;
        s = s.trim();
        try {
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            return LocalDateTime.from(dtf.parse(s)).withHour(0).withMinute(0).withSecond(0);
        } catch (Exception e) {
            log.warn("解析发布时间失败: {}", s);
            return null;
        }
    }
}
