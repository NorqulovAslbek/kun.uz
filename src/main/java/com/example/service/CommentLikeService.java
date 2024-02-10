package com.example.service;

import com.example.dto.ArticleLikeDTO;
import com.example.dto.CommentLikeDTO;
import com.example.entity.ArticleLikeEntity;
import com.example.entity.CommentLikeEntity;
import com.example.enums.LikeStatus;
import com.example.repository.CommentLikeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class CommentLikeService {
    @Autowired
    private CommentLikeRepository commentLikeRepository;

    public CommentLikeDTO commentLikeClick(Integer commentId, Integer profileId) {
        return getCommentEmotionDTO(commentId, profileId, LikeStatus.LIKE);
    }

    public CommentLikeDTO commentDislikeClick(Integer commentId, Integer profileId) {
        return getCommentEmotionDTO(commentId, profileId, LikeStatus.DISLIKE);
    }

    public CommentLikeDTO commentRemoveLike(Integer commentId, Integer profileId) {
        return getCommentEmotionDTO(commentId, profileId, null);
    }

    private CommentLikeDTO getCommentEmotionDTO(Integer commentId, Integer profileId, LikeStatus emotion) {
        Optional<CommentLikeEntity> optional = commentLikeRepository.checkClickLike(commentId, profileId);
        CommentLikeEntity entity = new CommentLikeEntity();
        if (optional.isPresent()) {
            Integer id = optional.get().getId();
            entity.setId(id);
        }
        entity.setCommentId(commentId);
        entity.setStatus(emotion);
        entity.setCreatedDate(LocalDateTime.now());
        entity.setProfileId(profileId);
        commentLikeRepository.save(entity);
        return toDTO(entity);
    }

    private CommentLikeDTO toDTO(CommentLikeEntity entity) {
        CommentLikeDTO dto = new CommentLikeDTO();
        dto.setStatus(entity.getStatus());
        dto.setCommentId(entity.getCommentId());
        dto.setCreatedDate(entity.getCreatedDate());
        dto.setProfileId(entity.getProfileId());
        return dto;
    }
}
