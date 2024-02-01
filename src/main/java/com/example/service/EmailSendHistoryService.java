package com.example.service;

import com.example.dto.EmailSendHistoryDTO;
import com.example.entity.EmailSendHistoryEntity;
import com.example.exp.AppBadException;
import com.example.repository.EmailSendHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.LinkedList;
import java.util.List;

@Service
public class EmailSendHistoryService {
    @Autowired
    private EmailSendHistoryRepository emailHistoryRepository;

    public List<EmailSendHistoryDTO> getByEmail(String email) {
        List<EmailSendHistoryEntity> emailHistoryEntityList = emailHistoryRepository.findByEmail(email);
        if (emailHistoryEntityList.isEmpty()) {
            throw new AppBadException("email not fount");
        }
        List<EmailSendHistoryDTO> list = new LinkedList<>();
        for (EmailSendHistoryEntity entity : emailHistoryEntityList) {
            list.add(toDTO(entity));
        }
        return list;
    }


    public List<EmailSendHistoryDTO> getByDate(LocalDate date) {
        LocalDateTime fromDate = LocalDateTime.of(date, LocalTime.MIN);
        LocalDateTime toDate = LocalDateTime.of(date, LocalTime.MAX);
        List<EmailSendHistoryEntity> emailHistoryEntities = emailHistoryRepository.findByCreatedDataBetween(fromDate, toDate);
        if (emailHistoryEntities.isEmpty()) {
            throw new AppBadException("email not fount");
        }
        List<EmailSendHistoryDTO> list = new LinkedList<>();
        for (EmailSendHistoryEntity entity : emailHistoryEntities) {
            list.add(toDTO(entity));
        }
        return list;
    }

    public PageImpl<EmailSendHistoryDTO> pagination(Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<EmailSendHistoryEntity> all = emailHistoryRepository.findAll(pageable);
        List<EmailSendHistoryEntity> content = all.getContent();
        long totalElements = all.getTotalElements();
        List<EmailSendHistoryDTO> list = new LinkedList<>();
        for (EmailSendHistoryEntity entity : content) {
            list.add(toDTO(entity));
        }
        return new PageImpl<>(list, pageable, totalElements);
    }


    private EmailSendHistoryDTO toDTO(EmailSendHistoryEntity byEmail) {
        EmailSendHistoryDTO dto = new EmailSendHistoryDTO();
        dto.setId(byEmail.getId());
        dto.setEmail(byEmail.getEmail());
        dto.setMessage(byEmail.getMessage());
        dto.setCreatedData(byEmail.getCreatedData());
        return dto;
    }

}
