package com.example.dto;


import com.example.enums.ProfileStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class SmsHistoryDTO {
    private Integer id;
    private String phone;
    private String message;
    private ProfileStatus status;
    private LocalDateTime createdDate;
}
