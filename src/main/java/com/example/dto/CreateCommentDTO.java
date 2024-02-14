package com.example.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateCommentDTO {
    private String content;
    private String articleId;
    private Integer replyId;
    private Integer profileId;
}
