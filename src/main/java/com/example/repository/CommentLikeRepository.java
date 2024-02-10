package com.example.repository;

import com.example.entity.CommentEntity;
import com.example.entity.CommentLikeEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface CommentLikeRepository extends CrudRepository<CommentLikeEntity,Integer> {
    @Query("SELECT cl FROM CommentLikeEntity AS  cl WHERE  cl.commentId=?1 AND cl.profileId=?2")
    Optional<CommentLikeEntity> checkClickLike(Integer commentId, Integer profileId);
}
