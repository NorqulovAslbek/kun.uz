package com.example.controller;

import com.example.dto.RegionDTO;
import com.example.enums.AppLanguage;
import com.example.service.RegionService;
import com.example.util.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/region")
public class RegionController {
    @Autowired
    private RegionService regionService;

    @PostMapping("")
    public ResponseEntity<RegionDTO> create(@RequestBody RegionDTO dto,
                                            @RequestHeader(value = "Authorization") String jwt) {
        return JWTUtil.checkRole(jwt) ? ResponseEntity.ok(regionService.create(dto)) :
                ResponseEntity.status(HttpStatus.FORBIDDEN).build();

    }

    @PutMapping("/update")
    public ResponseEntity<?> update(@RequestParam(value = "id") Integer id, @RequestBody RegionDTO dto,
                                    @RequestHeader(value = "Authorization", required = true) String jwt) {
        return JWTUtil.checkRole(jwt) ? ResponseEntity.ok(regionService.update(id, dto)) :
                ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> delete(@RequestParam Integer id, @RequestHeader(value = "Authorization") String jwt) {
        return JWTUtil.checkRole(jwt) ?  ResponseEntity.ok(regionService.delete(id)) :
                ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @GetMapping("")
    public ResponseEntity<List<RegionDTO>> getAll(@RequestHeader(value = "Authorization") String jwt) {
        return JWTUtil.checkRole(jwt) ? ResponseEntity.ok(regionService.getAll()) :
                ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @GetMapping("/lang")
    public ResponseEntity<List<RegionDTO>> getByLang(@RequestParam(value = "lang", defaultValue = "UZ") AppLanguage lang) {
        return ResponseEntity.ok(regionService.getByLang(lang));
    }

}
