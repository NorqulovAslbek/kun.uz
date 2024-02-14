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
import org.springframework.security.access.prepost.PreAuthorize;
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
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> create(@RequestBody CreateCategoryDTO dto) {
        log.info("create category {}", dto);
        return ResponseEntity.ok(categoryService.create(dto));
    }

    @Operation(summary = "Api for update", description = "this api used for update category")
    @PutMapping("/adm/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> update(@RequestBody CreateCategoryDTO dto, @PathVariable Integer id) {
        log.info("update category by id {}", id);
        return ResponseEntity.ok(categoryService.update(id, dto));
    }

    @Operation(summary = "Api for delete", description = "this api used for delete category by id")
    @DeleteMapping("/adm/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> delete(@PathVariable("id") Integer id) {
        log.info("delete category by id {}", id);
        return ResponseEntity.ok(categoryService.delete(id));
    }

    @Operation(summary = "Api for getALl", description = "this api used for getAll category by id")
    @GetMapping("/adm")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getALl() {
        log.info("get all category");
        return ResponseEntity.ok(categoryService.getAll());
    }

    @Operation(summary = "Api for getLang", description = "this api used for category by getLang")
    @GetMapping("/adm/lang")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getLang(@RequestParam AppLanguage lang) {
        log.info("get category by lang {}", lang);
        return ResponseEntity.ok(categoryService.getLang(lang));
    }


}
