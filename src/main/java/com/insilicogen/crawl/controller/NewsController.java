package com.insilicogen.crawl.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.insilicogen.crawl.dto.InfoDto;
import com.insilicogen.crawl.service.NewsService;

@Controller
public class NewsController {

	@Autowired
	private NewsService newsService;

	@GetMapping("/news") // 통신할 url
	public String crawlAndSaveNews(Model model) {
		List<InfoDto> newsList = newsService.crawlAndSaveNews(); // 서비스 단에서 로직 실행하여 데이터 불러옴
		
		model.addAttribute("imagePath", NewsService.destinationFolder); // 컨트롤러에서 뷰 단에 해당 데이터를 보냄
		
		model.addAttribute("newsList", newsList);
	
		return "news"; // news.jsp로 포워딩
	}
}
