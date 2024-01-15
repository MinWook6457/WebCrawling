package crolling;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class News {

    public static String news_url = "https://news.naver.com/main/list.naver?mode=LS2D&sid2=230&mid=shm";
    public static String tag = "#main_content > div.list_body.newsflash_body";

    private static void printArticleInfo(String category, Elements elements) {
        System.out.println("[" + category + " 기사]");
        for (Element element : elements) { // elements로 받아와서 element에 입력
            String title = element.select("dl > dt:nth-child(2) > a").text();
            String content = element.select("dd > span.lede").text();
            String publisher = element.select("dd > span.writing").text();
            String date = element.select("dd > span.date").text();

            System.out.println("기사 제목: " + title);
            System.out.println("기사 내용: " + content);
            System.out.println("기사 출처: " + publisher);
            System.out.println("기사 업로드: " + date);

            Element imageElement = element.selectFirst("dl > dt.photo img"); // 이미지는 따로 추출
            if (imageElement != null) {
                String imageUrl = imageElement.attr("src"); // imageElement 에서 src 속성을 추출하여 imageUrl 저장
                System.out.println("이미지 경로: " + imageUrl);
            } else {
                System.out.println("이미지가 없습니다.");
            }

            System.out.println("------------------------");
        }
    }

    public static void main(String[] args) {
        try {
            Document doc = Jsoup.connect(news_url).get(); // url 파싱

            Element body = doc.selectFirst(tag); // 태그 몸체

            // headline 기사 추출
            Elements headlineElements = body.select("ul.type06_headline > li");
            printArticleInfo("headline", headlineElements);

            // 일반 기사 추출
            Elements normalElements = body.select("ul.type06 > li");
            printArticleInfo("normal", normalElements);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
