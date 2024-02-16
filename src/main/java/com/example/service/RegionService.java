package com.example.service;

import com.example.dto.CreateRegionDTO;
import com.example.dto.RegionDTO;
import com.example.entity.RegionEntity;
import com.example.enums.AppLanguage;
import com.example.exp.AppBadException;
import com.example.repository.RegionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
@Slf4j
@Service
public class RegionService {
    @Autowired
    private RegionRepository regionRepository;

    public RegionDTO create(CreateRegionDTO dto) {
        if (dto.getOrder_number() == null || dto.getName_uz() == null
                || dto.getName_ru() == null || dto.getName_en() == null) {
            log.warn("uz,ru,en and one of the fields named orderNumber came out empty!{}",dto);
            throw new AppBadException("uz , ru, en and one of the fields named orderNumber came out empty!");
        }
        RegionEntity save = regionRepository.save(getEntity(dto));
        return getDTO(save);
    }

    public RegionDTO update(Integer id, CreateRegionDTO dto) {
        Optional<RegionEntity> optional = regionRepository.findById(id);
        if (optional.isEmpty() || optional.get().getVisible().equals(false)) {
            log.warn("region with this id was not found {}",dto);
            throw new AppBadException("region with this id was not found!");
        }
        RegionEntity entity = optional.get();
        if (entity.getVisible().equals(true)) {
            entity.setNameEn(dto.getName_en());
            entity.setNameUz(dto.getName_uz());
            entity.setNameRu(dto.getName_ru());
            entity.setUpdatedDate(LocalDateTime.now());
        }
        return getDTO(entity);
    }

    public boolean delete(Integer id) {
        Optional<RegionEntity> optional = regionRepository.findById(id);
        if (optional.isEmpty()||!optional.get().getVisible()) {
            log.warn("region with this id was not found {}",id);
            throw new AppBadException("region with this id was not found!");
        }
        RegionEntity entity = optional.get();
        entity.setVisible(false);
        regionRepository.save(entity);
        return true;
    }

    public List<RegionDTO> getAll() {
        Iterable<RegionEntity> all = regionRepository.findAll();
        List<RegionDTO> list = new LinkedList<>();
        for (RegionEntity entity : all) {
            if (entity.getVisible()) {
                list.add(getDTO(entity));
            }
        }
        return list;
    }

    public List<RegionDTO> getByLang(AppLanguage lang) {
        Iterable<RegionEntity> all = regionRepository.findAll();
        List<RegionDTO> list = new LinkedList<>();
        for (RegionEntity entity : all) {
            if (entity.getVisible()) {
                RegionDTO dto = new RegionDTO();
                dto.setId(entity.getId());
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


    private RegionEntity getEntity(CreateRegionDTO dto) {
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
