package com.example.repository;

import com.example.entity.ArticleEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface ArticleRepository extends CrudRepository<ArticleEntity, String> {
    @Query("FROM ArticleEntity WHERE id=?1 and visible=true")
    Optional<ArticleEntity> findArticleEntity(String id);

    @Query("FROM ArticleEntity WHERE id=?1 AND visible=true ORDER BY createdDate LIMIT 5")
    ArticleEntity getById(String id);
}
