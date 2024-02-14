package com.example.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
public class CommentFilterDTO {
   private Integer id;
   private LocalDate fromDate;
   private LocalDate toDate;
   private Integer profileId;
   private String articleId;
}
