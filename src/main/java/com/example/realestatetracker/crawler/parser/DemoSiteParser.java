package com.example.realestatetracker.crawler.parser;

import com.example.realestatetracker.crawler.model.HouseRawData;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Document;

import java.util.Collections;
import java.util.List;

@Slf4j
public class DemoSiteParser implements SiteParser {

    private static final String SITE = "demo_site";

    @Override
    public List<String> parseListPageForDetailUrls(Document listDoc) {
        // 这里先返回空列表，后续你根据实际网站结构来实现
        log.info("DemoSiteParser.parseListPageForDetailUrls - 尚未实现");
        return Collections.emptyList();
    }

    @Override
    public HouseRawData parseDetailPage(Document detailDoc, String detailUrl) {
        // 这里给出一个最简单的示例
        HouseRawData raw = new HouseRawData();
        raw.setSourceSite(SITE);
        raw.setSourceId(detailUrl);
        raw.setTitle("Demo Title");
        raw.setTotalPriceStr("100");
        raw.setUnitPriceStr("20000");
        raw.setAreaStr("80");
        raw.setCity("北京");
        raw.setRegion("示例区域");
        raw.setCommunityName("示例小区");
        raw.setPublishTimeStr("2025-01-01");
        return raw;
    }
}
