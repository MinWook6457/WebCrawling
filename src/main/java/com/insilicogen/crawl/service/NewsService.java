package com.insilicogen.crawl.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



import java.util.List;

@Service
public class NewsService {

    @Autowired
    private News newsRepository;

    public List<info> crawlAndSaveNews() {
        // 크롤링 로직 및 데이터 저장
    	newsRepository.crawlNews();

        // News 클래스에서 저장한 데이터를 반환
        return newsRepository.getList();
    }
}