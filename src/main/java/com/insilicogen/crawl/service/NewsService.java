package com.insilicogen.crawl.service;


import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.helper.StringUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.insilicogen.crawl.dto.ImageDto;
import com.insilicogen.crawl.dto.InfoDto;
import com.insilicogen.crawl.model.Info;
import com.insilicogen.crawl.repository.NewsRepository;

@Service
public class NewsService {

    public static String destinationFolder = "C:\\Users\\kih25\\OneDrive\\바탕 화면\\Test\\crawling\\image";
    private final String newsUrl = "https://news.naver.com/main/list.naver?mode=LS2D&sid2=230&sid1=105&mid=shm&";
    private final String newsTag = "#main_content > div.list_body.newsflash_body";

    @Autowired
    private NewsRepository newsRepository;

    @Autowired
    public NewsService(NewsRepository newsRepository) {
        this.newsRepository = newsRepository;
    }

    // 이미지 다운로드 함수
    public void downloadImage(String imageUrl, String destinationFolder, InfoDto i) {
        try {
            if (StringUtil.isBlank(imageUrl)) {
                System.out.println("Image URL is blank. Skipping download.");
            }

            URL url = new URL(imageUrl);
            try (InputStream in = url.openStream()) {
                String fileName = i.getImageDto().getId() + ".jpg";
                Path destinationPath = Paths.get(destinationFolder, fileName);
                Files.copy(in, destinationPath, StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 크롤링 함수 => 우선 데이터 정보를 객체에 담아두고 DTO에 저장
    private List<InfoDto> crawlingNewsInInfo(Elements elements) {
        List<InfoDto> newsList = new ArrayList<>();

        for (Element element : elements) {
            String title = element.select("dt:not(.photo) > a").text().trim();
            String content = element.select("dd > span.lede").text().trim();
            String publisher = element.select("dd > span.writing").text().trim();
            String upload1 = element.select("dd > span.date.is_new").text().trim();
            String upload2 = element.select("dd > span.date.is_outdated").text().trim();

            Element imageElement = element.selectFirst("dl > dt.photo img");

            if (upload1.contains("분")) {
                String imageUrl = (imageElement != null) ? imageElement.attr("src") : "";             
                
                Info info = new Info(title, content, publisher, upload1, imageUrl);
                newsList.add(saveData(info));
               
            }

            if (upload2.contains("시")) {
                String imageUrl = (imageElement != null) ? imageElement.attr("src") : "";

                Info info = new Info(title, content, publisher, upload2, imageUrl);
                newsList.add(saveData(info));         
            }
        }

        return newsList;
    }

    private InfoDto saveData(Info info){
        InfoDto infoDto = new InfoDto(info.getTitle(),info.getContent(),info.getPublisher(),info.getUpload());
        ImageDto imageDto = new ImageDto(info.getUrl(), infoDto);
        infoDto.setImageDto(imageDto);

        return infoDto;
    }

    // 초기 크롤링 함수
    public List<InfoDto> crawlAndSaveNews() {
        List<InfoDto> newsList = new ArrayList<>();

        Date nowDate = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
        String page = "&page=1";

        try {
            for (int date = 0; date < 30; date++) {
                Date crawlingDate = new Date(nowDate.getTime() - (date * 24L * 3600 * 1000));

                String formattedDate = simpleDateFormat.format(crawlingDate);
                String newUrl = newsUrl.concat("date=").concat(formattedDate).concat(page);
                System.out.println(newUrl);

                Document doc = Jsoup.connect(newUrl).get();
                Element body = doc.selectFirst(newsTag);

                Elements headlineElements = body.select("ul.type06_headline > li");
                newsList.addAll(crawlingNewsInInfo(headlineElements));

                Elements normalElements = body.select("ul.type06 > li");
                newsList.addAll(crawlingNewsInInfo(normalElements));
                

                // 이미지 다운로드 및 뉴스 저장
                for (InfoDto info : newsList) {
                    if (StringUtil.isBlank(info.getImageDto().getImageUrl())) {
                        downloadImage(info.getImageDto().getImageUrl(), destinationFolder, info);
                    }
                }

                System.out.println("크롤링 완료");

                newsList = newsRepository.saveAll(newsList);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        return newsList;
    }
    
    public List<InfoDto> getPage(int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        return newsRepository.findNewsList(offset, pageSize);
    }

    public int getTotalNewsCount() {
        return (int) newsRepository.count();
    }
}
