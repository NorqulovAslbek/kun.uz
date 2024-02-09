package com.example.repository;

import com.example.entity.ArticleEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.Optional;

public interface ArticleRepository extends CrudRepository<ArticleEntity, String>, PagingAndSortingRepository<ArticleEntity, String> {
    @Query("FROM ArticleEntity WHERE id=?1 and visible=true")
    Optional<ArticleEntity> findArticleEntity(String id);

    @Query("FROM ArticleEntity WHERE id=?1 AND visible=true ORDER BY createdDate LIMIT 5")
    ArticleEntity getById(String id);

    @Query("FROM ArticleEntity ORDER BY viewCount DESC LIMIT 4")
    List<ArticleEntity> getMostReadArticles();

    @Query("FROM ArticleEntity AS a WHERE a.regionId = :id")
    Page<ArticleEntity> getArticleListByRegionId(Pageable pageable, Integer id);
    @Query("FROM ArticleEntity AS a WHERE a.categoryId = :id")
    List<ArticleEntity> getLast5ArticleCategoryId(Integer id);
    @Query("FROM ArticleEntity AS a WHERE a.categoryId = :id")
    Page<ArticleEntity> getArticlesByCategoryKey(Pageable pageable,Integer id);
}
