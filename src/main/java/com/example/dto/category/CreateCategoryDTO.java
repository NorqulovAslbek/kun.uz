package com.example.dto.category;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateCategoryDTO {
    private Integer order_number;
    private String name_uz;
    private String name_ru;
    private String name_en;
}
