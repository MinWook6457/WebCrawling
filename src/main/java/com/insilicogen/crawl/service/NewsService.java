package com.insilicogen.crawl.service;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.insilicogen.crawl.dto.InfoDto;
import com.insilicogen.crawl.model.Info;
import com.insilicogen.crawl.repository.NewsRepository;

@Service
public class NewsService {

	public static ArrayList<Info> list = new ArrayList<>();
	public static String destinationFolder = "C:\\Users\\kih25\\OneDrive\\바탕 화면\\Test\\crawling\\image";

	private final String newsUrl = "https://news.naver.com/main/list.naver?mode=LS2D&sid2=230&mid=shm";
	private final String newsTag = "#main_content > div.list_body.newsflash_body";

	@Autowired
	private NewsRepository newsRepository;

	@Autowired
	public NewsService(NewsRepository newsRepository) {
		this.newsRepository = newsRepository;
	}

	public void downloadImage(String imageUrl, String destinationFolder, String title, Long ID) {
		try {
			URL url = new URL(imageUrl);	
			try (InputStream in = url.openStream()) {
				String fileName = ID + ".jpg";
				Path destinationPath = Paths.get(destinationFolder, fileName);

				Files.copy(in, destinationPath, StandardCopyOption.REPLACE_EXISTING);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private List<InfoDto> extractArticleInfo(String type, Elements elements) {
		List<InfoDto> newsList = new ArrayList<>();

		for (Element element : elements) {
			String title = element.select("dt:not(.photo) > a").text().trim();
			String content = element.select("dd").text().trim();
			String publisher = element.select("dd > span.writing").text().trim();
			String upload = element.select("dd > span.date.is_new").text().trim();
			String url = element.select("dt:not(.photo) > a").attr("href").trim();

			InfoDto infoDto = new InfoDto(title, content, publisher, upload, url);
			newsList.add(infoDto);
		}

		return newsList;
	}

	public List<InfoDto> crawlAndSaveNews() {
		List<InfoDto> newsList = new ArrayList<>();

		try {
			Document doc = Jsoup.connect(newsUrl).get();
			Element body = doc.selectFirst(newsTag);

			Elements headlineElements = body.select("ul.type06_headline > li");
			newsList.addAll(extractArticleInfo("headline", headlineElements));

			Elements normalElements = body.select("ul.type06 > li");
			newsList.addAll(extractArticleInfo("normal", normalElements));

			// 저장 전에 이미지 다운로드 및 뉴스 저장
			for (InfoDto infoDto : newsList) {
				if (infoDto.getUrl() != null && !infoDto.getUrl().isEmpty()) { // url이 있을 때만 다운로드
					// 이미지 다운로드
					downloadImage(infoDto.getUrl(), destinationFolder, infoDto.getTitle(),infoDto.getId());
				}
			}

			// 뉴스 정보 저장
			newsRepository.saveAll(newsList);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return newsList;
	}
}
