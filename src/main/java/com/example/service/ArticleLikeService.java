package com.example.service;

import com.example.dto.ArticleLikeDTO;
import com.example.entity.ArticleEntity;
import com.example.entity.ArticleLikeEntity;
import com.example.enums.ArticleStatus;
import com.example.enums.LikeStatus;
import com.example.exp.AppBadException;
import com.example.repository.ArticleLikeRepository;
import com.example.repository.ArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class ArticleLikeService {

    @Autowired
    private ArticleLikeRepository articleLikeRepository;
    @Autowired
    private ArticleRepository articleRepository;

    public ArticleLikeDTO articleClickLike(String articleId, Integer profileId) {
        return getArticleEmotionDTO(articleId, profileId, LikeStatus.LIKE);
    }

    public ArticleLikeDTO articleClickDislike(String articleId, Integer profileId) {
        return getArticleEmotionDTO(articleId, profileId, LikeStatus.DISLIKE);
    }

    public boolean articleLikeOrDislikeRemove(String articleId, Integer profileId) {
        Optional<ArticleEntity> optionalArticle = articleRepository.findById(articleId);
        if (optionalArticle.isPresent() && optionalArticle.get().getStatus().equals(ArticleStatus.NOT_PUBLISHER)) {
            throw new AppBadException("No such article exists");
        }
        ArticleEntity articleEntity = optionalArticle.get();
        articleEntity.setDislikeCount(0);
        articleEntity.setLikeCount(0);
        articleRepository.save(articleEntity);
        return true;
    }

    private ArticleLikeDTO getArticleEmotionDTO(String articleId, Integer profileId, LikeStatus emotion) {
        Optional<ArticleEntity> optionalArticle = articleRepository.findById(articleId);
        if (optionalArticle.isPresent() && optionalArticle.get().getStatus().equals(ArticleStatus.NOT_PUBLISHER)) {
            throw new AppBadException("No such article exists");
        }
        Optional<ArticleLikeEntity> optional = articleLikeRepository.checkClickLike(articleId, profileId);
        ArticleLikeEntity entity = new ArticleLikeEntity();
        if (optional.isPresent()) {
            Integer id = optional.get().getId();
            entity.setId(id);
        }
        entity.setArticleId(articleId);
        entity.setStatus(emotion);
        entity.setCreatedDate(LocalDateTime.now());
        entity.setProfileId(profileId);
        articleLikeRepository.save(entity);
        return toDTO(entity);
    }


    private ArticleLikeDTO toDTO(ArticleLikeEntity entity) {
        ArticleLikeDTO dto = new ArticleLikeDTO();
        dto.setStatus(entity.getStatus());
        dto.setArticleId(entity.getArticleId());
        dto.setCreatedDate(entity.getCreatedDate());
        dto.setProfileId(entity.getProfileId());
        return dto;
    }


}
