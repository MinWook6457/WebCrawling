package com.insilicogen.crawl.dto;

import lombok.Data;
import lombok.Getter;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
@Getter
// 외부 시스템과 데이터 통신을 할 경우 DTO로 정의
// DB에서 가져오는 Data 는 VO로 정의 후 사용
public class InfoDto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
    private String title;
    private String content;
    private String publisher;
    private String upload;
    private String url;

    public InfoDto() {
        // Default Constructor
    }

    public InfoDto(int id,String title, String content, String publisher, String upload, String url) {
    	this.id = id;
    	this.title = title;
        this.content = content;
        this.publisher = publisher;
        this.upload = upload;
        this.url = url;
    }
}
