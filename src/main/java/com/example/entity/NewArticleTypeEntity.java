package com.example.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "new_article_type")
public class NewArticleTypeEntity extends BaseEntity {
    @Column(name = "article_id")
    private String articleId;
    @ManyToOne
    @JoinColumn(name = "article_id",insertable = false,updatable = false)
    private ArticleEntity article;

    @Column(name = "article_type_id")
    private Integer articleTypeId;
    @ManyToOne
    @JoinColumn(name = "article_type_id",insertable = false,updatable = false)
    private ArticleTypeEntity articleType;
}
