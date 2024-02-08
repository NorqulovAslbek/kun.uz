package com.example.service;

import com.example.dto.PaginationResultDTO;
import com.example.dto.ProfileDTO;
import com.example.dto.UserDTO;
import com.example.entity.ProfileEntity;
import com.example.exp.AppBadException;
import com.example.repository.ProfileFilterRepository;
import com.example.repository.ProfileRepository;
import com.example.util.JWTUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
@Slf4j
@Service
public class ProfileService {
    @Autowired
    private ProfileRepository profileRepository;
    @Autowired
    private ProfileFilterRepository repository;


    public ProfileDTO create(ProfileDTO dto) {
        if (profileRepository.findByEmail(dto.getEmail()).isEmpty()) {
            ProfileEntity save = profileRepository.save(toEntity(dto));
            dto.setJwt(JWTUtil.encode(save.getId(), dto.getRole()));
            return dto;
        }
        log.warn("A user with this email already exists{}",dto);
        throw new AppBadException("A user with this email already exists!");
    }

    public boolean updateAdmin(Integer id, ProfileDTO dto) {
        Optional<ProfileEntity> optional = profileRepository.findById(id);
        if (optional.isEmpty() || !optional.get().getVisible()) {
            log.warn("profile with such id does not exist{}",dto);
            throw new AppBadException("profile with such id does not exist!");
        }
        ProfileEntity entity = optional.get();
        if (dto.getName() != null) {
            entity.setName(dto.getName());
        }
        if (dto.getSurname() != null) {
            entity.setSurname(dto.getSurname());
        }
        if (dto.getEmail() != null) {
            entity.setEmail(dto.getEmail());
        }
        if (dto.getPhone() != null) {
            entity.setPhone(dto.getPhone());
        }
        if (dto.getPassword() != null) {
            entity.setPassword(dto.getPassword());
        }
        if (dto.getRole() != null) {
            entity.setRole(dto.getRole());
        }
        if (dto.getStatus() != null) {
            entity.setStatus(dto.getStatus());
        }
        entity.setUpdatedDate(LocalDateTime.now());
        profileRepository.save(entity);
        return true;

    }

    public boolean updateProfile(Integer id, UserDTO dto) {
        Optional<ProfileEntity> optional = profileRepository.findById(id);
        if (optional.isEmpty() || !optional.get().getVisible()) {
            log.warn("user with this id does not exist! {}",dto);
            throw new AppBadException("user with this id does not exist!");
        }
        ProfileEntity entity = optional.get();
        if (dto.getName() != null) {
            entity.setName(dto.getName());
        }
        if (dto.getSurname() != null) {
            entity.setSurname(dto.getSurname());
        }
        if (dto.getEmail() != null) {
            entity.setEmail(dto.getEmail());
        }
        if (dto.getPhone() != null) {
            entity.setPhone(dto.getPhone());
        }
        if (dto.getPassword() != null) {
            entity.setPassword(dto.getPassword());
        }
        entity.setUpdatedDate(LocalDateTime.now());
        profileRepository.save(entity);
        return true;
    }

    public PageImpl<ProfileDTO> getAll(Integer page,Integer size){
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<ProfileEntity> all = profileRepository.findAll(pageable);
        List<ProfileEntity> content = all.getContent();
        long totalElements = all.getTotalElements();
        List<ProfileDTO> list = new LinkedList<>();
        for (ProfileEntity entity : content) {
            list.add(toDTO(entity));
        }
        return new PageImpl<>(list, pageable, totalElements);
    }

    public boolean delete(Integer id){
        Optional<ProfileEntity> optional = profileRepository.findById(id);
        if (optional.isPresent() && optional.get().getVisible()){
            optional.get().setVisible(false);
            ProfileEntity entity = optional.get();
            profileRepository.save(entity);
            return true;
        }
        log.warn("profile with such id does not exist!{}",id);
        throw new AppBadException("profile with such id does not exist!");
    }

    public PageImpl<ProfileDTO> filter(ProfileDTO dto, Integer page, Integer size) {
        PaginationResultDTO<ProfileEntity> filter = repository.filter(dto, page, size);
        List<ProfileDTO> list = new LinkedList<>();
        for (ProfileEntity studentEntity : filter.getList()) {
            list.add(toDTO(studentEntity));
        }
        Pageable pageable = PageRequest.of(page - 1, size, Sort.Direction.ASC, "createdDate");
        return new PageImpl<>(list, pageable, filter.getTotalSize());
    }



    private ProfileEntity toEntity(ProfileDTO dto) {
        ProfileEntity entity = new ProfileEntity();
        entity.setName(dto.getName());
        entity.setSurname(dto.getSurname());
        entity.setEmail(dto.getEmail());
        entity.setPhone(dto.getPhone());
        entity.setPassword(dto.getPassword());
        entity.setRole(dto.getRole());
        entity.setStatus(dto.getStatus());
        return entity;
    }

    private ProfileDTO toDTO(ProfileEntity entity) {
        ProfileDTO dto = new ProfileDTO();
        dto.setName(entity.getName());
        dto.setSurname(entity.getSurname());
        dto.setEmail(entity.getEmail());
        dto.setPhone(entity.getPhone());
        dto.setPassword(entity.getPassword());
        dto.setStatus(entity.getStatus());
        dto.setRole(entity.getRole());
        return dto;
    }


}
