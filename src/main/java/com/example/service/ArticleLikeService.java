package com.example.service;

import com.example.dto.ArticleLikeDTO;
import com.example.entity.ArticleLikeEntity;
import com.example.enums.LikeStatus;
import com.example.repository.ArticleLikeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class ArticleLikeService {

    @Autowired
    private ArticleLikeRepository articleLikeRepository;

    public ArticleLikeDTO articleClickLike(String articleId, Integer profileId) {
        return getArticleEmotionDTO(articleId, profileId, LikeStatus.LIKE);
    }

    public ArticleLikeDTO articleClickDislike(String articleId, Integer profileId) {
        return getArticleEmotionDTO(articleId, profileId, LikeStatus.DISLIKE);
    }

    public ArticleLikeDTO articleLikeOrDislikeRemove(String articleId, Integer profileId) {
        return getArticleEmotionDTO(articleId, profileId, null);
    }

    private ArticleLikeDTO getArticleEmotionDTO(String articleId, Integer profileId, LikeStatus emotion) {
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
