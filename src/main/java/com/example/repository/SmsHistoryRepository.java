package com.example.repository;

import com.example.entity.SmsHistoryEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface SmsHistoryRepository extends CrudRepository<SmsHistoryEntity, Integer>, PagingAndSortingRepository<SmsHistoryEntity, Integer> {
    List<SmsHistoryEntity> findByPhone(String phone);
    @Query("SELECT s  FROM SmsHistoryEntity AS s WHERE s.createdDate BETWEEN ?1 AND ?2")
    List<SmsHistoryEntity> getByDate(LocalDateTime from, LocalDateTime to);
    @Query("SELECT count (*) from SmsHistoryEntity s where s.phone =?1 and s.createdDate between ?2 and ?3")
    Long countSendSms(String phone,LocalDateTime from,LocalDateTime to);
}
