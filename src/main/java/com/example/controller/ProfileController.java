package com.example.controller;

import com.example.dto.ProfileDTO;
import com.example.dto.UserDTO;
import com.example.enums.ProfileRole;
import com.example.service.ProfileService;
import com.example.util.HttpRequestUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Profile Api list", description = "Api list for Profile")
@Slf4j
@RestController
@RequestMapping("/profile")
public class ProfileController {
    @Autowired
    private ProfileService profileService;
    @Operation(summary = "Api for create",description = "this api used for create profile")
    @PostMapping("/adm")
    public ResponseEntity<?> create(@RequestBody ProfileDTO dto, HttpServletRequest request) {
        log.info("create admin{}", dto.getPhone());
        HttpRequestUtil.getProfileId(request, ProfileRole.ADMIN);
        return ResponseEntity.ok(profileService.create(dto));
    }

    @Operation(summary = "Api for updateAdmin",description = "this api used for update profile by id admin")
    @PutMapping("/adm/update")//admin
    public ResponseEntity<?> updateAdmin(@RequestParam Integer id,
                                         @RequestBody ProfileDTO dto,
                                         HttpServletRequest request) {
        log.info("update admin{}", dto.getPhone());
        HttpRequestUtil.getProfileId(request, ProfileRole.ADMIN);
        return ResponseEntity.ok(profileService.updateAdmin(id, dto));
    }
    @Operation(summary = "Api for updateProfile",description = "this api used for update profile by id user")
    @PutMapping("/update") // user
    public ResponseEntity<?> updateProfile(@RequestBody UserDTO dto,
                                           HttpServletRequest request) {
        log.info("update profile{}", dto.getPhone());
        Integer profileId = HttpRequestUtil.getProfileId(request);
        return ResponseEntity.ok(profileService.updateProfile(profileId, dto));
    }
    @Operation(summary = "Api for delete",description = "this api used for delete profile by id")
    @DeleteMapping("/adm/delete")
    public ResponseEntity<?> delete(@RequestParam Integer id, HttpServletRequest request) {
        log.info("delete profile by id {}", id);
        HttpRequestUtil.getProfileId(request, ProfileRole.ADMIN);
        return ResponseEntity.ok(profileService.delete(id));
    }
    @Operation(summary = "Api for getAll",description = "this api to get all profile")
    @GetMapping("/adm/all")
    public ResponseEntity<?> getAll(@RequestParam Integer page, @RequestParam Integer size,
                                    HttpServletRequest request) {
        log.info("get all profile");
        HttpRequestUtil.getProfileId(request, ProfileRole.ADMIN);
        return ResponseEntity.ok(profileService.getAll(page, size));
    }
    @Operation(summary = "Api for filter",description = "this api is to filter all profile ")
    @GetMapping("/adm/filter")
    public ResponseEntity<?> filter(@RequestBody ProfileDTO dto, @RequestParam Integer page
            , @RequestParam Integer size, HttpServletRequest request) {
        log.info("get profile filter");
        HttpRequestUtil.getProfileId(request, ProfileRole.ADMIN);
        return ResponseEntity.ok(profileService.filter(dto, page, size));
    }

}
