package com.insilicogen.crawl.model;

//크롤링한 정보를 담을 객체
public class Info {
	private String title;
	private String content;
	private String publisher;
	private String upload;
	private String url;
	
	public Info() {
		// 기본 생성자
	}
	
	public Info(String title,String content,String publisher,String upload,String url) {
		this.title = title;
		this.content = content;
		this.publisher = publisher;
		this.upload = upload;
		this.url = url;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getPublisher() {
		return publisher;
	}

	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}

	public String getUpload() {
		return upload;
	}

	public void setUpload(String upload) {
		this.upload = upload;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	

}
