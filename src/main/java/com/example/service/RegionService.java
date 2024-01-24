package com.example.service;

import com.example.dto.ArticleTypeDTO;
import com.example.dto.RegionDTO;
import com.example.entity.ArticleTypeEntity;
import com.example.entity.RegionEntity;
import com.example.exp.AppBadException;
import com.example.repository.RegionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
public class RegionService {
    @Autowired
    private RegionRepository regionRepository;

    public RegionDTO create(RegionDTO dto) {
        if (dto.getOrder_number() == null || dto.getName_uz() == null
                || dto.getName_ru() == null || dto.getName_en() == null) {
            throw new AppBadException("uz , ru, en and one of the fields named orderNumber came out empty!");
        }
        RegionEntity save = regionRepository.save(getEntity(dto));
        return getDTO(save);
    }

    public RegionDTO update(Integer id, RegionDTO dto) {
        if (!regionRepository.existsById(id)) {
            throw new AppBadException("region with this id was not found!");
        }
        Optional<RegionEntity> optional = regionRepository.findById(id);
        RegionEntity entity = optional.get();
        entity.setNameEn(dto.getName_en());
        entity.setNameUz(dto.getName_uz());
        entity.setNameRu(dto.getName_ru());
        return getDTO(entity);
    }

    public boolean delete(Integer id) {
        if (!regionRepository.existsById(id)) {
            throw new AppBadException("region with this id was not found!");
        }
        regionRepository.deleteById(id);
        return true;
    }

    public List<RegionDTO> getAll() {
        Iterable<RegionEntity> all = regionRepository.findAll();
        List<RegionDTO> list = new LinkedList<>();
        for (RegionEntity entity : all) {
            list.add(getDTO(entity));
        }
        return list;
    }

    public List<RegionDTO> getByLang(String lang) {
        Iterable<RegionEntity> all = regionRepository.findAll();
        List<RegionDTO> list = new LinkedList<>();
        for (RegionEntity entity : all) {
            RegionDTO dto = new RegionDTO();
            dto.setId(entity.getId());
            switch (lang) {
                case "en" -> dto.setName_en(entity.getNameEn());
                case "ru" -> dto.setName_ru(entity.getNameRu());
                default -> dto.setName_uz(entity.getNameUz());
            }
            list.add(dto);
        }
        return list;
    }


    private RegionEntity getEntity(RegionDTO dto) {
        RegionEntity entity = new RegionEntity();
        entity.setNameUz(dto.getName_uz());
        entity.setNameRu(dto.getName_ru());
        entity.setNameEn(dto.getName_en());
        entity.setOrderNumber(dto.getOrder_number());
        return entity;
    }

    private RegionDTO getDTO(RegionEntity entity) {
        RegionDTO dto = new RegionDTO();
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
