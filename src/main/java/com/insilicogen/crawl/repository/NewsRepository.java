package com.insilicogen.crawl.repository;

/*
 * 데이터베이스와 상호작용하는 레포지스토리 인터페이스
 * */

import com.insilicogen.crawl.model.info;

import org.springframework.data.jpa.repository.JpaRepository;

public interface NewsRepository extends JpaRepository<info, Long> {
	
}
