package com.example.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateArticleTypeDTO {
    private Integer order_number;
    private String name_uz;
    private String name_ru;
    private String name_en;
}
