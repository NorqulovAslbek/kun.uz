package com.example.controller;

import com.example.dto.JwtDTO;
import com.example.dto.ProfileDTO;
import com.example.dto.UserDTO;
import com.example.enums.ProfileRole;
import com.example.service.ProfileService;
import com.example.util.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/profile")
public class ProfileController {
    @Autowired
    private ProfileService profileService;

    @PostMapping("")
    public ResponseEntity<?> create(@RequestBody ProfileDTO dto, @RequestHeader("Authorization") String jwt) {
        return JWTUtil.checkRole(jwt) ? ResponseEntity.ok(profileService.create(dto)) :
                ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }


    @PutMapping("/updateAdmin")
    public ResponseEntity<?> updateAdmin(@RequestParam Integer id,
                                         @RequestBody ProfileDTO dto,
                                         @RequestHeader("Authorization") String jwt) {
        return JWTUtil.checkRole(jwt) ? ResponseEntity.ok(profileService.updateAdmin(id, dto)) :
                ResponseEntity.status(HttpStatus.FORBIDDEN).build();

    }

    @PutMapping("/updateUser")
    public ResponseEntity<?> updateProfile(@RequestBody UserDTO dto,
                                           @RequestHeader("Authorization") String jwt) {
        return ResponseEntity.ok(profileService.updateProfile(JWTUtil.decode(jwt).getId(), dto));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> delete(@RequestParam Integer id, @RequestHeader("Authorization") String jwt) {
        JwtDTO jwtDTO = JWTUtil.decode(jwt);
        ProfileRole role = jwtDTO.getRole();
        if (role.equals(ProfileRole.ADMIN)) {
            return ResponseEntity.ok(profileService.delete(id));
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAll(@RequestParam Integer page, @RequestParam Integer size,
                                    @RequestHeader("Authorization") String jwt) {
        return JWTUtil.checkRole(jwt) ? ResponseEntity.ok(profileService.getAll(page, size)) :
                ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @GetMapping("/filter")
    public ResponseEntity<?> filter(@RequestBody ProfileDTO dto, @RequestParam Integer page
            , @RequestParam Integer size, @RequestHeader("Authorization") String jwt) {
        return JWTUtil.checkRole(jwt) ? ResponseEntity.ok(profileService.filter(dto, page, size)) :
                ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

}
