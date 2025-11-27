package com.example.realestatetracker.crawler.impl;

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
public class AnjukeHouseCrawler implements HouseCrawler {

    @Resource
    private HouseInfoMapper houseInfoMapper;

    @Override
    public boolean support(String site) {
        return "anjuke".equalsIgnoreCase(site);
    }

    @Override
    public int crawlCity(String city, String region, int maxPage) {
        int total = 0;

        String domain = cityDomain(city);
        if (domain == null) {
            log.error("不支持城市: {}", city);
            return 0;
        }

        try (Playwright playwright = Playwright.create()) {

            Browser browser = playwright.chromium().launch(
                    new BrowserType.LaunchOptions()
                            .setHeadless(false)
                            .setArgs(java.util.List.of(
                                    "--disable-blink-features=AutomationControlled",
                                    "--disable-web-security",
                                    "--allow-running-insecure-content",
                                    "--disable-features=IsolateOrigins,site-per-process",
                                    "--disable-infobars",
                                    "--ignore-certificate-errors",
                                    "--no-sandbox",
                                    "--disable-setuid-sandbox",
                                    "--disable-dev-shm-usage",
                                    "--start-maximized",
                                    "--window-size=1920,1080"
                            ))
            );

            BrowserContext context = browser.newContext(
                    new Browser.NewContextOptions()
                            .setViewportSize(1920, 1080)
                            .setUserAgent(
                                    "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 " +
                                            "(KHTML, like Gecko) Chrome/123.0.0.0 Safari/537.36"
                            )
            );

            Page page = context.newPage();

            // --- 加载前注入反爬 ---
            page.addInitScript("""
                    Object.defineProperty(navigator, 'webdriver', { get: () => undefined });
                    window.chrome = { runtime: {} };
                    Object.defineProperty(navigator, 'plugins', { get: () => [1, 2, 3] });
                    Object.defineProperty(navigator, 'languages', { get: () => ['zh-CN', 'zh'] });
                    """ );

            for (int p = 1; p <= maxPage; p++) {

                String url = "https://" + domain + ".anjuke.com/sale/p" + p + "/?from=navigation";
                log.info("打开页面：{}", url);

                // ----------- ➤ 不使用 LoadState，使用兼容写法 -----------
                page.navigate(url);
                page.waitForLoadState(); // 等同于 load
                page.waitForTimeout(1500); // 给 JS 渲染时间

                // 等待房源列表加载
                try {
                    page.waitForSelector("div.property", new Page.WaitForSelectorOptions().setTimeout(15000));
                } catch (Exception e) {
                    log.warn("⚠ 页面主体未加载：{}", url);
                    continue;
                }

                List<ElementHandle> houses = page.querySelectorAll("div.property");
                if (houses.isEmpty()) {
                    log.warn("⚠ 未找到房源：{}", url);
                    continue;
                }

                int pageCount = 0;

                for (ElementHandle h : houses) {
                    try {
                        HouseInfo info = new HouseInfo();

                        info.setSourceSite("anjuke");
                        info.setCity(city);
                        info.setRegion(region);
                        info.setCrawlTime(LocalDateTime.now());
                        info.setIsValid(1);

                        // 标题
                        info.setTitle(text(h, ".property-content-title-name"));

                        // 小区
                        info.setCommunityName(text(h, "p.property-content-info-comm-name"));

                        // 地址
                        info.setAddress(text(h, "p.property-content-info-comm-address"));

                        // 户型/面积 等
                        String infoLine = text(h, "div.property-content-info p:nth-child(1)");
                        info.setLayout(infoLine);

                        String areaText = text(h, "div.property-content-info p:nth-child(2)");
                        info.setArea(num(areaText));

                        // 总价
                        info.setTotalPrice(num(text(h, "span.property-price-total-num")));

                        // 单价
                        info.setUnitPrice(num(text(h, "p.property-price-average")));

                        // 房源链接
                        String detailUrl = attr(h, "a", "href");
                        if (detailUrl != null) {
                            info.setSourceId(extractSourceId(detailUrl));
                        }

                        houseInfoMapper.insertOrUpdateBySource(info);
                        total++;
                        pageCount++;

                    } catch (Exception ignore) {
                    }
                }

                log.info("成功解析 {} 条房源（第 {} 页）", pageCount, p);
            }

        } catch (Exception e) {
            log.error("安居客爬取失败", e);
        }

        return total;
    }

    // ---------------- 工具方法区域 ----------------

    private String text(ElementHandle h, String selector) {
        try {
            ElementHandle e = h.querySelector(selector);
            String t = e == null ? null : e.innerText();
            return t == null ? null : t.trim();
        } catch (Exception ex) {
            return null;
        }
    }

    private String attr(ElementHandle h, String selector, String name) {
        try {
            ElementHandle e = h.querySelector(selector);
            return e == null ? null : e.getAttribute(name);
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

    private String extractSourceId(String url) {
        try {
            int idx = url.lastIndexOf("/");
            if (idx < 0) return null;
            String tail = url.substring(idx + 1);
            int q = tail.indexOf("?");
            if (q > 0) tail = tail.substring(0, q);
            int dot = tail.indexOf(".");
            if (dot > 0) tail = tail.substring(0, dot);
            return tail;
        } catch (Exception e) {
            return null;
        }
    }

    private String cityDomain(String city) {
        return switch (city) {
            case "北京" -> "beijing";
            case "上海" -> "shanghai";
            case "广州" -> "guangzhou";
            case "深圳" -> "shenzhen";
            case "成都" -> "chengdu";
            case "重庆" -> "chongqing";
            default -> null;
        };
    }
}
