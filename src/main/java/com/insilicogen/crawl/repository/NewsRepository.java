package com.insilicogen.crawl.repository;

import com.insilicogen.crawl.dto.InfoDto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

@Repository
public interface NewsRepository extends JpaRepository<InfoDto, Long> {
    // 제목으로 데이터 조회
    List<InfoDto> findByTitle(String title);
}