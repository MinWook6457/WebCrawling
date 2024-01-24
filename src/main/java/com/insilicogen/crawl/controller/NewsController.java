package com.insilicogen.crawl.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.insilicogen.crawl.dto.InfoDto;
import com.insilicogen.crawl.repository.NewsRepository;
import com.insilicogen.crawl.service.NewsService;

@Controller
public class NewsController {

	@Autowired
	private NewsService newsService;

	@Autowired
	private NewsRepository newsRepository;

	@GetMapping("/news")
	public String news() {
		return "news"; // news.jsp로 포워딩
	}

	@GetMapping("/crawling")
	public String crawlingNews(Model model) {
		List<InfoDto> newsList = newsService.crawlAndSaveNews(); // 기본값으로 1일치 크롤링
		model.addAttribute("newsList", newsList);
		model.addAttribute("imagePath", NewsService.destinationFolder);
		return "news";
	}

	@GetMapping("/selectNewsList")
	@ResponseBody
	public Map<String, Object> selectNewsList(Model model, 
	        @RequestParam(defaultValue = "1") int page,
	        @RequestParam(defaultValue = "10") int pageSize) {

	    List<InfoDto> newsList = newsService.getPage(page, pageSize);
	    int totalItems = newsService.getTotalNewsCount();

	    Map<String, Object> response = new HashMap<>();
	    response.put("newsList", newsList);
	    response.put("totalItems", totalItems);
	    
	    return response;
	}


	@GetMapping("/initCrawling")
	@ResponseBody
	public String initCrawling() {
		try {
			newsService.crawlAndSaveNews();
			return "Crawling initiated successfully!";
		} catch (Exception e) {
			e.printStackTrace();
			return "Error initiating crawling.";
		}
	}
}
