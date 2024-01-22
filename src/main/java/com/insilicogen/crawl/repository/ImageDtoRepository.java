package com.insilicogen.crawl.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.insilicogen.crawl.dto.ImageDto;

public interface ImageDtoRepository extends JpaRepository<ImageDto, Long> {
    // 
}
