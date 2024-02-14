package com.example.repository;

import com.example.entity.CommentEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends CrudRepository<CommentEntity, Integer> , PagingAndSortingRepository<CommentEntity, Integer> {
    @Query("FROM CommentEntity AS c WHERE c.id=?1 AND c.visible=true")
    Optional<CommentEntity> findByIdComment(Integer commentId);
    @Query("FROM CommentEntity AS c WHERE c.articleId=?1 AND c.visible=true")
    List<CommentEntity> findByIdArticleId(String articleId);

}
