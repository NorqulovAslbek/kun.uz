package com.example.service;

import com.example.dto.SavedArticleDTO;
import com.example.entity.SavedArticleEntity;
import com.example.exp.AppBadException;
import com.example.repository.ArticleRepository;
import com.example.repository.SavedArticleRepository;
import com.example.util.SpringSecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class SavedArticleService {
    @Autowired
    private SavedArticleRepository savedArticleRepository;
    @Autowired
    private ArticleRepository articleRepository;

    public boolean create(String id) { //article id kelyabdi
        Integer profileId = SpringSecurityUtil.getCurrentUser().getId();
        if (!articleRepository.existsById(id)) {
            throw new AppBadException("not found article");
        }
        if (savedArticleRepository.checkArticle(id) != null) {
            throw new AppBadException("article is available");
        }
        SavedArticleEntity entity = toDTO(id, profileId);
        savedArticleRepository.save(entity);
        return true;
    }

    public boolean delete(String id) { ////article id kelyabdi
        Integer profileId = SpringSecurityUtil.getCurrentUser().getId();
        if (savedArticleRepository.checkArticle(id) == null) {
            throw new AppBadException("article not found");
        }
        if (savedArticleRepository.getProfileId(id)!=profileId){
            throw new AppBadException("you are not allowed");
        }
        if (!articleRepository.existsById(id)) {
            throw new AppBadException("not found article");
        }
        int effectiveRow = savedArticleRepository.deleteByIdArticle(id);
        return effectiveRow != 0;
    }

    private SavedArticleEntity toDTO(String id, Integer profileId) {
        SavedArticleEntity entity = new SavedArticleEntity();
        entity.setArticleId(id);
        entity.setProfileId(profileId);
        entity.setCreatedDate(LocalDateTime.now());
        return entity;
    }

}
