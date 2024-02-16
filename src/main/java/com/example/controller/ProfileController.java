package com.example.controller;

import com.example.dto.CreateProfileDTO;
import com.example.dto.ProfileDTO;
import com.example.dto.UserDTO;
import com.example.service.ProfileService;
import com.example.util.SpringSecurityUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Profile Api list", description = "Api list for Profile")
@Slf4j
@RestController
@RequestMapping("/profile")
public class ProfileController {
    @Autowired
    private ProfileService profileService;

    @Operation(summary = "Api for create", description = "this api used for create profile")
    @PostMapping("/adm")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> create(@RequestBody CreateProfileDTO dto) {
        log.info("create admin{}", dto.getPhone());

        return ResponseEntity.ok(profileService.create(dto));
    }

    @Operation(summary = "Api for updateAdmin", description = "this api used for update profile by id admin")
    @PutMapping("/adm/update")//admin
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateAdmin(@RequestParam Integer id,
                                         @RequestBody CreateProfileDTO dto) {
        log.info("update admin{}", dto.getPhone());
        return ResponseEntity.ok(profileService.updateAdmin(id, dto));
    }

    @Operation(summary = "Api for updateProfile", description = "this api used for update profile by id user")
    @PutMapping("/update") // user
    @PreAuthorize("hasAnyRole('USER','PABLISHER','MODERATOR')")
    public ResponseEntity<?> updateProfile(@RequestBody UserDTO dto) {
        log.info("update profile{}", dto.getPhone());
        Integer profileId = SpringSecurityUtil.getCurrentUser().getId();
        return ResponseEntity.ok(profileService.updateProfile(profileId, dto));
    }

    @Operation(summary = "Api for delete", description = "this api used for delete profile by id")
    @DeleteMapping("/adm/delete")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> delete(@RequestParam Integer id) {
        log.info("delete profile by id {}", id);
        return ResponseEntity.ok(profileService.delete(id));
    }

    @Operation(summary = "Api for getAll", description = "this api to get all profile")
    @GetMapping("/adm/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getAll(@RequestParam Integer page, @RequestParam Integer size) {
        log.info("get all profile");
        return ResponseEntity.ok(profileService.getAll(page, size));
    }

    @Operation(summary = "Api for filter", description = "this api is to filter all profile ")
    @GetMapping("/adm/filter")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> filter(@RequestBody ProfileDTO dto, @RequestParam Integer page
            , @RequestParam Integer size) {
        log.info("get profile filter");
        return ResponseEntity.ok(profileService.filter(dto, page, size));
    }

}
