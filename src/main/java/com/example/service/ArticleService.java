package com.example.service;

import com.example.dto.ArticleDTO;
import com.example.dto.CreateArticleDTO;
import com.example.entity.ArticleEntity;
import com.example.enums.ArticleStatus;
import com.example.exp.AppBadException;
import com.example.repository.ArticleRepository;
import com.example.repository.NewArticleTypeRepository;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Slf4j
@Service
public class ArticleService {
    @Autowired
    private ArticleRepository articleRepository;
    @Autowired
    private NewArticleTypeService newArticleTypeService;

    @Autowired
    private NewArticleTypeRepository newArticleTypeRepository;


    public CreateArticleDTO create(CreateArticleDTO dto, Integer moderatorId) {
        ArticleEntity entity = getArticleEntity(dto, moderatorId);
        articleRepository.save(entity);
        newArticleTypeService.create(entity.getId(), dto.getArticleType());
        return dto;
    }

    public CreateArticleDTO update(CreateArticleDTO dto, Integer moderatorId, String articleId) {
        ArticleEntity entity = get(articleId);
        entity.setDescription(dto.getDescription());
        entity.setTitle(dto.getTitle());
        entity.setContent(dto.getContent());
        entity.setImageId(dto.getImageId());
        entity.setCategoryId(dto.getCategoryId());
        entity.setRegionId(dto.getRegionId());
        entity.setModeratorId(moderatorId);
        articleRepository.save(entity);
        newArticleTypeService.merge(articleId, dto.getArticleType());
        return dto;
    }


    public Boolean delete(String id) {
        ArticleEntity entity = get(id);
        entity.setVisible(false);
        articleRepository.save(entity);
        return true;
    }

    public ArticleDTO update(String id) {
        ArticleEntity entity = get(id);
        entity.setStatus(ArticleStatus.PUBLISHER);
        articleRepository.save(entity);
        return getShortArticleDTO(entity);
    }

    public List<ArticleDTO> getLastFiveArticleByTypes(Integer articleTypeId, Integer size) {
        List<String> newArticleIdList = newArticleTypeRepository.getArticleId(articleTypeId, size);
        List<ArticleDTO> list = new LinkedList<>();
        for (String articleId : newArticleIdList) {
            list.add(getShortArticleDTO(articleRepository.getById(articleId)));
        }
        return list;
    }

    public List<ArticleDTO> getLast8ArticlesNotIncludedInList(List<String> articlesId) {
        Iterable<ArticleEntity> all = articleRepository.findAll();
        //  all[1,2,3,5,6,7,8,9,12]
        // new[1,2,3]
        List<ArticleDTO> list = new LinkedList<>();
        for (ArticleEntity entity : all) {
            int count = 0;
            for (String s : articlesId) {
                if (entity.getId().equals(s)) {
                    count++;
                }
            }
            if (count == 0 && entity.getStatus().equals(ArticleStatus.PUBLISHER)) {
                list.add(getFullArticleDTO(entity));
            }
            if (list.size() == 8) return list;
        }
        return null;
    }


    public List<ArticleDTO> getLast4ArticlesByTypesExceptGivenId(String aId, Integer articleTypeId) {
        List<String> articleList = newArticleTypeRepository.getArticleId(aId, articleTypeId);
        List<ArticleDTO> list = new LinkedList<>();
        for (String articleId : articleList) {
            list.add(getFullArticleDTO(articleRepository.getById(articleId)));
        }
        return list;
    }


    private static ArticleDTO getShortArticleDTO(ArticleEntity entity) {
        return getArticleDTO(entity);
    }

    private static ArticleDTO getFullArticleDTO(ArticleEntity entity) {
        ArticleDTO dto = getArticleDTO(entity);
        dto.setCategoryId(entity.getCategoryId());
        dto.setCreatedDate(entity.getCreatedDate());
        dto.setImageId(entity.getImageId());
        dto.setModeratorId(entity.getModeratorId());
        dto.setPublisherId(entity.getPublisherId());
        return dto;
    }

    @NotNull
    private static ArticleDTO getArticleDTO(ArticleEntity entity) {
        ArticleDTO dto = new ArticleDTO();
        dto.setTitle(entity.getTitle());
        dto.setContent(entity.getContent());
        dto.setDescription(entity.getDescription());
        dto.setSharedCount(entity.getSharedCount());
        dto.setViewCount(entity.getViewCount());
        dto.setStatus(entity.getStatus());
        return dto;
    }

    private ArticleEntity get(String id) {
        return articleRepository.findArticleEntity(id).orElseThrow(() -> {
            log.warn("get by id article {}", id);
            return new AppBadException("Article not found");
        });

    }


    private static ArticleEntity getArticleEntity(CreateArticleDTO dto, Integer moderatorId) {
        ArticleEntity entity = new ArticleEntity();
        entity.setTitle(dto.getTitle());
        entity.setDescription(dto.getDescription());
        entity.setContent(dto.getContent());
        entity.setImageId(dto.getImageId());
        entity.setRegionId(dto.getRegionId());
        entity.setModeratorId(moderatorId);
        entity.setCategoryId(dto.getCategoryId());
        return entity;
    }


}
