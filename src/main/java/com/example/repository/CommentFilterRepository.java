package com.example.repository;

import com.example.dto.CommentFilterDTO;
import com.example.dto.PaginationResultDTO;
import com.example.entity.CommentEntity;
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
public class CommentFilterRepository {
    @Autowired
    private EntityManager entityManager;
    public PaginationResultDTO<CommentEntity> filter(CommentFilterDTO filter, Integer page, Integer size) {
        StringBuilder builder = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        if (filter.getId() != null) {
            builder.append(" and id=:id ");
            params.put("id", filter.getId());
        }
        if (filter.getProfileId() != null) {
            builder.append(" and profileId=:profileId ");
            params.put("profileId", filter.getProfileId());
        }
        if (filter.getArticleId() != null) {
            builder.append(" and articleId=:articleId");

            params.put("articleId", filter.getArticleId());
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

        String selectBuilder = "From CommentEntity s where 1=1 " + builder + " order by createdDate desc";

        String countBuilder = "Select count(s) from CommentEntity as s where 1=1 " + builder;

        Query selectQuery = entityManager.createQuery(selectBuilder);
        Query countQuery = entityManager.createQuery(countBuilder);

        selectQuery.setMaxResults(size);
        selectQuery.setFirstResult((page - 1) * size);

        for (Map.Entry<String, Object> param : params.entrySet()) {
            selectQuery.setParameter(param.getKey(), param.getValue());
            countQuery.setParameter(param.getKey(), param.getValue());
        }
        List<CommentEntity> entityList = selectQuery.getResultList();
        Long totalElements = (Long) countQuery.getSingleResult();

        return new PaginationResultDTO<>(entityList, totalElements);

    }
}
