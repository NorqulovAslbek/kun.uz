package com.example.entity;

import com.example.enums.ProfileStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
@Getter
@Setter
@Entity
@Table(name = "email_send_history")
public class EmailHistoryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="profile_id")
    private ProfileEntity profile;
    private String message;
    private String email;
    @Enumerated(EnumType.STRING)
    private ProfileStatus status;
    private LocalDateTime createdData=LocalDateTime.now();
}
