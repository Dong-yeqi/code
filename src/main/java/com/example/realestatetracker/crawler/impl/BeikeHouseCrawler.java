package com.example.realestatetracker.crawler.impl;

import com.example.realestatetracker.config.CityCodeConfig;
import com.example.realestatetracker.crawler.HouseCrawler;
import com.example.realestatetracker.entity.HouseInfo;
import com.example.realestatetracker.mapper.HouseInfoMapper;
import com.microsoft.playwright.*;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Component
public class BeikeHouseCrawler implements HouseCrawler {

    @Resource
    private HouseInfoMapper houseInfoMapper;

    @Resource
    private CityCodeConfig cityCodeConfig;

    @Override
    public boolean support(String site) {
        return "beike".equalsIgnoreCase(site);
    }

    @Override
    public int crawlCity(String city, String region, int maxPage) {
        int total = 0;

        String cityCode = cityCodeConfig.getBeikeCode(city);
        if (cityCode == null) {
            log.error("不支持城市：{}", city);
            return 0;
        }

        try (Playwright playwright = Playwright.create()) {

            Browser browser = playwright.chromium().launch(
                    new BrowserType.LaunchOptions()
                            .setHeadless(false)
                            .setArgs(java.util.List.of(
                                    "--disable-blink-features=AutomationControlled",
                                    "--disable-features=IsolateOrigins,site-per-process",
                                    "--disable-web-security",
                                    "--allow-running-insecure-content",
                                    "--ignore-certificate-errors",
                                    "--disable-infobars",
                                    "--no-sandbox",
                                    "--disable-gpu",
                                    "--window-size=1920,1080"
                            ))
            );

            BrowserContext context = browser.newContext(
                    new Browser.NewContextOptions()
                            .setViewportSize(1920, 1080)
                            .setUserAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) " +
                                    "AppleWebKit/537.36 (KHTML, like Gecko) " +
                                    "Chrome/123.0.0.0 Safari/537.36")
            );

            // 反爬处理
            context.addInitScript("""
                    Object.defineProperty(navigator, 'webdriver', { get: () => undefined });
                    Object.defineProperty(navigator, 'plugins', { get: () => [1,2,3] });
                    Object.defineProperty(navigator, 'languages', { get: () => ['zh-CN', 'zh'] });
                    window.navigator.chrome = { runtime: {} };
                    """);

            Page page = context.newPage();

            for (int p = 1; p <= maxPage; p++) {

                String url = "https://" + cityCode + ".ke.com/ershoufang/pg" + p + "/";

                log.info("打开页面：{}", url);

                page.navigate(url, new Page.NavigateOptions().setTimeout(30000));
                page.waitForLoadState();

                try {
                    page.waitForSelector("li.clear", new Page.WaitForSelectorOptions().setTimeout(20000));
                } catch (Exception e) {
                    log.warn("⚠ 页面未加载房源列表：{}", url);
                    continue;
                }

                List<ElementHandle> items = page.querySelectorAll("li.clear");

                if (items.isEmpty()) {
                    log.warn("⚠ 未找到房源数据：{}", url);
                    continue;
                }

                int pageCount = 0;

                for (ElementHandle item : items) {
                    try {

                        HouseInfo info = new HouseInfo();
                        info.setSourceSite("beike");
                        info.setCity(city);
                        info.setRegion(region);
                        info.setCrawlTime(LocalDateTime.now());
                        info.setIsValid(1);

                        // 标题 + 链接
                        String title = text(item, "div.title a");
                        String detailUrl = attr(item, "div.title a", "href");
                        String sourceId = extractSourceId(detailUrl);

                        info.setTitle(title);
                        info.setSourceId(sourceId);

                        // --- 基本信息 ---
                        String houseInfoText = text(item, "div.houseInfo");
                        if (houseInfoText != null) {
                            String[] arr = houseInfoText.split("/");
                            if (arr.length > 0) info.setLayout(arr[0].trim());
                            if (arr.length > 1) info.setArea(num(arr[1]));
                            if (arr.length > 2) info.setOrientation(arr[2].trim());
                            if (arr.length > 3) info.setDecoration(arr[3].trim());
                        }

                        // --- 区域 & 小区 ---
                        String position = text(item, "div.positionInfo");
                        if (position != null) {
                            // 城市区名称，例如：成华区
                            String[] posArr = position.split("\\s+");
                            if (posArr.length > 0) info.setRegion(posArr[0]);
                            if (posArr.length > 1) info.setCommunityName(posArr[1]);
                        }

                        // --- 总价 ---
                        info.setTotalPrice(num(text(item, "div.totalPrice span")));

                        // --- 单价 ---
                        info.setUnitPrice(num(text(item, "div.unitPrice span")));

                        houseInfoMapper.insertOrUpdateBySource(info);
                        total++;
                        pageCount++;

                    } catch (Exception e) {
                        log.warn("解析房源数据失败，跳过该条记录: {}", e.getMessage());
                    }
                }

                log.info("成功解析 {} 条房源（第 {} 页）", pageCount, p);
            }

        } catch (Exception e) {
            log.error("贝壳爬取失败", e);
        }

        return total;
    }

    private String text(ElementHandle h, String selector) {
        ElementHandle e = h.querySelector(selector);
        if (e == null) return null;
        try {
            return e.innerText().trim();
        } catch (Exception ex) {
            return null;
        }
    }

    private String attr(ElementHandle h, String selector, String name) {
        ElementHandle e = h.querySelector(selector);
        if (e == null) return null;
        try {
            return e.getAttribute(name);
        } catch (Exception ex) {
            return null;
        }
    }

    private BigDecimal num(String s) {
        if (s == null) return null;
        s = s.replaceAll("[^0-9.]", "");
        if (s.isEmpty()) return null;
        try {
            return new BigDecimal(s);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 从链接中提取房源 ID
     * https://cd.ke.com/ershoufang/1234567890.html
     */
    private String extractSourceId(String url) {
        if (url == null) return null;
        try {
            int lastSlash = url.lastIndexOf('/');
            int dot = url.lastIndexOf(".html");
            if (lastSlash < 0 || dot < 0) return null;
            return url.substring(lastSlash + 1, dot).trim();
        } catch (Exception e) {
            return null;
        }
    }

}
