package com.example.controller;

import com.example.dto.CreateCategoryDTO;
import com.example.enums.AppLanguage;
import com.example.enums.ProfileRole;
import com.example.service.CategoryService;
import com.example.util.HttpRequestUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @PostMapping("/adm")
    public ResponseEntity<?> create(@RequestBody CreateCategoryDTO dto, HttpServletRequest request) {
        Integer profileId = HttpRequestUtil.getProfileId(request, ProfileRole.ADMIN);//agar admin bolsa category qo'sha oladi bu methodlar shuni check qilib beradi
        return ResponseEntity.ok(categoryService.create(dto));
    }

    @PutMapping("/adm/{id}")
    public ResponseEntity<?> update(@RequestBody CreateCategoryDTO dto, @PathVariable Integer id,
                                    HttpServletRequest request) {
        HttpRequestUtil.getProfileId(request, ProfileRole.ADMIN);
        return ResponseEntity.ok(categoryService.update(id, dto));
    }

    @DeleteMapping("/adm/{id}")
    public ResponseEntity<?> delete(HttpServletRequest request,
                                    @PathVariable("id") Integer id) {
        HttpRequestUtil.getProfileId(request, ProfileRole.ADMIN);
        return ResponseEntity.ok(categoryService.delete(id));
    }

    @GetMapping("/adm")
    public ResponseEntity<?> getALl(HttpServletRequest request) {
        HttpRequestUtil.getProfileId(request, ProfileRole.ADMIN);
        return ResponseEntity.ok(categoryService.getAll());
    }

    @GetMapping("/adm/lang")
    public ResponseEntity<?> getLang(HttpServletRequest request, @RequestParam AppLanguage lang) {
        HttpRequestUtil.getProfileId(request);
        return ResponseEntity.ok(categoryService.getLang(lang));
    }


}
