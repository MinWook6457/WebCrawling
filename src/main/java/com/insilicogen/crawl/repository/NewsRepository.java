package com.insilicogen.crawl.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.insilicogen.crawl.dto.InfoDto;

@Repository
public interface NewsRepository extends JpaRepository<InfoDto, Long> {
	@Query(value = "SELECT info.*, image.image_url " +
            "FROM info_dto info " +
            "INNER JOIN image_dto image ON info.id = image.info_id " +
            "LIMIT :offset, :pageSize", nativeQuery = true)
			// LIMIT ?1, ?2
	Page<InfoDto> findPagedNewsList(Pageable pageable);
}
