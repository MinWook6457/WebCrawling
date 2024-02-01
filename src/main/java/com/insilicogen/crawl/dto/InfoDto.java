package com.insilicogen.crawl.dto;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter // Getter 메서드 자동 주입
@Setter
@Table(name="info_dto")
@AllArgsConstructor 
@Entity // 디비와 연결해주겠다. 라는 의미
// 외부 시스템과 데이터 통신을 할 경우 DTO로 정의
// DB에서 가져오는 Data 는 VO로 정의 후 사용
public class InfoDto {
    @Id  
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
    @Column(length = 1000)
    private String title;
    
    @Column(length = 1000)
    private String content;
    
    private String publisher;
    
    private String upload;
    
    private String url;
    
    @Transient
    private int pageNo;
    @Transient
	private int pageUnit;
    
    public InfoDto() {
        // Default Constructor
    }

    public InfoDto(String title, String content, String publisher, String upload,String url) {
    	this.title = title;
        this.content = content;
        this.publisher = publisher;
        this.upload = upload;
        this.url = url;
    }
}

