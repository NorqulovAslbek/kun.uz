package com.example.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateCommentDTO {
    private String content;
    private String articleId;
    private Integer commentId;
}
