package com.example.service;

import com.example.dto.ArticleDTO;
import com.example.dto.AttachDTO;
import com.example.dto.SavedArticleDTO;
import com.example.dto.SavedArticleDTOList;
import com.example.entity.SavedArticleEntity;
import com.example.exp.AppBadException;
import com.example.mapper.SavedArticleMapper;
import com.example.repository.ArticleRepository;
import com.example.repository.SavedArticleRepository;
import com.example.util.SpringSecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

@Service
public class SavedArticleService {
    @Autowired
    private SavedArticleRepository savedArticleRepository;
    @Autowired
    private ArticleRepository articleRepository;
    @Autowired
    private AttachService attachService;

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
        if (savedArticleRepository.getProfileId(id) != profileId) {
            throw new AppBadException("you are not allowed");
        }
        if (!articleRepository.existsById(id)) {
            throw new AppBadException("not found article");
        }
        int effectiveRow = savedArticleRepository.deleteByIdArticle(id);
        return effectiveRow != 0;
    }

    public List<SavedArticleDTOList> getProfileSavedArticleList() {
        Integer profileId = SpringSecurityUtil.getCurrentUser().getId();
        List<SavedArticleMapper> list = savedArticleRepository.getList(profileId);
        List<SavedArticleDTOList> dtoList = new LinkedList<>();
        for (SavedArticleMapper savedArticleMapper : list) {
            SavedArticleDTOList dto = new SavedArticleDTOList();
            dto.setId(savedArticleMapper.getId());
            ArticleDTO articleDTO = new ArticleDTO();
            articleDTO.setId(savedArticleMapper.getArticleId());
            articleDTO.setTitle(savedArticleMapper.getTitle());
            articleDTO.setDescription(savedArticleMapper.getDescription());
            AttachDTO attachDTO = new AttachDTO();
            if (savedArticleMapper.getImageId()!=null) {
                attachDTO.setId(savedArticleMapper.getImageId());
                attachDTO.setUrl(attachService.getURL(savedArticleMapper.getImageId()).getUrl());
                articleDTO.setImage(attachDTO);
            }
            dto.setArticle(articleDTO);
            dtoList.add(dto);
        }
        return dtoList;
    }

    private SavedArticleEntity toDTO(String id, Integer profileId) {
        SavedArticleEntity entity = new SavedArticleEntity();
        entity.setArticleId(id);
        entity.setProfileId(profileId);
        entity.setCreatedDate(LocalDateTime.now());
        return entity;
    }

}
