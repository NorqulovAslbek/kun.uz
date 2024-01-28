package com.example.controller;

import com.example.dto.RegionDTO;
import com.example.enums.AppLanguage;
import com.example.enums.ProfileRole;
import com.example.service.RegionService;
import com.example.util.HttpRequestUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/region")
public class RegionController {
    @Autowired
    private RegionService regionService;

    @PostMapping("/adm")
    public ResponseEntity<RegionDTO> create(@RequestBody RegionDTO dto, HttpServletRequest request) {
        HttpRequestUtil.getProfileId(request, ProfileRole.ADMIN, ProfileRole.MODERATOR); //region create qilyotgan odam adminlikk TEKSHIRIB BERADI.
        return ResponseEntity.ok(regionService.create(dto));

    }

    @PutMapping("/adm/update")
    public ResponseEntity<?> update(@RequestParam(value = "id") Integer id, @RequestBody RegionDTO dto,
                                    HttpServletRequest request) {
        HttpRequestUtil.getProfileId(request, ProfileRole.ADMIN);
        return ResponseEntity.ok(regionService.update(id, dto));
    }

    @DeleteMapping("/adm/delete")
    public ResponseEntity<?> delete(@RequestParam Integer id, HttpServletRequest request) {
        HttpRequestUtil.getProfileId(request, ProfileRole.ADMIN);
        return ResponseEntity.ok(regionService.delete(id));
    }

    @GetMapping("/adm")
    public ResponseEntity<List<RegionDTO>> getAll(HttpServletRequest request) {
        HttpRequestUtil.getProfileId(request,ProfileRole.ADMIN);
        return ResponseEntity.ok(regionService.getAll());
    }

    @GetMapping("/lang")
    public ResponseEntity<List<RegionDTO>> getByLang(@RequestParam(value = "lang", defaultValue = "UZ") AppLanguage lang,
                                                     HttpServletRequest request) {
        HttpRequestUtil.getProfileId(request);
        return ResponseEntity.ok(regionService.getByLang(lang));
    }

}
