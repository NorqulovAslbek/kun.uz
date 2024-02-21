package com.example.repository;

import com.example.entity.SavedArticleEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface SavedArticleRepository extends CrudRepository<SavedArticleEntity, Integer> {
    @Query("FROM SavedArticleEntity WHERE articleId=?1")
    SavedArticleEntity checkArticle(String id);

    @Transactional
    @Modifying
    @Query("DELETE FROM SavedArticleEntity WHERE articleId=?1")
    int deleteByIdArticle(String id);

    @Query("SELECT s.profileId FROM SavedArticleEntity AS s WHERE s.articleId=?1")
    int getProfileId(String id);
}
