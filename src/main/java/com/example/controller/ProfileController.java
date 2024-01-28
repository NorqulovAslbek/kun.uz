package com.example.controller;

import com.example.dto.JwtDTO;
import com.example.dto.ProfileDTO;
import com.example.dto.UserDTO;
import com.example.enums.ProfileRole;
import com.example.service.ProfileService;
import com.example.util.HttpRequestUtil;
import com.example.util.JWTUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/profile")
public class ProfileController {
    @Autowired
    private ProfileService profileService;

    @PostMapping("/adm")
    public ResponseEntity<?> create(@RequestBody ProfileDTO dto, HttpServletRequest request) {
        HttpRequestUtil.getProfileId(request, ProfileRole.ADMIN);
        return ResponseEntity.ok(profileService.create(dto));
    }


    @PutMapping("/adm/update")//admin
    public ResponseEntity<?> updateAdmin(@RequestParam Integer id,
                                         @RequestBody ProfileDTO dto,
                                         HttpServletRequest request) {
        HttpRequestUtil.getProfileId(request, ProfileRole.ADMIN);
        return ResponseEntity.ok(profileService.updateAdmin(id, dto));
    }

    @PutMapping("/update") // user
    public ResponseEntity<?> updateProfile(@RequestBody UserDTO dto,
                                           HttpServletRequest request) {
        Integer profileId = HttpRequestUtil.getProfileId(request);
        return ResponseEntity.ok(profileService.updateProfile(profileId, dto));
    }

    @DeleteMapping("/adm/delete")
    public ResponseEntity<?> delete(@RequestParam Integer id, HttpServletRequest request) {
        HttpRequestUtil.getProfileId(request, ProfileRole.ADMIN);
        return ResponseEntity.ok(profileService.delete(id));
    }

    @GetMapping("/adm/all")
    public ResponseEntity<?> getAll(@RequestParam Integer page, @RequestParam Integer size,
                                    HttpServletRequest request) {
        HttpRequestUtil.getProfileId(request, ProfileRole.ADMIN);
        return ResponseEntity.ok(profileService.getAll(page, size));
    }

    @GetMapping("/adm/filter")
    public ResponseEntity<?> filter(@RequestBody ProfileDTO dto, @RequestParam Integer page
            , @RequestParam Integer size, HttpServletRequest request) {
         HttpRequestUtil.getProfileId(request,ProfileRole.ADMIN);
        return ResponseEntity.ok(profileService.filter(dto, page, size));
    }

}
