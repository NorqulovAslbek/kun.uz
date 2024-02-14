package com.example.controller;

import com.example.dto.RegionDTO;
import com.example.enums.AppLanguage;
import com.example.service.RegionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Region Api list", description = "Api list for Region")
@Slf4j
@RestController
@RequestMapping("/region")
public class RegionController {
    @Autowired
    private RegionService regionService;

    @Operation(summary = "Api for create", description = "this api used for create region ")
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/adm")
    public ResponseEntity<RegionDTO> create(@RequestBody RegionDTO dto) {
        log.info("create region {}", dto.getName());
        return ResponseEntity.ok(regionService.create(dto));

    }

    @Operation(summary = "Api for update", description = "this api used for update region by id")
    @PutMapping("/adm/update/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> update(@PathVariable("id") Integer id, @RequestBody RegionDTO dto) {
        log.info("update region by id {}", id);
        return ResponseEntity.ok(regionService.update(id, dto));
    }

    @Operation(summary = "Api for delete", description = "this api used for delete region by id")
    @DeleteMapping("/adm/delete")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> delete(@RequestParam Integer id) {
        log.info("delete region by id {}", id);
        return ResponseEntity.ok(regionService.delete(id));
    }

    @Operation(summary = "Api for getAll", description = "this api to get all regions ")
    @GetMapping("/adm")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<RegionDTO>> getAll() {
        log.info("get all region");
        return ResponseEntity.ok(regionService.getAll());
    }

    @Operation(summary = "Api for getByLang", description = "this api to get all regions by language")
    @GetMapping("/lang")
    public ResponseEntity<List<RegionDTO>> getByLang(@RequestParam(value = "lang", defaultValue = "UZ") AppLanguage lang) {
        log.info("get region by lang {}", lang);
        return ResponseEntity.ok(regionService.getByLang(lang));
    }

}
