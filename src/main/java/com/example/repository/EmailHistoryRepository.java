package com.example.repository;

import com.example.entity.EmailHistoryEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface EmailHistoryRepository extends CrudRepository<EmailHistoryEntity, Integer>, PagingAndSortingRepository<EmailHistoryEntity, Integer> {
     List<EmailHistoryEntity> findByEmail(String email);

     List<EmailHistoryEntity> findByCreatedDataBetween(LocalDateTime from, LocalDateTime to);
}
