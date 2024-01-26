package com.example.service;

import com.example.dto.ArticleTypeDTO;
import com.example.entity.ArticleTypeEntity;
import com.example.enums.AppLanguage;
import com.example.exp.AppBadException;
import com.example.repository.ArticleTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
public class ArticleTypeService {
    @Autowired
    private ArticleTypeRepository articleTypeRepository;

    public ArticleTypeDTO create(ArticleTypeDTO dto) {
        if (dto.getOrder_number() == null || dto.getName_uz() == null
                || dto.getName_ru() == null || dto.getName_en() == null) {
            throw new AppBadException("uz , ru, en and one of the fields named orderNumber came out empty!");
        }

        ArticleTypeEntity entity = getEntity(dto);
        ArticleTypeEntity save = articleTypeRepository.save(entity);
        return getDTO(save);
    }

    public boolean update(Integer id, ArticleTypeDTO dto) {
        Optional<ArticleTypeEntity> optional = articleTypeRepository.findById(id);
        if (optional.isEmpty() || !optional.get().getVisible()) {
            throw new AppBadException("article type with this id was not found!");
        }
        ArticleTypeEntity entity = optional.get();
        entity.setNameEn(dto.getName_en());
        entity.setNameRu(dto.getName_ru());
        entity.setNameEn(dto.getName_en());
        entity.setOrderNumber(dto.getOrder_number());
        entity.setUpdatedDate(LocalDateTime.now());
        articleTypeRepository.save(entity);
        return true;
    }

    public boolean deleteById(Integer id) {
        Optional<ArticleTypeEntity> optional = articleTypeRepository.findById(id);
        if (optional.isEmpty() || !optional.get().getVisible()) {
            throw new AppBadException("article type with this id was not found!");
        }
        ArticleTypeEntity entity = optional.get();
        entity.setVisible(false);
        articleTypeRepository.save(entity);
        return true;
    }

    public PageImpl<ArticleTypeDTO> getPagination(Integer page, Integer size) {
        Sort sort = Sort.by(Sort.Direction.DESC, "createdDate");
        Pageable pageable = PageRequest.of(page - 1, size, sort);
        Page<ArticleTypeEntity> all = articleTypeRepository.findAll(pageable);
        long totalElements = all.getTotalElements();
        List<ArticleTypeEntity> content = all.getContent();
        List<ArticleTypeDTO> dtoList = new LinkedList<>();
        for (ArticleTypeEntity entity : content) {
            if (entity.getVisible()) {
                dtoList.add(getDTO(entity));
            }
        }
        return new PageImpl<>(dtoList, pageable, totalElements);
    }

    public List<ArticleTypeDTO> getByLang(AppLanguage lang) {
        Iterable<ArticleTypeEntity> all = articleTypeRepository.findAll();
        List<ArticleTypeDTO> list = new LinkedList<>();
        for (ArticleTypeEntity entity : all) {
            if (entity.getVisible()) {
                ArticleTypeDTO dto = new ArticleTypeDTO();
                dto.setId(entity.getId());
                switch (lang) {
                    case UZ -> dto.setName(entity.getNameUz());
                    case RU -> dto.setName_ru(entity.getNameRu());
                    default -> dto.setName(entity.getNameEn());
                }
                list.add(dto);
            }
        }
        return list;
    }


    private ArticleTypeEntity getEntity(ArticleTypeDTO dto) {
        ArticleTypeEntity entity = new ArticleTypeEntity();
        entity.setNameUz(dto.getName_uz());
        entity.setNameRu(dto.getName_ru());
        entity.setNameEn(dto.getName_en());
        entity.setOrderNumber(dto.getOrder_number());
        return entity;
    }

    private ArticleTypeDTO getDTO(ArticleTypeEntity entity) {
        ArticleTypeDTO dto = new ArticleTypeDTO();
        dto.setId(entity.getId());
        dto.setOrder_number(entity.getOrderNumber());
        dto.setName_uz(entity.getNameUz());
        dto.setName_ru(entity.getNameRu());
        dto.setName_en(entity.getNameEn());
        dto.setVisible(entity.getVisible());
        dto.setCreatedDate(entity.getCreatedDate());
        return dto;
    }


}
