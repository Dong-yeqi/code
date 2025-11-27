package com.example.realestatetracker.crawler.impl;

import com.example.realestatetracker.crawler.HouseCrawler;
import com.example.realestatetracker.entity.HouseInfo;
import com.example.realestatetracker.mapper.HouseInfoMapper;
import com.microsoft.playwright.*;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.nio.file.Paths;
import java.time.LocalDateTime;

@Slf4j
@Component
public class JiwuHouseCrawler implements HouseCrawler {

    @Resource
    private HouseInfoMapper houseInfoMapper;

    @Override
    public boolean support(String site) {
        return "jiwu".equalsIgnoreCase(site);
    }

    @Override
    public int crawlCity(String city, String region, int maxPage) {
        int count = 0;

        String cityCode = cityToCode(city);
        if (cityCode == null) {
            log.error("[JiwuCrawler] 不支持的城市: {}", city);
            return 0;
        }

        String base = "https://" + cityCode + ".jiwu.com/loupan/esf/list-page";

        try (Playwright playwright = Playwright.create()) {

            Browser browser = playwright.chromium().launch(
                    new BrowserType.LaunchOptions()
                            .setHeadless(false) // ❗必须人类浏览模式
            );

            // 使用真实浏览器 cookie
            BrowserContext context = browser.newContext(
                    new Browser.NewContextOptions()
                            .setViewportSize(1200, 800)
                            .setStorageStatePath(Paths.get("chrome_state.json"))
            );

            Page page = context.newPage();

            for (int i = 1; i <= maxPage; i++) {

                String url = base + i + ".html";
                log.info("[JiwuCrawler] 打开页面: {}", url);

                page.navigate(url);

                // 等待 DOM
                page.waitForLoadState();

                // 再人为等待，模拟真实访问（吉屋强反爬）
                page.waitForTimeout(1800);

                // 获取渲染后的 HTML
                String html = page.content();

                log.warn("[DEBUG HTML] {}", html.substring(0, Math.min(300, html.length())));

                Document doc = Jsoup.parse(html);

                var items = doc.select(".item, li.item, .house-item");

                if (items.isEmpty()) {
                    log.warn("[JiwuCrawler] 无数据（可能到底或反爬）: {}", url);
                    continue;
                }

                items.forEach(el -> {
                    String title = el.select(".title, .house-title").text();
                    String price = el.select(".price .total").text();
                    String area = el.select(".area").text();

                    HouseInfo h = new HouseInfo();
                    h.setCity(city);
                    h.setRegion(region);
                    h.setTitle(title);
                    h.setTotalPrice(parseNumber(price));
                    h.setArea(parseNumber(area));
                    h.setCrawlTime(LocalDateTime.now());
                    h.setIsValid(1);

                    houseInfoMapper.insertOrUpdateBySource(h);
                });

                count += items.size();
            }

            // 保存 cookie 下次继续用
            context.storageState(
                    new BrowserContext.StorageStateOptions()
                            .setPath(Paths.get("chrome_state.json"))
            );

        } catch (Exception e) {
            log.error("[JiwuCrawler] 出错", e);
        }

        return count;
    }

    private BigDecimal parseNumber(String txt) {
        if (txt == null) return null;
        txt = txt.replaceAll("[^0-9.]", "");
        if (txt.isEmpty()) return null;
        return new BigDecimal(txt);
    }

    private String cityToCode(String city) {
        return switch (city) {
            case "成都" -> "cd";
            case "重庆" -> "cq";
            case "北京" -> "bj";
            case "上海" -> "sh";
            default -> null;
        };
    }
}
