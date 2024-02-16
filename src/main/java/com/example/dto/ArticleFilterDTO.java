package com.example.dto;

import com.example.enums.ArticleStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
@Getter
@Setter
public class ArticleFilterDTO {
    private String id;
    private String title;
    private Integer regionId;
    private Integer categoryId;
    private LocalDate createdDate;
    private LocalDate fromDate;
    private LocalDate toDate;
    private LocalDate publishedDate;
    private Integer moderatorId;
    private Integer publisherId;
    private ArticleStatus status;
}
