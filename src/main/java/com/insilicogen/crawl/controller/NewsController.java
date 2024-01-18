package com.insilicogen.crawl.controller;

import java.util.List;

import com.insilicogen.crawl.model.info;
import com.insilicogen.crawl.service.NewsService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

// 디스패처 서블릿이 우선적으로 @Controller로 매핑된 모든 어노테이션 파싱
@Controller
public class NewsController {

	@Autowired
	private NewsService newsService;

	@GetMapping("/news")
	public String showNews(Model model) {

		return "news"; // news.jsp 파일로 이동
	}

	/*
	 * @ResponseBody
	 * 
	 * @RequestMapping("/home/map") public Map<String, Object>
	 * selectMap(@RequestBody Map<String, Object> map, Model model) {
	 * 
	 * model.addAttribute("message", "Hello, Spring Boot MVC!"); return map; }
	 */
}