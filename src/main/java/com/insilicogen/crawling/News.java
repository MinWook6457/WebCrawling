package com.insilicogen.crawling;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.insilicogen.crawl.model.info;

import java.io.IOException;
import java.util.ArrayList;

import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class News {

    public static String news_url = "https://news.naver.com/main/list.naver?mode=LS2D&sid2=230&mid=shm";
    public static String tag = "#main_content > div.list_body.newsflash_body";
    public static ArrayList<info> list = new ArrayList<info>();
    
    public static String destinationFolder = "C:\\Users\\kih25\\OneDrive\\바탕 화면\\Test\\crolling\\image"; // 절대 경로 사용
    public static int imgCount = 0;
    
    private static void downloadImage(String imageUrl, String destinationFolder, String title) {
        try {
            URL url = new URL(imageUrl); // url 객체 생성

            // 이미지 다운로드
            
            try (InputStream in = url.openStream()) { // url로 파일 스트림 open
                // 파일 저장 경로 설정
                String fileName = imgCount + ".jpg"; 
                imgCount++;
                Path destinationPath = Paths.get(destinationFolder, fileName); // 경로 정의

                // 이미지 저장
                Files.copy(in, destinationPath, StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private static void printArticleInfo(String category, Elements elements) {
    	
        System.out.println("[" + category + " 기사]");       
        for (Element element : elements) { // elements로 받아와서 element에 입력
            String title = element.select("dl > dt:nth-child(2) > a").text();
            String content = element.select("dd > span.lede").text();
            String publisher = element.select("dd > span.writing").text();
            String upload = element.select("dd > span.date").text();
            
            System.out.println("기사 제목: " + title);
            System.out.println("기사 내용: " + content);
            System.out.println("기사 출처: " + publisher);
            System.out.println("기사 업로드: " + upload);

            Element imageElement = element.selectFirst("dl > dt.photo img"); // 이미지는 따로 추출
            if (imageElement != null) {
                String imageUrl = imageElement.attr("src"); // imageElement 에서 src 속성을 추출하여 imageUrl 저장
                System.out.println("이미지 경로: " + imageUrl);
                
                info i = new info(title,content,publisher,upload,imageUrl);
                
                list.add(i);
            } else {
                System.out.println("이미지가 없습니다.");
                String imageUrl = "";
                info i = new info(title,content,publisher,upload,imageUrl);
                list.add(i);
            }
            System.out.println("------------------------");
        }
    }
    
    public static void main(String[] args) {
    	
    	
        try {
            Document doc = Jsoup.connect(news_url).get(); // url 파싱
            db DatabaseManager = new db(); // db 연동 객체 선언

            Element body = doc.selectFirst(tag); // 태그 몸체

            // headline 기사 추출
            Elements headlineElements = body.select("ul.type06_headline > li");
            printArticleInfo("headline", headlineElements);

            // 일반 기사 추출
            Elements normalElements = body.select("ul.type06 > li");
            printArticleInfo("normal", normalElements);
            
            for(info i : list) { //for문을 통한 전체출력
            	if (!i.getUrl().equals("")) { // 이미지가 있는 경우에만 다운로드 및 저장
                    downloadImage(i.getUrl(), destinationFolder, i.getTitle()); // 폴더에 저장
                    DatabaseManager.insertArticle(i); // 디비에 저장
                }else {
                	DatabaseManager.insertArticle(i); // 디비에 저장
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
