package com.example.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
@Getter
@Setter
public class EmailHistoryDTO {
    private Integer id;
    private Integer profile_id;
    private String message;
    private String email;
    private LocalDateTime createdData;
}
