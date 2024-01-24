package com.insilicogen.crawl.controller;

import com.insilicogen.crawl.dto.InfoDto;

import java.util.List;

public class NewsResponse {
    private List<InfoDto> newsList;
    private int totalNewsCount;

    public NewsResponse(List<InfoDto> newsList, int totalNewsCount) {
        this.newsList = newsList;
        this.totalNewsCount = totalNewsCount;
    }

    /* Getter 정의 */
    public List<InfoDto> getNewsList() {
        return newsList;
    }

    public int getTotalNewsCount() {
        return totalNewsCount;
    }
}
