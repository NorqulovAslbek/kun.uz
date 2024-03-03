package com.example.repository;

import com.example.entity.SavedArticleEntity;
import com.example.mapper.SavedArticleMapper;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SavedArticleRepository extends CrudRepository<SavedArticleEntity, Integer> {
    @Query("FROM SavedArticleEntity WHERE articleId=?1")
    SavedArticleEntity checkArticle(String id);

    @Transactional
    @Modifying
    @Query("DELETE FROM SavedArticleEntity WHERE articleId=?1")
    int deleteByIdArticle(String id);

    @Query("SELECT s.profileId FROM SavedArticleEntity AS s WHERE s.articleId=?1")
    int getProfileId(String id);

    @Query(value = """
            SELECT sa.id,
                  a.id AS articleId,
                  a.description,
                  a.title,
                  a.image_id AS imageId
            FROM saved_article AS sa
            INNER JOIN article AS a ON sa.article_id = a.id
                     WHERE sa.profile_id= :profileId
            """, nativeQuery = true)
    List<SavedArticleMapper> getList(@Param("profileId") Integer profileId);
}
