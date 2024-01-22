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
import org.jsoup.helper.StringUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.insilicogen.crawl.dto.ImageDto;
import com.insilicogen.crawl.dto.InfoDto;
import com.insilicogen.crawl.repository.ImageDtoRepository;
import com.insilicogen.crawl.repository.InfoDtoRepository;
import com.insilicogen.crawl.repository.NewsRepository;

@Service
public class NewsService {

	public static String destinationFolder = "C:\\Users\\kih25\\OneDrive\\바탕 화면\\Test\\crawling\\image";

	private final String newsUrl = "https://news.naver.com/main/list.naver?mode=LS2D&sid2=230&mid=shm";
	private final String newsTag = "#main_content > div.list_body.newsflash_body";

	@Autowired
	private NewsRepository newsRepository;

	@Autowired
	public NewsService(NewsRepository newsRepository) {
		this.newsRepository = newsRepository;
	}

	public void downloadImage(String imageUrl, String destinationFolder,InfoDto i) {
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

	private List<InfoDto> extractArticleInfo(String type, Elements elements) {
		
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
				System.out.println("이미지 경로: " + imageUrl);

				InfoDto infoDto = new InfoDto(title, content, publisher, upload1);
				ImageDto imageDto = new ImageDto(imageUrl, infoDto);
				infoDto.setImageDto(imageDto); // ImageDto url Setting

				newsList.add(infoDto);
			}

			if (upload2.contains("시")) {
				String imageUrl = (imageElement != null) ? imageElement.attr("src") : "";
				System.out.println("이미지 경로: " + imageUrl);

				InfoDto infoDto = new InfoDto(title, content, publisher, upload2);
				ImageDto imageDto = new ImageDto(imageUrl, infoDto);
				infoDto.setImageDto(imageDto); // Set the image DTO in the info DTO

				newsList.add(infoDto);
			}
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

			// 이미지 다운로드 및 뉴스 저장
			for (InfoDto info : newsList) {
				System.out.println("크롤링 정보에 따른 이미지 url " );
				if (StringUtils.hasText(info.getImageDto().getImageUrl())) {
					downloadImage(info.getImageDto().getImageUrl(), destinationFolder,info);
				}
			}

			// 뉴스 정보 저장
			newsList = newsRepository.saveAll(newsList); // ID를 얻어오기 위해 저장 후 다시 받아옴
		} catch (IOException e) {
			e.printStackTrace();
		}

		return newsList;
	}
}
