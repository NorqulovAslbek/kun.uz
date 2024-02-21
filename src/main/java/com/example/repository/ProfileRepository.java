package com.example.repository;

import com.example.entity.ProfileEntity;
import com.example.enums.ProfileStatus;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface ProfileRepository extends CrudRepository<ProfileEntity, Integer>, PagingAndSortingRepository<ProfileEntity, Integer> {
    Optional<ProfileEntity> findByEmail(String email);
    @Query("FROM ProfileEntity WHERE email=?1 AND password=?2 AND visible=true")
    Optional<ProfileEntity> findByEmailAndPassword(String email, String password);
    Optional<ProfileEntity> findByPhone(String phone);
        @Transactional
        @Modifying
        @Query("Update ProfileEntity  set status =?2 where id = ?1")
        void updateStatus(Integer id, ProfileStatus active);


    @Transactional
    @Modifying
    @Query("Update ProfileEntity  set status =?2 where phone = ?1")
    void updateStatusActive(String phone, ProfileStatus active);

//    @Transactional
//    @Modifying
//    @Query(value = "select count(*) from ProfileEntity where createdDate between fromDate=?2 and toDate=?3  and email=?1",nativeQuery = true)
//    int countSendEmailLimit(String email, LocalDateTime fromDate,LocalDateTime toDate);

}
