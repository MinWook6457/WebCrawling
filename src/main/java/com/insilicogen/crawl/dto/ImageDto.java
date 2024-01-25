package com.insilicogen.crawl.dto;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class ImageDto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String imageUrl;

    @JsonIgnore
    @OneToOne
    @JoinColumn(name = "info_id", unique = true) // info_id를 외래키로 가짐
    private InfoDto infoDto;

    public ImageDto() {
        // Default Constructor
    }

    public ImageDto(String imageUrl, InfoDto infoDto) {
        this.imageUrl = imageUrl;
        this.infoDto = infoDto;
    }
 
}
