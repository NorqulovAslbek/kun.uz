package com.example.service;

import com.example.dto.EmailHistoryDTO;
import com.example.entity.EmailHistoryEntity;
import com.example.exp.AppBadException;
import com.example.repository.EmailHistoryRepository;
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
public class EmailHistoryService {
    @Autowired
    private EmailHistoryRepository emailHistoryRepository;

    public List<EmailHistoryDTO> getByEmail(String email) {
        List<EmailHistoryEntity> emailHistoryEntityList = emailHistoryRepository.findByEmail(email);
        if (emailHistoryEntityList.isEmpty()) {
            throw new AppBadException("email not fount");
        }
        List<EmailHistoryDTO> list = new LinkedList<>();
        for (EmailHistoryEntity entity : emailHistoryEntityList) {
            list.add(toDTO(entity));
        }
        return list;
    }


    public List<EmailHistoryDTO> getByDate(LocalDate date) {
        LocalDateTime fromDate = LocalDateTime.of(date, LocalTime.MIN);
        LocalDateTime toDate = LocalDateTime.of(date, LocalTime.MAX);
        List<EmailHistoryEntity> emailHistoryEntities = emailHistoryRepository.findByCreatedDataBetween(fromDate, toDate);
        if (emailHistoryEntities.isEmpty()) {
            throw new AppBadException("email not fount");
        }
        List<EmailHistoryDTO> list = new LinkedList<>();
        for (EmailHistoryEntity entity : emailHistoryEntities) {
            list.add(toDTO(entity));
        }
        return list;
    }

    public PageImpl<EmailHistoryDTO> pagination(Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<EmailHistoryEntity> all = emailHistoryRepository.findAll(pageable);
        List<EmailHistoryEntity> content = all.getContent();
        long totalElements = all.getTotalElements();
        List<EmailHistoryDTO> list = new LinkedList<>();
        for (EmailHistoryEntity entity : content) {
            list.add(toDTO(entity));
        }
        return new PageImpl<>(list, pageable, totalElements);
    }


    private EmailHistoryDTO toDTO(EmailHistoryEntity byEmail) {
        EmailHistoryDTO dto = new EmailHistoryDTO();
        dto.setId(byEmail.getId());
        dto.setEmail(byEmail.getEmail());
        dto.setMessage(byEmail.getMessage());
        dto.setProfile_id(byEmail.getProfile().getId());
        dto.setCreatedData(byEmail.getCreatedData());
        return dto;
    }

}
