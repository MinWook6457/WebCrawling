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

	/*
	@GetMapping("/crawling")
	public String crawlingNews(Model model) {
		List<InfoDto> newsList = newsService.crawlAndSaveNews();
		model.addAttribute("newsList", newsList);
		model.addAttribute("imagePath", NewsService.destinationFolder);
		return "news";
	}
	*/

	@GetMapping("/selectNewsList")
	@ResponseBody
	public Map<String, Object> selectNewsList(Model model, 
	        @RequestParam(defaultValue = "1") int page,
	        @RequestParam(defaultValue = "20") int pageSize,
	        @RequestParam(defaultValue = "") String imgUrl
			) {
	    List<InfoDto> newsList = newsService.getPage(page, pageSize,imgUrl);
	    int totalItems = newsService.getTotalNewsCount();

	    Map<String, Object> response = new HashMap<>();
	    response.put("newsList", newsList);
	    response.put("totalItems", totalItems);
	    
	    
	    return response;
	}


	// 크롤링 컨트롤러
	@GetMapping("/initCrawling")
	@ResponseBody
	public ModelAndView initCrawling(Model model) {
		try {			
			List<InfoDto> newsList = newsService.crawlAndSaveNews();
			model.addAttribute("newsList", newsList);
			model.addAttribute("imagePath", NewsService.destinationFolder);
			return new ModelAndView("news", model.asMap());
		} catch (Exception e) {
			e.printStackTrace();
			return new ModelAndView("errorPage", "error", "Error initiating crawling.");
		}
	}
}
