package com.insilicogen.crawl.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.insilicogen.crawl.dto.InfoDto;
import com.insilicogen.crawl.service.NewsService;

@Controller
public class NewsController {

    @Autowired
    private NewsService newsService;

    @GetMapping("/news")
    public String news() {
    	
        return "news"; // news.jsp로 포워딩
    }
    
    @PostMapping("/news/selectNewsList")
    @ResponseBody
    public Page<InfoDto> selectNewsList(@RequestBody InfoDto infoDto) {
    	// , infoDto.getImageDto().getImageUrl()
    	System.out.println("현재 페이지 번호 : " + infoDto.getPageNo() + " 현재 페이지 단위 : " + infoDto.getPageUnit());
    	
        return newsService.getPagedNews(infoDto.getPageNo(),infoDto.getPageUnit());
    }

    @GetMapping("/news/initCrawling")
    public ModelAndView initCrawling(Model model) {
        try {
            newsService.crawlAndSaveNews();              
            return new ModelAndView("news", model.asMap());
        } catch (Exception e) {
            e.printStackTrace();
            return new ModelAndView("errorPage", "error", "Error initiating crawling.");
        }
    }

}
