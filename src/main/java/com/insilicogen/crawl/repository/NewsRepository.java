package com.insilicogen.crawl.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.insilicogen.crawl.dto.InfoDto;

@Repository
public interface NewsRepository extends JpaRepository<InfoDto, Long> {

    @Query(value = "SELECT * FROM INFO_DTO info_dto LIMIT ?1, ?2", nativeQuery = true)
    List<InfoDto> findNewsList(int pageSize, int offset);

}
