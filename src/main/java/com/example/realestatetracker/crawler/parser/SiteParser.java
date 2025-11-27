package com.example.realestatetracker.crawler.parser;

import com.example.realestatetracker.crawler.model.HouseRawData;
import org.jsoup.nodes.Document;

import java.util.List;

public interface SiteParser {

    List<String> parseListPageForDetailUrls(Document listDoc);

    HouseRawData parseDetailPage(Document detailDoc, String detailUrl);
}
