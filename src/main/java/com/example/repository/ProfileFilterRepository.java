package com.example.repository;


import com.example.dto.PaginationResultDTO;
import com.example.dto.ProfileDTO;
import com.example.entity.ProfileEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Repository
public class ProfileFilterRepository {
    @Autowired
    private EntityManager entityManager;

    public PaginationResultDTO<ProfileEntity> filter(ProfileDTO filter, Integer page, Integer size) {

        StringBuilder builder = new StringBuilder();
        Map<String, Object> params = new HashMap<>();

        if (filter.getName() != null) {
            builder.append(" and lower(name) like :name ");
            params.put("name", "%" + filter.getName().toLowerCase() + "%");
        }
        if (filter.getSurname() != null) {
            builder.append(" and lower(surname) like :surname ");
            params.put("surname", "%" + filter.getSurname() + "%");
        }
        if (filter.getPhone() != null) {
            builder.append(" and s.phone=:phone ");
            params.put("phone", filter.getPhone());
        }
        if (filter.getRole() != null) {
            builder.append(" and role=:role ");
            params.put("role", filter.getRole());
        }

        if (filter.getFromDate() != null && filter.getToDate() != null) {
            LocalDateTime fromDate = LocalDateTime.of(filter.getFromDate(), LocalTime.MIN);
            LocalDateTime toDate = LocalDateTime.of(filter.getToDate(), LocalTime.MAX);
            builder.append(" and createdDate between :fromDate and :toDate ");
            params.put("fromDate", fromDate);
            params.put("toDate", toDate);
        }
        if (filter.getFromDate() != null) {
            LocalDateTime fromDate = LocalDateTime.of(filter.getFromDate(), LocalTime.MIN);
            LocalDateTime toDate = LocalDateTime.of(filter.getFromDate(), LocalTime.MAX);
            builder.append(" and createdDate between : fromDate and toDate ");
            params.put("fromDate", fromDate);
            params.put("toDate", toDate);
        }
        if (filter.getToDate() != null) {
            LocalDateTime time = LocalDateTime.of(filter.getToDate(), LocalTime.MAX);
            builder.append("and createdDate <= :time ");
            params.put("time", time);
        }

        String selectBuilder = "From ProfileEntity s where 1=1 " + builder + " order by createdDate desc";

        String countBuilder = "Select count(s) from ProfileEntity as s where 1=1 " + builder;

        Query selectQuery = entityManager.createQuery(selectBuilder);
        Query countQuery = entityManager.createQuery(countBuilder);

        selectQuery.setMaxResults(size);
        selectQuery.setFirstResult((page - 1) * size);

        for (Map.Entry<String, Object> param : params.entrySet()) {
            selectQuery.setParameter(param.getKey(), param.getValue());
            countQuery.setParameter(param.getKey(), param.getValue());
        }
        List<ProfileEntity> entityList = selectQuery.getResultList();
        Long totalElements = (Long) countQuery.getSingleResult();

        return new PaginationResultDTO<>(entityList, totalElements);

    }


}
