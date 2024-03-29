package com.insilicogen.crawl.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.insilicogen.crawl.dto.InfoDto;

@Repository
public interface NewsRepository extends JpaRepository<InfoDto, Long> {
	@Query("SELECT n FROM InfoDto n")
	Page<InfoDto> findPagedNewsList(Pageable pageable);
	
	@Transactional
	@Modifying
	@Query(value = "truncate info_dto",nativeQuery = true)
	void truncateInfoDtoTable();
}
