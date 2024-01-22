package com.insilicogen.crawl.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.insilicogen.crawl.dto.InfoDto;

public interface InfoDtoRepository extends JpaRepository<InfoDto, Long> {
   
}
