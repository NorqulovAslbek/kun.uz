package com.example.service;

import com.example.dto.CategoryDTO;
import com.example.dto.CreateCategoryDTO;
import com.example.entity.CategoryEntity;
import com.example.enums.AppLanguage;
import com.example.exp.AppBadException;
import com.example.repository.CategoryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    public CategoryDTO create(CreateCategoryDTO dto) {
        CategoryEntity categoryEntity = categoryRepository.save(toEntity(dto));
        return toDTO(categoryEntity);
    }

    public CategoryDTO update(Integer id, CreateCategoryDTO dto) {
        Optional<CategoryEntity> optional = categoryRepository.findById(id);
        if (optional.isEmpty() || !optional.get().getVisible()) {
            log.warn("category not fount {}", dto);
            throw new AppBadException("category not found!");
        }
        CategoryEntity categoryEntity = optional.get();
        if (dto.getOrder_number() != null) {
            categoryEntity.setOrderNumber(dto.getOrder_number());
        }
        if (dto.getName_uz() != null) {
            categoryEntity.setNameUz(dto.getName_uz());
        }
        if (dto.getName_ru() != null) {
            categoryEntity.setNameRu(dto.getName_ru());
        }
        if (dto.getName_en() != null) {
            categoryEntity.setNameEn(dto.getName_en());
        }
        return toDTO(categoryRepository.save(categoryEntity));
    }


    public Boolean delete(Integer id) {
        Optional<CategoryEntity> optional = categoryRepository.findById(id);
        if (optional.isEmpty() || !optional.get().getVisible()) {
            log.warn("category not fount by id {}", id);
            throw new AppBadException("category not found!");
        }
        CategoryEntity categoryEntity = optional.get();
        categoryEntity.setVisible(false);
        categoryRepository.save(categoryEntity);
        return true;
    }

    public List<CategoryDTO> getAll() {
        List<CategoryEntity> all = categoryRepository.getAll();
        List<CategoryDTO> categoryDTOS = new LinkedList<>();
        for (CategoryEntity categoryEntity : all) {
            categoryDTOS.add(toAllDTO(categoryEntity));
        }
        return categoryDTOS;
    }

    public List<CategoryDTO> getLang(AppLanguage lang) {
        Iterable<CategoryEntity> all = categoryRepository.findAll();
        List<CategoryDTO> list = new LinkedList<>();
        for (CategoryEntity entity : all) {
            if (entity.getVisible()) {
                CategoryDTO dto = new CategoryDTO();
                dto.setId(entity.getId());
                dto.setOrder_number(entity.getOrderNumber());
                switch (lang) {
                    case UZ -> dto.setName(entity.getNameUz());
                    case RU -> dto.setName(entity.getNameRu());
                    default -> dto.setName(entity.getNameEn());
                }
                list.add(dto);
            }
        }
        return list;

    }

    private CategoryEntity toEntity(CreateCategoryDTO dto) {
        CategoryEntity entity = new CategoryEntity();
        entity.setNameEn(dto.getName_en());
        entity.setNameUz(dto.getName_uz());
        entity.setNameRu(dto.getName_ru());
        entity.setOrderNumber(dto.getOrder_number());
        return entity;
    }

    private CategoryDTO toDTO(CategoryEntity entity) {
        CategoryDTO dto = new CategoryDTO();
        dto.setName_uz(entity.getNameUz());
        dto.setName_ru(entity.getNameRu());
        dto.setName_en(entity.getNameEn());
        dto.setOrder_number(entity.getOrderNumber());
        return dto;
    }

    private CategoryDTO toAllDTO(CategoryEntity entity) {
        CategoryDTO dto = new CategoryDTO();
        dto.setId(entity.getId());
        dto.setName_uz(entity.getNameUz());
        dto.setName_ru(entity.getNameRu());
        dto.setName_en(entity.getNameEn());
        dto.setOrder_number(entity.getOrderNumber());
        dto.setVisible(entity.getVisible());
        dto.setCreated_date(entity.getCreatedDate());
        return dto;
    }

}
