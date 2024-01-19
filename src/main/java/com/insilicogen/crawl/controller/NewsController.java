package com.insilicogen.crawl.controller;

import com.insilicogen.crawl.service.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/news")
public class NewsController {

    @Autowired
    private NewsService newsService;

    @GetMapping("/crawl")
    @ResponseBody
    public String crawlAndSaveNews() {
        newsService.crawlAndSaveNews();
        return "Crawling and saving news completed!";
    }
}
