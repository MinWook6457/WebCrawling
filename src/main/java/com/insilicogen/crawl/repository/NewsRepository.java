package com.insilicogen.crawl.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.insilicogen.crawl.dto.InfoDto;

@Repository
public interface NewsRepository extends JpaRepository<InfoDto, Long> {
    // 제목으로 데이터 조회
    List<InfoDto> findByTitle(String title);
}