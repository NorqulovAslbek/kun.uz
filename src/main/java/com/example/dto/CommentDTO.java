package com.example.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommentDTO {
    private Integer id;
    private Integer profileId;
    private ProfileDTO profile;
    private ArticleDTO article;
    private String content;
    private String articleId;
    private Integer replyId;
    private LocalDateTime createdDate;
    private LocalDateTime updateDate;
    private Boolean visible;
}
