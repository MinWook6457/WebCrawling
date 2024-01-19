package com.insilicogen.crawl.service;

import com.insilicogen.crawl.dto.InfoDto;
import com.insilicogen.crawl.model.Info;
import com.insilicogen.crawl.repository.NewsRepository;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;

@Service
public class NewsService {

    public static ArrayList<Info> list = new ArrayList<>();
    public static String destinationFolder = "C:\\Users\\kih25\\OneDrive\\바탕 화면\\Test\\crawling\\image";
    public static int imgCount = 0;

    @Autowired
    private NewsRepository newsRepository;
    
    @Autowired
    public NewsService(NewsRepository newsRepository) {
        this.newsRepository = newsRepository;
    }

    public void downloadImage(String imageUrl, String destinationFolder, String title) {
        try {
            URL url = new URL(imageUrl);

            try (InputStream in = url.openStream()) {
                String fileName = imgCount + ".jpg";
                imgCount++;
                Path destinationPath = Paths.get(destinationFolder, fileName);

                Files.copy(in, destinationPath, StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void printArticleInfo(String category, Elements elements) {
        System.out.println("[" + category + " 기사]");
        for (Element element : elements) {
            String title = element.select("dl > dt:nth-child(2) > a").text();
            String content = element.select("dd > span.lede").text();
            String publisher = element.select("dd > span.writing").text();
            String upload = element.select("dd > span.date").text();

            System.out.println("기사 제목: " + title);
            System.out.println("기사 내용: " + content);
            System.out.println("기사 출처: " + publisher);
            System.out.println("기사 업로드: " + upload);

            Element imageElement = element.selectFirst("dl > dt.photo img");
            if (imageElement != null) {
                String imageUrl = imageElement.attr("src");
                System.out.println("이미지 경로: " + imageUrl);

                Info info = new Info(title, content, publisher, upload, imageUrl);
                list.add(info);
            } else {
                System.out.println("이미지가 없습니다.");
                String imageUrl = "";
                Info info = new Info(title, content, publisher, upload, imageUrl);
                list.add(info);
            }
            System.out.println("------------------------");
        }
    }

    @Autowired
    public void crawlAndSaveNews() {
        String newsUrl = "https://news.naver.com/main/list.naver?mode=LS2D&sid2=230&mid=shm";
        String newsTag = "#main_content > div.list_body.newsflash_body";
 
        try {
        	int user_id = 1;
            Document doc = Jsoup.connect(newsUrl).get();
            Element body = doc.selectFirst(newsTag);

            Elements headlineElements = body.select("ul.type06_headline > li");
            printArticleInfo("headline", headlineElements);

            Elements normalElements = body.select("ul.type06 > li");
            printArticleInfo("normal", normalElements);

            for (Info info : list) {
                if (!info.getUrl().isEmpty()) {
                    downloadImage(info.getUrl(), destinationFolder, info.getTitle());
                    InfoDto infoDto = new InfoDto(user_id,info.getTitle(), info.getContent(), info.getPublisher(), info.getUpload(), info.getUrl());
                    user_id++;
                    newsRepository.save(infoDto);
                } else {
                    InfoDto infoDto = new InfoDto(user_id,info.getTitle(), info.getContent(), info.getPublisher(), info.getUpload(), info.getUrl());
                    user_id++;
                    newsRepository.save(infoDto);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
