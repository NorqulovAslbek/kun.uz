package com.example.service;

import com.example.config.CustomUserDetails;
import com.example.dto.*;
import com.example.entity.ArticleEntity;
import com.example.entity.CommentEntity;
import com.example.entity.ProfileEntity;
import com.example.exp.AppBadException;
import com.example.repository.ArticleRepository;
import com.example.repository.CommentFilterRepository;
import com.example.repository.CommentRepository;
import com.example.repository.ProfileRepository;
import com.example.util.SpringSecurityUtil;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class CommentService {
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private ProfileRepository profileRepository;
    @Autowired
    private ArticleRepository articleRepository;
    @Autowired
    private CommentFilterRepository repository;

    public CommentDTO create(CreateCommentDTO dto, Integer id) {
        dto.setProfileId(id);
        CommentEntity entity = toEntity(dto);
        CommentEntity commentEntity = commentRepository.save(entity);
        return toDTO(commentEntity);
    }

    public CommentDTO update(UpdateCommentDTO dto) {
        CommentEntity entity = get(dto.getCommentId());
        if (!articleRepository.existsById(dto.getArticleId())) {
            throw new AppBadException("No such article exists!");
        }
        entity.setArticleId(dto.getArticleId());
        entity.setContent(dto.getContent());
        entity.setUpdatedDate(LocalDateTime.now());
        commentRepository.save(entity);
        return toDTO(entity);
    }

    public boolean deleteUSER(Integer commentId) {
        CustomUserDetails currentUser = SpringSecurityUtil.getCurrentUser();
        Integer profileId = currentUser.getId();
        CommentEntity entity = get(commentId);
        if (!entity.getProfileId().equals(profileId)) {
            log.warn("Such a comment is not allowed on this profile{}", profileId);
            throw new AppBadException("Such a comment is not allowed on this profile");
        }
        entity.setVisible(false);
        commentRepository.save(entity);
        return true;

    }

    public boolean deleteADM(Integer commentId) {
        CommentEntity entity = get(commentId);
        entity.setVisible(false);
        commentRepository.save(entity);
        return true;
    }


    public List<CommentDTO> getCommentListByArticleId(String articleId) {
        List<CommentEntity> list = commentRepository.findByIdArticleId(articleId);
        List<CommentDTO> newList = new LinkedList<>();
        for (CommentEntity entity : list) {
            CommentDTO dto = new CommentDTO();
            dto.setId(list.get(0).getId());
            getCommentDTo(entity, dto);
            newList.add(dto);
        }
        return newList;
    }


    public PageImpl<CommentDTO> getCommentList(Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<CommentEntity> all = commentRepository.findAll(pageable);
        List<CommentEntity> content = all.getContent();
        long totalElements = all.getTotalElements();
        List<CommentDTO> list = new LinkedList<>();
        for (CommentEntity entity : content) {
            CommentDTO dto = new CommentDTO();
            dto.setId(entity.getId());
            getCommentDTo(entity, dto);
            dto.setContent(entity.getContent());
            dto.setReplyId(entity.getReplyId());
            Optional<ArticleEntity> optionalArticle = articleRepository.findById(entity.getArticleId());
            if (optionalArticle.isEmpty()) {
                throw new AppBadException("not fount article");
            }
            ArticleDTO articleDTO = new ArticleDTO();
            articleDTO.setId(entity.getArticleId());
            articleDTO.setTitle(optionalArticle.get().getTitle());
            dto.setArticle(articleDTO);
            list.add(dto);
        }
        return new PageImpl<>(list, pageable, totalElements);

    }

    private void getCommentDTo(CommentEntity entity, CommentDTO dto) {
        dto.setCreatedDate(entity.getCreatedDate());
        dto.setUpdateDate(entity.getUpdatedDate());
        Optional<ProfileEntity> optional = profileRepository.findById(entity.getProfileId());
        if (optional.isEmpty()) {
            throw new AppBadException("not fount profile");
        }
        ProfileDTO profileDTO = new ProfileDTO();
        ProfileEntity profileEntity = optional.get();
        profileDTO.setId(profileEntity.getId());
        profileDTO.setName(profileEntity.getName());
        profileDTO.setSurname(profileEntity.getSurname());
        dto.setProfile(profileDTO);
    }

    public CommentEntity get(Integer commentId) {
        Optional<CommentEntity> optional = commentRepository.findByIdComment(commentId);
        if (optional.isEmpty()) {
            log.warn("Inside the get function of the CommentService class:No comment found with this id {}", commentId);
            throw new AppBadException("comment  not found !");
        }
        return optional.get();
    }


    public PageImpl<CommentDTO> filter(CommentFilterDTO dto, Integer page, Integer size) {
        PaginationResultDTO<CommentEntity> filter = repository.filter(dto, page, size);
        List<CommentDTO> list = new LinkedList<>();
        for (CommentEntity entity : filter.getList()) {
            list.add(getEntityToDTO(entity));
        }
        Pageable pageable = PageRequest.of(page - 1, size);
        return new PageImpl<>(list, pageable, filter.getTotalSize());
    }


    public List<CommentDTO> getRepliedCommentListByCommentId(Integer commentId) {
        List<CommentEntity> commentByIdList = commentRepository.getCommentByReplyId(commentId);
        return getCommentDTOS(commentByIdList);
    }


    private List<CommentDTO> getCommentDTOS(List<CommentEntity> commentByIdList) {
        List<CommentDTO> list = new LinkedList<>();
        for (CommentEntity entity : commentByIdList) {
            CommentDTO dto = new CommentDTO();
            dto.setId(entity.getId());
            dto.setCreatedDate(entity.getCreatedDate());
            dto.setUpdateDate(entity.getUpdatedDate());
            Optional<ProfileEntity> optional = profileRepository.findById(entity.getProfileId());
            if (optional.isEmpty()) {
                throw new AppBadException("not fount profile");
            }
            ProfileEntity profileEntity = optional.get();
            ProfileDTO profileDTO = new ProfileDTO();
            profileDTO.setId(profileEntity.getId());
            profileDTO.setName(profileEntity.getName());
            profileDTO.setSurname(profileEntity.getSurname());
            dto.setProfile(profileDTO);
            list.add(dto);
        }
        return list;
    }

    public CommentDTO getEntityToDTO(CommentEntity entity) {
        CommentDTO dto = new CommentDTO();
        dto.setId(entity.getId());
        dto.setCreatedDate(entity.getCreatedDate());
        dto.setUpdateDate(entity.getUpdatedDate());
        dto.setProfileId(entity.getProfileId());
        dto.setContent(entity.getContent());
        dto.setArticleId(entity.getArticleId());
        dto.setReplyId(entity.getReplyId());
        dto.setVisible(entity.getVisible());
        return dto;
    }

    private CommentEntity toEntity(CreateCommentDTO dto) {
        CommentEntity entity = new CommentEntity();
        entity.setReplyId(dto.getReplyId());
        entity.setProfileId(dto.getProfileId());
        entity.setArticleId(dto.getArticleId());
        entity.setContent(dto.getContent());
        return entity;
    }

    private CommentDTO toDTO(CommentEntity entity) {
        CommentDTO dto = new CommentDTO();
        dto.setId(entity.getId());
        dto.setArticleId(entity.getArticleId());
        dto.setCreatedDate(entity.getCreatedDate());
        dto.setVisible(entity.getVisible());
        dto.setProfileId(entity.getProfileId());
        dto.setContent(entity.getContent());
        dto.setReplyId(entity.getReplyId());
        dto.setUpdateDate(entity.getUpdatedDate());
        return dto;
    }

}
