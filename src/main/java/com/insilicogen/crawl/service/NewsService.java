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
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.insilicogen.crawl.dto.ImageDto;
import com.insilicogen.crawl.dto.InfoDto;
import com.insilicogen.crawl.model.Info;
import com.insilicogen.crawl.repository.NewsRepository;

@Service
public class NewsService {

	public static String destinationFolder = "C:\\Users\\kih25\\OneDrive\\바탕 화면\\Test\\crawling\\image";

	private final String newsUrl = "https://news.naver.com/breakingnews/section/105/230";
	private final String newsTag = "#newsct > div.section_latest > div > div.section_latest_article._CONTENT_LIST._PERSIST_META"; // 공통
																																	// 태그

	@Autowired
	private NewsRepository newsRepository;

	@Autowired
	public NewsService(NewsRepository newsRepository) {
		this.newsRepository = newsRepository;
	}

	public void initData() {
		newsRepository.deleteAllInBatch();
	}

	// 이미지 다운로드 함수
	public void downloadImage(String imageUrl, String destinationFolder, InfoDto i) {
		try {
			if (!imageUrl.equals("")) {
				URL url = new URL(imageUrl);
				try (InputStream in = url.openStream()) {
					String fileName = i.getImageDto().getId() + ".jpg";
					Path destinationPath = Paths.get(destinationFolder, fileName);
					Files.copy(in, destinationPath, StandardCopyOption.REPLACE_EXISTING);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// 크롤링 함수 => 우선 데이터 정보를 객체에 담아두고 DTO에 저장
	private List<InfoDto> crawlingNewsInInfo(Elements elements, Elements imageElement) {
		List<InfoDto> newsList = new ArrayList<>();
		System.out.println("daily 기사 개수 : " + elements.size());
		for (Element element : elements) {

			String title = element.select("a > strong").text().trim();
			String content = element.select("div.sa_text_lede").text().trim();
			String publisher = element.select("div.sa_text_info > div.sa_text_info_left > div.sa_text_press").text()
					.trim();
			String upload1 = element.select("div.sa_text_info > div.sa_text_info_left > div.sa_text_datetime.is_recent")
					.text().trim();
			String upload2 = element.select("div.sa_text_info > div.sa_text_info_left > div.sa_text_datetime").text()
					.trim();

			if (upload1.contains("분")) {
				String imageUrl = imageElement.select(".sa_thumb_inner img").attr("data-src");
				Info info = new Info(title, content, publisher, upload1, imageUrl);
				newsList.add(saveData(info));
			} else {
				String imageUrl = imageElement.select(".sa_thumb_inner img").attr("data-src");

				Info info = new Info(title, content, publisher, upload2, imageUrl);
				newsList.add(saveData(info));
			}
		}

		return newsList;
	}

	private InfoDto saveData(Info info) {
		InfoDto infoDto = new InfoDto(info.getTitle(), info.getContent(), info.getPublisher(), info.getUpload());
		ImageDto imageDto = new ImageDto(info.getUrl(), infoDto);
		infoDto.setImageDto(imageDto);

		return infoDto;
	}

	// 초기 크롤링 함수
	public List<InfoDto> crawlAndSaveNews() {
		System.out.println("크롤링 시작!!");
		List<InfoDto> newsList = new ArrayList<>();
		// news.naver.com/breakingnews/section/105/230?date=20240125
		try {
			for (int date = 0; date < 30; date++) {
				Date currentDate = new Date();
				currentDate = decrementDate(currentDate, date);

				SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");

				String formattedDate = simpleDateFormat.format(currentDate);
				String newUrl = newsUrl.concat("?date=").concat(formattedDate);
				System.out.println(newUrl);

				Document doc = Jsoup.connect(newUrl).get();
				Element body = doc.selectFirst(newsTag);

				List<InfoDto> dailyNewsList = new ArrayList<>(); // 하루의 뉴스를 저장할 리스트 생성

				Elements childElements = body.select("div:nth-child(1)");

				System.out.println(childElements.toArray());

				Elements articleElements = childElements.select("ul > li:nth-child(2) > div > div > div.sa_text");

				Elements imgElement = childElements
						.select("ul > li:nth-child(3) > div > div > div.sa_thumb._LAZY_LOADING_ERROR_HIDE > div");

				// 이미지 다운로드 및 뉴스 저장
				dailyNewsList.addAll(crawlingNewsInInfo(articleElements, imgElement));

				newsList.addAll(dailyNewsList);
				System.out.println("크롤링 완료");

			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		newsRepository.saveAll(newsList);
		return newsList;
	}

	public Page<InfoDto> getPagedNews(int page, int pageSize) {
		
        Pageable pageable = PageRequest.of(page - 1, pageSize);
        return newsRepository.findPagedNewsList(pageable);
    }

	public int getTotalNewsCount() {
		return (int) newsRepository.count();
	}
	

	/* 날짜가 주어지면 하루 씩 줄어드는 메소드 작성 */
	private static Date decrementDate(Date date, int minusDate) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);

		// 현재 일자에서 1일을 뺍니다.
		calendar.add(Calendar.DATE, -minusDate);

		return calendar.getTime();
	}
}
