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

//    @GetMapping("/news/selectNewsList")
//    @ResponseBody
//    public Map<String, Object> selectNewsList(Model model,
//    		@RequestBody InfoDto infoDto,
//            @RequestParam(defaultValue = "1") int page,
//            @RequestParam(defaultValue = "10") int pageSize,
//            @RequestParam(defaultValue = "5") int pageUnit,
//            @RequestParam(defaultValue = "") String imgUrl) {
//        System.out.println("Controller method is called!");
//
//        Page<InfoDto> pagedNews = newsService.getPagedNews(page, pageSize);
//        
//        List<InfoDto> newsList = pagedNews.getContent();
//        int totalItems = (int) pagedNews.getTotalElements();
//
//        Map<String, Object> response = new HashMap<>();
//        response.put("newsList", newsList);
//        response.put("totalItems", totalItems);
//        
//        // response.put(imgUrl, response);
//        
//        System.out.println("page: " + page + ", pageSize: " + pageSize);
//
//        return response;
//    }

    @GetMapping("/news/initCrawling")
    public ModelAndView initCrawling(Model model,
    		@RequestParam(defaultValue = "1") int currentPage,
    		@RequestParam(defaultValue = "10") int pageUnit
    		) {
        try {
            newsService.crawlAndSaveNews();
//            model.addAttribute("newsList", newsList);
//            model.addAttribute("imagePath", NewsService.destinationFolder);
//            
//            Page<InfoDto> initPagedNews = newsService.getPagedNews(currentPage, pageSize);
//            List<InfoDto> initNewsList = initPagedNews.getContent();
//            
//            int totalItems = (int)initPagedNews.getTotalElements();
//            
//            Map<String,Object> response = new HashMap<>();
//            
//            response.put("newsList", initNewsList);
//            response.put("totalItems", totalItems);
//            response.put("currentPage", currentPage);
//            response.put("pageSize", pageSize);
//            
//            model.addAttribute("initResponse",response);
//            
            
            return new ModelAndView("news", model.asMap());
        } catch (Exception e) {
            e.printStackTrace();
            return new ModelAndView("errorPage", "error", "Error initiating crawling.");
        }
    }

}
