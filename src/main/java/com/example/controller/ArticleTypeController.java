package com.example.controller;

import com.example.dto.ArticleTypeDTO;
import com.example.enums.AppLanguage;
import com.example.enums.ProfileRole;
import com.example.service.ArticleTypeService;
import com.example.util.HttpRequestUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "ArticleType Api list", description = "Api list for ArticleType")
@Slf4j
@RestController
@RequestMapping("/articleType")
public class ArticleTypeController {
    @Autowired
    private ArticleTypeService articleTypeService;

    @Operation(summary = "Api for create", description = "this api used for create ArticleType")
    @PostMapping("/adm")
    public ResponseEntity<ArticleTypeDTO> create(@RequestBody ArticleTypeDTO dto,
                                                 HttpServletRequest request) {
        log.info("create articleType {}", dto);
        HttpRequestUtil.getProfileId(request, ProfileRole.ADMIN);
        return ResponseEntity.ok(articleTypeService.create(dto));
    }

    @Operation(summary = "Api for updateById", description = "this api used for update by id ArticleType")
    @PutMapping("/adm/update")
    public ResponseEntity<?> updateById(@RequestParam("id") Integer id,
                                        @RequestBody ArticleTypeDTO dto,
                                        HttpServletRequest request) {
        log.info("update articleType by id {}", dto);
        HttpRequestUtil.getProfileId(request, ProfileRole.ADMIN);
        return ResponseEntity.ok(articleTypeService.update(id, dto));
    }

    @Operation(summary = "Api for delete", description = "this api used for delete by id ArticleType")
    @DeleteMapping("/adm/delete")
    public ResponseEntity<?> delete(@RequestParam("id") Integer id, HttpServletRequest request) {
        log.info("delete articleType by id {}", id);
        HttpRequestUtil.getProfileId(request, ProfileRole.ADMIN);
        return ResponseEntity.ok(articleTypeService.deleteById(id));
    }

    @Operation(summary = "Api for getPagination", description = "this api used for get all article type by pagination")
    @GetMapping("/admin/pagination")
    public ResponseEntity<PageImpl<ArticleTypeDTO>> getPagination(@RequestParam("page") Integer page,
                                                                  @RequestParam("size") Integer size,
                                                                  HttpServletRequest request) {
        log.info("get articleType pagination");
        HttpRequestUtil.getProfileId(request, ProfileRole.ADMIN);
        return ResponseEntity.ok(articleTypeService.getPagination(page, size));
    }

    @Operation(summary = "Api for getByLang", description = "this api used for get all article type by lang")
    @GetMapping("/lang")
    public ResponseEntity<List<ArticleTypeDTO>> getByLang(@RequestParam("lang") AppLanguage lang, HttpServletRequest request) {
        log.info("get articleType by lang {}", lang);
        HttpRequestUtil.getProfileId(request);
        return ResponseEntity.ok(articleTypeService.getByLang(lang));
    }

}
