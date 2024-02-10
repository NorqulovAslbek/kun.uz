package com.example.repository;

import com.example.entity.ArticleLikeEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface ArticleLikeRepository extends CrudRepository<ArticleLikeEntity, Integer> {
    @Query("SELECT al FROM ArticleLikeEntity AS al WHERE al.articleId=?1 AND al.profileId=?2")
    Optional<ArticleLikeEntity> checkClickLike(String articleId, Integer profileId);
}
