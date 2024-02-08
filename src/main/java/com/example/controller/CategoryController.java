package com.example.controller;

import com.example.dto.CreateCategoryDTO;
import com.example.enums.AppLanguage;
import com.example.enums.ProfileRole;
import com.example.service.CategoryService;
import com.example.util.HttpRequestUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Category Api list", description = "Api list for Category")
@Slf4j
@RestController
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @Operation(summary = "Api for create", description = "this api used for create category")
    @PostMapping("/adm")
    public ResponseEntity<?> create(@RequestBody CreateCategoryDTO dto, HttpServletRequest request) {
        log.info("create category {}", dto);
        Integer profileId = HttpRequestUtil.getProfileId(request, ProfileRole.ADMIN);//agar admin bolsa category qo'sha oladi bu methodlar shuni check qilib beradi
        return ResponseEntity.ok(categoryService.create(dto));
    }

    @Operation(summary = "Api for update", description = "this api used for update category")
    @PutMapping("/adm/{id}")
    public ResponseEntity<?> update(@RequestBody CreateCategoryDTO dto, @PathVariable Integer id,
                                    HttpServletRequest request) {
        log.info("update category by id {}", id);
        HttpRequestUtil.getProfileId(request, ProfileRole.ADMIN);
        return ResponseEntity.ok(categoryService.update(id, dto));
    }
    @Operation(summary = "Api for delete", description = "this api used for delete category by id")
    @DeleteMapping("/adm/{id}")
    public ResponseEntity<?> delete(HttpServletRequest request,
                                    @PathVariable("id") Integer id) {
        log.info("delete category by id {}", id);
        HttpRequestUtil.getProfileId(request, ProfileRole.ADMIN);
        return ResponseEntity.ok(categoryService.delete(id));
    }
    @Operation(summary = "Api for getALl", description = "this api used for getAll category by id")
    @GetMapping("/adm")
    public ResponseEntity<?> getALl(HttpServletRequest request) {
        log.info("get all category");
        HttpRequestUtil.getProfileId(request, ProfileRole.ADMIN);
        return ResponseEntity.ok(categoryService.getAll());
    }
    @Operation(summary = "Api for getLang", description = "this api used for category by getLang")
    @GetMapping("/adm/lang")
    public ResponseEntity<?> getLang(HttpServletRequest request, @RequestParam AppLanguage lang) {
        log.info("get category by lang {}", lang);
        HttpRequestUtil.getProfileId(request);
        return ResponseEntity.ok(categoryService.getLang(lang));
    }


}
