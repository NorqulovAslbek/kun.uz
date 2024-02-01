package com.example.service;

import com.example.dto.SmsHistoryDTO;
import com.example.entity.SmsHistoryEntity;
import com.example.exp.AppBadException;
import com.example.repository.SmsHistoryRepository;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.LinkedList;
import java.util.List;

@Service
public class SmsHistoryService {
    @Autowired
    private SmsHistoryRepository smsHistoryRepository;


    public List<SmsHistoryDTO> getByPhone(String phone) {
        List<SmsHistoryEntity> smsHistoryRepositoryByPhone = smsHistoryRepository.findByPhone(phone);
        if (smsHistoryRepositoryByPhone.isEmpty()) throw new AppBadException("phone not found");
        return getSmsHistoryDTOS(smsHistoryRepositoryByPhone);
    }


    public List<SmsHistoryDTO> getByDate(LocalDate date) {
        LocalDateTime from = LocalDateTime.of(date, LocalTime.MIN);
        LocalDateTime to = LocalDateTime.of(date, LocalTime.MAX);
        List<SmsHistoryEntity> byDateList = smsHistoryRepository.getByDate(from, to);
        if (byDateList.isEmpty()) throw new AppBadException("SMS from that time was not found!");
        return getSmsHistoryDTOS(byDateList);
    }

    public PageImpl<SmsHistoryDTO> getPagination(Integer page, Integer size) {
        Sort sort = Sort.by(Sort.Direction.DESC, "createdDate");
        Pageable pageable = PageRequest.of(page - 1, size, sort);
        Page<SmsHistoryEntity> bySms = smsHistoryRepository.findAll(pageable);
        long totalElements = bySms.getTotalElements();
        List<SmsHistoryEntity> content = bySms.getContent();
        List<SmsHistoryDTO> list = new LinkedList<>();
        for (SmsHistoryEntity entity : content) {
            list.add(toDTO(entity));
        }
        return new PageImpl<>(list, pageable, totalElements);
    }


    @NotNull
    private List<SmsHistoryDTO> getSmsHistoryDTOS(List<SmsHistoryEntity> smsHistoryEntityList) {
        List<SmsHistoryDTO> list = new LinkedList<>();
        for (SmsHistoryEntity smsHistoryEntity : smsHistoryEntityList) {
            list.add(toDTO(smsHistoryEntity));
        }
        return list;
    }

    private SmsHistoryDTO toDTO(SmsHistoryEntity entity) {
        SmsHistoryDTO smsHistoryDTO = new SmsHistoryDTO();
        smsHistoryDTO.setId(entity.getId());
        smsHistoryDTO.setStatus(entity.getStatus());
        smsHistoryDTO.setPhone(entity.getPhone());
        smsHistoryDTO.setMessage(entity.getMessage());
        smsHistoryDTO.setCreatedDate(entity.getCreatedDate());
        return smsHistoryDTO;
    }
}
