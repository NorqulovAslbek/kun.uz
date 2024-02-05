package com.example.repository;

import com.example.entity.SmsHistoryEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface SmsHistoryRepository extends CrudRepository<SmsHistoryEntity, Integer>, PagingAndSortingRepository<SmsHistoryEntity, Integer> {
    //    @Query("FROM SmsHistoryEntity  WHERE phone=?1")
    List<SmsHistoryEntity> findByPhone(String phone);

    @Query("SELECT s  FROM SmsHistoryEntity AS s WHERE s.createdDate BETWEEN ?1 AND ?2")
    List<SmsHistoryEntity> getByDate(LocalDateTime from, LocalDateTime to);

    @Query("SELECT count (*) from SmsHistoryEntity s where s.phone =?1 and s.createdDate between ?2 and ?3")
    Long countSendSms(String phone, LocalDateTime from, LocalDateTime to);

    @Query("SELECT s FROM SmsHistoryEntity AS s WHERE s.phone=?1 AND s.message=?2 AND s.createdDate between ?3 and ?4")
    Optional<SmsHistoryEntity> getPhoneAndCode(String phone, String code,LocalDateTime from,LocalDateTime to);
}
