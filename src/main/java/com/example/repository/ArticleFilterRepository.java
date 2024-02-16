package com.example.repository;

import com.example.dto.ArticleDTO;
import com.example.dto.PaginationResultDTO;
import com.example.entity.ArticleEntity;
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
public class ArticleFilterRepository {

    @Autowired
    private EntityManager entityManager;

    public PaginationResultDTO<ArticleEntity> filter(ArticleDTO filter, Integer page, Integer size) {
        StringBuilder builder = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
//      status)
        if (filter.getId() != null) {
            builder.append(" and s.id=:id ");
            params.put("id", filter.getId());
        }
        if (filter.getTitle() != null) {
            builder.append(" and title=:title ");
            params.put("title", filter.getTitle());
        }
        if (filter.getRegionId() != null) {
            builder.append(" and regionId=:regionId ");
            params.put("regionId", filter.getRegionId());
        }

        if (filter.getCategoryId() != null) {
            builder.append(" and categoryId=:categoryId ");
            params.put("categoryId", filter.getCategoryId());
        }

        if (filter.getModeratorId() != null) {
            builder.append(" and moderatorId=:moderatorId ");
            params.put("moderatorId", filter.getModeratorId());
        }
        if (filter.getPublisherId() != null) {
            builder.append(" and publisherId=:publisherId ");
            params.put("publisherId", filter.getPublisherId());
        }
        if (filter.getStatus() != null) {
            builder.append(" and status=:status ");
            params.put("status", filter.getStatus());
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

        String selectBuilder = "From ArticleEntity s where 1=1 " + builder + " order by createdDate desc";

        String countBuilder = "Select count(s) from ArticleEntity as s where 1=1 " + builder;

        Query selectQuery = entityManager.createQuery(selectBuilder);
        Query countQuery = entityManager.createQuery(countBuilder);

        selectQuery.setMaxResults(size);
        selectQuery.setFirstResult((page - 1) * size);

        for (Map.Entry<String, Object> param : params.entrySet()) {
            selectQuery.setParameter(param.getKey(), param.getValue());
            countQuery.setParameter(param.getKey(), param.getValue());
        }
        List<ArticleEntity> entityList = selectQuery.getResultList();
        Long totalElements = (Long) countQuery.getSingleResult();

        return new PaginationResultDTO<>(entityList, totalElements);

    }
}
