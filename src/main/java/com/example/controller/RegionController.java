package com.example.controller;

import com.example.dto.RegionDTO;
import com.example.enums.AppLanguage;
import com.example.enums.ProfileRole;
import com.example.service.RegionService;
import com.example.util.HttpRequestUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
    @PostMapping("/adm")
    public ResponseEntity<RegionDTO> create(@RequestBody RegionDTO dto, HttpServletRequest request) {
        log.info("create region {}", dto.getName());
        HttpRequestUtil.getProfileId(request, ProfileRole.ADMIN, ProfileRole.MODERATOR); //region create qilyotgan odam adminlikk TEKSHIRIB BERADI.
        return ResponseEntity.ok(regionService.create(dto));

    }

    @Operation(summary = "Api for update", description = "this api used for update region by id")
    @PutMapping("/adm/update")
    public ResponseEntity<?> update(@RequestParam(value = "id") Integer id, @RequestBody RegionDTO dto,
                                    HttpServletRequest request) {
        log.info("update region by id {}", id);
        HttpRequestUtil.getProfileId(request, ProfileRole.ADMIN);
        return ResponseEntity.ok(regionService.update(id, dto));
    }

    @Operation(summary = "Api for delete", description = "this api used for delete region by id")
    @DeleteMapping("/adm/delete")
    public ResponseEntity<?> delete(@RequestParam Integer id, HttpServletRequest request) {
        log.info("delete region by id {}", id);
        HttpRequestUtil.getProfileId(request, ProfileRole.ADMIN);
        return ResponseEntity.ok(regionService.delete(id));
    }

    @Operation(summary = "Api for getAll", description = "this api to get all regions ")
    @GetMapping("/adm")
    public ResponseEntity<List<RegionDTO>> getAll(HttpServletRequest request) {
        log.info("get all region");
        HttpRequestUtil.getProfileId(request, ProfileRole.ADMIN);
        return ResponseEntity.ok(regionService.getAll());
    }

    @Operation(summary = "Api for getByLang", description = "this api to get all regions by language")
    @GetMapping("/lang")
    public ResponseEntity<List<RegionDTO>> getByLang(@RequestParam(value = "lang", defaultValue = "UZ") AppLanguage lang,
                                                     HttpServletRequest request) {
        log.info("get region by lang {}", lang);
        HttpRequestUtil.getProfileId(request);
        return ResponseEntity.ok(regionService.getByLang(lang));
    }

}
