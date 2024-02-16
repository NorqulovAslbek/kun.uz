package com.example.service;

import com.example.dto.*;
import com.example.entity.*;
import com.example.enums.ArticleStatus;
import com.example.exp.AppBadException;
import com.example.repository.*;
import com.example.util.SpringSecurityUtil;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class ArticleService {
    @Autowired
    private ArticleRepository articleRepository;
    @Autowired
    private NewArticleTypeService newArticleTypeService;
    @Autowired
    private NewArticleTypeRepository newArticleTypeRepository;
    @Autowired
    private AttachRepository attachRepository;
    @Autowired
    private AttachService attachService;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private RegionRepository regionRepository;
    @Autowired
    private ArticleFilterRepository repository;


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

    public ArticleShortInfoDTO update(String id) {
        ArticleEntity entity = get(id);
        entity.setPublishedDate(LocalDateTime.now());
        Integer publisherId = SpringSecurityUtil.getCurrentUser().getId();
        entity.setPublisherId(publisherId);
        entity.setStatus(ArticleStatus.PUBLISHER);
        articleRepository.save(entity);
        return getShortArticleDTO(entity);
    }

    public List<ArticleShortInfoDTO> getLastFiveArticleByTypes(Integer articleTypeId, Integer size) {
        List<String> newArticleIdList = newArticleTypeRepository.getArticleId(articleTypeId, size);
        List<ArticleShortInfoDTO> list = new LinkedList<>();
        for (String articleId : newArticleIdList) {
            ArticleEntity articleEntity = articleRepository.getById(articleId);
            if (articleEntity.getStatus().equals(ArticleStatus.PUBLISHER)) {
                list.add(getShortArticleDTO(articleEntity));
            }

        }
        return list;
    }

    public List<ArticleFullInfoDTO> getLast8ArticlesNotIncludedInList(List<String> articlesId) {
        Iterable<ArticleEntity> all = articleRepository.findAll();
        //  all[1,2,3,5,6,7,8,9,12]
        // new[1,2,3]
        List<ArticleFullInfoDTO> list = new LinkedList<>();
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
        return list;
    }


    public List<ArticleFullInfoDTO> getLast4ArticlesByTypesExceptGivenId(String aId, Integer articleTypeId) {
        List<String> articleList = newArticleTypeRepository.getArticleId(aId, articleTypeId);
        List<ArticleFullInfoDTO> list = new LinkedList<>();
        for (String articleId : articleList) {
            ArticleEntity articleEntity = articleRepository.getById(articleId);
            if (articleEntity.getStatus().equals(ArticleStatus.PUBLISHER)) {
                list.add(getFullArticleDTO(articleEntity));
            }
        }
        return list;
    }


    private ArticleShortInfoDTO getShortArticleDTO(ArticleEntity entity) {
        ArticleShortInfoDTO dto = new ArticleShortInfoDTO();
        dto.setDescription(entity.getDescription());
        dto.setTitle(entity.getTitle());
        dto.setId(entity.getId());
        dto.setPublishedDate(entity.getPublishedDate());
        String id = entity.getImage().getId();
        Optional<AttachEntity> optional = attachRepository.findById(id);
        if (optional.isEmpty()) {
            log.warn("not found attach");
            throw new AppBadException("not found attach");
        }
        dto.setImage(attachService.toDTO(optional.get()));
        return dto;
    }

    private ArticleFullInfoDTO getFullArticleDTO(ArticleEntity entity) {
        ArticleFullInfoDTO dto = new ArticleFullInfoDTO();
        dto.setId(entity.getId());
        dto.setContent(entity.getContent());
        dto.setTitle(entity.getTitle());
        dto.setSharedCount(entity.getSharedCount());
        dto.setViewCount(entity.getViewCount());
        Optional<CategoryEntity> optionalCategory = categoryRepository.findById(entity.getCategoryId());
        if (optionalCategory.isEmpty()) {
            log.warn("not found category");
            throw new AppBadException("not found caetgory");
        }
        CategoryEntity categoryEntity = optionalCategory.get();
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setId(categoryEntity.getId());
        categoryDTO.setName(categoryEntity.getNameUz());
        dto.setCategory(categoryDTO);

        Optional<RegionEntity> regionEntityOptional = regionRepository.findById(entity.getRegionId());
        if (regionEntityOptional.isEmpty()) {
            log.warn("not found region");
            throw new AppBadException("not found region");
        }
        RegionEntity regionEntity = regionEntityOptional.get();
        RegionDTO regionDTO = new RegionDTO();
        regionDTO.setId(regionEntity.getId());
        regionDTO.setName(regionEntity.getNameUz());
        dto.setRegion(regionDTO);
        dto.setPublishedDate(entity.getPublishedDate());
        return dto;
    }


    public List<ArticleShortInfoDTO> getMostReadArticles() {
        List<ArticleEntity> mostReadArticles = articleRepository.getMostReadArticles();
        List<ArticleShortInfoDTO> list = new LinkedList<>();
        for (ArticleEntity mostReadArticle : mostReadArticles) {
            list.add(getShortArticleDTO(mostReadArticle));
        }
        return list;
    }

    public PageImpl<ArticleShortInfoDTO> getArticleListByRegionId(Integer id, Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<ArticleEntity> all = articleRepository.getArticleListByRegionId(pageable, id);
        return getArticleShortInfoDTOS(pageable, all);
    }

    public List<ArticleShortInfoDTO> getLast5ArticleCategoryKey(Integer id) {
        List<ArticleEntity> last5ArticleCategoryId = articleRepository.getLast5ArticleCategoryId(id);
        List<ArticleShortInfoDTO> list = new LinkedList<>();
        for (ArticleEntity entity : last5ArticleCategoryId) {
            list.add(getShortArticleDTO(entity));
        }
        return list;
    }

    public PageImpl<ArticleShortInfoDTO> getArticlesByCategoryKey(Integer categoryID, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<ArticleEntity> all = articleRepository.getArticlesByCategoryKey(pageable, categoryID);
        return getArticleShortInfoDTOS(pageable, all);
    }

    @NotNull
    private PageImpl<ArticleShortInfoDTO> getArticleShortInfoDTOS(Pageable pageable, Page<ArticleEntity> all) {
        List<ArticleEntity> content = all.getContent();
        long totalElements = all.getTotalElements();
        List<ArticleShortInfoDTO> list = new LinkedList<>();
        for (ArticleEntity entity : content) {
            list.add(getShortArticleDTO(entity));
        }
        return new PageImpl<>(list, pageable, totalElements);
    }

    public Integer IncreaseArticleViewCount(String id) {
        ArticleEntity entity = get(id);
        if (entity.getStatus().equals(ArticleStatus.NOT_PUBLISHER)) {
            throw new AppBadException("not fount article");
        }
        int viewCount = entity.getViewCount() + 1;
        entity.setViewCount(viewCount);
        articleRepository.save(entity);
        return viewCount;
    }

    public Integer IncreaseShareViewCount(String id) {
        ArticleEntity entity = get(id);
        if (entity.getStatus().equals(ArticleStatus.NOT_PUBLISHER)) {
            throw new AppBadException("not fount article");
        }
        int shareCount = entity.getSharedCount() + 1;
        entity.setSharedCount(shareCount);
        articleRepository.save(entity);
        return shareCount;
    }

    public PageImpl<ArticleShortInfoDTO> filter(ArticleFilterDTO dto, Integer page, Integer size) {
        PaginationResultDTO<ArticleEntity> filter = repository.filter(dto, page, size);

        List<ArticleShortInfoDTO> list = new LinkedList<>();

        for (ArticleEntity articleEntity : filter.getList()) {
            list.add(getShortArticleDTO(articleEntity));
        }
        Pageable pageable = PageRequest.of(page - 1, size, Sort.Direction.ASC, "createdDate");
        return new PageImpl<>(list, pageable, filter.getTotalSize());
    }


    public ArticleEntity get(String id) {
        return articleRepository.findArticleEntity(id).orElseThrow(() -> {
            log.warn("get by id article {}", id);
            return new AppBadException("Article not found");
        });

    }


    private ArticleEntity getArticleEntity(CreateArticleDTO dto, Integer moderatorId) {
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
