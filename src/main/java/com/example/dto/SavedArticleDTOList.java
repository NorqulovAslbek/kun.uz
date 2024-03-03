package com.example.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
@Getter
@Setter
public class SavedArticleDTOList {
    private Integer id;
    private ArticleDTO article;
}
