package com.example.realestatetracker.crawler.service;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

@Slf4j
public class HttpClientUtil {

    public static Document getHtml(String url) throws IOException {
        Connection connection = Jsoup.connect(url)
                .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) " +
                        "AppleWebKit/537.36 (KHTML, like Gecko) " +
                        "Chrome/122.0.0.0 Safari/537.36")
                .timeout(10000)
                .ignoreHttpErrors(true);

        return connection.get();
    }
}
